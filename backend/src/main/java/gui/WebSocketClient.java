package gui;

import back.Ressources_Humaines.Personne;
import back.fusee.Fusee;
import back.fusee.booster.Booster;
import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.moteur.Ergol;
import back.fusee.moteur.Moteur;
import back.fusee.reservoir.ReservoirFusee;
import back.fusee.reservoir.ReservoirPose;
import back.objectAchetable.CarburantAchetable;
import back.objectAchetable.ObjectAchetable;
import back.programme.Programme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/")
public class WebSocketClient {

    @OnOpen
    public void onOpen(Session session) throws IOException {
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
                    String name = jsonMessage.getString("name");
                    handleActionWithName(action, name, session, response);
                    break;

                case "getProgrammeState":
                    handleGetProgrammeState(session);
                    break;

                case "getFuseesState":
                    getFusees(session);
                    break;

                case "getBoostersState":
                    getBoosters(session);
                    break;

                case "getMarcheEmploisState":
                    getMarcheEmploie(session);
                    break;

                case "getEmployesState":
                    getEmployes(session);
                    break;
                
                case "getCarburants":
                    getCarburant(session);
                    break;

                case "creerUnProgramme":
                    handleCreerUnProgramme(jsonMessage, session);
                    break;

                case "embaucherEmploye":
                    handleEmbaucherEmploye(jsonMessage, session);
                    break;

                case "licencierEmploye":
                    handlelicencierEmploye(jsonMessage, session);
                    break;

                case "addReservoir":
                    String fuelType = jsonMessage.getString("fuelType");
                    handleAddReservoir(session, fuelType);
                    break;

                case "getCarburantQuantite":
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
    public void onClose(Session session) throws IOException {
        GameServer.removeClient(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    private void handleAddReservoir(Session session, String fuelTypeName) throws IOException {
        JSONObject response = new JSONObject();

        try {
            Ergol ergol = null;
            try {
                ergol = Ergol.valueOf(fuelTypeName.toUpperCase());
            } catch (IllegalArgumentException e) {
                response.put("error", "Type de carburant non supporté: " + fuelTypeName);
                session.getBasicRemote().sendText(response.toString());
                return;
            }

            ReservoirPose newReservoir = new ReservoirPose.Builder()
                    .setNom("Réservoir d'" + ergol.getNom() + " " + (GameServer.jeu.getReservoirs().size() + 1))
                    .setErgol(ergol)
                    .setQuantite(0.0)
                    .setQuantiteTotal(1000.0)
                    .build();

            GameServer.jeu.ajouterReservoir(newReservoir);

            response.put("action", "reservoirAdded");
            response.put("nom", newReservoir.getNom());
            response.put("type", ergol.name());
            response.put("capacite", newReservoir.getQuantiteTotal());
            session.getBasicRemote().sendText(response.toString());

            GameServer.sendGameStateToClients("reservoirs");
        } catch (Exception e) {
            response.put("error", "Erreur lors de l'ajout du réservoir: " + e.getMessage());
            session.getBasicRemote().sendText(response.toString());
        }
    }

    private void handleEmbaucherEmploye(JSONObject jsonMessage, Session session) throws IOException {
        JSONObject response = new JSONObject();

        try {
            JSONObject employeJson = jsonMessage.getJSONObject("employe");
            int clePrimaire = employeJson.getInt("cleprimaire");
            Personne personne = GameServer.jeu.retrouverEmployeParId(clePrimaire);

            boolean removed = false;
            for (List<Personne> liste : GameServer.jeu.getMarcheEmploie().values()) {
                if (liste.remove(personne)) {
                    removed = true;
                    break;
                }
            }

            if (removed) {
                GameServer.jeu.embaucherPersonne(personne);
                response.put("action", "personneEmbauchee");
                response.put("nom", personne.getNom());
                session.getBasicRemote().sendText(response.toString());
                GameServer.sendGameStateToClients("employes");
            } else {
                response.put("error", "Personne non trouvée dans le marché de l'emploi.");
                session.getBasicRemote().sendText(response.toString());
            }
        } catch (Exception e) {
            response.put("error", "Erreur lors de l'embauche : " + e.getMessage());
            session.getBasicRemote().sendText(response.toString());
        }
    }

    private void handlelicencierEmploye(JSONObject jsonMessage, Session session) throws IOException {
        JSONObject response = new JSONObject();
        try {
            JSONObject employeJson = jsonMessage.getJSONObject("employe");
            int clePrimaire = employeJson.getInt("cleprimaire");
            Personne personne = GameServer.jeu.retrouverEmployeParId(clePrimaire);
            GameServer.jeu.licencierPersonne(personne);
            response.put("action", "personneLicencier");
            response.put("nom", personne.getNom());
            response.put("clePrimaire", personne.getClePrimaire());
            session.getBasicRemote().sendText(response.toString());
            GameServer.sendGameStateToClients("employes");
        } catch (Exception e) {
            response.put("error", "Erreur lors du licenciement : " + e.getMessage());
            session.getBasicRemote().sendText(response.toString());
        }
    }
    
    private void handleActionWithName(String action, String name, Session session, JSONObject response)
            throws IOException {
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
                        GameServer.sendGameStateToClients(name);
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
                    GameServer.sendGameStateToClients("objectsAchetables");
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
                        GameServer.sendGameStateToClients("carburants");
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

    private void getFusees(Session session) throws IOException {
        List<Fusee> fusees = GameServer.jeu.getFusees();

        JSONArray fuseesArray = new JSONArray();

        for (Fusee fusee : fusees) {
            if (fusee != null) {
                JSONObject objJson = new JSONObject();
                objJson.put("nom", fusee.getNom());
                objJson.put("taille", fusee.getTaille());
                objJson.put("diametre", fusee.getDiametre());
                objJson.put("poidsTotal", fusee.getPoidsTotal());
                objJson.put("altitudeMax", fusee.getAltitudeMax());
                objJson.put("boosterPrincipal", fusee.getBoosterPrincipal().getNom());
                objJson.put("systemeSecurite", fusee.isSystemeSecurite());
                objJson.put("etat", fusee.getEtat());

                JSONArray chargesArray = new JSONArray();
                for (ChargeUtile charge : fusee.getPoidChargeUtiles()) {
                    JSONObject chargeJson = new JSONObject();
                    chargeJson.put("nom", charge.getNom());
                    chargeJson.put("poids", charge.getPoids());
                    chargesArray.put(chargeJson);
                }
                objJson.put("poidChargeUtiles", chargesArray);

                fuseesArray.put(objJson);
            }
        }

        JSONObject response = new JSONObject();
        response.put("action", "fuseesState");
        response.put("fusees", fuseesArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getBoosters(Session session) throws IOException {
        List<Booster> boosters = GameServer.jeu.getLanceurs();

        JSONArray jsonArray = new JSONArray();

        for (Booster booster : boosters) {
            JSONObject boosterJson = new JSONObject();
            boosterJson.put("nom", booster.getNom());
            boosterJson.put("taille", booster.getTaille());
            boosterJson.put("diametre", booster.getDiametre());
            boosterJson.put("poidsAVide", booster.getPoidsAVide());
            boosterJson.put("poids", booster.getPoids());
            boosterJson.put("altitudeMax", booster.getAltitudeMax());
            boosterJson.put("vitesseMax", booster.getVitesseMax());
            boosterJson.put("estPrototype", booster.getEstPrototype());
            boosterJson.put("estReetulisable", booster.getEstReetulisable());
            boosterJson.put("aSystemeAutoDestruction", booster.getASystèmeAutoDestruction());

            JSONArray historiquesLancementJson = new JSONArray();
            if (booster.getHistoriquesLancement() != null) {
                for (String lancement : booster.getHistoriquesLancement()) {
                    historiquesLancementJson.put(lancement);
                }
            }
            boosterJson.put("historiquesLancement", historiquesLancementJson);

            JSONArray moteursJson = new JSONArray();
            if (booster.getMoteur() != null) {
                for (Moteur moteur : booster.getMoteur()) {
                    JSONObject moteurJson = new JSONObject();
                    moteurJson.put("nom", moteur.getNom());
                    moteurJson.put("poids", moteur.getPoids());
                    moteursJson.put(moteurJson);
                }
            }
            boosterJson.put("moteurs", moteursJson);

            JSONArray reservoirsJson = new JSONArray();
            if (booster.getReservoirs() != null) {
                for (ReservoirFusee reservoir : booster.getReservoirs()) {
                    JSONObject reservoirJson = new JSONObject();
                    reservoirJson.put("nom", reservoir.getNom());
                    reservoirJson.put("poidsAVide", reservoir.getPoidsAvide());
                    reservoirJson.put("poids", reservoir.getPoids());
                    reservoirsJson.put(reservoirJson);
                }
            }
            boosterJson.put("reservoirs", reservoirsJson);

            jsonArray.put(boosterJson);
        }

        JSONObject response = new JSONObject();
        response.put("action", "boostersState");
        response.put("boosters", jsonArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getEmployes(Session session) throws IOException {
        Map<String, List<Personne>> employes = GameServer.jeu.getEmployes();

        JSONArray mainArray = new JSONArray();

        for (Map.Entry<String, List<Personne>> entry : employes.entrySet()) {
            JSONObject categoryObject = new JSONObject();
            String key = entry.getKey();
            List<Personne> personnes = entry.getValue();

            JSONArray personnesArray = new JSONArray();
            for (Personne personne : personnes) {
                JSONObject personneJson = new JSONObject();
                personneJson.put("cleprimaire", personne.getClePrimaire());
                personneJson.put("prenom", personne.getPrenom());
                personneJson.put("nom", personne.getNom());
                personneJson.put("salaire", personne.getSalaire());
                personneJson.put("age", personne.getAge());
                personneJson.put("sexe", personne.getSexe());
                personnesArray.put(personneJson);
            }

            categoryObject.put("type", key);
            categoryObject.put("personnes", personnesArray);
            mainArray.put(categoryObject);
        }

        JSONObject response = new JSONObject();
        response.put("action", "employesState");
        response.put("salaireTotal", GameServer.jeu.coutSalaireTotal());
        response.put("employes", mainArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getCarburant(Session session) throws IOException {
        List<CarburantAchetable> carburants = GameServer.jeu.getCarburantAchetables();

        JSONArray objectsArray = new JSONArray();
        for (CarburantAchetable carburant : carburants) {
            JSONObject objJson = new JSONObject();
            objJson.put("nom", carburant.getNom());
            objJson.put("prix", carburant.getPrix());
            objJson.put("type", carburant.getCarburant());
            objJson.put("estAchetable", carburant.getEstAchetable());
            objJson.put("quantite", carburant.getQuantite());

            objJson.put("capaciteMax", GameServer.jeu.getCapaciteMaximaleErgol(carburant.getCarburant()));
            objJson.put("quantiteStock", GameServer.jeu.getQuantiteCarburant(carburant.getCarburant()));

            objectsArray.put(objJson);
        }

        JSONObject response = new JSONObject();
        response.put("action", "carburantsState");
        response.put("carburants", objectsArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getMarcheEmploie(Session session) throws IOException {
        Map<String, List<Personne>> marcheEmploi = GameServer.jeu.getMarcheEmploie();

        JSONArray mainArray = new JSONArray();

        for (Map.Entry<String, List<Personne>> entry : marcheEmploi.entrySet()) {
            JSONObject categoryObject = new JSONObject();
            String key = entry.getKey();
            List<Personne> personnes = entry.getValue();

            JSONArray personnesArray = new JSONArray();
            for (Personne personne : personnes) {
                JSONObject personneJson = new JSONObject();
                personneJson.put("cleprimaire", personne.getClePrimaire());
                personneJson.put("prenom", personne.getPrenom());
                personneJson.put("nom", personne.getNom());
                personneJson.put("salaire", personne.getSalaire());
                personneJson.put("age", personne.getAge());
                personneJson.put("sexe", personne.getSexe());
                personnesArray.put(personneJson);
            }

            categoryObject.put("type", key);
            categoryObject.put("personnes", personnesArray);
            mainArray.put(categoryObject);
        }

        JSONObject response = new JSONObject();
        response.put("action", "marcheEmploisState");
        response.put("marcheEmploie", mainArray);

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
        return GameServer.jeu.getProgrammes().get(0);
    }
}
