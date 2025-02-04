package back.Batiment;

public class HangarAssemblage extends IBatiment {
    

    public HangarAssemblage(String nom, int superficie, int capacite, int tempsConstruction) {
        this.nom = nom;
        this.superficie = superficie;
        this.capacite = capacite;
        this.tempsConstruction = tempsConstruction;
    }

    @Override
    public double calculerCoutEntretien() {
        return this.superficie * 10 + this.etat * 50;
    }

    @Override
    public String toString() {
        return "Hangar_Assemblage{" +
                "nom='" + nom + '\'' +
                ", superficie=" + superficie +
                ", capacite=" + capacite +
                ", anneeConstruction=" + anneeConstruction +
                ", operationnel=" + operationnel +
                '}';
    }




}
