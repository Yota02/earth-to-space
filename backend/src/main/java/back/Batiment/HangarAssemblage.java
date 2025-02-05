package back.Batiment;

import java.util.ArrayList;
import java.util.List;

import back.fusee.Ifusee;

public class HangarAssemblage extends IBatiment {

    private int hauteur;
    private List<Ifusee> fusees;

    public HangarAssemblage(String nom, int superficie, int capacite, int tempsConstruction, int hauteur) {
        this.nom = nom;
        this.superficie = superficie;
        this.capacite = capacite;
        this.tempsConstruction = tempsConstruction;
        this.hauteur = hauteur;
        this.fusees = new ArrayList<>();
    }

    public int getHauteur(){
        return hauteur;
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

    public void ajouterFusee(Ifusee fusee){
        if(capacite >= fusees.size()){
            fusees.add(fusee);
        } else {
            System.out.println("trop de fusée");
        }   
    }

    public void retirerFusee(Ifusee fusee){
        fusees.remove(fusee);
    }

    private void assemblerFusee(Ifusee fusee, double pointsIngenieur){
        double taux = 1 + (pointsIngenieur / 10);
        fusee.construire(taux);
    }

    public void assemblerTouteFusee(double pointsIngenieur){
        for (Ifusee f : fusees) {
            assemblerFusee(f, pointsIngenieur);
        }
    }

}
