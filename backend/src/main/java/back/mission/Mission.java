package back.mission;

import java.time.LocalDateTime;
import back.fusee.Fusee;
import back.fusee.chargeUtile.ChargeUtile;

public class Mission {

    private static int idCounter = 0; // Compteur pour générer les IDs
    private final int missionId; // ID unique pour chaque mission

    private String nomMission;
    private LocalDateTime dateHeureLancement;
    private SiteLancement siteLancement;
    private Fusee fusee;
    private String statutMission;
    private ChargeUtile chargeUtile;
    private int tempsVolEstime;
    private boolean missionReussie;
    private String[] membresEquipage;
    private double tauxReussiteHistorique;
    private boolean lancementAutomatique;
    private TypeMission typeMission;
    private boolean aSubiTestMission;
    private Destination destinationMission;
    private String[] etapeMission;

    // Constructeur privé pour imposer l'utilisation du Builder
    private Mission(Builder builder) {
        this.missionId = ++idCounter; // Incrémente l'ID et l'attribue à la mission
        this.nomMission = builder.nomMission;
        this.dateHeureLancement = builder.dateHeureLancement;
        this.siteLancement = builder.siteLancement;
        this.fusee = builder.fusee;
        this.statutMission = builder.statutMission;
        this.chargeUtile = builder.chargeUtile;
        this.tempsVolEstime = builder.tempsVolEstime;
        this.missionReussie = builder.missionReussie;
        this.membresEquipage = builder.membresEquipage;
        this.tauxReussiteHistorique = builder.tauxReussiteHistorique;
        this.lancementAutomatique = builder.lancementAutomatique;
        this.typeMission = builder.typeMission;
        this.aSubiTestMission = builder.aSubiTestMission;
        this.destinationMission = builder.destinationMission;
        this.etapeMission = builder.etapeMission;
    }

    public int getMissionId() { return missionId; }
    public String getNomMission() { return nomMission; }
    public LocalDateTime getDateHeureLancement() { return dateHeureLancement; }
    public SiteLancement getSiteLancement() { return siteLancement; }
    public Fusee getFusee() { return fusee; }
    public String getStatutMission() { return statutMission; }
    public ChargeUtile getChargeUtile() { return chargeUtile; }
    public int getTempsVolEstime() { return tempsVolEstime; }
    public boolean isMissionReussie() { return missionReussie; }
    public String[] getMembresEquipage() { return membresEquipage; }
    public double getTauxReussiteHistorique() { return tauxReussiteHistorique; }
    public boolean isLancementAutomatique() { return lancementAutomatique; }
    public TypeMission getTypeMission() { return typeMission; }
    public boolean isASubiTestMission() { return aSubiTestMission; }
    public Destination getDestinationMission() { return destinationMission; }
    public String[] getEtapeMission() { return etapeMission; }

    // Classe statique Builder
    public static class Builder {

        private String nomMission;
        private LocalDateTime dateHeureLancement;
        private SiteLancement siteLancement;
        private Fusee fusee;
        private String statutMission;
        private ChargeUtile chargeUtile;
        private int tempsVolEstime;
        private boolean missionReussie;
        private String[] membresEquipage;
        private double tauxReussiteHistorique;
        private boolean lancementAutomatique;
        private TypeMission typeMission;
        private boolean aSubiTestMission;
        private Destination destinationMission;
        private String[] etapeMission;

        public Builder nomMission(String nomMission) {
            this.nomMission = nomMission;
            return this;
        }

        public Builder dateHeureLancement(LocalDateTime dateHeureLancement) {
            this.dateHeureLancement = dateHeureLancement;
            return this;
        }

        public Builder siteLancement(SiteLancement siteLancement) {
            this.siteLancement = siteLancement;
            return this;
        }

        public Builder fusee(Fusee fusee) {
            this.fusee = fusee;
            return this;
        }

        public Builder statutMission(String statutMission) {
            this.statutMission = statutMission;
            return this;
        }

        public Builder chargeUtile(ChargeUtile chargeUtile) {
            this.chargeUtile = chargeUtile;
            return this;
        }

        public Builder tempsVolEstime(int tempsVolEstime) {
            this.tempsVolEstime = tempsVolEstime;
            return this;
        }

        public Builder missionReussie(boolean missionReussie) {
            this.missionReussie = missionReussie;
            return this;
        }

        public Builder membresEquipage(String[] membresEquipage) {
            this.membresEquipage = membresEquipage;
            return this;
        }

        public Builder tauxReussiteHistorique(double tauxReussiteHistorique) {
            this.tauxReussiteHistorique = tauxReussiteHistorique;
            return this;
        }

        public Builder lancementAutomatique(boolean lancementAutomatique) {
            this.lancementAutomatique = lancementAutomatique;
            return this;
        }

        public Builder typeMission(TypeMission typeMission) {
            this.typeMission = typeMission;
            return this;
        }

        public Builder aSubiTestMission(boolean aSubiTestMission) {
            this.aSubiTestMission = aSubiTestMission;
            return this;
        }

        public Builder destinationMission(Destination destinationMission) {
            this.destinationMission = destinationMission;
            return this;
        }

        public Builder etapeMission(String[] etapeMission) {
            this.etapeMission = etapeMission;
            return this;
        }

        public Mission build() {
            return new Mission(this);
        }
    }
}