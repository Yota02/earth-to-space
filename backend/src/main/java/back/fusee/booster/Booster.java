package back.fusee.booster;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        
        // Spécifications basiques
        json.put("nom", this.nom);
        json.put("taille", this.taille);
        json.put("diametre", this.diametre);
        json.put("poidsAVide", this.poidsAVide);
        json.put("altitudeMax", this.altitudeMax);
        json.put("vitesseMax", this.VitesseMax);

        // État
        json.put("etat", this.etat);
        json.put("poids", this.poids);
        json.put("vitesse", this.vitesse);
        json.put("necessiteMaintenance", this.nécessiteMaintenance);

        // Spécifications spéciales
        json.put("estPrototype", this.estPrototype);
        json.put("estReetulisable", this.estReetulisable);
        json.put("aSystemeAutoDestruction", this.aSystèmeAutoDestruction);

        // Conversion des moteurs
        JSONArray moteursArray = new JSONArray();
        if (this.moteur != null) {
            for (Moteur moteur : this.moteur) {
                if (moteur != null) {
                    JSONObject moteurJson = new JSONObject();
                    moteurJson.put("nom", moteur.getNom());
                    moteurJson.put("poids", moteur.getPoids());
                    moteursArray.put(moteurJson);
                }
            }
        }
        json.put("moteurs", moteursArray);
        json.put("nombreMoteurs", getNbMoteur());
        // Conversion des réservoirs
        JSONArray reservoirsArray = new JSONArray();
        if (this.reservoirs != null) {
            for (ReservoirFusee reservoir : this.reservoirs) {
                if (reservoir != null) {
                    JSONObject reservoirJson = new JSONObject();
                    reservoirJson.put("nom", reservoir.getNom());
                    reservoirJson.put("poidsAVide", reservoir.getPoidsAvide());
                    reservoirJson.put("poids", reservoir.getPoids());
                    reservoirsArray.put(reservoirJson);
                }
            }
        }
        json.put("reservoirs", reservoirsArray);

        // Conversion de l'historique des lancements
        JSONArray historiqueArray = new JSONArray();
        if (this.historiquesLancement != null) {
            for (String lancement : this.historiquesLancement) {
                if (lancement != null) {
                    historiqueArray.put(lancement);
                }
            }
        }
        json.put("historiquesLancement", historiqueArray);
        
        return json;
    }


    private int getNbMoteur(){
        return moteur.size();
    }

    /**
     * Convertit une liste de boosters en JSONArray
     * @param boosters Liste des boosters à convertir
     * @return JSONArray contenant tous les boosters
     */
    public static JSONArray toJsonArray(List<Booster> boosters) {
        JSONArray jsonArray = new JSONArray();
        if (boosters != null) {
            for (Booster booster : boosters) {
                if (booster != null) {
                    jsonArray.put(booster.toJson());
                }
            }
        }
        return jsonArray;
    }

}
