package back.fusee.booster;

import java.util.ArrayList;
import java.util.List;

import back.fusee.moteur.Moteur;
import back.fusee.reservoir.Reservoir;
import back.fusee.reservoir.ReservoirFusee;

public class Booster {

    // Spécification basique
    public String nom;
    public Double taille;
    public Double diametre;
    public Double poidsAVide;

    public Double altitudeMax;
    public Double VitesseMax;

    // Element compose
    public List<Moteur> moteur;
    public List<ReservoirFusee> reservoirs;

    // Spécification spécial
    public Boolean estPrototype;
    public Boolean estReetulisable;
    public Boolean aSystèmeAutoDestruction;

    // Etat
    public int etat;
    public Double poids;
    public Double vitesse;
    public Boolean nécessiteMaintenance;

    // Historique
    List<String> historiquesLancement;

    // Constructeur
    public Booster(String nom, Double taille, Double diametre, Double poidsAVide, 
                   Double altitudeMax, Double VitesseMax, List<Moteur> moteur, 
                   List<ReservoirFusee> reservoirs, Boolean estPrototype, 
                   Boolean estReetulisable, Boolean aSystèmeAutoDestruction, 
                   int etat, Double vitesse, Boolean nécessiteMaintenance, 
                   List<String> historiquesLancement) {
        this.nom = nom;
        this.taille = taille;
        this.diametre = diametre;
        this.poidsAVide = poidsAVide;
        this.altitudeMax = altitudeMax;
        this.VitesseMax = VitesseMax;
        this.moteur = moteur;
        this.reservoirs = reservoirs;
        this.estPrototype = estPrototype;
        this.estReetulisable = estReetulisable;
        this.aSystèmeAutoDestruction = aSystèmeAutoDestruction;
        this.etat = etat;
        this.poids = calculerPoids();
        this.vitesse = vitesse;
        this.nécessiteMaintenance = nécessiteMaintenance;
        this.historiquesLancement = historiquesLancement != null ? historiquesLancement : new ArrayList<>();
    }

    // Méthode de calcul du poids total
    private Double calculerPoids() {
        Double totalPoids = poidsAVide;
        for (Moteur m : moteur) {
            totalPoids += m.getPoids();
        }
        for (Reservoir r : reservoirs) {
            totalPoids += r.getPoidsAvide();
        }
        return totalPoids;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public Double getTaille() {
        return taille;
    }

    public Double getDiametre() {
        return diametre;
    }

    public Double getPoidsAVide() {
        return poidsAVide;
    }

    public Double getPoids() {
        return poids;
    }

    public Double getAltitudeMax() {
        return altitudeMax;
    }

    public Double getVitesseMax() {
        return VitesseMax;
    }

    public List<Moteur> getMoteur() {
        return moteur;
    }

    public List<ReservoirFusee> getReservoirs() {
        return reservoirs;
    }

    public Boolean getEstPrototype() {
        return estPrototype;
    }

    public Boolean getEstReetulisable() {
        return estReetulisable;
    }

    public Boolean getASystèmeAutoDestruction() {
        return aSystèmeAutoDestruction;
    }

    public int getEtat() {
        return etat;
    }

    public Double getVitesse() {
        return vitesse;
    }

    public Boolean getNécessiteMaintenance() {
        return nécessiteMaintenance;
    }

    public List<String> getHistoriquesLancement() {
        return historiquesLancement;
    }

    public void ajouterLancementAHistorique(String mission) {
        historiquesLancement.add(mission);
    }

}
