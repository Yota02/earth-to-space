package back.fusee.booster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import back.fusee.Ifusee;
import back.fusee.Piece.PieceFusee;
import back.fusee.moteur.Moteur;
import back.fusee.reservoir.Reservoir;
import back.fusee.reservoir.ReservoirFusee;

public class BoosterModel extends Ifusee {

    private Map<PieceFusee, Integer> cout;

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
    public Double poids;

    // Constructeur
    public BoosterModel(String nom, Double taille, Double diametre, Double poidsAVide,
            Double altitudeMax, Double VitesseMax, Moteur moteur, int nbMoteur,
            List<ReservoirFusee> reservoirs, Boolean estPrototype,
            Boolean estReetulisable, Boolean aSystèmeAutoDestruction) {

        
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
        this.poids = calculerPoids();
        this.cout = calculerCout();
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

    public Map<PieceFusee, Integer> calculerCout() {
        Map<PieceFusee, Integer> res = new HashMap<>();
        res.put(PieceFusee.MOTEUR, moteur.getCout() * this.nbMoteur);
        res.put(PieceFusee.RESERVOIR, 0);
        res.put(PieceFusee.COQUE, (int) Math.round(this.taille * this.diametre));
        return res;
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
        json.put("poids", this.poids);

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

        JSONObject coutJson = new JSONObject();
        for (Map.Entry<PieceFusee, Integer> entry : this.cout.entrySet()) {
            coutJson.put(entry.getKey().toString(), entry.getValue());
        }
        json.put("cout", coutJson);

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
                .append("\n  Poids à vide: ").append(poidsAVide).append(" tonne")
                .append("\n  Poids actuel: ").append(poids).append(" tonne")
                .append("\n  Altitude max: ").append(altitudeMax).append(" m")
                .append("\n  Vitesse max: ").append(VitesseMax).append(" m/s")
                .append("\n  Est prototype: ").append(estPrototype)
                .append("\n  Est réutilisable: ").append(estReetulisable)
                .append("\n  Système auto-destruction: ").append(aSystèmeAutoDestruction)
                .append("\n  Nombre de moteurs: ").append(nbMoteur);

        if (moteur != null) {
            sb.append("\n  Moteur: {")
                    .append("\n    Nom: ").append(moteur.getNom())
                    .append("\n    Poids: ").append(moteur.getPoids()).append(" tonne")
                    .append("\n    Poussée: ").append(moteur.getPousse()).append(" N")
                    .append("\n  }");
        }

        if (reservoirs != null && !reservoirs.isEmpty()) {
            sb.append("\n  Réservoirs:");
            for (ReservoirFusee reservoir : reservoirs) {
                sb.append("\n    { Nom: ").append(reservoir.getNom())
                        .append(", Poids à vide: ").append(reservoir.getPoidsAvide()).append(" tonne")
                        .append(", Poids actuel: ").append(reservoir.getPoids()).append(" tonne }");
            }
        }

        sb.append("\n}");
        return sb.toString();
    }

}