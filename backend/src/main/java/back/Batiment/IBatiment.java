package back.Batiment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

import back.ObjectDeblocable;

public abstract class IBatiment extends ObjectDeblocable{

    int etat = 0;
    String nom;
    int superficie;
    
    LocalDateTime anneeConstruction;
    boolean operationnel = false;

    int tempsConstruction; // en mois
    double progression = 0; // en pourcent
    boolean enConstruction = false;

    public boolean getEnConstruction() {
        return enConstruction;
    }

    public int getCout(){
        return (int) superficie * tempsConstruction * 10;
    }

    public int getEtat() {
        return this.etat;
    }

    public String getNom() {
        return this.nom;
    }

    public int getSuperficie() {
        return this.superficie;
    }

    public LocalDateTime getAnneeConstruction() {
        return this.anneeConstruction;
    }

    public void setAnneeConstruction(LocalDateTime time){
        this.anneeConstruction = time;
    }

    public boolean estOperationnel() {
        return this.operationnel;
    }

    public void setOperationnel(boolean operationnel) {
        this.operationnel = operationnel;
    }

    public void setEnConstruction(boolean valeur) {
        this.enConstruction = valeur;
    }

    public double getProgression(){
        return progression;
    }
    
    public int getTempsRestant(){
        return 1;
    }

    public int getTempsConstruction(){
        return tempsConstruction;
    }

    public void construireParJour(double pointsConstruction){
        if(progression < 100){
            progression += (tempsConstruction * 30) * (1 + (pointsConstruction / 10)) / 30;
        } else {
            setOperationnel(true);
            setEnConstruction(false);
            System.out.println("Construction terminé !");
        }
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
    
        json.put("nom", this.nom);
        json.put("superficie", this.superficie);
        
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

    // Calcule le coût d’entretien mensuel du bâtiment
    abstract double calculerCoutEntretien();

    // Retourne une description complète du bâtiment
    public abstract String toString();
}
