package back.Batiment;

import java.util.ArrayList;
import java.util.List;

import back.Ressources_Humaines.Astronaute;
import back.recherche.Recherche;

public class CentreDentrainement extends IBatiment{

    private List<Astronaute> astronautes;
    private int capaciteMax;

    public CentreDentrainement(String nom, int superficie, int tempsConstruction,  int capaciteMax, Recherche rechercheAssociee) {
        this.nom = nom;
        this.superficie = superficie;
        this.tempsConstruction = tempsConstruction;
        this.capaciteMax = capaciteMax;
        this.astronautes = new ArrayList<>();
    }

    public CentreDentrainement(String nom, int superficie, int tempsConstruction,  int capaciteMax) {
        this.nom = nom;
        this.superficie = superficie;
        this.capaciteMax = capaciteMax;
        this.tempsConstruction = tempsConstruction;
        this.astronautes = new ArrayList<>();
    }

    public void ajouterAstronaute(Astronaute astronaute) {
        this.astronautes.add(astronaute);
    }

    public void retirerAstronaute(Astronaute astronaute) {
        this.astronautes.remove(astronaute);
    }

    public List<Astronaute> getAstronautes() {
        return this.astronautes;
    }

    public void entrainerAstronautes() {
        for (Astronaute astronaute : this.astronautes) {
            astronaute.entrainer();
        }
    }

    @Override
    double calculerCoutEntretien() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculerCoutEntretien'");
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toString'");
    }
    


}
