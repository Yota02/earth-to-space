package back.Batiment;

public abstract class IBatiment {

    int etat;
    String nom;
    double superficie;
    int capacite;
    int anneeConstruction;
    boolean operationnel;

    public int getEtat() {
        return this.etat;
    }


    public String getNom() {
        return this.nom;
    }

    public double getSuperficie() {
        return this.superficie;
    }

    public int getCapacite() {
        return this.capacite;
    }

    public int getAnneeConstruction() {
        return this.anneeConstruction;
    }

    public boolean estOperationnel() {
        return this.operationnel;
    }

    public void setOperationnel(boolean operationnel) {
        this.operationnel = operationnel;
    }

    // Calcule le coût d’entretien mensuel du bâtiment
    abstract double calculerCoutEntretien();

    // Retourne une description complète du bâtiment
    public abstract String toString();
}
