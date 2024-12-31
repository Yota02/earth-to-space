package back.usine;

import back.fusee.Fusee;
import back.moteur.Moteur;
import back.fusee.fuseeTier1;

import java.time.LocalDate;

public class UsineTier2 extends Usine {

    public UsineTier2(String nom, float superficie, int capaciteProductionMensuelle) {
        super(nom, superficie, capaciteProductionMensuelle);
    }

    @Override
    public Fusee creerFusee(String nom, float taille, float poidsTotal, Moteur moteur,
                            float carburantRestant, float poidChargeUtile, boolean systemeSecurite,
                            LocalDate dateLancement, String destination, String typeMission, int etat) {
        produireFusee(); 
        return new fuseeTier1.Builder()
                .nom(nom)
                .taille(taille)
                .poidsTotal(poidsTotal)
                .moteur(moteur)
                .carburantRestant(carburantRestant)
                .poidChargeUtile(poidChargeUtile)
                .systemeSecurite(systemeSecurite)
                .dateLancement(dateLancement)
                .destination(destination)
                .typeMission(typeMission)
                .etat(etat)
                .build();
    }
}
