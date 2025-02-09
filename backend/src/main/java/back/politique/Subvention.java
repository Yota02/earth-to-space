package back.politique;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class Subvention {

    private static int nextId = 1;

    private int id;
    private String nom;
    private int quantite;
    private int duree;
    private boolean active;
    private LocalDateTime dateFin;

    public Subvention(String nom, int quantite, int duree) {
        this.id = nextId++;
        this.nom = nom;
        this.quantite = quantite;
        this.duree = duree;
        this.active = false;
        this.dateFin = null;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        if (this.active && this.dateFin != null) {
            if (LocalDateTime.now().isAfter(this.dateFin)) {
                this.active = false;
            }
        }
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.dateFin = LocalDateTime.now().plusMonths(this.duree);
        } else {
            this.dateFin = null;
        }
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

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("nom", this.nom);
        json.put("quantite", this.quantite);
        json.put("duree", this.duree);
        json.put("active", this.isActive());
        if (this.dateFin != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            json.put("dateFin", this.dateFin.format(formatter));
        } else {
            json.put("dateFin", JSONObject.NULL);
        }
        return json;
    }
}