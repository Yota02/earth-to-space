package back.Batiment;

import back.fusee.Fusee;

public class PasDeTir extends IBatiment {

    double tauxUsure;
    boolean systemeRefroidissement;
    boolean systemeSecurite;
    int tempsPreparation;

    public PasDeTir(String nom, int superficie, int tempsConstruction, boolean systemeRefroidissement, boolean systemeSecurite, int tempsPreparation) {
        this.nom = nom;
        this.superficie = superficie;
        this.tempsConstruction = tempsConstruction;
        this.tauxUsure = 0;
        this.systemeRefroidissement = systemeRefroidissement;
        this.systemeSecurite = systemeSecurite;
        this.tempsPreparation = tempsPreparation;
    }

    public void initialiserPasDeTir() {
        systemeRefroidissement = true;
        systemeSecurite = true;
        tauxUsure += 0.1;
    }

    public boolean getSystemeSecurite() {
        return systemeSecurite;
    }

    public void lancerFusee(Fusee fusee) {
        if (systemeSecurite && systemeRefroidissement) {
            fusee.decoler();
        } 
    }

    public void effectuerMaintenance() {
        tauxUsure = 0;
    }

    @Override
    public double calculerCoutEntretien() {
        return tauxUsure * 1000; 
    }

    @Override
    public String toString() {
        return "Pas de tire [" + "taux d'Usure : " + tauxUsure;
    }

}
