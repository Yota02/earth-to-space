package back.booster;

import java.util.List;

import back.booster.reservoir.Reservoir;
import back.moteur.Moteur;

public class Booster {
    
    // Spécification basique
    public String nom;
    public Double taille;
    public Double diametre;
    public Double poidsAVide;
    public Double altitudeMax;
    public Double VitesseMax;

    // Element compose
    public List<Moteur> moteur;
    public List<Reservoir> reservoirs;

    // Spécification spécial
    public Boolean estPrototype;
    public Boolean estReetulisable;
    public Boolean aSystèmeAutoDestruction;

    // Etat
    public int etat;
    public Double poids;
    public Double vitesse;
    public Boolean nécessiteMaintenance;

    // Historique
    List<String> historiquesLancement;

    // Builder pour la classe Booster
    public static class Builder {

        private String nom;
        private Double taille;
        private Double diametre;
        private Double poidsAVide;
        private Double altitudeMax;
        private Double VitesseMax;

        private List<Moteur> moteur;
        private List<Reservoir> reservoirs;

        private Boolean estPrototype;
        private Boolean estReetulisable;
        private Boolean aSystèmeAutoDestruction;

        private int etat;
        private Double poids;
        private Double vitesse;
        private Boolean nécessiteMaintenance;

        private List<String> historiquesLancement;

        // Constructeur du Builder
        public Builder() {
        }

        // Méthodes de construction pour chaque attribut
        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder taille(Double taille) {
            this.taille = taille;
            return this;
        }

        public Builder diametre(Double diametre) {
            this.diametre = diametre;
            return this;
        }

        public Builder poidsAVide(Double poidsAVide) {
            this.poidsAVide = poidsAVide;
            return this;
        }

        public Builder altitudeMax(Double altitudeMax) {
            this.altitudeMax = altitudeMax;
            return this;
        }

        public Builder VitesseMax(Double VitesseMax) {
            this.VitesseMax = VitesseMax;
            return this;
        }

        public Builder moteur(List<Moteur> moteur) {
            this.moteur = moteur;
            return this;
        }

        public Builder reservoirs(List<Reservoir> reservoirs) {
            this.reservoirs = reservoirs;
            return this;
        }

        public Builder estPrototype(Boolean estPrototype) {
            this.estPrototype = estPrototype;
            return this;
        }

        public Builder estReetulisable(Boolean estReetulisable) {
            this.estReetulisable = estReetulisable;
            return this;
        }

        public Builder aSystèmeAutoDestruction(Boolean aSystèmeAutoDestruction) {
            this.aSystèmeAutoDestruction = aSystèmeAutoDestruction;
            return this;
        }

        public Builder etat(int etat) {
            this.etat = etat;
            return this;
        }

        public Builder poids(Double poids) {
            this.poids = poids;
            return this;
        }

        public Builder vitesse(Double vitesse) {
            this.vitesse = vitesse;
            return this;
        }

        public Builder nécessiteMaintenance(Boolean nécessiteMaintenance) {
            this.nécessiteMaintenance = nécessiteMaintenance;
            return this;
        }

        public Builder historiquesLancement(List<String> historiquesLancement) {
            this.historiquesLancement = historiquesLancement;
            return this;
        }

        // Méthode pour construire un objet Booster
        public Booster build() {
            Booster booster = new Booster();
            booster.nom = this.nom;
            booster.taille = this.taille;
            booster.diametre = this.diametre;
            booster.poidsAVide = this.poidsAVide;
            booster.altitudeMax = this.altitudeMax;
            booster.VitesseMax = this.VitesseMax;
            booster.moteur = this.moteur;
            booster.reservoirs = this.reservoirs;
            booster.estPrototype = this.estPrototype;
            booster.estReetulisable = this.estReetulisable;
            booster.aSystèmeAutoDestruction = this.aSystèmeAutoDestruction;
            booster.etat = this.etat;
            booster.poids = this.poids;
            booster.vitesse = this.vitesse;
            booster.nécessiteMaintenance = this.nécessiteMaintenance;
            booster.historiquesLancement = this.historiquesLancement;
            return booster;
        }
    }


    public String getNom() {
        return nom;
    }

    public Double getTaille() {
        return taille;
    }

    public Double getDiametre() {
        return diametre;
    }

    public Double getPoidsAVide() {
        return poidsAVide;
    }

    public Double getAltitudeMax() {
        return altitudeMax;
    }

    public Double getVitesseMax() {
        return VitesseMax;
    }

    public List<Moteur> getMoteur() {
        return moteur;
    }

    public List<Reservoir> getReservoirs() {
        return reservoirs;
    }

    public Boolean getEstPrototype() {
        return estPrototype;
    }

    public Boolean getEstReetulisable() {
        return estReetulisable;
    }

    public Boolean getASystèmeAutoDestruction() {
        return aSystèmeAutoDestruction;
    }

    public int getEtat() {
        return etat;
    }

    public Double getPoids() {
        return poids;
    }

    public Double getVitesse() {
        return vitesse;
    }

    public Boolean getNécessiteMaintenance() {
        return nécessiteMaintenance;
    }

    public List<String> getHistoriquesLancement() {
        return historiquesLancement;
    }
}
