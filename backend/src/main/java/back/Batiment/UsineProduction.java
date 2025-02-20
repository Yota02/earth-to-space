package back.Batiment;

import org.json.JSONObject;

import back.Metaux.Materiaux;
import back.fusee.Piece.PieceFusee;
import back.recherche.Recherche;

public class UsineProduction extends IBatiment{

    int quantiteProduite;
    int hauteur;
    Materiaux materiauxEnEntree;
    PieceFusee pieceProduite;

    public UsineProduction(String nom, int superficie, int tempsConstruction, Materiaux materiauxEnEntree, PieceFusee pieceProduite, int quantiteProduite, Recherche rechercheAssociee) {
        this.nom = nom;
        this.superficie = superficie;
        this.tempsConstruction = tempsConstruction;
        this.quantiteProduite = quantiteProduite;
        this.materiauxEnEntree = materiauxEnEntree;
        this.pieceProduite = pieceProduite;
    }

    public Materiaux getMateriauxEnEntree() {
        return materiauxEnEntree;
    }

    public UsineProduction(String nom, int superficie, int tempsConstruction, Materiaux materiauxEnEntree, PieceFusee pieceProduite, int quantiteProduite) {
        this.nom = nom;
        this.superficie = superficie;
        this.tempsConstruction = tempsConstruction;
        this.quantiteProduite = quantiteProduite;
        this.materiauxEnEntree = materiauxEnEntree;
        this.pieceProduite = pieceProduite;
    }

    public PieceFusee getPieceProduite() {
        return pieceProduite;
    }

    public int getQuantiteProduite() {
        return quantiteProduite;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("nom", nom);
        json.put("cout", getCout());
        json.put("superficie", superficie);
        json.put("tempsConstruction", tempsConstruction);
        json.put("materiauxEnEntree", materiauxEnEntree.getNom());
        json.put("pieceProduite", pieceProduite);
        json.put("quantiteProduite", quantiteProduite);
        return json;
    }

    public double getProductionParJour() {
        return quantiteProduite / 30;
    }

    @Override
    double calculerCoutEntretien() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculerCoutEntretien'");
    }

    @Override
    public String toString() {
        return "UsineProduction [materiauxEnEntree=" + materiauxEnEntree + ", pieceProduite=" + pieceProduite
                + ", quantiteProduite=" + quantiteProduite + ", hauteur=" + hauteur + ", nom=" + nom + ", superficie="
                + superficie + ", tempsConstruction=" + tempsConstruction + "]";
    }

}
