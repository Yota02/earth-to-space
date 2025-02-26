package back.fusee.booster;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import back.fusee.Ifusee;
import back.fusee.moteur.Moteur;
import back.fusee.reservoir.Reservoir;
import back.fusee.reservoir.ReservoirFusee;

public class Booster extends Ifusee {

    // Spécification basique
    public String nom;
    public Double taille;
    public Double diametre;
    public Double poidsAVide;
    public Double altitudeMax;
    public Double VitesseMax;

    // Element compose
    public Moteur moteur;
    public int nbMoteur;
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
            Double altitudeMax, Double VitesseMax, Moteur moteur, int nbMoteur,
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
        this.nbMoteur = nbMoteur;
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
        totalPoids += moteur.getPoids() * nbMoteur;

        for (Reservoir r : reservoirs) {
            totalPoids += r.getPoidsAvide();
        }
        return totalPoids;
    }

    public void calculerAltitude() {
        // 1. Calcul de la poussée nette (poussée des moteurs - force gravitationnelle)
        Double pousséeTotale = moteur.getPousse() * nbMoteur;

        if (this.vitesse == 0) {
            this.altitude = 0;
        }

        Double forceGravitationnelle = poids * 9.81; // Poids du booster * accélération gravitationnelle (g = 9.81 m/s²)
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

    public void calculerVitesse() {
        // 1. Calcul de la poussée totale
        Double pousséeTotale = moteur.getPousse() * nbMoteur;

        // 2. Vérification si la poussée est suffisante pour décoller la fusée
        if (pousséeTotale < poids) {
            vitesse = 0.0; // Réinitialisation de la vitesse à 0 si la poussée est insuffisante
            return;
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

        // 6. Calcul de la consommation totale de carburant
        Double consommationTotale = moteur.getConsommationCarburant() * nbMoteur;

        // 7. Mise à jour du poids du booster (poids à vide ne doit pas être dépassé)
        poids -= consommationTotale;
        if (poids < poidsAVide) {
            poids = poidsAVide;
        }
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

    public Moteur getMoteur() {
        return moteur;
    }

    public int getNbMoteur() {
        return nbMoteur;
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

    public double getAltitude() {
        return altitude;
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

        // Moteur
        if (this.moteur != null) {
            JSONObject moteurJson = new JSONObject();
            moteurJson.put("nom", moteur.getNom());
            moteurJson.put("poids", moteur.getPoids());
            json.put("moteur", moteurJson);
        }
        json.put("nombreMoteurs", this.nbMoteur);

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Booster {")
                .append("\n  Nom: ").append(nom)
                .append("\n  Taille: ").append(taille).append("m")
                .append("\n  Diametre: ").append(diametre).append("m")
                .append("\n  Poids à vide: ").append(poidsAVide).append(" kg")
                .append("\n  Poids actuel: ").append(poids).append(" kg")
                .append("\n  Altitude max: ").append(altitudeMax).append(" m")
                .append("\n  Vitesse max: ").append(VitesseMax).append(" m/s")
                .append("\n  Etat: ").append(etat)
                .append("\n  Vitesse actuelle: ").append(vitesse).append(" m/s")
                .append("\n  Altitude actuelle: ").append(altitude).append(" m")
                .append("\n  Nécessite maintenance: ").append(nécessiteMaintenance)
                .append("\n  Est prototype: ").append(estPrototype)
                .append("\n  Est réutilisable: ").append(estReetulisable)
                .append("\n  Système auto-destruction: ").append(aSystèmeAutoDestruction)
                .append("\n  Nombre de moteurs: ").append(nbMoteur);

        if (moteur != null) {
            sb.append("\n  Moteur: {")
                    .append("\n    Nom: ").append(moteur.getNom())
                    .append("\n    Poids: ").append(moteur.getPoids()).append(" kg")
                    .append("\n    Poussée: ").append(moteur.getPousse()).append(" N")
                    .append("\n  }");
        }

        if (reservoirs != null && !reservoirs.isEmpty()) {
            sb.append("\n  Réservoirs:");
            for (ReservoirFusee reservoir : reservoirs) {
                sb.append("\n    { Nom: ").append(reservoir.getNom())
                        .append(", Poids à vide: ").append(reservoir.getPoidsAvide()).append(" kg")
                        .append(", Poids actuel: ").append(reservoir.getPoids()).append(" kg }");
            }
        }

        if (historiquesLancement != null && !historiquesLancement.isEmpty()) {
            sb.append("\n  Historique des lancements: ").append(historiquesLancement);
        }

        sb.append("\n}");
        return sb.toString();
    }

}