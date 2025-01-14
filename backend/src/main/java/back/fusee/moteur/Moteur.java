package back.fusee.moteur;

public class Moteur {

    // Attributs Basique
    protected String nom;
    protected Boolean capaciteRedemarrage;
    protected Double pousseeMax; // en kilonewtons
    public Ergol carburant;
    protected Double rendement;
    protected Double fiabilite; // en pourcentage
    protected int anneeFabrication;
    protected int nbFoisUtilise;

    // Attributs physiques
    protected Double poids; // en kg
    protected Double diametre; // en mètres
    protected Double longueur; // en mètres
    protected Double pressionChambre; // en bars
    protected Double temperatureMax; // en kelvins

    // Etat
    protected Double temperatureCritique; // en kelvins
    protected Boolean arretUrgence; // true ou false
    protected Double tauxMelange; // ratio (carburant/oxydant)
    protected Double consommationCarburant; // en kg/s
    protected String statutOperationnel; // "actif", "hors service", "en maintenance"
    protected Double tempsFonctionnement; // en secondes

    protected Moteur(Builder builder) {
        this.nom = builder.nom;
        this.carburant = builder.carburant;
        this.rendement = builder.rendement;
        this.anneeFabrication = builder.anneeFabrication;
        this.nbFoisUtilise = builder.nbFoisUtilise;
        this.poids = builder.poids;
        this.diametre = builder.diametre;
        this.longueur = builder.longueur;
        this.pressionChambre = builder.pressionChambre;
        this.temperatureMax = builder.temperatureMax;
        this.tempsFonctionnement = builder.tempsFonctionnement;
        this.statutOperationnel = builder.statutOperationnel;
        this.fiabilite = builder.fiabilite;
        this.capaciteRedemarrage = builder.capaciteRedemarrage;
        this.temperatureCritique = builder.temperatureCritique;
        this.arretUrgence = builder.arretUrgence;
        this.tauxMelange = builder.tauxMelange;
        this.pousseeMax = builder.pousseeMax;
        this.consommationCarburant = builder.consommationCarburant;
    }

    public String getNom() {
        return nom;
    }

    public Ergol getCarburant() {
        return carburant;
    }

    public Double getRendement() {
        return rendement;
    }

    public int getAnneeFabrication() {
        return anneeFabrication;
    }

    public int getNbFoisUtilise() {
        return nbFoisUtilise;
    }

    public Double getPoids() {
        return poids;
    }

    public Double getDiametre() {
        return diametre;
    }

    public Double getLongueur() {
        return longueur;
    }

    public Double getPressionChambre() {
        return pressionChambre;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public Double getTempsFonctionnement() {
        return tempsFonctionnement;
    }

    public String getStatutOperationnel() {
        return statutOperationnel;
    }

    public Double getFiabilite() {
        return fiabilite;
    }

    public Boolean getCapaciteRedemarrage() {
        return capaciteRedemarrage;
    }

    public Double getTemperatureCritique() {
        return temperatureCritique;
    }

    public Boolean getArretUrgence() {
        return arretUrgence;
    }

    public Double getTauxMelange() {
        return tauxMelange;
    }

    public Double getPousseeMax() {
        return pousseeMax;
    }

    public Double getConsommationCarburant() {
        return consommationCarburant;
    }

    @Override
    public String toString() {
        return "Moteur [nom=" + nom + ", puissance="  + " kN, carburant=" + carburant +
                ", rendement=" + rendement + "%, anneeFabrication=" + anneeFabrication +
                ", nbFoisUtilise=" + nbFoisUtilise + ", poids=" + poids + " kg, diametre=" + diametre +
                " m, longueur=" + longueur + " m, pressionChambre=" + pressionChambre + " bars, temperatureMax=" + temperatureMax +
                " K, dureeVie=" + " h, tempsFonctionnement=" + tempsFonctionnement + " s, statutOperationnel=" + statutOperationnel +
                ", fiabilite=" + fiabilite + "%, capaciteRedemarrage=" + capaciteRedemarrage +
                ", temperatureCritique=" + temperatureCritique + " K, arretUrgence=" + arretUrgence +
                ", tauxMelange=" + tauxMelange + ", pousseeMax=" + pousseeMax + " kN, consommationCarburant=" + consommationCarburant + " kg/s]";
    }

    public static class Builder {

        private String nom;
        private Ergol carburant;
        private Double rendement;
        private int anneeFabrication;
        private int nbFoisUtilise;

        private Double poids;
        private Double diametre;
        private Double longueur;
        private Double pressionChambre;
        private Double temperatureMax;

        private Double tempsFonctionnement;
        private String statutOperationnel;
        private Double fiabilite;

        private Boolean capaciteRedemarrage;
        private Double temperatureCritique;
        private Boolean arretUrgence;
        private Double tauxMelange;
        private Double pousseeMax;
        private Double consommationCarburant;

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder carburant(Ergol carburant) {
            this.carburant = carburant;
            return this;
        }

        public Builder rendement(Double rendement) {
            this.rendement = rendement;
            return this;
        }

        public Builder anneeFabrication(int anneeFabrication) {
            this.anneeFabrication = anneeFabrication;
            return this;
        }

        public Builder nbFoisUtilise(int nbFoisUtilise) {
            this.nbFoisUtilise = nbFoisUtilise;
            return this;
        }

        public Builder poids(Double poids) {
            this.poids = poids;
            return this;
        }

        public Builder diametre(Double diametre) {
            this.diametre = diametre;
            return this;
        }

        public Builder longueur(Double longueur) {
            this.longueur = longueur;
            return this;
        }

        public Builder pressionChambre(Double pressionChambre) {
            this.pressionChambre = pressionChambre;
            return this;
        }

        public Builder temperatureMax(Double temperatureMax) {
            this.temperatureMax = temperatureMax;
            return this;
        }

        public Builder tempsFonctionnement(Double tempsFonctionnement) {
            this.tempsFonctionnement = tempsFonctionnement;
            return this;
        }

        public Builder statutOperationnel(String statutOperationnel) {
            this.statutOperationnel = statutOperationnel;
            return this;
        }

        public Builder fiabilite(Double fiabilite) {
            this.fiabilite = fiabilite;
            return this;
        }

        public Builder capaciteRedemarrage(Boolean capaciteRedemarrage) {
            this.capaciteRedemarrage = capaciteRedemarrage;
            return this;
        }

        public Builder temperatureCritique(Double temperatureCritique) {
            this.temperatureCritique = temperatureCritique;
            return this;
        }

        public Builder arretUrgence(Boolean arretUrgence) {
            this.arretUrgence = arretUrgence;
            return this;
        }

        public Builder tauxMelange(Double tauxMelange) {
            this.tauxMelange = tauxMelange;
            return this;
        }

        public Builder pousseeMax(Double pousseeMax) {
            this.pousseeMax = pousseeMax;
            return this;
        }

        public Builder consommationCarburant(Double consommationCarburant) {
            this.consommationCarburant = consommationCarburant;
            return this;
        }

        public Moteur build() {
            return new Moteur(this);
        }
    }
}
