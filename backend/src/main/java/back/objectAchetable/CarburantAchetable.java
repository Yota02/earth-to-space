package back.objectAchetable;

import back.Jeu;
import back.fusee.reservoir.Reservoir;
import back.moteur.Ergol;

public class CarburantAchetable {
    private final Ergol carburant;
    private final double quantite;
    private final Double capaciteMax;
    private Double quantiteStock;

    private final int prix;
    private final String nom;
    private final Boolean estAchetable;

    private CarburantAchetable(Builder builder) {
        this.carburant = builder.carburant;
        this.quantite = builder.quantite;
        this.capaciteMax = builder.capaciteMax;
        this.quantiteStock = builder.quantiteStock != null ? builder.quantiteStock : 0.0;
        this.prix = builder.prix;
        this.nom = builder.nom;
        this.estAchetable = builder.estAchetable;
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

    public int getPrix() {
        return prix;
    }

    public String getNom() {
        return nom;
    }

    public Boolean getEstAchetable() {
        return estAchetable;
    }

    /**
     * Effectue l'achat du carburant en vérifiant les contraintes de capacité et met à jour les réservoirs.
     *
     * @param jeu Instance du jeu contenant les informations globales.
     * @throws IllegalStateException si la capacité de stockage est insuffisante.
     */
    public void effectuerAchat(Jeu jeu) {
        // Calcul de la capacité disponible
        double capaciteDisponible = jeu.getCapaciteMaximaleErgol() - jeu.calculerQuantiteTotaleErgol(carburant.getNom());

        if (capaciteDisponible < quantite) {
            throw new IllegalStateException("Capacité de stockage insuffisante pour " + carburant.getNom());
        }

        // Ajout de carburant dans les réservoirs compatibles
        double quantiteRestante = quantite;
        for (Reservoir reservoir : jeu.getReservoirs()) {
            if (reservoir.getErgol().equals(carburant)) {
                double espaceDisponible = reservoir.getQuantiteTotal() - reservoir.getQuantite();
                if (espaceDisponible > 0) {
                    double quantiteAAjouter = Math.min(quantiteRestante, espaceDisponible);
                    reservoir.ajouterErgol(quantiteAAjouter);
                    quantiteRestante -= quantiteAAjouter;

                    if (quantiteRestante <= 0) {
                        break;
                    }
                }
            }
        }

        // Vérification finale pour s'assurer que tout a été ajouté
        if (quantiteRestante > 0) {
            throw new IllegalStateException("Impossible d'ajouter toute la quantité au stockage.");
        }

        // Mise à jour de la quantité en stock
        this.quantiteStock = jeu.calculerQuantiteTotaleErgol(carburant.getNom());
    }

    public static class Builder {
        private Ergol carburant;
        private double quantite;
        private Double capaciteMax;
        private Double quantiteStock;
        private int prix;
        private String nom;
        private Boolean estAchetable;

        public Builder setPrix(int prix) {
            this.prix = prix;
            return self();
        }

        public Builder setNom(String nom) {
            this.nom = nom;
            return self();
        }

        public Builder setEstAchetable(Boolean estAchetable) {
            this.estAchetable = estAchetable;
            return self();
        }

        public Builder setCarburant(Ergol carburant) {
            if (carburant == null) {
                throw new IllegalArgumentException("Le carburant ne peut pas être null.");
            }
            this.carburant = carburant;
            return this;
        }

        public Builder setQuantite(double quantite) {
            if (quantite <= 0) {
                throw new IllegalArgumentException("La quantité doit être supérieure à 0.");
            }
            this.quantite = quantite;
            return this;
        }

        public Builder setCapaciteMax(Double capaciteMax) {
            if (capaciteMax != null && capaciteMax <= 0) {
                throw new IllegalArgumentException("La capacité maximale doit être supérieure à 0.");
            }
            this.capaciteMax = capaciteMax;
            return this;
        }

        public Builder setQuantiteStock(Double quantiteStock) {
            if (quantiteStock != null && quantiteStock < 0) {
                throw new IllegalArgumentException("La quantité en stock ne peut pas être négative.");
            }
            this.quantiteStock = quantiteStock;
            return this;
        }

        protected Builder self() {
            return this;
        }

        public CarburantAchetable build() {
            return new CarburantAchetable(this);
        }
    }
}
