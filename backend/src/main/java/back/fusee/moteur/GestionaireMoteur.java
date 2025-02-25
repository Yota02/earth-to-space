package back.fusee.moteur;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GestionaireMoteur {
    private Map<TypeMoteur, List<Moteur>> moteurMap;
    
    public GestionaireMoteur() {
        this.moteurMap = new HashMap<>();
        for (TypeMoteur type : TypeMoteur.values()) {
            moteurMap.put(type, new ArrayList<>());
        }

        initializeMoteur();
    }

    private void initializeMoteur() {
        // Moteurs Chimiques
        List<Moteur> moteursChimiques = new ArrayList<>();
        moteursChimiques.add(new Moteur(
            "Chimiques", 
            2279.0,                 // poussée (kN)
            Ergol.METHANES,         // carburant
            0.96,                   // rendement
            3177.0,                 // poids (kg)
            2.4,                    // diamètre (m)
            4.3,                    // longueur (m)
            3588.0,                 // température max (K)
            0.995,                  // fiabilité
            true,                   // capacité redémarrage
            3700.0,                 // température critique (K)
            false,                  // arrêt urgence
            2279.0,                 // poussée max (kN)
            473.0,                  // consommation carburant (kg/s)
            TypeMoteur.CHIMIQUE,
            1
        ));
    
        // Moteurs Électriques
        List<Moteur> moteursElectriques = new ArrayList<>();
        moteursElectriques.add(new Moteur(
            "Electriques",
            0.5,                    // poussée (kN)
            Ergol.PROPULSION_ELECTRIQUE, // carburant
            0.72,                   // rendement
            300.0,                  // poids (kg)
            0.5,                    // diamètre (m)
            1.5,                    // longueur (m)
            1200.0,                 // température max (K)
            0.99,                   // fiabilité
            true,                   // capacité redémarrage
            1500.0,                 // température critique (K)
            false,                  // arrêt urgence
            0.5,                    // poussée max (kN)
            0.1,                    // consommation carburant (kg/s)
            TypeMoteur.ELECTRIQUE,
            1
        ));
    
        // Moteurs Nucléaires
        List<Moteur> moteursNucleaires = new ArrayList<>();
        moteursNucleaires.add(new Moteur(
            "Nucleaires",
            333.6,                  // poussée (kN)
            Ergol.NUCLEAIRE,        // carburant
            0.85,                   // rendement
            11300.0,                // poids (kg)
            2.1,                    // diamètre (m)
            6.8,                    // longueur (m)
            2650.0,                 // température max (K)
            0.95,                   // fiabilité
            true,                   // capacité redémarrage
            2800.0,                 // température critique (K)
            false,                  // arrêt urgence
            333.6,                  // poussée max (kN)
            8.5,                    // consommation carburant (kg/s)
            TypeMoteur.NUCLEAIRE, 
            4
        ));
    
        // Moteurs Ioniques
        List<Moteur> moteursIoniques = new ArrayList<>();
        moteursIoniques.add(new Moteur(
            "Ioniques",
            0.236,                  // poussée (kN)
            Ergol.IONIQUE,          // carburant
            0.70,                   // rendement
            12.7,                   // poids (kg)
            0.4,                    // diamètre (m)
            0.5,                    // longueur (m)
            500.0,                  // température max (K)
            0.995,                  // fiabilité
            true,                   // capacité redémarrage
            600.0,                  // température critique (K)
            false,                  // arrêt urgence
            0.236,                  // poussée max (kN)
            0.005,                  // consommation carburant (kg/s)
            TypeMoteur.IONIQUE,
            10
        ));
    
        // Moteurs à Propergol Solide (utilisant des carburants liquides comme boosters)
        List<Moteur> moteursSolides = new ArrayList<>();
        moteursSolides.add(new Moteur(
            "Solides",
            1688.4,                 // poussée (kN)
            Ergol.OXYGEN,           // carburant (oxidant)
            0.92,                   // rendement
            33300.0,                // poids (kg)
            1.6,                    // diamètre (m)
            19.0,                   // longueur (m)
            3400.0,                 // température max (K)
            0.99,                   // fiabilité
            false,                  // capacité redémarrage
            3600.0,                 // température critique (K)
            false,                  // arrêt urgence
            1688.4,                 // poussée max (kN)
            2721.0,                 // consommation carburant (kg/s)
            TypeMoteur.SOLIDE,
            2
        ));
    
        // Ajout des listes au Map
        moteurMap.put(TypeMoteur.CHIMIQUE, moteursChimiques);
        moteurMap.put(TypeMoteur.ELECTRIQUE, moteursElectriques);
        moteurMap.put(TypeMoteur.NUCLEAIRE, moteursNucleaires);
        moteurMap.put(TypeMoteur.IONIQUE, moteursIoniques);
        moteurMap.put(TypeMoteur.SOLIDE, moteursSolides);
    }

    public List<Moteur> getAllMoteurs() {
        return moteurMap.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public List<Moteur> getMoteurParType(TypeMoteur type) {
        return moteurMap.get(type);
    }

    public Moteur getMoteurParNom(String nom) {
        return getAllMoteurs().stream()
            .filter(moteur -> moteur.getNom().equals(nom))
            .findFirst()
            .orElse(null);
    }
}