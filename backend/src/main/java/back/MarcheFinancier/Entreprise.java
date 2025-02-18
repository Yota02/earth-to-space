package back.MarcheFinancier;

import org.json.JSONObject;

public class Entreprise {

    private String nom;
    private String pays;
    private double capitalisationBoursiere;
    private double coursAction;
    private double dividendeParAction;
    private double variationCours;

    public Entreprise(String nom, String pays, double capitalisationBoursiere, double coursAction,
            double dividendeParAction, double variationCours) {
        this.nom = nom;
        this.pays = pays;
        this.capitalisationBoursiere = capitalisationBoursiere;
        this.coursAction = coursAction;
        this.dividendeParAction = dividendeParAction;
        this.variationCours = variationCours;
    }

    public String getNom() {
        return nom;
    }

    public String getPays() {
        return pays;
    }

    public double getCapitalisationBoursiere() {
        return capitalisationBoursiere;
    }

    public double getCoursAction() {
        return coursAction;
    }

    public double getDividendeParAction() {
        return dividendeParAction;
    }

    public double getVariationCours() {
        return variationCours;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setCapitalisationBoursiere(double capitalisationBoursiere) {
        this.capitalisationBoursiere = capitalisationBoursiere;
    }

    public void setCoursAction(double coursAction) {
        this.coursAction = coursAction;
    }

    public void setDividendeParAction(double dividendeParAction) {
        this.dividendeParAction = dividendeParAction;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("nom", this.nom);
        json.put("pays", this.pays);
        json.put("capitalisationBoursiere", this.capitalisationBoursiere);
        json.put("coursAction", this.coursAction);
        json.put("dividendeParAction", this.dividendeParAction);
        json.put("variationCours", this.variationCours);

        return json;
    }

}
