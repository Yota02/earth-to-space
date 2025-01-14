package gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

import back.Ressources_Humaines.Personne;
import back.fusee.Fusee;
import back.fusee.booster.Booster;
import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.moteur.Moteur;
import back.fusee.reservoir.ReservoirFusee;
import back.fusee.reservoir.ReservoirPose;
import back.objectAchetable.CarburantAchetable;
import back.objectAchetable.ObjectAchetable;
import back.recherche.Recherche;
import org.glassfish.tyrus.server.Server;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameServer {
    public static final CopyOnWriteArrayList<Session> clients = new CopyOnWriteArrayList<>();
    private static final ReentrantLock lock = new ReentrantLock();
    private static String etatJeu = "";
    static JeuWebsocket jeu;

    public static void main(String[] args) throws IOException, DeploymentException {
        String[] nomsJoueurs = { "Joueur" };
        jeu = new JeuWebsocket(nomsJoueurs);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0", 4242), 0);
        httpServer.createContext("/", new StaticFileHandler("/", "front/", "index.html"));
        httpServer.start();

        System.out.println("Pour s'y connecter : http://localhost:4242");
        Server server = new Server("0.0.0.0", 3232, "/", new HashMap<>(), WebSocketClient.class);

        try {
            server.start();
            new Thread(jeu).start();

            // Replace the scanner with simulated input for Docker environments
            while (true) {
                String input = "default"; // Simulating input (replace this with any logic you want)
                if (input != null && !input.trim().isEmpty()) {
                    jeu.addInput(input);
                }

                // Sleep to simulate delay
                Thread.sleep(1000); // Adjust the sleep time as needed
            }
        } catch (InterruptedException e) {
            System.err.println("Error during input simulation: " + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                System.err.println("Erreur lors de l'arrêt du serveur: " + e.getMessage());
            }
        }
    }

    public static void addInput(String message) {
        if (message != null && !message.trim().isEmpty()) {
            jeu.addInput(message);
        }
    }

    public static void setEtatJeu(String nouvelEtat) {
        try {
            lock.lock();
            etatJeu = nouvelEtat;
            sendGameStateToClients();
        } finally {
            lock.unlock();
        }
    }

    public static void sendGameStateToClients() {
        try {
            lock.lock();
            JSONObject gameState = new JSONObject();

            // Ajout des données de base
            gameState.put("etatJeu", etatJeu);
            gameState.put("salaireTotal", jeu.coutSalaireTotal());
            gameState.put("argent", jeu.getArgent());
            gameState.put("pointsRecherche", jeu.getPointsRecherche());
            gameState.put("date", jeu.getDate());

            //liste 
            gameState.put("objectsAchetables", new JSONArray(convertObjectsToJson(jeu.getObjectAchetables())));
            gameState.put("carburants", new JSONArray(convertCarburantToJson(jeu.getCarburantAchetables())));
            gameState.put("recherches", new JSONArray(convertResearchesToJson(jeu.getRecherchesTotal())));
            gameState.put("boosters", new JSONArray(convertLanceurToJson(jeu.getLanceurs())));
            gameState.put("reservoirs", new JSONArray(convertReservoirsToJson(jeu.getReservoirs())));
            gameState.put("fusees", new JSONArray(convertFuseeToJson(jeu.getFusees())));

            //resourcesHuamines
            gameState.put("employes", new JSONArray(convertEmployesToJson(jeu.getEmployes())));

            String gameStateStr = gameState.toString();

            for (Session session : clients) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(gameStateStr);
                    } catch (IOException e) {
                        System.err.println("Erreur d'envoi pour la session " + session.getId() + ": " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'état du jeu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static String convertReservoirsToJson(List<ReservoirPose> reservoirs) {
        JSONArray jsonArray = new JSONArray();
        for (ReservoirPose reservoir : reservoirs) {
            JSONObject reservoirJson = new JSONObject();
            reservoirJson.put("nom", reservoir.getNom());
            reservoirJson.put("type", reservoir.getErgol().getNom());
            reservoirJson.put("quantite", reservoir.getQuantite());
            reservoirJson.put("capacite", reservoir.getQuantiteTotal());
            jsonArray.put(reservoirJson);
        }
        return jsonArray.toString();
    }

    public static String convertCarburantToJson(List<CarburantAchetable> carburants) {
        JSONArray objectsArray = new JSONArray();
        for (CarburantAchetable carburant : carburants) {
            JSONObject objJson = new JSONObject();
            objJson.put("nom", carburant.getNom());
            objJson.put("prix", carburant.getPrix());
            objJson.put("type", carburant.getCarburant());
            objJson.put("estAchetable", carburant.getEstAchetable());
            objJson.put("quantite", carburant.getQuantite());

            objJson.put("capaciteMax", jeu.getCapaciteMaximaleErgol(carburant.getCarburant()));
            objJson.put("quantiteStock", jeu.getQuantiteCarburant(carburant.getCarburant()));

            objectsArray.put(objJson);
        }
        return objectsArray.toString();
    }

    public static String convertEmployesToJson(List<Personne> employes) {
        JSONArray objectsArray = new JSONArray();
        for (Personne e : employes) {
            JSONObject objJson = new JSONObject();
            objJson.put("prenom", e.getPrenom());
            objJson.put("nom", e.getNom());
            objJson.put("salaire", e.getSalaire());
            objJson.put("age", e.getAge());
            objJson.put("sexe", e.getSexe());
            objectsArray.put(objJson);
        }
        return objectsArray.toString();
    }

    public static String convertFuseeToJson(List<Fusee> fusees) {
        JSONArray objectsArray = new JSONArray();
        for (Fusee fusee : fusees) {
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
            objectsArray.put(objJson);
        }
        return objectsArray.toString();
    }

    public static String convertObjectsToJson(List<ObjectAchetable> objects) {
        JSONArray objectsArray = new JSONArray();
        for (ObjectAchetable obj : objects) {
            JSONObject objJson = new JSONObject();
            objJson.put("nom", obj.getNom());
            objJson.put("prix", obj.getPrix());
            objJson.put("estAchetable", obj.getEstAchetable());
            objectsArray.put(objJson);
        }
        return objectsArray.toString();
    }

    public static String convertResearchesToJson(List<Recherche> researches) {
        JSONArray jsonArray = new JSONArray();
        for (Recherche recherche : researches) {
            JSONObject researchObj = new JSONObject();
            researchObj.put("nom", recherche.getNom());
            researchObj.put("prix", recherche.getPrix());
            researchObj.put("temps", recherche.getTemps());
            researchObj.put("description", recherche.getDescription());
            researchObj.put("type", recherche.getType());
            researchObj.put("niveau", recherche.getNiveau());
            researchObj.put("etat", recherche.getEtat());
            researchObj.put("progression", recherche.getProgression());
            jsonArray.put(researchObj);
        }
        return jsonArray.toString();
    }

    public static String convertLanceurToJson(List<Booster> boosters) {
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

            // Ajouter l'historique des lancements sous forme de tableau JSON
            JSONArray historiquesLancementJson = new JSONArray();
            if (booster.getHistoriquesLancement() != null) {
                for (String lancement : booster.getHistoriquesLancement()) {
                    historiquesLancementJson.put(lancement);
                }
            }
            boosterJson.put("historiquesLancement", historiquesLancementJson);

            // Ajouter les moteurs (si disponibles)
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

            // Ajouter les réservoirs (si disponibles)
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
        return jsonArray.toString();
    }

    public static void addClient(Session session) {
        if (session != null && session.isOpen()) {
            clients.add(session);

            try {
                JSONObject gameState = new JSONObject();
                gameState.put("etatJeu", etatJeu);
                gameState.put("argent", jeu.getArgent());
                gameState.put("pointsRecherche", jeu.getPointsRecherche());
                gameState.put("recherches", new JSONArray(convertResearchesToJson(jeu.getRecherchesTotal())));

                session.getBasicRemote().sendText(gameState.toString());
            } catch (Exception e) {
                System.err.println(
                        "Erreur lors de l'initialisation du client " + session.getId() + ": " + e.getMessage());
                removeClient(session);
            }
        }
    }

    public static void removeClient(Session session) {
        if (session != null && session.isOpen()) {
            clients.add(session);
            sendGameStateToClients();
        }
    }
}