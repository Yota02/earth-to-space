package back.fusee.chargeUtile.Satellite;

import java.util.ArrayList;
import java.util.List;

public class GestionaireSatelite {
    
    private List<Satelite> satelites;

    public GestionaireSatelite(){
        satelites = new ArrayList<>();
    }

    public void ajouterSatelite(Satelite satelite){
        satelites.add(satelite);
    }

    public void removeSatelite(Satelite satelite){
        satelites.remove(satelite);
    }

    public List<Satelite> getSatelites(){
        return satelites;
    }
    
}
