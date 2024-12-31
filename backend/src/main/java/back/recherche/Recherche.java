package back.recherche;

import java.util.UUID;

public class Recherche {

    private final String id;
    private int prix;
    private String nom;
    private double temps;
    private String description;
    private String type;
    private String categorie;
    private int niveau;
    private String etat;
    private double progression;

    private Recherche(Builder builder) {
        this.id = UUID.randomUUID().toString();
        this.prix = builder.prix;
        this.nom = builder.nom;
        this.temps = builder.temps;
        this.description = builder.description;
        this.type = builder.type;
        this.categorie = builder.categorie;
        this.niveau = builder.niveau;
        this.etat = builder.etat;
        this.progression = builder.progression;
    }

    public String getId() {
        return id;
    }

    public int getPrix() {
        return prix;
    }

    public String getNom() {
        return nom;
    }

    public double getTemps() {
        return temps;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getCategorie() {
        return categorie;
    }

    public int getNiveau() {
        return niveau;
    }

    public String getEtat() {
        return etat;
    }

    public double getProgression() {
        return progression;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setProgression(double progression) {
        this.progression = progression;
    }

    public static class Builder {
        private int prix;
        private String nom;
        private double temps;
        private String description;
        private String type;
        private String categorie;
        private int niveau;
        private String etat;
        private double progression;

        public Builder setPrix(int prix) {
            this.prix = prix;
            return this;
        }

        public Builder setNom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder setTemps(double temps) {
            this.temps = temps;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setCategorie(String categorie) {
            this.categorie = categorie;
            return this;
        }

        public Builder setNiveau(int niveau) {
            this.niveau = niveau;
            return this;
        }

        public Builder setEtat(String etat) {
            this.etat = etat;
            return this;
        }

        public Builder setProgression(double progression) {
            this.progression = progression;
            return this;
        }

        public Recherche build() {
            return new Recherche(this);
        }
    }
}
