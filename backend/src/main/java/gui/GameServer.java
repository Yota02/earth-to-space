package gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    // Gestion des clients avec limite de taille
    private static final Map<String, Session> clients = new ConcurrentHashMap<>(100);
    
    // Cache pour réduire la création d'objets
    private static final Map<String, JSONObject> jsonCache = new ConcurrentHashMap<>();
    
    static volatile JeuWebsocket jeu;
    private static volatile boolean isRunning = true;
    private static String etatJeu = "";
    
    // Limites et configurations
    private static final int MAX_CLIENTS = 50;
    private static final int CACHE_SIZE = 100;
    private static final int MESSAGE_QUEUE_SIZE = 1000;
    
    // File d'attente pour les messages
    private static final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    public static void main(String[] args) {
        try {
            initialize();
            startMessageProcessor();
            Runtime.getRuntime().addShutdownHook(new Thread(GameServer::shutdown));
        } catch (Exception e) {
            System.err.println("Erreur critique au démarrage: " + e.getMessage());
            shutdown();
        }
    }

    private static void initialize() throws IOException {
        // Initialisation du jeu
        jeu = new JeuWebsocket(new String[]{"Joueur"});
        
        // Configuration du serveur HTTP
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0", 4242), 0);
        httpServer.createContext("/", new StaticFileHandler("/", "front/", "index.html"));
        httpServer.setExecutor(executorService);
        httpServer.start();

        // Configuration du serveur WebSocket
        Server server = new Server("0.0.0.0", 3232, "/", configureServerProperties(), WebSocketClient.class);
        
        try {
            server.start();
            executorService.submit(jeu);
        } catch (Exception e) {
            throw new IOException("Erreur de démarrage du serveur: " + e.getMessage());
        }
    }

    private static Map<String, Object> configureServerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("org.glassfish.tyrus.incomingBufferSize", 8192);
        properties.put("org.glassfish.tyrus.clientProperties.maxSessionIdleTimeout", 300000);
        return properties;
    }

    private static void startMessageProcessor() {
        executorService.submit(() -> {
            while (isRunning) {
                try {
                    String message = messageQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (message != null) {
                        processMessage(message);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private static void processMessage(String message) {
        if (message == null || message.isEmpty()) return;
        
        try {
            broadcast(createGameState());
        } catch (Exception e) {
            System.err.println("Erreur de traitement du message: " + e.getMessage());
        }
    }

    public static void addClient(Session session) {
        if (session == null || !session.isOpen()) return;
        
        if (clients.size() >= MAX_CLIENTS) {
            try {
                session.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la session: " + e.getMessage());
            }
            return;
        }

        clients.put(session.getId(), session);
        sendInitialState(session);
    }

    private static void sendInitialState(Session session) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(createGameState().toString());
            }
        } catch (Exception e) {
            removeClient(session);
        }
    }

    private static JSONObject createGameState() {
        JSONObject state = new JSONObject();
        state.put("argent", jeu.getArgent());
        state.put("date", jeu.getDate());
        return state;
    }

    private static void broadcast(JSONObject state) {
        String stateStr = state.toString();
        clients.values().removeIf(session -> !session.isOpen());
        
        for (Session session : clients.values()) {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(stateStr);
                }
            } catch (IOException e) {
                removeClient(session);
            }
        }
    }

    public static void removeClient(Session session) {
        if (session != null) {
            clients.remove(session.getId());
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la session: " + e.getMessage());
            }
        }
    }

    public static void shutdown() {
        isRunning = false;
        clients.values().forEach(GameServer::removeClient);
        clients.clear();
        jsonCache.clear();
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("L'executor ne s'est pas terminé proprement");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Méthodes utilitaires pour la gestion de la mémoire
    private static void cleanupResources() {
        clients.values().removeIf(session -> !session.isOpen());
        if (jsonCache.size() > CACHE_SIZE) {
            jsonCache.clear();
        }
        System.gc(); // Suggestion de garbage collection
    }
    
    public static void addInput(String message) {
        if (message != null && !message.trim().isEmpty()) {
            jeu.addInput(message);
        }
    }

    public static void sendGameStateToClients(String partToSend) {
        JSONObject gameState = new JSONObject();
        gameState.put("etatJeu", etatJeu);
        gameState.put("argent", jeu.getArgent());
        gameState.put("date", jeu.getDate());
        
        String gameStateStr = gameState.toString();
        
        clients.values().removeIf(session -> !session.isOpen());
        
        for (Session session : clients.values()) {
            try {
                session.getBasicRemote().sendText(gameStateStr);
            } catch (IOException e) {
                System.err.println("Error sending game state: " + e.getMessage());
                removeClient(session);
            }
        }
    }

    public static JSONArray convertMarcheEmploiToJson(Map<String, List<Personne>> marcheEmploi) {
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
    
        return mainArray;
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
    
    public static String convertPersonneToJson(List<Personne> employes) {
        JSONArray objectsArray = new JSONArray();
        for (Personne e : employes) {
            JSONObject objJson = new JSONObject();
            objJson.put("cleprimaire", e.getClePrimaire());
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

    public static void setEtatJeu(String string) {
        etatJeu = string;
    }

}