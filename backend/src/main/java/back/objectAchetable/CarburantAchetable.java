package back.objectAchetable;

import back.Jeu;
import back.fusee.moteur.Ergol;
import back.fusee.reservoir.Reservoir;

public class CarburantAchetable {
    private Ergol carburant;
    private double quantite;
    private int demandeMonthly;
    private int prix;
    private String nom;
    private Boolean estAchetable;

    private CarburantAchetable(Builder builder) {
        this.carburant = builder.carburant;
        this.quantite = builder.quantite;
        this.prix = builder.prix;
        this.nom = builder.nom;
        this.estAchetable = builder.estAchetable;
    }

    public Ergol getCarburant() {
        return carburant;
    }

    public int getDemandeMonthly() {
        return demandeMonthly;
    }

    public void setDemandeMonthly(int demandeMonthly) {
        this.demandeMonthly = demandeMonthly;
    }

    public double getQuantite() {
        return quantite;
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
     * Effectue l'achat du carburant en vérifiant les contraintes de capacité et met
     * à jour les réservoirs.
     *
     * @param jeu Instance du jeu contenant les informations globales.
     * @throws IllegalStateException si la capacité de stockage est insuffisante.
     */
    public void effectuerAchat(Jeu jeu, boolean isMonthly) {
        double capaciteDisponible = jeu.getCapaciteMaximaleErgol(this.getCarburant()) 
            - jeu.calculerQuantiteTotaleErgol(carburant.getNom());
    
        double quantiteAAcheter = isMonthly ? demandeMonthly : quantite;
    
        if (capaciteDisponible < quantiteAAcheter) {
            throw new IllegalStateException("Capacité de stockage insuffisante pour " + carburant.getNom());
        }
    
        double quantiteRestante = quantiteAAcheter;
    
        // ✅ Correction du calcul de l'espace disponible
        for (Reservoir reservoir : jeu.getReservoirs()) {
            if (reservoir.getErgol().equals(carburant)) {
                double espaceDisponible = jeu.getCapaciteMaximaleErgol(this.getCarburant())  - reservoir.getQuantite(); // Correction ici
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
    
        // ✅ Vérification après tentative de remplissage
        if (quantiteRestante > 0) {
            throw new IllegalStateException("Impossible d'ajouter toute la quantité au stockage. Quantité restante : " + quantiteRestante);
        }
    }
    

    public static class Builder {
        private Ergol carburant;
        private double quantite;

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

        protected Builder self() {
            return this;
        }

        public CarburantAchetable build() {
            return new CarburantAchetable(this);
        }
    }
}
