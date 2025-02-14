package back.objectAchetable;

import org.json.JSONObject;

import com.google.gson.JsonObject;

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

    public abstract JSONObject toJson();

    // Abstract method for purchasing
    public abstract void effectuerAchat(Jeu jeu);

    // Generic builder with covariant return type for subclasses
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

        // Ensure that each builder returns the correct type
        protected abstract T self();

        // Update the build method to return ObjectAchetable instead of a specific type
        public abstract ObjectAchetable build();
    }
}