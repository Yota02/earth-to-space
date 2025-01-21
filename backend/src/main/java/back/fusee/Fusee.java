package back.fusee;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import back.fusee.booster.Booster;
import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.moteur.Moteur;
public class Fusee {
    private String nom;
    private double taille;
    private double diametre;
    private double poidsTotal;
    private double altitudeMax;
    private Booster boosterPrincipal;
    private List<ChargeUtile> poidChargeUtiles;
    private boolean systemeSecurite;
    private int etat;
    private double vitesse;
    private double altitude;

    public Fusee(String nom, double taille, Booster boosterPrincipal, List<ChargeUtile> poidChargeUtiles, boolean systemeSecurite) {
        this.nom = nom;
        this.taille = taille;
        this.boosterPrincipal = boosterPrincipal; 
        this.poidChargeUtiles = poidChargeUtiles;
        this.systemeSecurite = systemeSecurite;
        this.etat = 0;
        this.vitesse = 0;
        this.altitude = 0;

        this.diametre = calculateDiametre();
        this.poidsTotal = calculatePoidsTotal();
        this.altitudeMax = calculateAltitudeMax();
    }

    private double calculateDiametre() {
        return boosterPrincipal != null ? boosterPrincipal.getDiametre() : 10.0;
    }

    private double calculatePoidsTotal() {
        double total = 0;
        if (boosterPrincipal != null) {
            total += boosterPrincipal.getPoids();
        }
        if (poidChargeUtiles != null) {
            for (ChargeUtile charge : poidChargeUtiles) {
                total += charge.getPoids();
            }
        }
        return total;
    }

    private double calculateAltitudeMax() {
        if (boosterPrincipal == null || boosterPrincipal.getMoteur() == null) {
            return 0.0;
        }
        
        double totalPoussee = 0;
        for (Moteur m : boosterPrincipal.getMoteur()) {
            if (m != null) {
                totalPoussee += m.getPousseeMax();
            }
        }
        return this.poidsTotal - totalPoussee;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        
        json.put("nom", this.nom);
        json.put("taille", this.taille);
        json.put("diametre", this.diametre);
        json.put("poidsTotal", this.poidsTotal);
        json.put("altitudeMax", this.altitudeMax);
        json.put("systemeSecurite", this.systemeSecurite);
        json.put("etat", this.etat);
        json.put("vitesse", this.vitesse);
        json.put("altitude", this.altitude);
        
        // Conversion du booster principal
        if (this.boosterPrincipal != null) {
            json.put("boosterPrincipal", this.boosterPrincipal.toJson());
        } else {
            json.put("boosterPrincipal", JSONObject.NULL);
        }
        
        // Conversion de la liste des charges utiles
        JSONArray chargesArray = new JSONArray();
        if (this.poidChargeUtiles != null) {
            for (ChargeUtile charge : this.poidChargeUtiles) {
                if (charge != null) {
                    chargesArray.put(charge.toJson());
                }
            }
        }
        json.put("poidChargeUtiles", chargesArray);
        
        return json;
    }

    /**
     * Convertit une liste de fusées en JSONArray
     * @param fusees Liste des fusées à convertir
     * @return JSONArray contenant toutes les fusées
     */
    public static JSONArray toJsonArray(List<Fusee> fusees) {
        JSONArray jsonArray = new JSONArray();
        if (fusees != null) {
            for (Fusee fusee : fusees) {
                if (fusee != null) {
                    jsonArray.put(fusee.toJson());
                }
            }
        }
        return jsonArray;
    }

    public void decoler() {
        System.out.println("Décollage de la fusée " + nom + "...");
        for (int i = 0; i < 10; i++) {
            vitesse++;
            altitude++;
            
            /* if(altitude >= 100){
                System.out.println("A depasser la ligne de karman");
            }

            if(vitesse >= 28000){
                System.out.println("en orbit");
            } */

            try {
                Thread.sleep(10);  
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void exploser() {
        System.out.println("Boom... Fusée " + nom + " a explosé...");
    }

    public void atterir() {
        System.out.println("Fusée " + nom + " a atterri...");
    }

    public void orbite() {
        System.out.println("La fusée " + nom + " est en orbite.");
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public double getTaille() {
        return taille;
    }

    public double getDiametre() {
        return diametre;
    }    

    public double getPoidsTotal() {
        return poidsTotal;
    }

    public double getAltitudeMax() {
        return altitudeMax;
    }

    public Booster getBoosterPrincipal() {
        return boosterPrincipal;
    }

    public List<ChargeUtile> getPoidChargeUtiles() {
        return poidChargeUtiles;
    }

    public boolean isSystemeSecurite() {
        return systemeSecurite;
    }

    public int getEtat() {
        return etat;
    }
}