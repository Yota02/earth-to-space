package back.recherche;

import java.util.ArrayList;
import java.util.List;

import back.Batiment.*;
import back.fusee.moteur.GestionaireMoteur;
import back.objectAchetable.GestionnaireObject;

public class GestionaireRechercheDeblocage {

    BatimentManager batimentManager;
    GestionaireMoteur moteurManager;
    GestionnaireObject gestionnaireObject;

    public GestionaireRechercheDeblocage(BatimentManager batimentManager, GestionaireMoteur moteurManager, GestionnaireObject gestionnaireObject) {
        this.batimentManager = batimentManager;
        this.moteurManager = moteurManager;
        this.gestionnaireObject = gestionnaireObject;
    }

    public List<RechercheDeblocage> initRecherche() {
        List<RechercheDeblocage> res = new ArrayList<>();
        res.addAll(initUsineProductionRecherche());
        res.addAll(initMoteurRecherche());
        res.addAll(initeHangarAssemblageRecherche());
        res.addAll(initRechercheBatiementStockage());
        res.addAll(initCentreEntrainement());
        res.addAll(initRechercheMateriaux());
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

    public List<RechercheDeblocage> initRechercheMateriaux() {
        List<RechercheDeblocage> res = new ArrayList<>();
        
        // Aluminium - Matériau de base avancé
        res.add(new RechercheDeblocage(
            200, 
            "Aluminium",
            100,
            "Débloque l'utilisation de l'aluminium, un métal léger idéal pour les structures aérospatiales",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            2,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Aluminium")
        ));
    
        // Titane - Matériau avancé
        res.add(new RechercheDeblocage(
            500,
            "Titane",
            200,
            "Débloque l'utilisation du titane, un métal résistant et léger pour les composants critiques",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            4,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Titane")
        ));
    
        // Acier - Matériau intermédiaire
        res.add(new RechercheDeblocage(
            300,
            "Acier",
            150,
            "Débloque l'utilisation de l'acier, un alliage robuste pour les structures principales",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            3,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Acier")
        ));
    
        // Carbone - Matériau haute performance
        res.add(new RechercheDeblocage(
            400,
            "Carbone composite",
            250,
            "Débloque l'utilisation du carbone composite, un matériau ultraléger et résistant",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.COMPOSITES,
            5,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Carbone composite")
        ));
    
        // Inox - Matériau spécialisé
        res.add(new RechercheDeblocage(
            350,
            "Inox",
            180,
            "Débloque l'utilisation de l'inox, un acier résistant à la corrosion",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            3,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Inox")
        ));
    
        // Inconel - Matériau haute température
        res.add(new RechercheDeblocage(
            600,
            "Inconel",
            300,
            "Débloque l'utilisation de l'Inconel, un superalliage pour les environnements extrêmes",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            6,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Inconel")
        ));
    
        // Cuivre - Matériau conducteur
        res.add(new RechercheDeblocage(
            250,
            "Cuivre",
            120,
            "Débloque l'utilisation du cuivre, excellent pour les systèmes électriques et thermiques",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            2,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Cuivre")
        ));
    
        // Nickel - Matériau résistant
        res.add(new RechercheDeblocage(
            450,
            "Nickel",
            200,
            "Débloque l'utilisation du nickel, un métal résistant pour les composants spécialisés",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            4,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Nickel")
        ));
    
        // Béryllium - Matériau exotique
        res.add(new RechercheDeblocage(
            800,
            "Béryllium",
            400,
            "Débloque l'utilisation du béryllium, un métal ultraléger aux propriétés uniques",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            7,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Béryllium")
        ));
    
        // Tantale - Matériau très avancé
        res.add(new RechercheDeblocage(
            1000,
            "Tantale",
            500,
            "Débloque l'utilisation du tantale, un métal rare aux propriétés exceptionnelles",
            CategorieRecherche.MATERIAUX,
            SousTypeRecherche.METAUX,
            8,
            TypeEffet.DEBLOCAGE,
            gestionnaireObject.getMateriau("Tantale")
        ));
    
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
