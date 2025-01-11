package back.objectAchetable;

import back.Jeu;
import back.booster.reservoir.Reservoir;
import back.moteur.Ergol;

public class CarburantAchetable extends ObjectAchetable {
    private final Ergol carburant;
    private final double quantite;
    private Double capaciteMax;
    private Double quantiteStock;

    private CarburantAchetable(Builder builder) {
        super(builder);
        this.carburant = builder.carburant;
        this.quantite = builder.quantite;
        this.capaciteMax = builder.capaciteMax;
        this.quantiteStock = builder.quantiteStock;
    }

    public Ergol getCarburant() {
        return carburant;
    }

    public double getQuantite() {
        return quantite;
    }

    public Double getCapaciteMax() {
        return capaciteMax;
    }

    public Double getQuantiteStock() {
        return quantiteStock;
    }
    
    @Override
    public void effectuerAchat(Jeu jeu) {
        // Calculate available capacity in reservoirs
        double capaciteDisponible = jeu.getCapaciteMaximaleErgol() - jeu.calculerQuantiteTotaleErgol(carburant.getNom());
        
        if (capaciteDisponible >= quantite) {
            // Update the game's fuel map
            jeu.getCarburants().merge(carburant.getNom(), quantite, Double::sum);
            
            // Find suitable reservoir and add fuel
            for (Reservoir reservoir : jeu.getReservoirs()) {
                if (reservoir.getErgol().equals(carburant)) {
                    double quantiteRestante = reservoir.getQuantiteTotal() - reservoir.getQuantite();
                    if (quantiteRestante > 0) {
                        double quantiteAAjouter = Math.min(quantite, quantiteRestante);
                        reservoir.ajouterErgol(quantiteAAjouter);
                        break;
                    }
                }
            }
            
            // Update stock quantity
            this.quantiteStock = jeu.calculerQuantiteTotaleErgol(carburant.getNom());
        } else {
            System.out.println("Capacité de stockage insuffisante pour " + carburant.getNom());
        }
    }

    public static class Builder extends ObjectAchetable.Builder<Builder> {
        private Ergol carburant;
        private double quantite;
        private Double capaciteMax;
        private Double quantiteStock;

        public Builder setCarburant(Ergol carburant) {
            this.carburant = carburant;
            return this;
        }

        public Builder setQuantite(double quantite) {
            this.quantite = quantite;
            return this;
        }

        public Builder setCapaciteMax(Double capaciteMax) {
            this.capaciteMax = capaciteMax;
            return this;
        }

        public Builder setQuantiteStock(Double quantiteStock) {
            this.quantiteStock = quantiteStock;
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