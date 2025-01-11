package back.programme;

public class Programme {

    private String nom;
    private String objectif;

    private int nbLancement;

    private double budget;

    private int dureePrevu;

    private String statut;

    private int nbMissionsReussies;
    private int nbMissionsEchouees;

    public Programme(String objectif, String nom, double budget, int dureePrevu) {
        this.objectif = objectif;
        this.nom = nom;
        this.budget = budget;
        this.dureePrevu = dureePrevu;
        this.nbMissionsEchouees = 0;
        this.nbMissionsReussies = 0;
        this.statut = "en cours";
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getNbLancement() {
        return nbLancement;
    }

    public void setNbLancement(Integer nbLancement) {
        this.nbLancement = nbLancement;
    }

    public double getBudget() {
        return budget;
    }

    public int getDureePrevu() {
        return dureePrevu;
    }

}