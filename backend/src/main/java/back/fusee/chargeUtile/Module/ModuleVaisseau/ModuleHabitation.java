package back.fusee.chargeUtile.Module.ModuleVaisseau;

import java.time.LocalDateTime;

import back.fusee.chargeUtile.Proprietaire;
import back.fusee.chargeUtile.Destination.Orbite;
import back.fusee.chargeUtile.Destination.Planete;

public class ModuleHabitation extends ModuleVaisseau {

    public ModuleHabitation(double poids, String nom, double volume, int cout, int dureeMission, int statut,
            LocalDateTime dateDeLancement, Planete planete, Orbite orbite, Proprietaire proprietaire) {
        super(poids, nom, volume, cout, dureeMission, statut, dateDeLancement, planete, orbite, proprietaire);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void effectuerAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'effectuerAction'");
    }
    
}
