package back.Batiment;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculerCoutEntretien'");
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toString'");
    }
    /*
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("nom", nom);
        json.put("superficie", superficie);
        json.put("cout", getCout());
        json.put("tempsConstruction", tempsConstruction);
        json.put("capaciteStockage", capaciteStockage);
        json.put("stockageActuel", stockageActuel);
        
        JSONObject stockageJson = new JSONObject();
        for (Map.Entry<PieceFusee, Integer> entry : stockage.entrySet()) {
            stockageJson.put(entry.getKey().getNom(), entry.getValue());
        }
        json.put("stockage", stockageJson);
        
        return json;
    }*/
    
}
