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
    public double altitude;

    // Historique
    List<String> historiquesLancement;

    // Constructeur
    public Booster(String nom, Double taille, Double diametre, Double poidsAVide, 
                   Double altitudeMax, Double VitesseMax, List<Moteur> moteur, 
                   List<ReservoirFusee> reservoirs, Boolean estPrototype, 
                   Boolean estReetulisable, Boolean aSystèmeAutoDestruction, 
                   int etat, Boolean nécessiteMaintenance, 
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
        this.vitesse = 0.0;
        this.nécessiteMaintenance = nécessiteMaintenance;
        this.historiquesLancement = historiquesLancement != null ? historiquesLancement : new ArrayList<>();
        this.altitude = 0;
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

    public void calculerAltitude() {
        // 1. Calcul de la poussée nette (poussée des moteurs - force gravitationnelle)
        Double pousséeTotale = 0.0;
        for (Moteur m : moteur) {
            pousséeTotale += m.getPousse();  // Récupérer la poussée du moteur
        }

        if(this.vitesse == 0){
            this.altitude = 0;
        }

        Double forceGravitationnelle = poids * 9.81;  // Poids du booster * accélération gravitationnelle (g = 9.81 m/s²)
        Double pousséeNette = pousséeTotale - forceGravitationnelle;
    
        // 2. Calcul de l'accélération (a = F / m)
        Double accélération = pousséeNette / poids;
    
        // 3. Mise à jour de la vitesse (vitesse = vitesse initiale + accélération)
        Double nouvelleVitesse = vitesse + accélération;
    
        // 4. Limiter la vitesse à la vitesse maximale (si nécessaire)
        if (nouvelleVitesse > VitesseMax) {
            vitesse = VitesseMax;
        } else {
            vitesse = nouvelleVitesse;
        }
    
        // 5. Mise à jour de l'altitude (altitude = altitude initiale + vitesse)
        Double nouvelleAltitude = altitude + vitesse;
    
        // 6. Limiter l'altitude à l'altitude maximale
        if (nouvelleAltitude > altitudeMax) {
            altitude = altitudeMax;
        } else {
            altitude = nouvelleAltitude;
        }
    
        // Mise à jour de l'altitude
        this.altitude = altitude;
    }
    
    public double getAltitude(){
        return altitude;
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

    public void calculerVitesse() {
        // 1. Calcul de la poussée totale (somme des poussées de tous les moteurs)
        Double pousséeTotale = 0.0;
        for (Moteur m : moteur) {
            pousséeTotale += m.getPousse();
        }
    
        // 2. Vérification si la poussée est suffisante pour décoller la fusée
        if (pousséeTotale < poids) {
            vitesse = 0.0; // Réinitialisation de la vitesse à 0 si la poussée est insuffisante
            return; // Arrêter l'exécution de la méthode si la poussée est insuffisante
        }
    
        // 3. Calcul de l'accélération (a = F / m)
        Double accélération = pousséeTotale / poids;
    
        // 4. Mise à jour de la vitesse en fonction de l'accélération
        Double nouvelleVitesse = vitesse + accélération;
    
        // 5. Limiter la vitesse à la vitesse maximale
        if (nouvelleVitesse > VitesseMax) {
            vitesse = VitesseMax;
        } else {
            vitesse = nouvelleVitesse;
        }
    
        // 6. Mise à jour du poids en fonction de la consommation de carburant (perte de masse)
        Double consommationTotale = 0.0;
        for (Moteur m : moteur) {
            consommationTotale += m.getConsommationCarburant();
        }
    
        // 7. Calcul de la masse perdue
        Double massePerdue = consommationTotale;
    
        // 8. Mise à jour du poids du booster (poids à vide ne doit pas être dépassé)
        poids -= massePerdue;
        if (poids < poidsAVide) {
            poids = poidsAVide;
        }
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
