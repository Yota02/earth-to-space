package back.fusee.chargeUtile;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChargeUtile {

    private double poids;
    private String nom;
    private double volume;

    public ChargeUtile(double poids, String nom, double volume) {
        this.poids = poids;
        this.nom = nom;
        this.volume = volume;
    }

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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        
        json.put("poids", this.poids);
        json.put("nom", this.nom);
        json.put("volume", this.volume);
        
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
}
