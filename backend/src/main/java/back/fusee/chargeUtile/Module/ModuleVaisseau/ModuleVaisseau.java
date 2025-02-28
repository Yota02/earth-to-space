package back.fusee.chargeUtile.Module.ModuleVaisseau;

import java.time.LocalDateTime;

import back.fusee.chargeUtile.Proprietaire;
import back.fusee.chargeUtile.Destination.Orbite;
import back.fusee.chargeUtile.Destination.Planete;
import back.fusee.chargeUtile.Module.Module;

public abstract class ModuleVaisseau extends Module{

    public ModuleVaisseau(double poids, String nom, double volume, int cout, int dureeMission, int statut,
            LocalDateTime dateDeLancement, Planete planete, Orbite orbite, Proprietaire proprietaire) {
        super(poids, nom, volume, cout, dureeMission, statut, dateDeLancement, planete, orbite, proprietaire);
    }
    
}
