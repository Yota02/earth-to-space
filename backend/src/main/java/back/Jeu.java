package back;

import back.Batiment.BatimentManager;
import back.Batiment.HangarAssemblage;
import back.Batiment.IBatiment;
import back.Batiment.UsineProduction;
import back.Batiment.UsineProductionCarburant;
import back.MarcheFinancier.Entreprise;
import back.MarcheFinancier.GestionnaireMarche;
import back.Metaux.Materiaux;
import back.Ressources_Humaines.GestionnaireRessources_Humaines;
import back.Ressources_Humaines.Ingenieur;
import back.Ressources_Humaines.Personne;
import back.Ressources_Humaines.PersonneSimple;
import back.Ressources_Humaines.Scientifique;
import back.fusee.Fusee;
import back.fusee.Piece.PieceFusee;
import back.fusee.booster.Booster;
import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.moteur.Ergol;
import back.fusee.moteur.GestionaireMoteur;
import back.fusee.moteur.Moteur;
import back.fusee.reservoir.Reservoir;
import back.fusee.reservoir.ReservoirFusee;
import back.fusee.reservoir.ReservoirPose;
import back.mission.Destination;
import back.mission.Mission;
import back.mission.SiteLancement;
import back.mission.TypeMission;
import back.objectAchetable.CarburantAchetable;
import back.objectAchetable.GestionnaireCarburant;
import back.objectAchetable.GestionnaireObject;
import back.objectAchetable.ObjectAchetable;
import back.politique.PolitiqueManager;
import back.programme.Programme;
import back.recherche.GestionnaireRecherche;
import back.recherche.Recherche;
import gui.GameServer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Jeu implements Runnable {
    // Attributs
    private int argent;
    private double pointsRecherche;
    private double pointsConstruction;
    private double pointsIngenieur;

    // element runnable
    private List<String> log;
    private Scanner scanner;
    private ExecutorService executorService;

    private Map<PieceFusee, Integer> stockPieces;

    // Carburant
    private List<ReservoirPose> reservoirs;
    private List<CarburantAchetable> carburantAchetables;

    // Programmes
    private List<Programme> programmes;

    // resources Humaine

    // Boosters
    private List<Booster> lanceurs;

    private List<Fusee> fusees;

    private List<Mission> missions;
    private Boolean missionEnCours;
    private Boolean DecolageMoinsUneMinutes;

    // Collections pour les recherches
    private List<Recherche> recherchesTotal;
    private List<Recherche> rechercheObtenue;

    private PolitiqueManager politiqueManager;

    private GestionnaireMarche gestionaireMarche;

    // batiments
    BatimentManager batimentManager;
    // Date
    private LocalDateTime date;

    // Gestionaire
    private GestionnaireRecherche gestionnaireRecherche;
    private GestionnaireObject gestionnaireObject;
    private GestionnaireCarburant gestionnaireCarburant;
    private GestionaireMoteur moteurManager;
    private GestionnaireRessources_Humaines gestionnaireRH;

    private Entreprise entreprise;
    private boolean debugMode = true;

    private int argentParMoi;

    public Jeu(String[] nomsJoueurs) {
        this.argent = 1000;
        this.pointsRecherche = 0;
        this.pointsConstruction = 0;
        this.pointsIngenieur = 0;

        this.argentParMoi = 1000;

        this.stockPieces = new HashMap<>();

        this.moteurManager = new GestionaireMoteur();

        this.politiqueManager = new PolitiqueManager();

        this.date = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

        this.missionEnCours = false;
        this.DecolageMoinsUneMinutes = false;

        this.reservoirs = new ArrayList<>();
        this.log = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        this.rechercheObtenue = Collections.synchronizedList(new ArrayList<>());

        this.programmes = new ArrayList<>();
        this.lanceurs = new ArrayList<>();
        this.fusees = new ArrayList<>();
        this.missions = new ArrayList<>();

        // Batiments
        this.batimentManager = new BatimentManager();

        this.executorService = Executors.newSingleThreadExecutor();

        gestionnaireRH = new GestionnaireRessources_Humaines();

        gestionnaireRecherche = new GestionnaireRecherche(batimentManager, moteurManager);
        gestionnaireRecherche.initialiserRecherches();
        this.recherchesTotal = gestionnaireRecherche.getRecherches();

        gestionnaireObject = new GestionnaireObject();

        moteurManager = new GestionaireMoteur();

        gestionnaireCarburant = new GestionnaireCarburant();
        gestionnaireCarburant.initialisationCarburant();
        this.carburantAchetables = gestionnaireCarburant.getObjects();

        this.gestionaireMarche = new GestionnaireMarche();
    }

    public void setDebugMode(boolean mode) {
        this.debugMode = mode;
    }
    
    public void createEntreprise(String nom, String pays) {
        if (!debugMode) {
            this.entreprise = new Entreprise(nom, pays, 0,1, 0, 0);
        }
    }

    public GestionnaireMarche getGestionaireMarche(){
        return gestionaireMarche;
    }

    public PolitiqueManager getPolitiqueManager() {
        return politiqueManager;
    }

    public void acheter(ObjectAchetable objectAchetable) {
        synchronized (gestionnaireObject.getObjectAcheter()) {
            if (getArgent() >= objectAchetable.getPrix()) {
                objectAchetable.effectuerAchat(this);
                retirerArgent(objectAchetable.getPrix());
            } else {
                System.out.println("Vous n'avez pas assez d'argent");
            }
        }
    }

    public void vendre(ObjectAchetable objectAchetable) {
        synchronized (gestionnaireObject.getObjectAcheter()) {
            gestionnaireObject.getObjectAcheter().remove(objectAchetable);
        }
    }

    public BatimentManager getBatimentManager() {
        return batimentManager;
    }

    public double ajouterCarburant(Ergol ergol, double quantite) {
        double quantiteRestante = quantite;

        for (Reservoir r : reservoirs) {
            if (r.getErgol().equals(ergol)) {
                double espaceDisponible = r.getQuantiteTotal() - r.getQuantite();
                if (quantiteRestante <= espaceDisponible) {
                    r.ajouterErgol(quantiteRestante);
                    return 0; // Tout a été stocké
                } else {
                    r.ajouterErgol(espaceDisponible);
                    quantiteRestante -= espaceDisponible;
                }
            }
        }

        return quantiteRestante; // Retourne la quantité qui n'a pas pu être stockée
    }

    public void effectuerAchatCarburant(CarburantAchetable carburantAchetable, Ergol ergol) {
        // carburantAchetable.effectuerAchat(this);

        double quantiteAajouter = carburantAchetable.getQuantite();
        ajouterCarburant(ergol, quantiteAajouter);

        retirerArgent(carburantAchetable.getPrix());
    }

    public void achatErgolFinDuMoi() {
        for (CarburantAchetable carburant : getCarburantAchetables()) {
            if (carburant.getDemandeMonthly() >= 0) {

                int totalCost = carburant.getDemandeMonthly() * carburant.getPrix();

                if (getArgent() >= totalCost) {
                    try {

                        carburant.effectuerAchat(this, true);
                        
                        retirerArgent(totalCost);
                    } catch (IllegalStateException e) {
                        System.err.println(carburant.getNom() + ": " + e.getMessage());
                    }
                }
            }
        }
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
        return capaciteMax > 0 ? capaciteMax : 1000.0;
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


    public synchronized void ajouterArgent(int montant) {
        this.argent += montant;
    }

    public synchronized void retirerArgent(int montant) {
        if (this.argent >= montant) {
            this.argent -= montant;
            GameServer.setEtatJeu("Mise à jour de l'argent");
        }
    }

    public synchronized void setPointRecherche(double montant) {
        this.pointsRecherche = montant;
    }

    public synchronized void setPointIngenieur(double montant) {
        this.pointsIngenieur = montant;
    }

    public synchronized void setPointConstruction(double montant) {
        this.pointsConstruction = montant;
    }

    public void creerUnProgramme(String nom, String objectif, double budget, int dureePrevu) {
        // Vérification des doublons
        for (Programme programme : programmes) {
            if (programme.getNom().equalsIgnoreCase(nom)) {
                System.out.println("Erreur : Un programme avec le nom '" + nom + "' existe déjà.");
                return;
            }
        }

        programmes.add(new Programme(nom, objectif, budget, dureePrevu));
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

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void init() {

        if (debugMode) {
            this.entreprise = new Entreprise("Space Y", "USA", 1000000, 100, 0, 0);
        }

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

        reservoirs.add(reservoir1);
        reservoirs.add(reservoir2);
        reservoirs.add(reservoir3);

        creerUnProgramme("StarShip", "Lune", 1000, 1);

    }

    public double getPointRecherche() {
        return pointsRecherche;
    }

    public double getPointIngenieur() {
        return pointsIngenieur;
    }

    public double getPointConstruction() {
        return pointsConstruction;
    }

    public double calculerPointRecherche() {
        double res = 0;
        if (gestionnaireRH.getPersonnesParTypeMap().containsKey("Scientifique")) {
            List<Personne> scientifiques = gestionnaireRH.getPersonnesParTypeMap().get("Scientifique");

            for (Personne p : scientifiques) {
                Scientifique s = (Scientifique) p;
                res += s.getTalent();
            }
        }

        return res;
    }

    public double calculerPointConstruction() {
        double res = 0;
        if (gestionnaireRH.getPersonnesParTypeMap().containsKey("Ouvrier")) {
            List<Personne> ouvriers = gestionnaireRH.getPersonnesParTypeMap().get("Ouvrier");
            res = ouvriers.size();
        }
        return res;
    }

    public double calculerPointIngenieur() {
        double res = 0;
        if (gestionnaireRH.getPersonnesParTypeMap().containsKey("Ingenieur")) {
            List<Personne> ingenieurs = gestionnaireRH.getPersonnesParTypeMap().get("Ingenieur");

            for (Personne p : ingenieurs) {
                Ingenieur i = (Ingenieur) p;
                res += i.getTalent();
            }
        }
        return res;
    }

    private void actionFinDeMoi() {
        if (date.toLocalDate().getDayOfMonth() == date.toLocalDate().lengthOfMonth()) {
            retirerArgent(coutSalaireTotal());
            setPointRecherche(calculerPointRecherche());
            setPointIngenieur(calculerPointRecherche());
            setPointConstruction(calculerPointRecherche());

            batimentManager.entrainerAstronautes();
            gestionnaireRH.ajouterPersonneMensuel();

            achatErgolFinDuMoi();
            gestionnaireRecherche.rechercheParMoi();
        }
    }

    private void actionFinDeAnnee() {
        if (date.toLocalDate().getDayOfYear() == date.toLocalDate().lengthOfYear()) {
            gestionnaireRH.ajouterPersonneAnuel();
        }
    }

    private void assemblerFusee() {
        for (IBatiment b : batimentManager.getBatimentsPossedes()) {
            if (b instanceof HangarAssemblage) {
                HangarAssemblage h = (HangarAssemblage) b;
                h.assemblerTouteFusee(this.pointsIngenieur);
            }
        }
    }

    private void productionCarburant() {
        for (IBatiment b : batimentManager.getBatimentsPossedes()) {
            if (b instanceof UsineProductionCarburant) {
                UsineProductionCarburant u = (UsineProductionCarburant) b;
                ajouterCarburant(u.getErgol(), u.getQuantiteProduiteParJour());
            }
        }
    }

    private void ajouterPieceParJour(){
        for (PieceFusee p : PieceFusee.values()) {
            ajouterQuantiteAStockPiece(p, batimentManager.getProductionParJour(p)); 
        }
    }

    private void actionFinJour() {
        if (!missionEnCours) {
            ajouterArgent(argentParMoi);
            incrementerDate();
            assemblerFusee();
            productionCarburant();
            ajouterPieceParJour();
            
            for (IBatiment b : getBatimentsEnConstruction()) {
                b.construireParJour(this.pointsConstruction);
            }
        }
    }

    public void ajouterQuantiteAStockPiece(PieceFusee piece, double quantite) {
            stockPieces.put(piece, (int) (stockPieces.getOrDefault(piece, 0) + quantite));
    }

    public List<IBatiment> getBatimentsEnConstruction() {
        List<IBatiment> liste = new ArrayList<>();
        for (IBatiment b : batimentManager.getBatimentsPossedes()) {
            if (b.getEnConstruction()) {
                liste.add(b);
            }
        }
        return liste;
    }

    public void setmissionEnCours(boolean missionEnCours) {
        this.missionEnCours = missionEnCours;

        if (missionEnCours) {
            for (Mission mission : missions) {
                if (mission.getDateHeureLancement() != null) {
                    mission.adjustDateForMissionEnCorus();
                }
            }
        }
    }

    public boolean isMissionEnCorus() {
        return missionEnCours;
    }

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    @Override
    public void run() {
        if (debugMode) {
            init();
        } else if (entreprise == null) {
            while (entreprise == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            init();
        }

        while (!estFinie()) {
            /*
             * Mission currentMission = missions.get(0);
             * if (!missionEnCours && currentMission != null) {
             * LocalDateTime launchTime = currentMission.getDateHeureLancement();
             * if (date.equals(launchTime)) {
             * setmissionEnCours(true);
             * fusees.get(0).decoler();
             * }
             * }
             */

            actionFinJour();
            actionFinDeMoi();
            actionFinDeAnnee();
            GameServer.sendGameStateToClients("all");

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrompu: " + e.getMessage());
                break;
            }
        }
    }

    public boolean canStart() {
        return debugMode || entreprise != null;
    }

    private void incrementerDate() {
        if (date == null) {
            throw new IllegalStateException("La date n'est pas initialisée !");
        }

        date = date.plusDays(1);

        /*
         * if (missionEnCours) {
         * date = date.plusMinutes(1);
         * } else {
         * Mission currentMission = missions.get(0);
         * LocalDateTime launchTime = currentMission.getDateHeureLancement();
         * Duration timeToLaunch = Duration.between(date, launchTime);
         * long minutesToLaunch = Math.abs(timeToLaunch.toMinutes());
         * 
         * if (minutesToLaunch <= 10) {
         * date = date.plusSeconds(1);
         * if (date.equals(launchTime)) {
         * setmissionEnCours(true);
         * fusees.get(0).decoler();
         * }
         * } else {
         * date = date.plusDays(1);
         * }
         * }
         */
    }

    public int coutSalaireTotal() {
        int coutTotal = 0;
        for (List<Personne> listeEmployes : gestionnaireRH.getEmployeMap().values()) {
            for (Personne employe : listeEmployes) {
                coutTotal += employe.getSalaire();
            }
        }
        return coutTotal;
    }

    public GestionnaireRessources_Humaines getGestionnaireRH() {
        return gestionnaireRH;
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

    public LocalDateTime getDate() {
        return date;
    }

    public List<Recherche> getRecherchesTotal() {
        return recherchesTotal;
    }

    public List<Mission> getMissions() {
        return missions;
    }


    public List<ObjectAchetable> getObjectAchetables() {
        return gestionnaireObject.getObjects();
    }

    public List<Fusee> getFusees() {
        return fusees;
    }


    public List<ObjectAchetable> getObjectAcheter() {
        return gestionnaireObject.getObjectAcheter();
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