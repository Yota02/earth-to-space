package back.booster;

import back.moteur.Moteur;

public class Booster {

    public String nom;
    float taille;
    float diametre;
    public float poidsTotal;

    float altitudeMax;
    public Moteur moteur;
    public float carburantRestant;

    int etat;

    protected Booster(Booster.Builder builder) {
        this.nom = builder.nom;
        this.taille = builder.taille;
        this.diametre = builder.diametre;
        this.poidsTotal = builder.poidsTotal;
        this.altitudeMax = builder.altitudeMax;
        this.moteur = builder.moteur;
        this.carburantRestant = builder.carburantRestant;
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

    public void afficherDetails() {
        System.out.println("Fusée : " + nom);
        System.out.println("Taille : " + taille + " m");
        System.out.println("Poids total : " + poidsTotal + " kg");
        System.out.println("Moteur : " + moteur.toString());
        System.out.println("Carburant : " + moteur.carburant.getNom());
    }

    public static class Builder {
        private String nom;
        private float taille;
        private float diametre;
        private float poidsTotal;
        private float altitudeMax;
        private Moteur moteur;
        private float carburantRestant;
        private int etat;

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

        public Builder etat(int etat) {
            this.etat = etat;
            return this;
        }


        public Booster build() {
            return new Booster(this);
        }
    }
}
