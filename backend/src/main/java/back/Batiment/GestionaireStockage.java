package back.Batiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void ajouterPieceParJour(Map<PieceFusee, Double> productionJournaliere) {
        for (Map.Entry<PieceFusee, Double> entry : productionJournaliere.entrySet()) {
            PieceFusee piece = entry.getKey();
            int quantite = (int) Math.floor(entry.getValue());
            
            if (quantite > 0) {
                distribuerPieceAuxBatiments(piece, quantite);
            }
        }
    }

    private void distribuerPieceAuxBatiments(PieceFusee piece, int quantite) {
        int remainingQuantity = quantite;
        
        for (BatimentStockage batiment : batimentsStockage) {
            if (remainingQuantity <= 0) break;
            
            int espaceDisponible = batiment.getCapaciteStockage() - batiment.getStockageActuel();
            if (espaceDisponible > 0) {
                int quantiteAajouter = Math.min(remainingQuantity, espaceDisponible);
                try {
                    batiment.ajouterPiece(piece, quantiteAajouter);
                    remainingQuantity -= quantiteAajouter;
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
        
        if (remainingQuantity > 0) {
            System.out.println("Warning: Insufficient storage space for " + remainingQuantity + " " + piece.name());
        }
    }

    public void ajouterBatimentStockage(BatimentStockage batiment) {
        batimentsStockage.add(batiment);
    }
    
    public void retirerBatimentStockage(BatimentStockage batiment) {
        batimentsStockage.remove(batiment);
    }

}
