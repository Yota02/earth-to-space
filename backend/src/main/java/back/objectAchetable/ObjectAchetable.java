package back.objectAchetable;

import back.Jeu;

public abstract class ObjectAchetable {
    private final int prix;
    private final String nom;
    private final Boolean estAchetable;

    protected ObjectAchetable(Builder<?> builder) {
        this.prix = builder.prix;
        this.nom = builder.nom;
        this.estAchetable = builder.estAchetable;
    }

    public int getPrix() {
        return prix;
    }

    public String getNom() {
        return nom;
    }

    public Boolean getEstAchetable() {
        return estAchetable;
    }

    // Méthode abstraite pour l'achat
    public abstract void effectuerAchat(Jeu jeu);

    // Builder générique avec covariant pour les sous-classes
    public static abstract class Builder<T extends Builder<T>> {
        private int prix;
        private String nom;
        private Boolean estAchetable;

        public T setPrix(int prix) {
            this.prix = prix;
            return self();
        }

        public T setNom(String nom) {
            this.nom = nom;
            return self();
        }

        public T setEstAchetable(Boolean estAchetable) {
            this.estAchetable = estAchetable;
            return self();
        }

        protected abstract T self();
        public abstract ObjectAchetable build();
    }
}