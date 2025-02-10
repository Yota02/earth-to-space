package back.recherche;

import java.util.*;

public class GestionnaireRecherche {
    private final List<Recherche> rechercheTotal = new ArrayList<>();
    private final Map<String, Recherche> recherchesMap = new HashMap<>();


    public void initRechercheBatiments(){
        initRechercheUsineCarburant();
    }

    public void initRechercheEnergie(){
        initRecherchesPanneauxSolaires();
        //initRecherchesReacteur();
        //initRecherchesBatteris();
    }

    public void initRechercheUsineCarburant(){
        ajouterRecherche(new Recherche(1000, "Usine Ergol de Niveau 1", 5.0, "", CategorieRecherche.BATIMENTS,  1));
    }

    public void initRecherchesPanneauxSolaires() {
        ajouterRecherche(new Recherche(1000, "Panneaux Solaires de Niveau 1", 5.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES,  1));
        ajouterRecherche(new Recherche(1500, "Panneaux Solaires de Niveau 2", 7.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 2));
        ajouterRecherche(new Recherche(2000, "Panneaux Solaires de Niveau 3", 9.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES,  3));
        ajouterRecherche(new Recherche(2500, "Panneaux Solaires de Niveau 4", 12.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 4));
        ajouterRecherche(new Recherche(3000, "Panneaux Solaires de Niveau 5", 15.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 5));
        ajouterRecherche(new Recherche(4000, "Panneaux Solaires de Niveau 6", 18.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES,  6));
        ajouterRecherche(new Recherche(5000, "Panneaux Solaires de Niveau 7", 22.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 7));
        ajouterRecherche(new Recherche(6000, "Panneaux Solaires de Niveau 8", 25.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 8));
    }

    public void initRecherchePropulsion() {
        ajouterRecherche(new Recherche(1000, "Moteur de Niveau 1", 5.0, "", CategorieRecherche.PROPULSION, SousTypeRecherche.MOTEURS, 1));
        ajouterRecherche(new Recherche(1500, "Fuel de Niveau 1", 7.0, "", CategorieRecherche.PROPULSION, SousTypeRecherche.FUEL, 1));
        ajouterRecherche(new Recherche(2000, "Système de Propulsion de Niveau 1", 10.0, "", CategorieRecherche.PROPULSION, SousTypeRecherche.SYSTEMES_DE_PROPULSION, 1));
    }
    
    public void initRechercheExploration() {
        ajouterRecherche(new Recherche(1000, "Capteurs de Niveau 1", 5.0, "", CategorieRecherche.EXPLORATION, SousTypeRecherche.CAPTEURS, 1));
    }
    
    public void initRechercheRobotiques() {
        ajouterRecherche(new Recherche(1000, "Automatisation de Niveau 1", 5.0, "", CategorieRecherche.ROBOTIQUE, SousTypeRecherche.AUTOMATISATION, 1));
    }
    
    public void initRechercheTransportHabite() {
        ajouterRecherche(new Recherche(1000, "Systèmes de Transport de Niveau 1", 5.0, "", CategorieRecherche.TRANSPORT_HABITE, SousTypeRecherche.SYSTEMES_DE_TRANSPORT, 1));
        ajouterRecherche(new Recherche(1500, "Vie Spatiale de Niveau 1", 7.0, "", CategorieRecherche.TRANSPORT_HABITE, SousTypeRecherche.VIE_SPATIALE, 1));
    }
    
    public void initRechercheColonisation() {
        ajouterRecherche(new Recherche(1000, "Infrastructures de Niveau 1", 5.0, "", CategorieRecherche.COLONISATION, SousTypeRecherche.INFRASTRUCTURES, 1));
        ajouterRecherche(new Recherche(1500, "Habitats de Niveau 1", 7.0, "", CategorieRecherche.COLONISATION, SousTypeRecherche.HABITATS, 1));
        ajouterRecherche(new Recherche(2000, "Agriculture Spatiale de Niveau 1", 10.0, "", CategorieRecherche.COLONISATION, SousTypeRecherche.AGRICULTURE_SPATIALE, 1));
    }
    
    public void initRechercheAstronomie() {
        ajouterRecherche(new Recherche(1000, "Observation Astronomique de Niveau 1", 5.0, "", CategorieRecherche.ASTRONOMIE, null, 1));
    }
    
    public void initRechercheRessourcesHumaines() {
        ajouterRecherche(new Recherche(1000, "Gestion des Ressources Humaines de Niveau 1", 5.0, "", CategorieRecherche.RH, null, 1));
    }
    
    public void initRechercheFinancier() {
        ajouterRecherche(new Recherche(1000, "Gestion Financière de Niveau 1", 5.0, "", CategorieRecherche.FINANCIER, null, 1));
    }
    
    public void initRechercheMateriaux() {
        ajouterRecherche(new Recherche(1000, "Matériaux Avancés de Niveau 1", 5.0, "", CategorieRecherche.MATERIAUX, null, 1));
    }
    
    public void initRechercheExobiologie() {
        ajouterRecherche(new Recherche(1000, "Exobiologie de Niveau 1", 5.0, "", CategorieRecherche.EXOBIOLOGIE, null, 1));
    }
    
    public void initialiserRecherches() {
        initRechercheEnergie();
        initRechercheBatiments();
        initRecherchePropulsion();
        initRechercheExploration();
        initRechercheRobotiques();
        initRechercheTransportHabite();
        initRechercheColonisation();
        initRechercheAstronomie();
        initRechercheRessourcesHumaines();
        initRechercheFinancier();
        initRechercheMateriaux();
        initRechercheExobiologie();
    }
    
    
    

    public Recherche getRecherche(String nom) {
        return recherchesMap.get(nom);
    }

    private void ajouterRecherche(Recherche recherche) {
        rechercheTotal.add(recherche);
        recherchesMap.put(recherche.getNom(), recherche);
    }

    public List<Recherche> getRecherches() {
        return rechercheTotal;
    }

    public void demarrerRecherche(int rechercheName) {
        Recherche recherche = findRechercheById(rechercheName);

        recherche.setEtat(1);
    }

    public void rechercheParMoi(){
        for(Recherche r : rechercheTotal){
            if(r.getEtat() == 1){
                r.ajouterProgression(r.getNbprogressionParMoi());

                if(r.getEtat() == 1 && r.getProgression() == 100){
                    r.setEtat(2);
                }
            }
        }
    }

    private Recherche findRechercheById(int id) {
        for (Recherche recherche : rechercheTotal) {
            if (recherche.getId() == id) {
                return recherche;
            }
        }
        return null;
    }


}