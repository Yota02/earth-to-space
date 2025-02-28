package back.fusee.chargeUtile.Module.ModulePlanetaire;

import java.time.LocalDateTime;
import java.util.List;

import back.Ressources_Humaines.Astronaute;
import back.fusee.chargeUtile.Proprietaire;
import back.fusee.chargeUtile.Destination.Orbite;
import back.fusee.chargeUtile.Destination.Planete;
import back.fusee.chargeUtile.Module.Module;

public abstract class ModulePlanetaire extends Module{

    List<Astronaute> astronautes;

    public ModulePlanetaire(double poids, String nom, double volume, int cout, int dureeMission, int statut,
            LocalDateTime dateDeLancement, Planete planete, Orbite orbite, Proprietaire proprietaire) {
        super(poids, nom, volume, cout, dureeMission, statut, dateDeLancement, planete, orbite, proprietaire);
    }
    


}
