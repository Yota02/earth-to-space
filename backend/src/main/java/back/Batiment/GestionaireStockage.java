package back.Batiment;

import java.util.ArrayList;
import java.util.List;

import back.fusee.Piece.PieceFusee;
import back.objectAchetable.ObjectAchetable;

public class GestionaireStockage {

    private List<ObjectAchetable> objectTotals;
    private List<BatimentStockage> batimentsStockage;

    public GestionaireStockage() {
        this.batimentsStockage = new ArrayList<>();
        this.objectTotals = new ArrayList<>();
    }

    public List<BatimentStockage> getBatimentsStockage() {
        return batimentsStockage;
    }

    public double getStockageTotal() {
        double total = 0;
        for (BatimentStockage batiment : batimentsStockage) {
            total += batiment.getStockageActuel();
        }
        return total;
    }

    public double getStockageTotalPiece(PieceFusee piece) {
        double total = 0;
        for (BatimentStockage batiment : batimentsStockage) {
            if (batiment.getStockage().containsKey(piece)) {
                total += batiment.getStockage().get(piece);
            }
        }
        return total;
    }

    public void ajouterBatimentStockage(BatimentStockage batiment) {
        batimentsStockage.add(batiment);
    }
    
    public void retirerBatimentStockage(BatimentStockage batiment) {
        batimentsStockage.remove(batiment);
    }

}
