package back.fusee.chargeUtile.Module;

import java.time.LocalDateTime;

import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.chargeUtile.Proprietaire;
import back.fusee.chargeUtile.Destination.Orbite;
import back.fusee.chargeUtile.Destination.Planete;

public abstract class Module extends ChargeUtile{

    public Module(double poids, String nom, double volume, int cout, int dureeMission, int statut,
            LocalDateTime dateDeLancement, Planete planete, Orbite orbite, Proprietaire proprietaire) {
        super(poids, nom, volume, cout, dureeMission, statut, dateDeLancement, planete, orbite, proprietaire);
    }
    
    abstract
    public void effectuerAction();

}
