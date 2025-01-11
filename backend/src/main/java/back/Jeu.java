package back;

import back.booster.Booster;
import back.booster.reservoir.Reservoir;
import back.booster.reservoir.ReservoirPose;
import back.moteur.Ergol;
import back.objectAchetable.CarburantAchetable;
import back.objectAchetable.GestionnaireObject;
import back.objectAchetable.ObjectAchetable;
import back.programme.Programme;
import back.recherche.GestionnaireRecherche;
import back.recherche.Recherche;
import gui.GameServer;

import javax.websocket.Session;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Jeu implements Runnable {
    // Attributs
    private int argent;
    private Integer pointsRecherche;

    //element runnable
    private List<String> log;
    private Scanner scanner;
    private ExecutorService executorService;

    // Carburant
    private Map<String, Double> carburantPosseder;
    private List<ReservoirPose> reservoirs;

    //Programmes
    private List<Programme> programmes;

    // Boosters
    private List<Booster> lanceurs;

    // Collections pour les recherches
    private List<Recherche> recherchesTotal;
    private List<Recherche> rechercheObtenue;

    // Collections pour les objets achetables
    private List<ObjectAchetable> objectAchetables;
    private List<ObjectAchetable> objectTotals;
    private List<ObjectAchetable> objectAcheter;

    // Gestionaire
    private GestionnaireRecherche gestionnaireRecherche;
    private GestionnaireObject gestionnaireObject;
    private final Lock researchLock;

    public Jeu(String[] nomsJoueurs) {
        this.argent = 1000;
        this.pointsRecherche = 0;

        this.carburantPosseder = new HashMap<>();

        ReservoirPose reservoir1 = new ReservoirPose.Builder()
    .setNom("Reservoir 1")
    .setErgol(Ergol.OXYGEN)
    .setQuantite(0.0)
    .setQuantiteTotal(1000.0)  // Définir explicitement la capacité totale
    .build();

        this.reservoirs = new ArrayList<>();

        ajouterReservoir(reservoir1);

        this.log = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        this.rechercheObtenue = Collections.synchronizedList(new ArrayList<>());

        this.objectTotals = new ArrayList<>();
        this.objectAcheter = new ArrayList<>();

        this.programmes = new ArrayList<>();

        this.lanceurs = new ArrayList<>();

        this.executorService = Executors.newSingleThreadExecutor();

        this.researchLock = new ReentrantLock();

        gestionnaireRecherche = new GestionnaireRecherche();
        gestionnaireRecherche.initialiserRecherches();
        this.recherchesTotal = gestionnaireRecherche.getRecherches();

        gestionnaireObject = new GestionnaireObject();
        gestionnaireObject.initialiserObject();
        this.objectAchetables = gestionnaireObject.getObjects();
    }

    public Map<String, Double> getCarburants() {
        return carburantPosseder;
    }

    public void acheter(ObjectAchetable objectAchetable) {
        synchronized (objectAcheter) {
            if(getArgent() >= objectAchetable.getPrix()) {
                objectAchetable.effectuerAchat(this);
                retirerArgent(objectAchetable.getPrix());
            } else {
                System.out.println("Vous n'avez pas assez d'argent");
            }
        }
    }

    public double getCapaciteMaximaleErgol() {
        double capaciteMax = 0;
        if (reservoirs != null) {
            for (Reservoir reservoir : reservoirs) {
                if (reservoir != null) {
                    Double quantiteTotal = reservoir.getQuantiteTotal();
                    if (quantiteTotal != null) {
                        capaciteMax += quantiteTotal;
                    }
                }
            }
        }
        return capaciteMax > 0 ? capaciteMax : 1000.0; // Valeur par défaut si aucun réservoir valide
    }

    public double calculerQuantiteTotaleErgol(String nomErgol) {
        double total = 0;
        if (reservoirs != null) {
            for (Reservoir reservoir : reservoirs) {
                if (reservoir != null && 
                    reservoir.getErgol() != null && 
                    reservoir.getErgol().getNom().equals(nomErgol)) {
                    Double quantite = reservoir.getQuantite();
                    if (quantite != null) {
                        total += quantite;
                    }
                }
            }
        }
        return total;
    }

    public void ajouterReservoir(ReservoirPose reservoir) {
        if (reservoir != null) {
            if (reservoirs == null) {
                reservoirs = new ArrayList<>();
            }
            reservoirs.add(reservoir);
        }
    }   

    public List<ReservoirPose> getReservoirs(){
        return reservoirs;
    }

    public void effectuerAchatCarburant(Jeu jeu, CarburantAchetable carburantAchetable, Reservoir reservoir) {
    // Effectuer l'achat de carburant
        carburantAchetable.effectuerAchat(jeu);

    // Ajouter l'ergol au réservoir
        String nomCarburant = carburantAchetable.getCarburant().getNom();
        double quantiteAajouter = carburantAchetable.getQuantite();
    
    // Mettre à jour la quantité de carburant dans le réservoir
        reservoir.ajouterErgol(quantiteAajouter);
    }
    
    public void retirerErgol(String nom, double quantite) {
        synchronized (carburantPosseder) {
            double quantiteActuelle = carburantPosseder.getOrDefault(nom, 0.0);
            if (quantiteActuelle >= quantite) {
                double nouvelleQuantite = quantiteActuelle - quantite;
                carburantPosseder.put(nom, nouvelleQuantite);
                GameServer.setEtatJeu("Mise à jour du carburant: " + nom);
            } else {
                System.out.println("Quantité insuffisante de " + nom);
            }
        }
    }

    public void vendre(ObjectAchetable objectAchetable) {
        synchronized (objectAcheter) {
            objectAcheter.remove(objectAchetable);
        }
    }

    public synchronized void ajouterArgent(int montant) {
        this.argent += montant;
        GameServer.setEtatJeu("Mise à jour de l'argent");
    }

    public synchronized void retirerArgent(int montant) {
        if (this.argent >= montant) {
            this.argent -= montant;
            GameServer.setEtatJeu("Mise à jour de l'argent");
        }
    }

    public synchronized void ajouterPointRecherche(int montant) {
        this.pointsRecherche += montant;
        GameServer.setEtatJeu("Mise à jour des points recherches");
    }

    public Integer getPointsRecherche() {
        return pointsRecherche;
    }

    public void creerUnProgramme(String nom, String objectif){
        programmes.add(new Programme(nom, objectif));
        System.out.println("Nouveau programme " + nom + " " + objectif);
    }   

    public void demarrerRecherche(String rechercheName) {
        System.out.println("Recherche de : " + rechercheName);
        System.out.println("Recherches disponibles : " + recherchesTotal.stream().map(Recherche::getNom).collect(Collectors.toList()));

        Recherche recherche = findRechercheByName(rechercheName);
        if (recherche != null) {
            executorService.submit(() -> {
                try {
                    recherche.setEtat("en cours");
                    recherche.setProgression(0);

                    GameServer.setEtatJeu("Recherche en cours : " + rechercheName);

                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(1000);
                        double currentProgress = recherche.getProgression();
                        double increment = 100.0 / recherche.getTemps();
                        double newProgress = Math.min(100.0, currentProgress + increment);

                        recherche.setProgression(newProgress);

                        if (i % 10 == 0) {
                            notifierClient(recherche);
                        }
                    }

                    if (recherche.getProgression() == 100.0) {
                        recherche.setEtat("terminée");
                        rechercheObtenue.add(recherche);
                    }

                    notifierClient(recherche);
                } catch (InterruptedException e) {
                    recherche.setEtat("échouée");
                    System.out.println("La recherche '" + rechercheName + "' a été interrompue.");
                    notifierClient(recherche);
                }
            });
        } else {
            System.out.println("La recherche '" + rechercheName + "' n'a pas été trouvée.");
        }
    }

    private Recherche findRechercheByName(String name) {
        for (Recherche recherche : recherchesTotal) {
            if (recherche.getNom().equals(name)) {
                return recherche;
            }
        }
        return null;
    }

    @Override
    public void run() {
        while (!estFinie()) {
            researchLock.lock();
            try {
                ajouterArgent(1000);
                ajouterPointRecherche(1);

                synchronized (carburantPosseder) {
                    Set<String> carburantsAConsommer = new HashSet<>(carburantPosseder.keySet());
        
                    for (String nomCarburant : carburantsAConsommer) {
                        retirerErgol(nomCarburant, 10);
                    }
                }

            } finally {
                researchLock.unlock();
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrompu: " + e.getMessage());
                break;
            }
        }
    }

    public ObjectAchetable findObjectByName(String name) {
        for (ObjectAchetable objectAchetable : objectAchetables) {
            if (objectAchetable.getNom().equals(name)) {
                return objectAchetable;
            }
        }
        return null;
    }

    private void notifierClient(Recherche recherche) {
        String message = "{ \"action\": \"updateResearch\", \"name\": \"" + recherche.getNom() + "\", \"etat\": \"" + recherche.getEtat() + "\", \"progression\": " + recherche.getProgression() + " }";
        for (Session session : GameServer.clients) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Recherche> getRecherchesTotal() {
        return recherchesTotal;
    }

    public List<ObjectAchetable> getObjectAchetables() {
        return objectAchetables;
    }

    public void setObjectAchetables(List<ObjectAchetable> objectAchetables) {
        this.objectAchetables = objectAchetables;
    }

    public List<ObjectAchetable> getObjectTotals() {
        return objectTotals;
    }

    public void setObjectTotals(List<ObjectAchetable> objectTotals) {
        this.objectTotals = objectTotals;
    }

    public List<ObjectAchetable> getObjectAcheter() {
        return objectAcheter;
    }

    public void setObjectAcheter(List<ObjectAchetable> objectAcheter) {
        this.objectAcheter = objectAcheter;
    }

    public double getQuantiteCarburant(String name) {
        synchronized (carburantPosseder) {
            return carburantPosseder.getOrDefault(name, 0.0);
        }
    }

    public String lireLigne() {
        return scanner.nextLine();
    }

    public boolean estFinie() {
        return false;
    }

    public int getArgent() {
        return argent;
    }

    public void setArgent(int argent) {
        this.argent = argent;
    }

    public void setPointsRecherche(Integer pointsRecherche) {
        this.pointsRecherche = pointsRecherche;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Map<String, Double> getCarburantPosseder() {
        return carburantPosseder;
    }

    public void setCarburantPosseder(Map<String, Double> carburantPosseder) {
        this.carburantPosseder = carburantPosseder;
    }

    public void setRecherchesTotal(List<Recherche> recherchesTotal) {
        this.recherchesTotal = recherchesTotal;
    }

    public List<Recherche> getRechercheObtenue() {
        return rechercheObtenue;
    }

    public void setRechercheObtenue(List<Recherche> rechercheObtenue) {
        this.rechercheObtenue = rechercheObtenue;
    }

    public GestionnaireRecherche getGestionnaireRecherche() {
        return gestionnaireRecherche;
    }

    public void setGestionnaireRecherche(GestionnaireRecherche gestionnaireRecherche) {
        this.gestionnaireRecherche = gestionnaireRecherche;
    }

    public GestionnaireObject getGestionnaireObject() {
        return gestionnaireObject;
    }

    public void setGestionnaireObject(GestionnaireObject gestionnaireObject) {
        this.gestionnaireObject = gestionnaireObject;
    }

    public Lock getResearchLock() {
        return researchLock;
    }

    public List<Programme> getProgrammes() {
        return programmes;
    }

    public List<Booster> getLanceurs() {
        return lanceurs;
    }

    public void setProgrammes(List<Programme> programmes) {
        this.programmes = programmes;
    }

}