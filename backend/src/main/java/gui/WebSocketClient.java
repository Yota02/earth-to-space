package gui;

import back.Jeu;
import back.fusee.reservoir.ReservoirPose;
import back.moteur.Ergol;
import back.objectAchetable.CarburantAchetable;
import back.objectAchetable.ObjectAchetable;
import back.programme.Programme;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/")
public class WebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
        GameServer.addClient(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            JSONObject jsonMessage = new JSONObject(message);

            String action = jsonMessage.getString("action");

            JSONObject response = new JSONObject();

            switch (action) {
                case "startResearch":
                case "buyObject":
                case "buyCarburant":
                case "sellObject":
                    if (!jsonMessage.has("name")) {
                        response.put("error", "Missing 'name' field for action '" + action + "'");
                        session.getBasicRemote().sendText(response.toString());
                        return;
                    }
                    String name = jsonMessage.getString("name");
                    handleActionWithName(action, name, session, response);
                    break;

                case "getProgrammeState":
                    handleGetProgrammeState(session);
                    break;

                case "creerUnProgramme":
                    handleCreerUnProgramme(jsonMessage, session);
                    break;
                
                case "addReservoir":
                    String fuelType = jsonMessage.getString("fuelType");

                    handleAddReservoir(session, fuelType);
                    break;

                case "getCarburantQuantite":
                    if (!jsonMessage.has("nom")) {
                        response.put("error", "Missing 'nom' field for action 'getCarburantQuantite'");
                        session.getBasicRemote().sendText(response.toString());
                        return;
                    }
                    handleGetCarburantQuantite(jsonMessage, session);
                    break;

                default:
                    response.put("error", "Unknown action: " + action);
                    session.getBasicRemote().sendText(response.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                session.getBasicRemote().sendText("Error processing message: Invalid JSON format");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        GameServer.removeClient(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    private void handleAddReservoir(Session session, String fuelTypeName) throws IOException {
        JSONObject response = new JSONObject();
        
        try {
            // Validation du type de carburant en utilisant le nom de l'énumération
            Ergol ergol = null;
            try {
                ergol = Ergol.valueOf(fuelTypeName.toUpperCase());
            } catch (IllegalArgumentException e) {
                response.put("error", "Type de carburant non supporté: " + fuelTypeName);
                session.getBasicRemote().sendText(response.toString());
                return;
            }
        
            // Création du réservoir avec des valeurs par défaut
            ReservoirPose newReservoir = new ReservoirPose.Builder()
                .setNom("Réservoir d'" + ergol.getNom() + " " + (GameServer.jeu.getReservoirs().size() + 1))
                .setErgol(ergol)
                .setQuantite(0.0)
                .setQuantiteTotal(1000.0)
                .build();
        
            // Ajout du réservoir au jeu
            GameServer.jeu.ajouterReservoir(newReservoir);
            
            // Création de la réponse de succès
            response.put("action", "reservoirAdded");
            response.put("nom", newReservoir.getNom());
            response.put("type", ergol.name());
            response.put("capacite", newReservoir.getQuantiteTotal());
            session.getBasicRemote().sendText(response.toString());
            
            // Mise à jour de l'état du jeu pour tous les clients
            GameServer.sendGameStateToClients();
            
        } catch (Exception e) {
            response.put("error", "Erreur lors de l'ajout du réservoir: " + e.getMessage());
            session.getBasicRemote().sendText(response.toString());
        }
    }

    private void handleActionWithName(String action, String name, Session session, JSONObject response) throws IOException {
        switch (action) {
            case "startResearch":
                GameServer.jeu.demarrerRecherche(name);
                break;

            case "buyObject":
                ObjectAchetable objectToBuy = GameServer.jeu.findObjectByName(name);
                if (objectToBuy != null) {
                    if (GameServer.jeu.getArgent() >= objectToBuy.getPrix()) {
                        GameServer.jeu.acheter(objectToBuy);
                        response.put("action", "buyObjectSuccess");
                        response.put("name", objectToBuy.getNom());
                        session.getBasicRemote().sendText(response.toString());
                        GameServer.sendGameStateToClients();
                    } else {
                        response.put("error", "Not enough money to buy " + objectToBuy.getNom());
                        session.getBasicRemote().sendText(response.toString());
                    }
                } else {
                    response.put("error", "Object not found: " + name);
                    session.getBasicRemote().sendText(response.toString());
                }
                break;

            case "sellObject":
                ObjectAchetable objectToSell = GameServer.jeu.findObjectByName(name);
                if (objectToSell != null) {
                    GameServer.jeu.vendre(objectToSell);
                    GameServer.sendGameStateToClients();
                }
                break;

            case "buyCarburant":
            
                CarburantAchetable carburantToBuy = GameServer.jeu.findCarburantByName(name);

                if (carburantToBuy != null) {
                    if (GameServer.jeu.getArgent() >= carburantToBuy.getPrix()) {
                        GameServer.jeu.effectuerAchatCarburant(carburantToBuy, carburantToBuy.getCarburant());

                        response.put("action", "buyObjectSuccess");
                        response.put("name", carburantToBuy.getNom());

                        session.getBasicRemote().sendText(response.toString());
                        GameServer.sendGameStateToClients();
                    } else {
                        response.put("error", "Not enough money to buy " + carburantToBuy.getNom());
                        session.getBasicRemote().sendText(response.toString());
                    }
                } else {
                    response.put("error", "Object not found: " + name);
                    session.getBasicRemote().sendText(response.toString());
                }
                break;
        }
    }

    private void handleGetProgrammeState(Session session) throws IOException {
        Programme programmeEnCours = getProgrammeEnCours();
        JSONObject response = new JSONObject();
        response.put("action", "programmeState");
        if (programmeEnCours != null) {
            response.put("programme", new JSONObject()
                    .put("nom", programmeEnCours.getNom())
                    .put("objectif", programmeEnCours.getObjectif())
                    .put("budget", programmeEnCours.getBudget())
                    .put("dureePrevu", programmeEnCours.getDureePrevu()));
        } else {
            response.put("programme", JSONObject.NULL);
        }
        session.getBasicRemote().sendText(response.toString());
    }
    
    private void handleCreerUnProgramme(JSONObject jsonMessage, Session session) throws IOException {
        JSONObject response = new JSONObject();
        if (jsonMessage.has("nom") && jsonMessage.has("objectif") && 
            jsonMessage.has("budget") && jsonMessage.has("dureePrevu")) {
            
            String nom = jsonMessage.getString("nom");
            String objectif = jsonMessage.getString("objectif");
            Double budget = jsonMessage.getDouble("budget");
            int dureePrevu = jsonMessage.getInt("dureePrevu");
    
            GameServer.jeu.creerUnProgramme(nom, objectif, budget, dureePrevu);
            response.put("action", "programmeCree");
            response.put("nom", nom);
            response.put("objectif", objectif);
            response.put("budget", budget);
            response.put("dureePrevu", dureePrevu);
            session.getBasicRemote().sendText(response.toString());
        } else {
            response.put("error", "Missing required fields for action 'creerUnProgramme'");
            session.getBasicRemote().sendText(response.toString());
        }
    }

    private void handleGetCarburantQuantite(JSONObject jsonMessage, Session session) throws IOException {
        String nom = jsonMessage.getString("nom");

        double quantite = GameServer.jeu.getQuantiteCarburant(GameServer.jeu.findErgolByName(nom));

        JSONObject response = new JSONObject();
        response.put("action", "updateCarburantQuantite");
        response.put("nom", nom);
        response.put("quantite", quantite);
        session.getBasicRemote().sendText(response.toString());
    }

    private Programme getProgrammeEnCours() {
        if (GameServer.jeu.getProgrammes().isEmpty()) {
            return null;
        }
        return GameServer.jeu.getProgrammes().get(0); // Vous pouvez ajuster cette logique si vous avez plusieurs programmes
    }
}
