package back.fusee.chargeUtile.Satellite;

import java.time.LocalDateTime;

import back.fusee.chargeUtile.Proprietaire;
import back.fusee.chargeUtile.Destination.Orbite;
import back.fusee.chargeUtile.Destination.Planete;

public class SateliteMilitaire extends Satelite{

    public SateliteMilitaire(double poids, String nom, double volume, int cout, int dureeMission, int statut,
            LocalDateTime dateDeLancement, Planete planete,
            Orbite orbite, Proprietaire proprietaire, int dureeVieEstimee) {
        super(poids, nom, volume, cout, dureeMission, statut, dateDeLancement, planete, orbite,
                proprietaire, dureeVieEstimee);
    }

    @Override
    public void effectuerAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'effectuerAction'");
    }
    
}
