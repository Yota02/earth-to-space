package back.programme;

public class Programme {

    String nom;
    String objectif;

    Integer nbLancement;


    public Programme(String objectif, String nom) {
        this.objectif = objectif;
        this.nom = nom;
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

}
