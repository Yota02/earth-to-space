package back.Batiment;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import back.fusee.Piece.PieceFusee;

public class BatimentStockage extends IBatiment {

    private int capaciteStockage;
    private int stockageActuel;
    private Map<PieceFusee, Integer> stockage;

    public BatimentStockage(String nom, int superficie, int tempsConstruction, int capaciteStockage) {
        this.nom = nom;
        this.superficie = superficie;
        this.tempsConstruction = tempsConstruction;
        this.capaciteStockage = capaciteStockage;
        this.stockageActuel = 0;
        this.stockage = new HashMap<>();
    }

    public int getCapaciteStockage() {
        return capaciteStockage;
    }

    public void setCapaciteStockage(int capaciteStockage) {
        this.capaciteStockage = capaciteStockage;
    }

    public void ajouterPiece(PieceFusee piece, int quantite) {
        if (stockageActuel + quantite > capaciteStockage) {
            throw new IllegalArgumentException("Stockage plein");
        }
        if (stockage.containsKey(piece)) {
            stockage.put(piece, stockage.get(piece) + quantite);
        } else {
            stockage.put(piece, quantite);
        }
        stockageActuel += quantite;
    }

    public int getStockageActuel() {
        return stockageActuel;
    }

    public void setStockageActuel(int stockageActuel) {
        this.stockageActuel = stockageActuel;
    }

    public Map<PieceFusee, Integer> getStockage() {
        return stockage;
    }

    @Override
    double calculerCoutEntretien() {
        return 10;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BatimentStockage{")
                .append("nom='").append(nom).append('\'')
                .append(", superficie=").append(superficie)
                .append(", capaciteStockage=").append(capaciteStockage)
                .append(", stockageActuel=").append(stockageActuel)
                .append(", pieces={");

        for (Map.Entry<PieceFusee, Integer> entry : stockage.entrySet()) {
            sb.append(entry.getKey().getNom()).append(": ").append(entry.getValue()).append(", ");
        }

        if (!stockage.isEmpty()) {
            sb.setLength(sb.length() - 2); // Enlève la dernière virgule et espace
        }

        sb.append("}}");
        return sb.toString();
    }

    public JSONObject toJSON2() {
        JSONObject json = new JSONObject();

        // Informations de base du bâtiment
        json.put("nom", this.nom != null ? this.nom : "");
        json.put("superficie", this.superficie);

        // État du bâtiment
        json.put("enConstruction", this.enConstruction);
        json.put("operationnel", this.operationnel);
        json.put("tempsConstruction", this.tempsConstruction);
        json.put("progression", this.progression);
        json.put("etat", this.etat);
        json.put("cout", getCout());

        // Informations spécifiques au stockage
        json.put("capaciteStockage", this.getCapaciteStockage());
        json.put("stockageActuel", this.stockageActuel);

        // Gestion de la date de construction
        if (this.anneeConstruction != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            json.put("anneeConstruction", this.anneeConstruction.format(formatter));
        } else {
            json.put("anneeConstruction", JSONObject.NULL);
        }
        
    
        // Gestion du stockage des pièces
        JSONObject stockageJson = new JSONObject();
        for (Map.Entry<PieceFusee, Integer> entry : stockage.entrySet()) {
            stockageJson.put(entry.getKey().name(), entry.getValue());
        }
        json.put("stockage", stockageJson);

        return json;
    }

}
