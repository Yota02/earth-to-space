package back.Batiment;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import back.fusee.Ifusee;

public class HangarAssemblage extends IBatiment {

    private int hauteur;
    private List<Ifusee> fusees;
    private int capacite;

    public HangarAssemblage(String nom, int superficie, int capacite, int tempsConstruction, int hauteur) {
        this.nom = nom;
        this.superficie = superficie;
        this.capacite = capacite;
        this.tempsConstruction = tempsConstruction;
        this.hauteur = hauteur;
        this.fusees = new ArrayList<>();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
    
        json.put("nom", this.nom);
        json.put("superficie", this.superficie);
        json.put("capacite", this.capacite);
        
        if (this.anneeConstruction != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            json.put("anneeConstruction", this.anneeConstruction.format(formatter));
        } else {
            json.put("anneeConstruction", JSONObject.NULL);
        }
    
        json.put("operationnel", this.operationnel);
        json.put("tempsConstruction", this.tempsConstruction);
        json.put("progression", this.progression);
        json.put("etat", this.etat);
        json.put("cout", getCout());
    
        return json;
    }

    public int getHauteur(){
        return hauteur;
    }

    public int getCapacite(){
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
