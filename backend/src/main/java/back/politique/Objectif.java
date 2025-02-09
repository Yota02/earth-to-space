package back.politique;

import java.time.LocalDateTime;
import org.json.JSONObject;

public class Objectif {
    private String titre;
    private String description;
    private LocalDateTime dateCible;
    private Effet effet;
    private Boolean terminee;
    private Proprietaire proprietaire; 
    private double progression;
    private int difficulte;

    public Objectif(String titre, String description, LocalDateTime dateCible, Effet effet, Proprietaire proprietaire, int difficulte) {
        this.titre = titre;
        this.description = description;
        this.dateCible = dateCible;
        this.effet = effet;
        this.terminee = false;
        this.proprietaire = proprietaire;
        this.progression = 0;
        this.difficulte = difficulte;
    }

    public Proprietaire getProprietaire() {
        return proprietaire;
    }

    public Boolean isTerminee() {
        return terminee;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("titre", this.titre);
        json.put("description", this.description);
        json.put("dateCible", this.dateCible.toString());
        json.put("effet", this.effet.toString());
        json.put("terminee", this.terminee);
        json.put("proprietaire", this.proprietaire.name());
        json.put("progression", this.progression);
        json.put("difficulte", this.difficulte);
        return json;
    }
}
