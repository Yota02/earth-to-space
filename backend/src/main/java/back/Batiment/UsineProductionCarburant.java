package back.Batiment;

import back.fusee.moteur.Ergol;

public class UsineProductionCarburant extends IBatiment{

    public double productionParJour;
    private Ergol ergol;
    public double efficaciteProduction;

    public UsineProductionCarburant(String nom, int superficie, int tempsConstruction, double productionParJour, Ergol ergol, double efficaciteProduction) {
        this.nom = nom;
        this.superficie = superficie;
        this.tempsConstruction = tempsConstruction;
        this.productionParJour = productionParJour;
        this.ergol = ergol;
        this.efficaciteProduction = efficaciteProduction;
    }

    public Ergol getErgol(){
        return ergol;
    }

    public double getQuantiteProduiteParJour(){
        return productionParJour;
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
    
}
