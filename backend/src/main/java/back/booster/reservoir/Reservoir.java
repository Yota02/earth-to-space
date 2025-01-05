package back.booster.reservoir;

import back.Metaux.Materiaux;
import back.moteur.Ergol;

public class Reservoir {

    // Le nom du réservoir pour l'identifier de manière unique
    private String nom;
    
    // L'ergol ou carburant utilisé dans le réservoir (référence à une autre classe Ergol)
    private Ergol Ergol;

    // Quantité actuelle d'ergol ou carburant dans le réservoir (en litres ou kilogrammes)
    private Double quantite;
    
    // La capacité totale du réservoir en termes de quantité d'ergol (en litres ou kilogrammes)
    private Double quantiteTotal;

    // Le poids du réservoir lorsqu'il est vide (important pour les calculs de masse totale)
    private Double poidsVide;

    // Dimensions du réservoir (par exemple, longueur, diamètre, forme)
    private String dimensions;

    // La pression interne actuelle du réservoir (peut être un paramètre clé de sécurité)
    private Double pressionInterne;
    
    // La pression maximale que le réservoir peut supporter sans risque de défaillance
    private Double capaciteMaxPression;

    // La température interne actuelle du réservoir (importante pour la gestion du carburant)
    private Double temperatureInterne;
    
    // La température maximale que le réservoir peut supporter en toute sécurité
    private Double temperatureMax;

    // Le matériau du réservoir, qui influence ses propriétés physiques (par exemple, inox, titane)
    private Materiaux matiereReservoir;

    // L'état du réservoir : vrai s'il est fonctionnel, faux s'il y a un problème
    private Boolean etatReservoir;

    // Indique si le réservoir possède une isolation thermique (utile pour maintenir la température interne stable)
    private boolean IsolationThermique;

    // Le débit de carburant sortant du réservoir, en fonction du moteur et de la propulsion
    private Double debitSortie;

    // Taille générale du réservoir (peut être utilisée pour l'encombrement ou la capacité)
    private Double taille;

    private Reservoir(Builder builder) {
        this.nom = builder.nom;
        this.Ergol = builder.Ergol;
        this.quantite = builder.quantite;
        this.quantiteTotal = builder.quantiteTotal;
        this.poidsVide = builder.poidsVide;
        this.dimensions = builder.dimensions;
        this.pressionInterne = builder.pressionInterne;
        this.capaciteMaxPression = builder.capaciteMaxPression;
        this.temperatureInterne = builder.temperatureInterne;
        this.temperatureMax = builder.temperatureMax;
        this.matiereReservoir = builder.matiereReservoir;
        this.etatReservoir = builder.etatReservoir;
        this.IsolationThermique = builder.IsolationThermique;
        this.debitSortie = builder.debitSortie;
        this.taille = builder.taille;
    }

    public static class Builder {

        private String nom;
        private Ergol Ergol;

        private Double quantite;
        private Double quantiteTotal;

        private Double poidsVide;
        private String dimensions;

        private Double pressionInterne;
        private Double capaciteMaxPression;

        private Double temperatureInterne;
        private Double temperatureMax;

        private Materiaux matiereReservoir;
        private Boolean etatReservoir;

        private boolean IsolationThermique;

        private Double debitSortie;

        private Double taille;

        public Builder setNom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder setErgol(Ergol Ergol) {
            this.Ergol = Ergol;
            return this;
        }

        public Builder setQuantite(Double quantite) {
            this.quantite = quantite;
            return this;
        }

        public Builder setQuantiteTotal(Double quantiteTotal) {
            this.quantiteTotal = quantiteTotal;
            return this;
        }

        public Builder setPoidsVide(Double poidsVide) {
            this.poidsVide = poidsVide;
            return this;
        }

        public Builder setDimensions(String dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder setPressionInterne(Double pressionInterne) {
            this.pressionInterne = pressionInterne;
            return this;
        }

        public Builder setCapaciteMaxPression(Double capaciteMaxPression) {
            this.capaciteMaxPression = capaciteMaxPression;
            return this;
        }

        public Builder setTemperatureInterne(Double temperatureInterne) {
            this.temperatureInterne = temperatureInterne;
            return this;
        }

        public Builder setTemperatureMax(Double temperatureMax) {
            this.temperatureMax = temperatureMax;
            return this;
        }

        public Builder setMatiereReservoir(Materiaux matiereReservoir) {
            this.matiereReservoir = matiereReservoir;
            return this;
        }

        public Builder setEtatReservoir(Boolean etatReservoir) {
            this.etatReservoir = etatReservoir;
            return this;
        }

        public Builder setIsolationThermique(boolean isolationThermique) {
            this.IsolationThermique = isolationThermique;
            return this;
        }

        public Builder setDebitSortie(Double debitSortie) {
            this.debitSortie = debitSortie;
            return this;
        }

        public Builder setTaille(Double taille) {
            this.taille = taille;
            return this;
        }

        public Reservoir build() {
            return new Reservoir(this);
        }
    }
}