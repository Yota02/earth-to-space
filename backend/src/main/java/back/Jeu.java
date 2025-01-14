package back;

import back.Ressources_Humaines.Personne;
import back.fusee.Fusee;
import back.fusee.booster.Booster;
import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.moteur.Ergol;
import back.fusee.moteur.Moteur;
import back.fusee.reservoir.Reservoir;
import back.fusee.reservoir.ReservoirFusee;
import back.fusee.reservoir.ReservoirPose;
import back.objectAchetable.CarburantAchetable;
import back.objectAchetable.GestionnaireCarburant;
import back.objectAchetable.GestionnaireObject;
import back.objectAchetable.ObjectAchetable;
import back.programme.Programme;
import back.recherche.GestionnaireRecherche;
import back.recherche.Recherche;
import gui.GameServer;

import javax.websocket.Session;

import java.io.IOException;
import java.time.LocalDate;
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

    // element runnable
    private List<String> log;
    private Scanner scanner;
    private ExecutorService executorService;

    // Carburant
    private List<ReservoirPose> reservoirs;
    private List<CarburantAchetable> carburantAchetables;

    // Programmes
    private List<Programme> programmes;

    //resources Humaine
    private List<Personne> employes;
    private List<Personne> marcheEmploi;

    // Boosters
    private List<Booster> lanceurs;

    private List<Fusee> fusees;

    // Collections pour les recherches
    private List<Recherche> recherchesTotal;
    private List<Recherche> rechercheObtenue;

    // Collections pour les objets achetables
    private List<ObjectAchetable> objectAchetables;
    private List<ObjectAchetable> objectTotals;
    private List<ObjectAchetable> objectAcheter;

    // Date
    private LocalDate date;

    // Gestionaire
    private GestionnaireRecherche gestionnaireRecherche;
    private GestionnaireObject gestionnaireObject;
    private GestionnaireCarburant gestionnaireCarburant;
    private final Lock researchLock;

    public Jeu(String[] nomsJoueurs) {
        this.argent = 1000;
        this.pointsRecherche = 0;
        this.date = LocalDate.of(2000, 1, 1);

        this.employes = new ArrayList<>();
        this.marcheEmploi = new ArrayList<>();

        this.reservoirs = new ArrayList<>();
        this.log = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        this.rechercheObtenue = Collections.synchronizedList(new ArrayList<>());

        this.objectTotals = new ArrayList<>();
        this.objectAcheter = new ArrayList<>();
        this.programmes = new ArrayList<>();
        this.lanceurs = new ArrayList<>();
        this.fusees = new ArrayList<>();

        this.executorService = Executors.newSingleThreadExecutor();

        this.researchLock = new ReentrantLock();

        gestionnaireRecherche = new GestionnaireRecherche();
        gestionnaireRecherche.initialiserRecherches();
        this.recherchesTotal = gestionnaireRecherche.getRecherches();

        gestionnaireObject = new GestionnaireObject();
        gestionnaireObject.initialiserObject();
        this.objectAchetables = gestionnaireObject.getObjects();

        gestionnaireCarburant = new GestionnaireCarburant();
        gestionnaireCarburant.initialisationCarburant();
        this.carburantAchetables = gestionnaireCarburant.getObjects();
    }

    private void incrementerDate() {
        date = date.plusDays(1);
    }

    public void acheter(ObjectAchetable objectAchetable) {
        synchronized (objectAcheter) {
            if (getArgent() >= objectAchetable.getPrix()) {
                objectAchetable.effectuerAchat(this);
                retirerArgent(objectAchetable.getPrix());
            } else {
                System.out.println("Vous n'avez pas assez d'argent");
            }
        }
    }

    public void effectuerAchatCarburant(CarburantAchetable carburantAchetable, Ergol ergol) {
        carburantAchetable.effectuerAchat(this);

        double quantiteAajouter = carburantAchetable.getQuantite();
        for (Reservoir r : reservoirs) {
            if (r.getErgol().equals(ergol)) {
                double quantiteDisponibleDansReservoir = r.getQuantiteTotal() - r.getQuantite();
                if (quantiteAajouter <= quantiteDisponibleDansReservoir) {
                    r.ajouterErgol(quantiteAajouter);
                    break;
                } else {
                    r.ajouterErgol(quantiteDisponibleDansReservoir);
                    quantiteAajouter -= quantiteDisponibleDansReservoir;
                }
            }
        }

        retirerArgent(carburantAchetable.getPrix());
    }

    public double getCapaciteMaximaleErgol(Ergol ergol) {
        double capaciteMax = 0;
        if (reservoirs != null) {
            for (Reservoir reservoir : reservoirs) {
                if (reservoir != null) {
                    if (reservoir.getErgol().equals(ergol)) {
                        Double quantiteTotal = reservoir.getQuantiteTotal();
                        if (quantiteTotal != null) {
                            capaciteMax += quantiteTotal;
                        }
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
            if (reservoir.getPrix() <= this.getArgent()) {
                reservoirs.add(reservoir);
                retirerArgent(reservoir.getPrix());
            }
        }
    }

    public List<ReservoirPose> getReservoirs() {
        return reservoirs;
    }

    public void retirerErgol(Ergol nom, double quantite) {
        double quantiteRetiree = 0.0;
        for (Reservoir r : reservoirs) {
            if (r.getErgol().equals(nom)) {
                double quantiteDisponible = r.getQuantite();

                if (quantiteRetiree + quantiteDisponible <= quantite) {
                    // Si la quantité demandée est plus grande que ce qu'il y a dans ce réservoir
                    r.retirerErgol(quantiteDisponible);
                    quantiteRetiree += quantiteDisponible;
                } else {
                    // Si la quantité demandée est partiellement disponible, on retire juste ce
                    // qu'il faut
                    r.retirerErgol(quantite - quantiteRetiree);
                    quantiteRetiree = quantite;
                    break;
                }
            }

            if (quantiteRetiree >= quantite) {
                break;
            }
        }
        if (quantiteRetiree < quantite) {
            System.out.println("Impossible de retirer toute la quantité d'ergol demandée.");
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

    public void creerUnProgramme(String nom, String objectif, double budget, int dureePrevu) {
        // Vérification des doublons
        for (Programme programme : programmes) {
            if (programme.getNom().equalsIgnoreCase(nom)) {
                System.out.println("Erreur : Un programme avec le nom '" + nom + "' existe déjà.");
                return;
            }
        }

        // Ajout du programme
        programmes.add(new Programme(nom, objectif, budget, dureePrevu));
        System.out.println("Nouveau programme créé : " + nom + " - Objectif : " + objectif + " - budget : " + budget
                + " - durePrevu : " + dureePrevu);
    }

    public void demarrerRecherche(String rechercheName) {
        System.out.println("Recherche de : " + rechercheName);
        System.out.println("Recherches disponibles : "
                + recherchesTotal.stream().map(Recherche::getNom).collect(Collectors.toList()));

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

    public CarburantAchetable findCarburantByName(String name) {
        for (CarburantAchetable carburantAchetable : carburantAchetables) {
            if (carburantAchetable.getNom().equals(name)) {
                return carburantAchetable;
            }
        }
        return null;
    }

    public Ergol findErgolByName(String name) {
        for (CarburantAchetable carburantAchetable : carburantAchetables) {
            if (carburantAchetable.getNom().equals(name)) {
                return carburantAchetable.getCarburant();
            }
        }
        return null;
    }

    public void init(){
        ReservoirPose reservoir1 = new ReservoirPose.Builder()
                .setNom("Reservoir 1")
                .setErgol(Ergol.OXYGEN)
                .setQuantite(0.0)
                .setQuantiteTotal(1000.0)
                .build();

        ReservoirPose reservoir2 = new ReservoirPose.Builder()
                .setNom("Reservoir 2")
                .setErgol(Ergol.HYDROGENE)
                .setQuantite(0.0)
                .setQuantiteTotal(1000.0)
                .build();

        ReservoirPose reservoir3 = new ReservoirPose.Builder()
                .setNom("Reservoir 3")
                .setErgol(Ergol.METHANES)
                .setQuantite(0.0)
                .setQuantiteTotal(1000.0)
                .build();
        
        ajouterReservoir(reservoir1);
        ajouterReservoir(reservoir2);
        ajouterReservoir(reservoir3);

        creerUnProgramme("StarShip", "Lune", 1000, 1);

        List<Moteur> moteurs = new ArrayList<>();
        Moteur moteurFusee = new Moteur.Builder()
                .nom("Moteur Fusion 3000")
                .carburant(Ergol.HYDROGENE)
                .rendement(95.0)
                .anneeFabrication(2025)
                .nbFoisUtilise(0)
                .poids(3500.0)
                .diametre(2.5)
                .longueur(10.0)
                .pressionChambre(250.0)
                .temperatureMax(3500.0)
                .tempsFonctionnement(0.0)
                .statutOperationnel("actif")
                .fiabilite(99.5)
                .capaciteRedemarrage(true)
                .temperatureCritique(3800.0)
                .arretUrgence(false)
                .tauxMelange(1.5)
                .pousseeMax(3500.0)
                .consommationCarburant(20.0)
                .build();

        moteurs.add(moteurFusee);

        List<ReservoirFusee> reservoirs = new ArrayList<>();
        ReservoirFusee reservoirFusee = new ReservoirFusee.Builder()
                .setNom("Réservoir Principal")
                .setErgol(Ergol.HYDROGENE)
                .setQuantite(500.0)
                .setQuantiteTotal(1000.0)
                .setPoidsVide(100.0)
                .setPressionInterne(10.0)
                .setCapaciteMaxPression(50.0)
                .setTemperatureInterne(20.0)
                .setTemperatureMax(100.0)
                .setEtatReservoir(true)
                .setIsolationThermique(true)
                .setDebitSortie(20.0)
                .setTaille(1.5)
                .build();

        reservoirs.add(reservoirFusee);

        List<String> historiques = new ArrayList<>();
        historiques.add("Lancement de la mission Mars");

        Booster booster = new Booster(
                "Booster Falcon", 20.0, 3.0, 5000.0, 100000.0, 25000.0, moteurs,
                reservoirs, true, true, false, 1, 12000.0, false, historiques);

        // Ajouter le booster à la liste des lanceurs
        lanceurs.add(booster);

        List<ChargeUtile> chargesUtiles = new ArrayList<>();
        chargesUtiles.add(new ChargeUtile(100.0, "Satellite", 10.0));
        chargesUtiles.add(new ChargeUtile(200.0, "Matériel scientifique", 15.0));

        Fusee f1 = new Fusee("StarShip1", 1000, booster, chargesUtiles, false);
        Fusee f2 = new Fusee("StarShip2", 1000, booster, chargesUtiles, true);

        fusees.add(f1);
        fusees.add(f2); 

        for(int i = 0; i < 10; i++){
            Personne p1 = new Personne();
            embaucherPersonne(p1);
        }

        for(int i = 0; i < 10; i++){
            Personne p1 = new Personne();
            marcheEmploi.add(p1);
        }
    
    }

    public void embaucherPersonne(Personne personne){
        employes.add(personne);
    }

    public void licencierPersonne(Personne personne){
        employes.add(personne);
    }

    public Personne retrouverEmployeParId(int clePrimaire) {
        for (Personne personne : marcheEmploi) {
            if (personne.getClePrimaire() == clePrimaire) {
                return personne;
            }
        }
        return null;  
    }

    @Override
    public void run() {
        init();
        while (!estFinie()) {
            researchLock.lock();
            try {
                ajouterArgent(1000);
                ajouterPointRecherche(1);
                incrementerDate();
                if (date.getDayOfMonth() == date.lengthOfMonth()) { 
                    retirerArgent(coutSalaireTotal());
                    for(int i = 0; i < new Random().nextInt(5) + 1;){
                        marcheEmploi.add(new Personne());
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

    public int coutSalaireTotal(){
        int rep = 0;
        for(Personne p : employes){
            rep += p.getSalaire();
        }
        return rep;
    }

    public ObjectAchetable findObjectByName(String name) {
        for (ObjectAchetable objectAchetable : objectAchetables) {
            if (objectAchetable.getNom().equals(name)) {
                return objectAchetable;
            }
        }
        return null;
    }

    public double getQuantiteCarburant(Ergol name) {
        double quantite = 0.0;
        for (ReservoirPose r : getReservoirs()) {
            if (r.getErgol().equals(name)) {
                quantite += r.getQuantite();
            }
        }
        return quantite;
    }

    public List<CarburantAchetable> getCarburantAchetables() {
        return carburantAchetables;
    }

    private void notifierClient(Recherche recherche) {
        String message = "{ \"action\": \"updateResearch\", \"name\": \"" + recherche.getNom() + "\", \"etat\": \""
                + recherche.getEtat() + "\", \"progression\": " + recherche.getProgression() + " }";
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

    public LocalDate getDate() {
        return date;
    }

    public List<Recherche> getRecherchesTotal() {
        return recherchesTotal;
    }

    public List<Personne> getEmployes() {
        return employes;
    }

    public List<Personne> getMarcheEmploie() {
        return marcheEmploi;
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

    public List<Fusee> getFusees() {
        return fusees;
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