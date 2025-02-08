package back.politique;

import org.json.JSONObject;
import java.util.UUID;

public class Subvention {

    private static int nextId = 1;

    private int id;
    private String nom;
    private int quantite;
    private int duree;

    public Subvention(String nom, int quantite, int duree) {
        this.id = nextId++; // Générer un ID unique
        this.nom = nom;
        this.quantite = quantite;
        this.duree = duree;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public int getDuree() {
        return duree;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id); // Ajouter l'ID au JSON
        json.put("nom", this.nom);
        json.put("quantite", this.quantite);
        json.put("duree", this.duree);
        return json;
    }
}