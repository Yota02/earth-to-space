package back.moteur;

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

    protected Moteur(Builder<?> builder) {
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

    public static abstract class Builder<T extends Builder<T>> {

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

        public T nom(String nom) {
            this.nom = nom;
            return self();
        }

        public T carburant(Ergol carburant) {
            this.carburant = carburant;
            return self();
        }

        public T rendement(Double rendement) {
            this.rendement = rendement;
            return self();
        }

        public T anneeFabrication(int anneeFabrication) {
            this.anneeFabrication = anneeFabrication;
            return self();
        }

        public T nbFoisUtilise(int nbFoisUtilise) {
            this.nbFoisUtilise = nbFoisUtilise;
            return self();
        }

        public T poids(Double poids) {
            this.poids = poids;
            return self();
        }

        public T diametre(Double diametre) {
            this.diametre = diametre;
            return self();
        }

        public T longueur(Double longueur) {
            this.longueur = longueur;
            return self();
        }

        public T pressionChambre(Double pressionChambre) {
            this.pressionChambre = pressionChambre;
            return self();
        }

        public T temperatureMax(Double temperatureMax) {
            this.temperatureMax = temperatureMax;
            return self();
        }

        public T tempsFonctionnement(Double tempsFonctionnement) {
            this.tempsFonctionnement = tempsFonctionnement;
            return self();
        }

        public T statutOperationnel(String statutOperationnel) {
            this.statutOperationnel = statutOperationnel;
            return self();
        }

        public T fiabilite(Double fiabilite) {
            this.fiabilite = fiabilite;
            return self();
        }

        public T capaciteRedemarrage(Boolean capaciteRedemarrage) {
            this.capaciteRedemarrage = capaciteRedemarrage;
            return self();
        }

        public T temperatureCritique(Double temperatureCritique) {
            this.temperatureCritique = temperatureCritique;
            return self();
        }

        public T arretUrgence(Boolean arretUrgence) {
            this.arretUrgence = arretUrgence;
            return self();
        }

        public T tauxMelange(Double tauxMelange) {
            this.tauxMelange = tauxMelange;
            return self();
        }

        public T pousseeMax(Double pousseeMax) {
            this.pousseeMax = pousseeMax;
            return self();
        }

        public T consommationCarburant(Double consommationCarburant) {
            this.consommationCarburant = consommationCarburant;
            return self();
        }

        protected abstract T self();

        public abstract Moteur build();
    }
}
