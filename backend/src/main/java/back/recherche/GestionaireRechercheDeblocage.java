package back.recherche;

import java.util.ArrayList;
import java.util.List;

import back.Batiment.*;
import back.fusee.moteur.GestionaireMoteur;

public class GestionaireRechercheDeblocage {

    BatimentManager batimentManager;
    GestionaireMoteur moteurManager;

    public GestionaireRechercheDeblocage(BatimentManager batimentManager, GestionaireMoteur moteurManager) {
        this.batimentManager = batimentManager;
        this.moteurManager = moteurManager;
    }

    public List<RechercheDeblocage> initRecherche() {
        List<RechercheDeblocage> res = new ArrayList<>();
        res.addAll(initUsineProductionRecherche());
        res.addAll(initMoteurRecherche());
        res.addAll(initeHangarAssemblageRecherche());
        res.addAll(initRechercheBatiementStockage());
        res.addAll(initCentreEntrainement());
        return res;
    }

    public List<RechercheDeblocage> initRechercheBatiementStockage(){

        List<RechercheDeblocage> res = new ArrayList<>();
        
        res.add(new RechercheDeblocage(100, "Stockage niveau 2", 150, "Deblocage du Stockage niveau 2, permet de stocker 200 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.STOCKAGE, 2, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Stockage niveau 2")));

        res.add(new RechercheDeblocage(200, "Stockage niveau 3", 200, "Deblocage du Stockage niveau 3, permet de stocker 300 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.STOCKAGE, 3, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Stockage niveau 3")));

        res.add(new RechercheDeblocage(100, "Stockage niveau 4", 300, "Deblocage du Stockage niveau 4, permet de stocker 400 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.STOCKAGE, 4, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Stockage niveau 4")));

        res.add(new RechercheDeblocage(200, "Stockage niveau 5", 400, "Deblocage du Stockage niveau 5, permet de stocker 500 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.STOCKAGE, 5, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Stockage niveau 5")));

        return res; 
    }

    public List<RechercheDeblocage> initCentreEntrainement() {
        List<RechercheDeblocage> res = new ArrayList<>();

        res.add(new RechercheDeblocage(100, "Centre d'entrainement niveau 1", 10, "Deblocage du Centre d'entrainement niveau 1, permet d'entrainer 5 astronautes",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.CENTRE_ENTRAINEMENT, 1, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Centre d'entrainement niveau 1")));

        res.add(new RechercheDeblocage(100, "Centre d'entrainement niveau 2", 15, "Deblocage du Centre d'entrainement niveau 2, permet d'entrainer 10 astronautes",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.CENTRE_ENTRAINEMENT, 2, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Centre d'entrainement niveau 2")));

        res.add(new RechercheDeblocage(200, "Centre d'entrainement niveau 3", 20, "Deblocage du Centre d'entrainement niveau 3, permet d'entrainer 15 astronautes",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.CENTRE_ENTRAINEMENT, 3, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Centre d'entrainement niveau 3")));

        return res;
    }

    public List<RechercheDeblocage> initUsineProductionRecherche() {
        List<RechercheDeblocage> res = new ArrayList<>();

        res.add(new RechercheDeblocage(100, "Usine niveau 1", 100, "Deblocage de l'Usine niveau 1, permet de produire 50 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.USINE_PRODUCTION, 1, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Usine niveau 1")));
        res.add(new RechercheDeblocage(100, "Usine niveau 2", 150, "Deblocage de l'Usine niveau 2, permet de produire 70 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.USINE_PRODUCTION, 2, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Usine niveau 2")));
        res.add(new RechercheDeblocage(200, "Usine niveau 3", 200, "Deblocage de l'Usine niveau 3, permet de produire 100 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.USINE_PRODUCTION, 5, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Usine niveau 3")));
        res.add(new RechercheDeblocage(100, "Usine niveau 4", 300, "Deblocage de l'Usine niveau 4, permet de produire 150 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.USINE_PRODUCTION, 7, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Usine niveau 4")));
        res.add(new RechercheDeblocage(200, "Usine niveau 5", 400, "Deblocage de l'Usine niveau 5, permet de produire 200 pieces",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.USINE_PRODUCTION, 9, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("Usine niveau 5")));
        return res;
    }

    public List<RechercheDeblocage> initMoteurRecherche() {
        List<RechercheDeblocage> res = new ArrayList<>();
    
        // Moteurs Chimiques - Technologie de base
        res.add(new RechercheDeblocage(
            100,                    // coût de recherche
            "Moteur Chimiques",     // nom
            150,                    // temps de recherche
            "Débloque le moteur Chimiques",
            CategorieRecherche.PROPULSION,
            SousTypeRecherche.MOTEURS,
            1,                      // niveau requis
            TypeEffet.DEBLOCAGE,
            moteurManager.getMoteurParNom("Chimiques")
        ));
    
        // Moteurs Électriques - Technologie intermédiaire
        res.add(new RechercheDeblocage(
            300,
            "Moteur Electrique",
            300,
            "Débloque le MPD-100, un moteur électrique compact pour missions spatiales",
            CategorieRecherche.PROPULSION,
            SousTypeRecherche.MOTEURS,
            4,
            TypeEffet.DEBLOCAGE,
            moteurManager.getMoteurParNom("Electriques")
        ));
    
        // Moteurs Ioniques - Technologie avancée
        res.add(new RechercheDeblocage(
            500,
            "Moteur Ionique",
            400,
            "Débloque le moteur Ionique, un propulseur ionique efficace pour missions longue durée",
            CategorieRecherche.PROPULSION,
            SousTypeRecherche.MOTEURS,
            7,
            TypeEffet.DEBLOCAGE,
            moteurManager.getMoteurParNom("Ioniques")
        ));
    
        // Moteurs Nucléaires - Technologie très avancée
        res.add(new RechercheDeblocage(
            800,
            "Moteur Nucleaires",
            500,
            "Débloque le moteur Nucleaires, un propulseur nucléaire pour missions interplanétaires",
            CategorieRecherche.PROPULSION,
            SousTypeRecherche.MOTEURS,
            9,
            TypeEffet.DEBLOCAGE,
            moteurManager.getMoteurParNom("Nucleaires")
        ));
    
        // Moteurs Solides - Technologie alternative
        res.add(new RechercheDeblocage(
            300,
            "Moteur Solides",
            200,
            "Débloque le booster Solides.",
            CategorieRecherche.PROPULSION,
            SousTypeRecherche.MOTEURS,
            2,
            TypeEffet.DEBLOCAGE,
            moteurManager.getMoteurParNom("Solides")
        ));
    
        return res;
    }

    public List<RechercheDeblocage> initeHangarAssemblageRecherche() {
        List<RechercheDeblocage> res = new ArrayList<>();

        res.add(new RechercheDeblocage(100, "High Bay", 100, "Deblocage du High Bay, peut accueillir 3 Fusée",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.HANGAR_ASSEMBLAGE, 2, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("HighBay")));
        res.add(new RechercheDeblocage(100, "Mega Bay", 150, "Deblocage du Mega Bay, peut accueillir 5 Fusée",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.HANGAR_ASSEMBLAGE, 3, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("MegaBay")));
        res.add(new RechercheDeblocage(200, "Giga Bay", 200, "Deblocage du Giga Bay, peut accueillir 20 Fusée",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.HANGAR_ASSEMBLAGE, 4, TypeEffet.DEBLOCAGE,
                batimentManager.getBatiment("GigaBay")));
        return res;
    }


}
