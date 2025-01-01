package gui;

import back.Jeu;
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

            // Retrieve the action
            String action = jsonMessage.getString("action");

            JSONObject response = new JSONObject();

            // Handle different actions
            switch (action) {
                case "startResearch":
                case "buyObject":
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

    private void handleActionWithName(String action, String name, Session session, JSONObject response) throws IOException {
        switch (action) {
            case "startResearch":
                GameServer.jeu.demarrerRecherche(name);
                break;

            case "buyObject":
                ObjectAchetable objectToBuy = GameServer.jeu.findObjectByName(name);
                if (objectToBuy != null) {
                    GameServer.jeu.acheter(objectToBuy);
                    GameServer.sendGameStateToClients();
                }
                break;

            case "sellObject":
                ObjectAchetable objectToSell = GameServer.jeu.findObjectByName(name);
                if (objectToSell != null) {
                    GameServer.jeu.vendre(objectToSell);
                    GameServer.sendGameStateToClients();
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
                    .put("objectif", programmeEnCours.getObjectif()));
        } else {
            response.put("programme", JSONObject.NULL);
        }
        session.getBasicRemote().sendText(response.toString());
    }

    private void handleCreerUnProgramme(JSONObject jsonMessage, Session session) throws IOException {
        JSONObject response = new JSONObject();
        if (jsonMessage.has("nom") && jsonMessage.has("objectif")) {
            String nom = jsonMessage.getString("nom");
            String objectif = jsonMessage.getString("objectif");
            GameServer.jeu.creerUnProgramme(nom, objectif);
            response.put("action", "programmeCree");
            response.put("nom", nom);
            response.put("objectif", objectif);
            session.getBasicRemote().sendText(response.toString());
        } else {
            response.put("error", "Missing 'nom' or 'objectif' fields for action 'creerUnProgramme'");
            session.getBasicRemote().sendText(response.toString());
        }
    }

    private void handleGetCarburantQuantite(JSONObject jsonMessage, Session session) throws IOException {
        String nom = jsonMessage.getString("nom");
        double quantite = GameServer.jeu.getQuantiteCarburant(nom);
        JSONObject response = new JSONObject();
        response.put("action", "updateCarburantQuantite");
        response.put("nom", nom);
        response.put("quantite", quantite);
        session.getBasicRemote().sendText(response.toString());
    }

    private Programme getProgrammeEnCours() {
        // Retourne le premier programme trouvé, ou null s'il n'y en a pas
        if (GameServer.jeu.getProgrammes().isEmpty()) {
            return null;
        }
        return GameServer.jeu.getProgrammes().get(0); // Vous pouvez ajuster cette logique si vous avez plusieurs programmes
    }
}