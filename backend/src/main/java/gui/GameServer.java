package gui;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.websocket.Session;
import back.fusee.reservoir.ReservoirPose;
import back.objectAchetable.ObjectAchetable;
import back.recherche.Recherche;
import org.glassfish.tyrus.server.Server;
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
        jeu = new JeuWebsocket(new String[]{"Joueur"});

        Server server = new Server("0.0.0.0", 3232, "/", configureServerProperties(), WebSocketClient.class);
        
        sendGameStateToClients("all");

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
        gameState.put("pointRecherche", jeu.getPointRecherche());
        gameState.put("pointConstruction", jeu.getPointConstruction());
        gameState.put("pointIngenieur", jeu.getPointIngenieur());

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

    public static void setEtatJeu(String string) {
        etatJeu = string;
    }

}