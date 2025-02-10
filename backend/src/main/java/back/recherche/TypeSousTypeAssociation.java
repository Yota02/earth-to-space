package back.recherche;

import java.util.HashMap;
import java.util.Map;

public class TypeSousTypeAssociation {

    // Association entre les types et leurs sous-types valides
    private static final Map<CategorieRecherche, SousTypeRecherche[]> validSousTypesMap = new HashMap<>();

    static {
        validSousTypesMap.put(CategorieRecherche.PROPULSION, new SousTypeRecherche[]{
                SousTypeRecherche.MOTEURS,
                SousTypeRecherche.FUEL,
                SousTypeRecherche.SYSTEMES_DE_PROPULSION
        });

        validSousTypesMap.put(CategorieRecherche.NAVIGATION, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour NAVIGATION si nécessaire
        });

        validSousTypesMap.put(CategorieRecherche.ENERGIE, new SousTypeRecherche[]{
                SousTypeRecherche.PANNEAUX_SOLAIRES,
                SousTypeRecherche.REACTEURS,
                SousTypeRecherche.BATTERIES
        });

        validSousTypesMap.put(CategorieRecherche.COMMUNICATIONS, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour COMMUNICATIONS si nécessaire
        });

        validSousTypesMap.put(CategorieRecherche.EXPLORATION, new SousTypeRecherche[]{
                SousTypeRecherche.CAPTEURS
        });

        validSousTypesMap.put(CategorieRecherche.TRANSPORT_HABITE, new SousTypeRecherche[]{
                SousTypeRecherche.SYSTEMES_DE_TRANSPORT,
                SousTypeRecherche.VIE_SPATIALE
        });

        validSousTypesMap.put(CategorieRecherche.COLONISATION, new SousTypeRecherche[]{
                SousTypeRecherche.INFRASTRUCTURES,
                SousTypeRecherche.HABITATS,
                SousTypeRecherche.AGRICULTURE_SPATIALE
        });

        validSousTypesMap.put(CategorieRecherche.ASTRONOMIE, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour ASTRONOMIE si nécessaire
        });

        validSousTypesMap.put(CategorieRecherche.BATIMENTS, new SousTypeRecherche[]{
                SousTypeRecherche.CONSTRUCTION,
                SousTypeRecherche.STOCKAGE
        });

        validSousTypesMap.put(CategorieRecherche.RH, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour RH si nécessaire
        });

        validSousTypesMap.put(CategorieRecherche.FINANCIER, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour FINANCIER si nécessaire
        });

        validSousTypesMap.put(CategorieRecherche.MATERIAUX, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour MATERIAUX si nécessaire
        });

        validSousTypesMap.put(CategorieRecherche.ROBOTIQUE, new SousTypeRecherche[]{
                SousTypeRecherche.AUTOMATISATION,
                SousTypeRecherche.SYSTÈMES_AUTONOMES
        });

        validSousTypesMap.put(CategorieRecherche.INTELLIGENCE_ARTIFICIELLE, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour INTELLIGENCE_ARTIFICIELLE si nécessaire
        });

        validSousTypesMap.put(CategorieRecherche.EXOBIOLOGIE, new SousTypeRecherche[]{
                // Ajoutez ici les sous-types pour EXOBIOLOGIE si nécessaire
        });
    }

    // Vérifie si un sous-type est valide pour un type donné
    public static boolean isSousTypeValidForType(CategorieRecherche type, SousTypeRecherche sousType) {
        SousTypeRecherche[] sousTypes = validSousTypesMap.get(type);
        if (sousTypes != null) {
            for (SousTypeRecherche validSousType : sousTypes) {
                if (validSousType == sousType) {
                    return true;
                }
            }
        }
        return false;
    }
}
