package back.fusee;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import back.fusee.booster.Booster;
import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.moteur.Moteur;
public class Fusee extends Ifusee {
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

    private void calculerVitesse(){
        vitesse = boosterPrincipal.vitesse;
    }

    private void calculerAltitude(){
        altitude = boosterPrincipal.altitude;
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
    
        // Boucle de décollage
       while (etat == 0) {   
            boosterPrincipal.calculerVitesse();
            boosterPrincipal.calculerAltitude();

            calculerVitesse();
            calculerAltitude();
    
            // Vérification de la ligne de Karman (altitude de 100 km)
            if (boosterPrincipal.altitude >= 100000) {
                System.out.println("La fusée a dépassé la ligne de Karman (100 km) !");
            }
    
            // Vérification de l'atteinte de l'orbite (vitesse de 28 000 km/h)
            if (boosterPrincipal.vitesse >= 28000) {
                System.out.println("La fusée est en orbite !");
            }
    
            // Attente de 1 seconde avant la prochaine mise à jour
            try {
                Thread.sleep(10);  // Délai d'1 seconde entre chaque itération
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