package gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

import back.Jeu;
import back.booster.Booster;
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
        String[] nomsJoueurs = {"Joueur"};
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
            gameState.put("etatJeu", etatJeu);
            gameState.put("argent", jeu.getArgent());
            gameState.put("pointsRecherche", jeu.getPointsRecherche());
            gameState.put("recherches", new JSONArray(convertResearchesToJson(jeu.getRecherchesTotal())));
            gameState.put("objectsAchetables", new JSONArray(convertObjectsToJson(jeu.getObjectAchetables())));
            gameState.put("lanceurs", new JSONArray(convertLanceurToJson(jeu.getLanceurs())));

            String gameStateStr = gameState.toString();

            for (Session session : clients) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(gameStateStr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static String convertObjectsToJson(List<ObjectAchetable> objects) {
        JSONArray objectsArray = new JSONArray();
        for (ObjectAchetable obj : jeu.getObjectAchetables()) {
            JSONObject objJson = new JSONObject();
            objJson.put("nom", obj.getNom());
            objJson.put("prix", obj.getPrix());
            objJson.put("estAchetable", obj.getEstAchetable());

            if (obj instanceof CarburantAchetable) {
                CarburantAchetable carburant = (CarburantAchetable) obj;
                objJson.put("type", "carburant");
                objJson.put("quantite", carburant.getQuantite());
                double quantiteStock = jeu.getQuantiteCarburant(carburant.getCarburant().getNom());
                objJson.put("quantiteStock", quantiteStock);
            } else {
                objJson.put("type", "objet");
            }

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
            boosterJson.put("poidsTotal", booster.getPoidsTotal());
            boosterJson.put("altitudeMax", booster.getAltitudeMax());
            boosterJson.put("carburantRestant", booster.getCarburantRestant());    
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
                System.err.println("Erreur lors de l'initialisation du client " + session.getId() + ": " + e.getMessage());
                removeClient(session);
            }
        }
    }

    public static void removeClient(Session session) {
        if (session != null) {
            clients.remove(session);
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la session " + session.getId() + ": " + e.getMessage());
            }
        }
    }
}