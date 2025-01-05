package back.objectAchetable;

import back.Jeu;
import back.moteur.Ergol;

public class CarburantAchetable extends ObjectAchetable {
    private final Ergol carburant;
    private final double quantite;

    private CarburantAchetable(Builder builder) {
        super(builder);
        this.carburant = builder.carburant;
        this.quantite = builder.quantite;
    }

    public Ergol getCarburant() {
        return carburant;
    }

    public double getQuantite() {
        return quantite;
    }

    @Override
    public void effectuerAchat(Jeu jeu) {
        String nomCarburant = this.carburant.getNom();
        jeu.getCarburants().merge(nomCarburant, this.quantite, Double::sum);
    }

    public static class Builder extends ObjectAchetable.Builder<Builder> {
        private Ergol carburant;
        private double quantite;

        public Builder setCarburant(Ergol carburant) {
            this.carburant = carburant;
            return this;
        }

        public Builder setQuantite(double quantite) {
            this.quantite = quantite;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public CarburantAchetable build() {
            return new CarburantAchetable(this);
        }
    }
}