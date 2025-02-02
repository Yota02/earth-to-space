package back.Batiment;

import java.time.LocalDateTime;

public class HangarAssemblage extends IBatiment {
    



    public HangarAssemblage(String nom, int superficie, int capacite, int tempsConstruction) {
        this.etat = 0;
        this.nom = nom;
        this.superficie = superficie;
        this.capacite = capacite;
        this.anneeConstruction =  LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        this.operationnel = false;
        this.tempsConstruction = tempsConstruction;
        this.progression = 0.0;
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
