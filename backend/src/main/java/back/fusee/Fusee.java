package back.fusee;

import java.util.List;

import back.chargeUtile.ChargeUtile;
import back.fusee.booster.Booster;

public class Fusee {

    private String nom;
    private double taille;
    private double diametre;

    private double poidsTotal;
    private float altitudeMax;

    private List<Booster> boosters;
    private Booster boosterPrincipal;


    private List<ChargeUtile> poidChargeUtiles;

    private boolean systemeSecurite;

    private int etat;

    // Constructeur privé pour forcer l'utilisation du Builder
    private Fusee(Builder builder) {
        this.nom = builder.nom;
        this.taille = builder.taille;
        this.diametre = builder.diametre;
        this.poidsTotal = builder.poidsTotal;
        this.altitudeMax = builder.altitudeMax;
        this.boosters = builder.boosters;
        this.boosterPrincipal = builder.boosterPrincipal;
        this.poidChargeUtiles = builder.poidChargeUtiles;
        this.systemeSecurite = builder.systemeSecurite;
        this.etat = builder.etat;
    }

    public void decoler() {
        System.out.println("Décollage de la fusée " + nom + "...");
    }

    public void exploser() {
        System.out.println("Boom... Fusée " + nom + " a explosé...");
    }

    public void atterir() {
        System.out.println("Fusée " + nom + " a atterri...");
    }

    public void orbite() {
        System.out.println("La fusée " + nom + " est en orbite.");
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public double getTaille() {
        return taille + boosterPrincipal.getTaille();
    }

    public double getDiametre() {
        return boosterPrincipal.getTaille();
    }

    public double getPoidsTotal() {
        return poidsTotal = boosterPrincipal.getPoids();
    }

    public float getAltitudeMax() {
        return altitudeMax;
    }

    public List<Booster> getBoosters() {
        return boosters;
    }

    public List<ChargeUtile> getPoidChargeUtiles() {
        return poidChargeUtiles;
    }

    public boolean isSystemeSecurite() {
        return systemeSecurite;
    }

    public int getEtat() {
        return etat;
    }

    // Classe Builder
    public static class Builder {
        private String nom;
        private double taille;
        private double diametre;

        private double poidsTotal;
        private float altitudeMax;

        private Booster boosterPrincipal;

        private List<Booster> boosters;

        private List<ChargeUtile> poidChargeUtiles;

        private boolean systemeSecurite;

        private int etat;

        public Builder(String nom) {
            this.nom = nom;
        }

        public Builder taille(double taille) {
            this.taille = taille;
            return this;
        }

        public Builder diametre(double diametre) {
            this.diametre = diametre;
            return this;
        }

        public Builder boosterPricip(Booster boosterPrincip) {
            this.boosterPrincipal = boosterPrincip;
            return this;
        }

        public Builder poidsTotal(double poidsTotal) {
            this.poidsTotal = poidsTotal;
            return this;
        }

        public Builder altitudeMax(float altitudeMax) {
            this.altitudeMax = altitudeMax;
            return this;
        }

        public Builder boosters(List<Booster> boosters) {
            this.boosters = boosters;
            return this;
        }

        public Builder poidChargeUtiles(List<ChargeUtile> poidChargeUtiles) {
            this.poidChargeUtiles = poidChargeUtiles;
            return this;
        }

        public Builder systemeSecurite(boolean systemeSecurite) {
            this.systemeSecurite = systemeSecurite;
            return this;
        }

        public Builder etat(int etat) {
            this.etat = etat;
            return this;
        }

        public Fusee build() {
            return new Fusee(this);
        }
    }
}