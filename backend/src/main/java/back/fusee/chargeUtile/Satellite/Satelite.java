package back.fusee.chargeUtile.Satellite;

import java.time.LocalDateTime;

import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.chargeUtile.Proprietaire;
import back.fusee.chargeUtile.Destination.Orbite;
import back.fusee.chargeUtile.Destination.Planete;

public abstract class Satelite extends ChargeUtile {
    
    private int dureeVieEstimee;

    public Satelite(double poids, String nom, double volume, int cout, int dureeMission, int statut, LocalDateTime dateDeLancement, Planete planete, Orbite orbite, Proprietaire proprietaire, int dureeVieEstimee) {
        super(poids, nom, volume, cout, dureeMission, statut, dateDeLancement, planete, orbite, proprietaire);
        this.dureeVieEstimee = dureeVieEstimee;
    }

    abstract
    public void effectuerAction();

    public int getDureeVieEstimee(){
        return dureeVieEstimee;
    }

    public void exploser(){
        
    }

}