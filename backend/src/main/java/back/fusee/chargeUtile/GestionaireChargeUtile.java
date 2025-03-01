package back.fusee.chargeUtile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import back.fusee.chargeUtile.Module.ModuleVaisseau.GestionaireModuleVaisseau;
import back.fusee.chargeUtile.Satellite.GestionaireSatelite;

public class GestionaireChargeUtile {
    
    GestionaireSatelite gestionaireSatelite;
    GestionaireModuleVaisseau gestionaireModuleVaisseau;
    private Map<String, List<ChargeUtile>> chargesUtiles;

    public GestionaireChargeUtile(){
        gestionaireSatelite = new GestionaireSatelite();
        chargesUtiles = new HashMap<>();
    }

    public void ajouterChargeUtile(String chargeUtileString, ChargeUtile chargeUtile) {
        if (!chargesUtiles.containsKey(chargeUtileString)) {
            chargesUtiles.put(chargeUtileString, new ArrayList<ChargeUtile>());
        }
        chargesUtiles.get(chargeUtileString).add(chargeUtile);
    }

    public Map<String, List<ChargeUtile>> getChargesUtiles() {
        return chargesUtiles;
    }

    public GestionaireSatelite getGestionaireSatelite() {
        return gestionaireSatelite;
    }


}
