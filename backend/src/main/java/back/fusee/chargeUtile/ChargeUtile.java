package back.fusee.chargeUtile;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import back.fusee.chargeUtile.Destination.Orbite;
import back.fusee.chargeUtile.Destination.Planete;

public class ChargeUtile {

    private double poids;
    private String nom;
    private double volume;
    private int cout;
    private int dureeMission;
    private int statut;
    private LocalDateTime dateDeLancement;
    private Planete planete;
    private Orbite orbite;
    private Proprietaire proprietaire;

    public ChargeUtile(double poids, String nom, double volume, int cout, int dureeMission, int statut, LocalDateTime dateDeLancement, Planete planete, Orbite orbite, Proprietaire proprietaire) {
        this.poids = poids;
        this.nom = nom;
        this.volume = volume;
        this.cout = cout;
        this.dureeMission = dureeMission;
        this.statut = statut;
        this.dateDeLancement = dateDeLancement;
        this.planete = planete;
        this.orbite = orbite;
        this.proprietaire = proprietaire;
    }

    // Getters and Setters
    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public int getDureeMission() {
        return dureeMission;
    }

    public void setDureeMission(int dureeMission) {
        this.dureeMission = dureeMission;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateDeLancement() {
        return dateDeLancement;
    }

    public void setDateDeLancement(LocalDateTime dateDeLancement) {
        this.dateDeLancement = dateDeLancement;
    }

    public Planete getPlanete() {
        return planete;
    }

    public void setPlanete(Planete planete) {
        this.planete = planete;
    }

    public Orbite getOrbite() {
        return orbite;
    }

    public void setOrbite(Orbite orbite) {
        this.orbite = orbite;
    }

    public Proprietaire getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Proprietaire proprietaire) {
        this.proprietaire = proprietaire;
    }

    // Méthode pour afficher le statut en chaîne de caractères
    public String getStatutDescription() {
        switch (this.statut) {
            case 1:
                return "En cours";
            case 2:
                return "Terminé";
            case 3:
                return "Annulé";
            default:
                return "Inconnu";
        }
    }

    // Méthode pour vérifier si la mission est en cours
    public boolean isMissionEnCours() {
        return this.statut == 1;
    }

    // Méthode pour vérifier si la mission est terminée
    public boolean isMissionTerminee() {
        return this.statut == 2;
    }

    // Méthode pour calculer la date de fin estimée de la mission
    public LocalDateTime getDateFinEstimee() {
        if (this.dateDeLancement != null && this.dureeMission > 0) {
            return this.dateDeLancement.plusDays(this.dureeMission);
        }
        return null;
    }

    // Convertir l'objet en JSON
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("poids", this.poids);
        json.put("nom", this.nom);
        json.put("volume", this.volume);
        json.put("cout", this.cout);
        json.put("dureeMission", this.dureeMission);
        json.put("statut", this.statut);
        json.put("dateDeLancement", this.dateDeLancement != null ? this.dateDeLancement.toString() : null);
        json.put("planete", this.planete != null ? this.planete.getNom() : null); 
        json.put("orbits", this.orbite != null ? this.orbite.getNom() : null); 
        return json;
    }

    /**
     * Convertit une liste de charges utiles en JSONArray
     * @param chargesUtiles Liste des charges utiles à convertir
     * @return JSONArray contenant toutes les charges utiles
     */
    public static JSONArray toJsonArray(List<ChargeUtile> chargesUtiles) {
        JSONArray jsonArray = new JSONArray();
        if (chargesUtiles != null) {
            for (ChargeUtile charge : chargesUtiles) {
                if (charge != null) {
                    jsonArray.put(charge.toJson());
                }
            }
        }
        return jsonArray;
    }

    // Nouvelle méthode pour obtenir une description complète de la charge utile
    public String getDescriptionComplete() {
        String description = "Nom: " + this.nom + "\n";
        description += "Poids: " + this.poids + " kg\n";
        description += "Volume: " + this.volume + " m³\n";
        description += "Coût: " + this.cout + " EUR\n";
        description += "Durée de la mission: " + this.dureeMission + " jours\n";
        description += "Statut: " + getStatutDescription() + "\n";
        description += "Date de lancement: " + (this.dateDeLancement != null ? this.dateDeLancement.toString() : "Non spécifiée") + "\n";
        description += "Planète cible: " + (this.planete != null ? this.planete.getNom() : "Non spécifiée") + "\n";
        description += "Orbite cible: " + (this.orbite != null ? this.orbite.getNom() : "Non spécifiée") + "\n";
        return description;
    }

}
