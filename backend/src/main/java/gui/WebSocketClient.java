package gui;

import back.Batiment.BatimentStockage;
import back.Batiment.HangarAssemblage;
import back.Batiment.IBatiment;
import back.Batiment.UsineProduction;
import back.Batiment.UsineProductionCarburant;
import back.MarcheFinancier.Entreprise;
import back.MarcheFinancier.MarcheFinancier;
import back.Ressources_Humaines.Personne;
import back.fusee.Fusee;
import back.fusee.Piece.PieceFusee;
import back.fusee.booster.Booster;
import back.fusee.moteur.Ergol;
import back.fusee.reservoir.ReservoirPose;
import back.mission.Mission;
import back.objectAchetable.CarburantAchetable;
import back.objectAchetable.GestionnaireObject;
import back.objectAchetable.ObjectAchetable;
import back.politique.Objectif;
import back.politique.PolitiqueManager;
import back.politique.Subvention;
import back.programme.Programme;
import back.recherche.Recherche;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
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

            String messageType = jsonMessage.has("type") ? jsonMessage.getString("type")
                    : jsonMessage.getString("action");

            JSONObject response = new JSONObject();

            switch (messageType) {
                case "buyObject":
                case "buyCarburant":
                case "sellObject":
                    String name = jsonMessage.getString("name");
                    handleActionWithName(messageType, name, session, response);
                    break;

                case "updateMonthlyDemand":
                    handleMonthlyDemand(session, jsonMessage);
                    break;

                case "getEntrepriseData":
                    handleGetEntrepriseData(session);
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

                case "getObjectifsState":
                    getObjectifsState(session);
                    break;

                case "creerUnProgramme":
                    handleCreerUnProgramme(jsonMessage, session);
                    break;

                case "embaucherEmploye":
                    handleEmbaucherEmploye(jsonMessage, session);
                    break;

                case "getMissionSteps":
                    getMissions(session);
                    break;

                case "getBatiment":
                    getBatiment(session);
                    break;

                case "buyBatiment":
                    handleAcheterBatiment(jsonMessage, session);
                    break;

                case "getMissionState":
                    getMissions(session);
                    break;

                case "getSubventionsState":
                    getSubventions(session);
                    break;

                case "getUsineCarburants":
                    getUsineCarburant(session);
                    break;

                case "getMarcheFinancierState":
                    getMarcheFinancier(session);
                    break;

                case "activateSubvention":
                    int subventionId = Integer.parseInt(jsonMessage.getString("subventionId"));
                    Subvention subvention = GameServer.jeu.getPolitiqueManager().findSubventionParId(subventionId);
                    if (subvention != null) {
                        activateSubvention(subvention);
                        // Send updated state to all clients
                        getSubventions(session);
                    } else {
                        response.put("type", "error");
                        response.put("message", "Subvention not found with ID: " + subventionId);
                        session.getBasicRemote().sendText(response.toString());
                    }
                    break;

                case "licencierEmploye":
                    handlelicencierEmploye(jsonMessage, session);
                    break;

                case "createEntreprise":
                    handleCreateEntreprise(jsonMessage);
                    break;

                case "checkDebugMode":
                    handleCheckDebugMode(session);
                    break;

                case "getRecherches":
                    getRecherches(session);
                    break;

                case "getObjectsAchetables":
                    getObjectsAchetables(session);
                    break;

                case "addReservoir":
                    String fuelType = jsonMessage.getString("fuelType");
                    handleAddReservoir(session, fuelType);
                    break;

                case "getProduction":
                    getProduction(session);
                    break;

                case "getProductionParPiece":
                    getProductionParPiece(session);
                    break;

                case "getCarburantQuantite":
                    handleGetCarburantQuantite(jsonMessage, session);
                    break;

                case "startResearch":
                    String name2 = jsonMessage.getString("name");
                    Recherche recherche = GameServer.jeu.getGestionnaireRecherche().getRecherche(name2);
                    if (recherche != null) {
                        GameServer.jeu.getGestionnaireRecherche().demarrerRecherche(recherche.getId());
                        response.put("action", "startResearchSuccess");
                        response.put("name", recherche.getNom());
                        session.getBasicRemote().sendText(response.toString());
                        getRecherches(session);
                    } else {
                        response.put("error", "Research not found: " + name2);
                        session.getBasicRemote().sendText(response.toString());
                    }
                    break;

                default:
                    response.put("error", "Unknown action: " + messageType);
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

    private void handleGetEntrepriseData(Session session) throws IOException {
        JSONObject response = new JSONObject();
        response.put("action", "entrepriseData");

        Entreprise entreprise = GameServer.jeu.getEntreprise();
        if (entreprise != null) {
            JSONObject entrepriseJson = entreprise.toJson();
            response.put("entreprise", entrepriseJson);
        } else {
            response.put("entreprise", JSONObject.NULL);
        }

        session.getBasicRemote().sendText(response.toString());
    }

    private void handleCheckDebugMode(Session session) throws IOException {
        JSONObject response = new JSONObject();
        response.put("action", "debugModeState");
        response.put("debugMode", GameServer.jeu.isDebugMode());
        session.getBasicRemote().sendText(response.toString());
    }

    public void getObjectifsState(Session session) throws IOException {
        PolitiqueManager politiqueManager = GameServer.jeu.getPolitiqueManager();

        JSONArray objectifsActifs = new JSONArray();
        JSONArray objectifsTermines = new JSONArray();

        for (Objectif obj : politiqueManager.getObjectifs()) {
            if (!obj.isTerminee()) {
                objectifsActifs.put(obj.toJson());
            } else {
                objectifsTermines.put(obj.toJson());
            }
        }

        JSONObject response = new JSONObject();
        response.put("action", "ObjectifsState");
        response.put("objectifsActifs", objectifsActifs);
        response.put("objectifsTermines", objectifsTermines);

        session.getBasicRemote().sendText(response.toString());
    }

    public int activateSubvention(Subvention subvention) {
        PolitiqueManager politiqueManager = GameServer.jeu.getPolitiqueManager();
        GameServer.jeu.ajouterArgent(politiqueManager.activateSubvention(subvention));

        if (!politiqueManager.getSubventionsPosseder().contains(subvention)) {
            subvention.setActive(true);
            politiqueManager.getSubventionsPosseder().add(subvention);
            politiqueManager.getSubventions().remove(subvention);
            return subvention.getQuantite();
        }
        return 0;
    }

    private void getMarcheFinancier(Session session) throws IOException {
        try {
            List<MarcheFinancier> marches = GameServer.jeu.getGestionaireMarche().getMarcheFinanciers();

            JSONObject response = new JSONObject();
            response.put("action", "getMarcheFinancierState");

            // Création du tableau des marchés financiers
            JSONArray marchesArray = new JSONArray();

            for (MarcheFinancier marche : marches) {

                JSONObject marcheJson = new JSONObject();
                marcheJson.put("nom", marche.getNom());

                // Création du tableau des parts de marché pour ce marché
                JSONArray partMarcheArray = new JSONArray();
                for (Map.Entry<Entreprise, Double> entry : marche.getPartMarche().entrySet()) {
                    Entreprise entreprise = entry.getKey();
                    Double part = entry.getValue();

                    JSONObject entrepriseData = new JSONObject();
                    JSONObject entrepriseJson = entreprise.toJson();
                    entrepriseData.put("entreprise", entrepriseJson);
                    entrepriseData.put("part", part);

                    partMarcheArray.put(entrepriseData);
                }

                marcheJson.put("partMarche", partMarcheArray);
                marchesArray.put(marcheJson);
            }

            response.put("marches", marchesArray);
            String jsonResponse = response.toString();

            session.getBasicRemote().sendText(jsonResponse);

        } catch (Exception e) {
            System.err.println("Erreur dans getMarcheFinancier : " + e.getMessage());
            e.printStackTrace();

            JSONObject errorResponse = new JSONObject();
            errorResponse.put("action", "error");
            errorResponse.put("message",
                    "Erreur lors de la récupération des données du marché financier: " + e.getMessage());
            session.getBasicRemote().sendText(errorResponse.toString());
        }
    }

    private void handleMonthlyDemand(Session session, JSONObject jsonMessage) throws IOException {
        try {
            if (jsonMessage.has("data")) {
                JSONArray data = jsonMessage.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject fuelData = data.getJSONObject(i);

                    String name = fuelData.getString("name");

                    // Vérification que "monthlyDemand" existe dans fuelData
                    if (!fuelData.has("monthlyDemand")) {
                        System.err.println("Erreur: 'monthlyDemand' est manquant pour le carburant: " + name);
                        continue;
                    }

                    int monthlyQuantity = fuelData.getInt("monthlyDemand");

                    CarburantAchetable carburant = GameServer.jeu.findCarburantByName(name);
                    if (carburant != null) {

                        carburant.setDemandeMonthly(monthlyQuantity);

                        JSONObject response = new JSONObject();
                        response.put("action", "updateMonthlyDemandSuccess");
                        response.put("name", name);
                        response.put("quantity", monthlyQuantity);
                        session.getBasicRemote().sendText(response.toString());
                    } else {
                        System.err.println("Carburant non trouvé pour le nom: " + name);
                    }
                }
                GameServer.sendGameStateToClients("carburants");
            } else {
                System.err.println("Erreur : le champ 'data' est manquant dans le message !");
            }

        } catch (JSONException e) {
            onError(session, e);
        }
    }

    private void getObjectsAchetables(Session session) {
        try {
            GestionnaireObject gestionnaireObject = GameServer.jeu.getGestionnaireObject();
            List<ObjectAchetable> objects = gestionnaireObject.getObjects();

            JSONObject response = new JSONObject();
            response.put("action", "getObjectsAchetables");

            JSONArray objectsArray = new JSONArray();
            for (ObjectAchetable obj : objects) {
                objectsArray.put(obj.toJson());
            }

            response.put("objectsAchetables", objectsArray);

            session.getBasicRemote().sendText(response.toString());
        } catch (Exception e) {
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

    private void handleAcheterBatiment(JSONObject jsonMessage, Session session) throws IOException {
        JSONObject response = new JSONObject();

        try {
            JSONObject batimentJson = jsonMessage.getJSONObject("batiment");
            String nomBatiment = batimentJson.getString("nom");
            int superficie = batimentJson.getInt("superficie");

            // Trouver le prototype de bâtiment correspondant
            IBatiment prototypeBatiment = findBatimentPrototype(nomBatiment);
            if (prototypeBatiment == null) {
                response.put("error", "Type de bâtiment non trouvé : " + nomBatiment);
                session.getBasicRemote().sendText(response.toString());
                return;
            }

            // Calculer le coût
            int coutBatiment = prototypeBatiment.getCout();
            if (GameServer.jeu.getArgent() < coutBatiment) {
                response.put("error", "Fonds insuffisants pour acheter le bâtiment");
                session.getBasicRemote().sendText(response.toString());
                return;
            }

            // Retirer l'argent
            GameServer.jeu.retirerArgent(coutBatiment);

            // Créer une nouvelle instance de bâtiment basée sur le prototype
            HangarAssemblage nouveauBatiment = new HangarAssemblage(
                    nomBatiment + " (" + superficie + "m²)",
                    superficie,
                    ((HangarAssemblage) prototypeBatiment).getCapacite() * (superficie / 100),
                    prototypeBatiment.getTempsConstruction(),
                    ((HangarAssemblage) prototypeBatiment).getHauteur());

            // Marquer comme en construction et ajouter
            nouveauBatiment.setEnConstruction(true);
            nouveauBatiment.setAnneeConstruction(GameServer.jeu.getDate());
            GameServer.jeu.getBatimentManager().ajouterBatimentPossede(nouveauBatiment);

            response.put("success", true);
            response.put("batiment", nouveauBatiment.toJson());
            session.getBasicRemote().sendText(response.toString());

            // Notifier tous les clients de la mise à jour des bâtiments
            GameServer.sendGameStateToClients("batiments");

        } catch (Exception e) {
            response.put("error", "Erreur lors de l'achat du bâtiment : " + e.getMessage());
            session.getBasicRemote().sendText(response.toString());
        }
    }

    private IBatiment findBatimentPrototype(String nom) {
        for (List<IBatiment> batiments : GameServer.jeu.getBatimentManager().getBatimentMap().values()) {
            for (IBatiment batiment : batiments) {
                if (batiment.getNom().equalsIgnoreCase(nom)) {
                    return batiment;
                }
            }
        }
        return null;
    }

    public static void handleCreateEntreprise(JSONObject data) {
        String nom = data.getString("nom");
        String pays = data.getString("pays");

        if (GameServer.jeu != null) {
            GameServer.jeu.createEntreprise(nom, pays);
            GameServer.sendGameStateToClients("all");
        }
    }

    private void handleEmbaucherEmploye(JSONObject jsonMessage, Session session) throws IOException {
        JSONObject response = new JSONObject();

        try {
            JSONObject employeJson = jsonMessage.getJSONObject("employe");
            int clePrimaire = employeJson.getInt("cleprimaire");

            Personne personne = GameServer.jeu.getGestionnaireRH().retrouverEmployeParId(clePrimaire);

            if (personne == null) {
                System.out.println("Aucun employé trouvé avec l'ID " + clePrimaire);
            }

            GameServer.jeu.getGestionnaireRH().embaucherPersonne(personne);

            response.put("action", "personneEmbauchee");
            response.put("nom", personne.getNom());
            session.getBasicRemote().sendText(response.toString());
            GameServer.sendGameStateToClients("employes");

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
            Personne personne = GameServer.jeu.getGestionnaireRH().retrouverEmployeParId(clePrimaire);
            GameServer.jeu.getGestionnaireRH().licencierPersonne(personne);
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

    private void getProductionParPiece(Session session) throws IOException {
        JSONObject response = new JSONObject();
        response.put("action", "productionParPieceState");
        
        List<BatimentStockage> batiments = GameServer.jeu.getBatimentManager().getBatimentsStockage();
        
        JSONArray piecesArray = new JSONArray();
        for (BatimentStockage batiment : batiments) {
            JSONObject stockageJson = batiment.toJSON2();
            piecesArray.put(stockageJson);
        }
        
        response.put("pieces", piecesArray);
        session.getBasicRemote().sendText(response.toString());
    }

    private void getProduction(Session session) throws IOException {
        JSONObject response = new JSONObject();
        response.put("action", "productionState");

        JSONArray productionArray = new JSONArray();
        for (UsineProduction usine : GameServer.jeu.getBatimentManager().getUsineProduction()) {
            productionArray.put(usine.toJson());
        }

        response.put("production", productionArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void handleActionWithName(String action, String name, Session session, JSONObject response)
            throws IOException {
        switch (action) {

            case "buyObject":
                ObjectAchetable objectToBuy = GameServer.jeu.getGestionnaireObject().findObjectByName(name);
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
                ObjectAchetable objectToSell = GameServer.jeu.getGestionnaireObject().findObjectByName(name);
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
            JSONObject objJson = fusee.toJson();
            fuseesArray.put(objJson);
        }

        JSONObject response = new JSONObject();
        response.put("action", "fuseesState");
        response.put("fusees", fuseesArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getBatiment(Session session) throws IOException {
        Map<String, List<IBatiment>> batiments = GameServer.jeu.getBatimentManager().getBatimentMap();

        // Listes pour séparer les bâtiments disponibles et en construction
        List<JSONObject> batimentsDisponibles = new ArrayList<>();
        List<JSONObject> batimentsEnConstruction = new ArrayList<>();

        // Parcourir tous les bâtiments
        for (List<IBatiment> batimentsList : batiments.values()) {
            for (IBatiment batiment : batimentsList) {
                if (batiment.estDebloquer()) {
                    JSONObject batimentJson = batiment.toJson();
                    // Ajouter le type basé sur le nom de la classe
                    batimentJson.put("type", batiment.getClass().getSimpleName());
                    batimentsDisponibles.add(batimentJson);
                }
            }
        }

        // Parcourir les bâtiments possédés
        for (IBatiment batiment : GameServer.jeu.getBatimentManager().getBatimentsPossedes()) {
            if (batiment.getEnConstruction()) {
                JSONObject batimentJson = batiment.toJson();
                batimentJson.put("type", batiment.getClass().getSimpleName());
                batimentJson.put("progression", batiment.getProgression());
                batimentJson.put("tempsRestant", batiment.getTempsRestant());
                batimentsEnConstruction.add(batimentJson);
            }
        }

        // Créer la réponse
        JSONObject response = new JSONObject();
        response.put("action", "batimentsState");
        response.put("batimentsDisponibles", new JSONArray(batimentsDisponibles));
        response.put("batimentsEnConstruction", new JSONArray(batimentsEnConstruction));

        // Envoyer la réponse
        session.getBasicRemote().sendText(response.toString());
    }

    private void getBoosters(Session session) throws IOException {
        List<Booster> boosters = GameServer.jeu.getLanceurs();

        JSONArray jsonArray = new JSONArray();

        for (Booster booster : boosters) {
            JSONObject boosterJson = booster.toJson();
            jsonArray.put(boosterJson);
        }

        JSONObject response = new JSONObject();
        response.put("action", "boostersState");
        response.put("boosters", jsonArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getMissions(Session session) throws IOException {
        List<Mission> missions = GameServer.jeu.getMissions();

        JSONArray jsonArray = new JSONArray();

        for (Mission mission : missions) {
            JSONObject missionJson = mission.toJson();
            jsonArray.put(missionJson);
        }

        JSONObject response = new JSONObject();
        response.put("action", "missionsState");
        response.put("missions", jsonArray);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getMissionById(Session session, int id) throws IOException {
        List<Mission> missions = GameServer.jeu.getMissions();

        JSONObject missionJson = null;

        for (Mission mission : missions) {
            if (mission.getMissionId() == id) {
                missionJson = mission.toJson();
                break;
            }
        }

        JSONObject response = new JSONObject();
        response.put("action", "missionsState");
        response.put("mission", missionJson);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getSubventions(Session session) throws IOException {
        PolitiqueManager politiqueManager = GameServer.jeu.getPolitiqueManager();

        JSONArray subventionsActives = new JSONArray();
        JSONArray subventionsDisponibles = new JSONArray();

        // Ajouter les subventions possédées (actives)
        for (Subvention sub : politiqueManager.getSubventionsPosseder()) {
            subventionsActives.put(sub.toJson());
        }

        // Ajouter les subventions disponibles (non possédées)
        for (Subvention sub : politiqueManager.getSubventions()) {
            if (!politiqueManager.getSubventionsPosseder().contains(sub)) {
                subventionsDisponibles.put(sub.toJson());
            }
        }

        JSONObject response = new JSONObject();
        response.put("action", "getSubventionsState");
        response.put("subventionsActives", subventionsActives);
        response.put("subventionsDisponibles", subventionsDisponibles);

        session.getBasicRemote().sendText(response.toString());
    }

    private void getEmployes(Session session) throws IOException {
        Map<String, List<Personne>> employes = GameServer.jeu.getGestionnaireRH().getEmployeMap();

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

    private void getRecherches(Session session) throws IOException {

        JSONArray objectsArray = new JSONArray();
        for (Recherche r : GameServer.jeu.getGestionnaireRecherche().getRecherches()) {
            objectsArray.put(r.toJson());
        }

        JSONObject response = new JSONObject();
        response.put("action", "recherchesState");
        response.put("recherches", objectsArray);

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
        Map<String, List<Personne>> marcheEmploi = GameServer.jeu.getGestionnaireRH().getMarcheEmploie();

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

    private void getUsineCarburant(Session session) throws IOException {
        List<UsineProductionCarburant> usines = GameServer.jeu.getBatimentManager().getUsineCarburants();

        JSONArray usinesArray = new JSONArray();
        for (UsineProductionCarburant usine : usines) {
            JSONObject usineJson = usine.toJson();
            usinesArray.put(usineJson);
        }

        JSONObject response = new JSONObject();
        response.put("action", "usineCarburantsState");
        response.put("usineCarburants", usinesArray);

        session.getBasicRemote().sendText(response.toString());
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
