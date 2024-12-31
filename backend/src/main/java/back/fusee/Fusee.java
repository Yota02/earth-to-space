package back.fusee;

import java.time.LocalDate;

import back.moteur.Moteur;

public abstract class Fusee {

    public String nom;
    float taille;
    float diametre;

    public float poidsTotal;
    float altitudeMax;

    public Moteur moteur;
    public float carburantRestant;
    float poidChargeUtile;

    public boolean systemeSecurite;
    LocalDate dateLancement;

    public String destination;
    String typeMission;
    int etat;

    protected Fusee(Builder builder) {
        this.nom = builder.nom;
        this.taille = builder.taille;
        this.diametre = builder.diametre;
        this.poidsTotal = builder.poidsTotal;
        this.altitudeMax = builder.altitudeMax;
        this.moteur = builder.moteur;
        this.carburantRestant = builder.carburantRestant;
        this.poidChargeUtile = builder.poidChargeUtile;
        this.systemeSecurite = builder.systemeSecurite;
        this.dateLancement = builder.dateLancement;
        this.destination = builder.destination;
        this.typeMission = builder.typeMission;
        this.etat = builder.etat;
    }

    public abstract void decoler();
    public abstract void exploser();
    public abstract void atterir();
    public abstract void orbite();

    public void afficherDetails() {
        System.out.println("Fusée : " + nom);
        System.out.println("Taille : " + taille + " m");
        System.out.println("Poids total : " + poidsTotal + " kg");
        System.out.println("Moteur : " + moteur.toString());
        System.out.println("Carburant : " + moteur.carburant.getNom());
    }

    public static class Builder {
        public String nom;
        public float taille;
        public float diametre;
        public float poidsTotal;
        public float altitudeMax;
        public Moteur moteur;
        public float carburantRestant;
        public float poidChargeUtile;
        public boolean systemeSecurite;
        public LocalDate dateLancement;
        public String destination;
        public String typeMission;
        public int etat;

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder taille(float taille) {
            this.taille = taille;
            return this;
        }

        public Builder diametre(float diametre) {
            this.diametre = diametre;
            return this;
        }

        public Builder poidsTotal(float poidsTotal) {
            this.poidsTotal = poidsTotal;
            return this;
        }

        public Builder altitudeMax(float altitudeMax) {
            this.altitudeMax = altitudeMax;
            return this;
        }

        public Builder moteur(Moteur moteur) {
            this.moteur = moteur;
            return this;
        }

        public Builder carburantRestant(float carburantRestant) {
            this.carburantRestant = carburantRestant;
            return this;
        }

        public Builder poidChargeUtile(float poidChargeUtile) {
            this.poidChargeUtile = poidChargeUtile;
            return this;
        }

        public Builder systemeSecurite(boolean systemeSecurite) {
            this.systemeSecurite = systemeSecurite;
            return this;
        }

        public Builder dateLancement(LocalDate dateLancement) {
            this.dateLancement = dateLancement;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder typeMission(String typeMission) {
            this.typeMission = typeMission;
            return this;
        }

        public Builder etat(int etat) {
            this.etat = etat;
            return this;
        }

        public Fusee build() {
            return new fuseeTier1(this); 
        }
    }
}
