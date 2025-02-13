package back.MarcheFinancier;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MarcheFinancier {

    private String nom;
    private Map<Entreprise, Double> partMarche;

    public MarcheFinancier(String nom) {
        this.nom = nom;
        this.partMarche = new java.util.HashMap<>();
    }

    public String getNom() {
        return nom;
    }

    public Map<Entreprise, Double> getPartMarche() {
        return partMarche;
    }

    public void setPartMarche(Map<Entreprise, Double> partMarche) {
        this.partMarche = partMarche;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void ajouterEntreprise(Entreprise entreprise, double part) {
        if (getTotalPart() + part > 1.0) {
            throw new IllegalArgumentException("La somme des parts de marché ne peut pas dépasser 100%.");
        }
        this.partMarche.put(entreprise, part);
    }

    public void retirerEntreprise(Entreprise entreprise) {
        this.partMarche.remove(entreprise);
    }

    public void modifierPart(Entreprise entreprise, double part) {
        double totalSansAnciennePart = getTotalPart() - this.partMarche.getOrDefault(entreprise, 0.0);

        if (totalSansAnciennePart + part > 1.0) {
            throw new IllegalArgumentException("La somme des parts de marché ne peut pas dépasser 100%.");
        }
        
        this.partMarche.put(entreprise, part);
    }

    public double getPart(Entreprise entreprise) {
        return this.partMarche.getOrDefault(entreprise, 0.0);
    }

    private double getTotalPart() {
        return this.partMarche.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("nom", this.nom);
        JSONArray partMarcheArray = new JSONArray();

        for (Map.Entry<Entreprise, Double> entry : this.partMarche.entrySet()) {
            JSONObject entrepriseJson = new JSONObject();
            entrepriseJson.put("entreprise", entry.getKey().toJson());
            entrepriseJson.put("part", entry.getValue());
            partMarcheArray.put(entrepriseJson);
        }

        json.put("partMarche", partMarcheArray);

        return json;
    }
}
