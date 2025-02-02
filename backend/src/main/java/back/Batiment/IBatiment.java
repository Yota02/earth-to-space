package back.Batiment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public abstract class IBatiment {

    int etat;
    String nom;
    int superficie;
    int capacite;
    LocalDateTime anneeConstruction;
    boolean operationnel;
    int tempsConstruction; // en mois
    double progression; // en pourcent

    public int getCout(){
        return (int) superficie * tempsConstruction * 1000;
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

    public int getCapacite() {
        return this.capacite;
    }

    public LocalDateTime getAnneeConstruction() {
        return this.anneeConstruction;
    }

    public boolean estOperationnel() {
        return this.operationnel;
    }

    public void setOperationnel(boolean operationnel) {
        this.operationnel = operationnel;
    }

    public double getProgression(){
        return progression;
    }
    
    public int getTempsRestant(){
        return 1;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        // Spécifications basiques
        json.put("nom", this.nom);
        json.put("superficie", this.superficie);
        json.put("capacite", this.capacite);
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formattedDate = anneeConstruction.format(formatter);
        json.put("anneeConstruction", formattedDate);

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
