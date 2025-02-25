package back.fusee.moteur;

import back.ObjectDeblocable;

public class Moteur extends ObjectDeblocable{

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

    TypeMoteur typeMoteur;

    private int cout;


    // Etat
    private double poussee;
    protected Double temperatureCritique; // en kelvins
    protected Boolean arretUrgence; // true ou false
    protected Double consommationCarburant; // en kg/s
    protected int statutOperationnel; // "actif", "hors service", "en maintenance"
    protected Double tempsFonctionnement; // en secondes

    public Moteur(String nom, Double poussee, Ergol carburant, Double rendement, Double poids, Double diametre, Double longueur, Double temperatureMax, Double fiabilite, Boolean capaciteRedemarrage, Double temperatureCritique, Boolean arretUrgence, Double pousseeMax, Double consommationCarburant, TypeMoteur typeMoteur, int cout) {
        this.nom = nom;
        this.poussee = poussee;
        this.carburant = carburant;
        this.rendement = rendement;
        this.anneeFabrication = 0;
        this.nbFoisUtilise = 0;
        this.cout = cout;
        this.poids = poids;
        this.diametre = diametre;
        this.longueur = longueur;
        this.pressionChambre = 0.0;
        this.temperatureMax = temperatureMax;
        this.fiabilite = fiabilite;
        this.capaciteRedemarrage = capaciteRedemarrage;
        this.temperatureCritique = temperatureCritique;
        this.arretUrgence = arretUrgence;
        this.pousseeMax = pousseeMax;
        this.consommationCarburant = consommationCarburant;
        this.typeMoteur = typeMoteur;
        this.statutOperationnel = 0;
    }

    public int getCout(){
        return cout;
    }

    public Double getPousse() {
        return poussee;
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

    public int getStatutOperationnel() {
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

    public Double getPousseeMax() {
        return pousseeMax;
    }

    public Double getConsommationCarburant() {
        return consommationCarburant;
    }

    public TypeMoteur getType() {
        return typeMoteur;
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
                ", pousseeMax=" + pousseeMax + " kN, consommationCarburant=" + consommationCarburant + " kg/s]";
    }

}
