package back.recherche;

import java.util.*;

import back.Batiment.BatimentManager;

public class GestionnaireRecherche {
    private final List<Recherche> rechercheTotal = new ArrayList<>();
    private final Map<String, Recherche> recherchesMap = new HashMap<>();
    BatimentManager batimentManager;
    GestionaireRechercheDeblocage rechercheDeblocage;
    GestionaireEffet gestionaireEffet;

    public GestionnaireRecherche(BatimentManager batimentManager) {
        this.batimentManager = batimentManager;
        this.gestionaireEffet = new GestionaireEffet();
        this.rechercheDeblocage = new GestionaireRechercheDeblocage(batimentManager);
    }

    public void initRechercheBatiments() {
        initRechercheUsineCarburant();

        for (Recherche recherche : rechercheDeblocage.initeHangarAssemblageRecherche()) {
            ajouterRecherche(recherche);
        }
    }

    public void initRechercheEnergie() {
        initRecherchesPanneauxSolaires();
        // initRecherchesReacteur();
        // initRecherchesBatteris();
    }

    public void initRechercheUsineCarburant() {
        ajouterRecherche(new Recherche(1000, "Usine Ergol de Niveau 1", 5.0, "", CategorieRecherche.BATIMENTS, 1,
                TypeEffet.RENTABILITE));
    }

    public void initRecherchesPanneauxSolaires() {
        ajouterRecherche(new Recherche(1000, "Panneaux Solaires de Niveau 1", 5.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 1, TypeEffet.RENTABILITE, 0.1));
        ajouterRecherche(new Recherche(1500, "Panneaux Solaires de Niveau 2", 7.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 2, TypeEffet.RENTABILITE, 0.2));
        ajouterRecherche(new Recherche(2000, "Panneaux Solaires de Niveau 3", 9.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 3, TypeEffet.RENTABILITE, 0.4));
        ajouterRecherche(new Recherche(2500, "Panneaux Solaires de Niveau 4", 12.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 4, TypeEffet.RENTABILITE, 0.6));
        ajouterRecherche(new Recherche(3000, "Panneaux Solaires de Niveau 5", 15.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 5, TypeEffet.RENTABILITE, 0.8));
        ajouterRecherche(new Recherche(4000, "Panneaux Solaires de Niveau 6", 18.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 6, TypeEffet.RENTABILITE, 1.1));
        ajouterRecherche(new Recherche(5000, "Panneaux Solaires de Niveau 7", 22.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 7, TypeEffet.RENTABILITE, 1.8));
        ajouterRecherche(new Recherche(6000, "Panneaux Solaires de Niveau 8", 25.0, "", CategorieRecherche.ENERGIE,
                SousTypeRecherche.PANNEAUX_SOLAIRES, 8, TypeEffet.RENTABILITE, 2.5));
    }

    public void initRecherchePropulsion() {
        ajouterRecherche(new Recherche(1000, "Moteur de Niveau 1", 5.0, "", CategorieRecherche.PROPULSION,
                SousTypeRecherche.MOTEURS, 1, TypeEffet.RENTABILITE, 0.1));
        ajouterRecherche(new Recherche(1500, "Fuel de Niveau 1", 7.0, "", CategorieRecherche.PROPULSION,
                SousTypeRecherche.FUEL, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheExploration() {
        ajouterRecherche(new Recherche(1000, "Capteurs de Niveau 1", 5.0, "", CategorieRecherche.EXPLORATION,
                SousTypeRecherche.CAPTEURS, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheRobotiques() {
        ajouterRecherche(new Recherche(1000, "Automatisation de Niveau 1", 5.0, "", CategorieRecherche.ROBOTIQUE,
                SousTypeRecherche.AUTOMATISATION, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheTransportHabite() {
        ajouterRecherche(
                new Recherche(1000, "Systèmes de Transport de Niveau 1", 5.0, "", CategorieRecherche.TRANSPORT_HABITE,
                        SousTypeRecherche.SYSTEMES_DE_TRANSPORT, 1, TypeEffet.RENTABILITE, 0.2));
        ajouterRecherche(new Recherche(1500, "Vie Spatiale de Niveau 1", 7.0, "", CategorieRecherche.TRANSPORT_HABITE,
                SousTypeRecherche.VIE_SPATIALE, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheColonisation() {
        ajouterRecherche(new Recherche(1000, "Infrastructures de Niveau 1", 5.0, "", CategorieRecherche.COLONISATION,
                SousTypeRecherche.INFRASTRUCTURES, 1, TypeEffet.RENTABILITE, 0.2));
        ajouterRecherche(new Recherche(1500, "Habitats de Niveau 1", 7.0, "", CategorieRecherche.COLONISATION,
                SousTypeRecherche.HABITATS, 1, TypeEffet.RENTABILITE, 0.2));
        ajouterRecherche(   
                new Recherche(2000, "Agriculture Spatiale de Niveau 1", 10.0, "", CategorieRecherche.COLONISATION,
                        SousTypeRecherche.AGRICULTURE_SPATIALE, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheAstronomie() {
        ajouterRecherche(new Recherche(1000, "Observation Astronomique de Niveau 1", 5.0, "",
                CategorieRecherche.ASTRONOMIE, null, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheRessourcesHumaines() {
        ajouterRecherche(new Recherche(1000, "Gestion des Ressources Humaines de Niveau 1", 5.0, "",
                CategorieRecherche.RH, null, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheFinancier() {
        ajouterRecherche(new Recherche(1000, "Gestion Financière de Niveau 1", 5.0, "", CategorieRecherche.FINANCIER,
                null, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheMateriaux() {
        ajouterRecherche(new Recherche(1000, "Matériaux Avancés de Niveau 1", 5.0, "", CategorieRecherche.MATERIAUX,
                null, 1, TypeEffet.RENTABILITE, 0.2));
    }

    public void initRechercheExobiologie() {
        ajouterRecherche(new Recherche(1000, "Exobiologie de Niveau 1", 5.0, "", CategorieRecherche.EXOBIOLOGIE, null,
                1, TypeEffet.RENTABILITE, 0.2));
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

    protected void ajouterRecherche(Recherche recherche) {
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

    public void rechercheParMoi() {
        for (Recherche r : rechercheTotal) {
            if (r.getEtat() == 1) {
                r.ajouterProgression(r.getNbprogressionParMoi());

                if (r.getEtat() == 1 && r.getProgression() >= 100) {
                    r.setEtat(2);

                    switch (r.getTypeEffet()) {
                        case FIABILITE:
                            switch (r.getSousType()) {
                                case HANGAR_ASSEMBLAGE:
                                    gestionaireEffet.ajouterFiabiliteAssemblage(r.getEffet());
                                    break;
                                case MOTEURS:
                                    gestionaireEffet.ajouterFiabiliteMoteurs(r.getEffet());
                                    break;
                                case FUEL:
                                    gestionaireEffet.ajouterFiabiliteFuel(r.getEffet());
                                    break;
                                case CAPTEURS:
                                    gestionaireEffet.ajouterFiabiliteCapteurs(r.getEffet());
                                    break;
                                case PANNEAUX_SOLAIRES:
                                    gestionaireEffet.ajouterFiabilitePanneauxSolaires(r.getEffet());
                                    break;
                                case REACTEURS:
                                    gestionaireEffet.ajouterFiabiliteReacteurs(r.getEffet());
                                    break;
                                case BATTERIES:
                                    gestionaireEffet.ajouterFiabiliteBatteries(r.getEffet());
                                    break;
                                case AUTOMATISATION:
                                    gestionaireEffet.ajouterFiabiliteAutomatisation(r.getEffet());
                                    break;
                                /*
                                 * case SYSTEMES_AUTONOMES:
                                 * gestionaireEffet.ajouterFiabiliteSystemesAutonomes(r.getEffet());
                                 * break;
                                 */
                                case SYSTEMES_DE_TRANSPORT:
                                    gestionaireEffet.ajouterFiabiliteSystemesDeTransport(r.getEffet());
                                    break;
                                case VIE_SPATIALE:
                                    gestionaireEffet.ajouterFiabiliteVieSpatiale(r.getEffet());
                                    break;
                                case INFRASTRUCTURES:
                                    gestionaireEffet.ajouterFiabiliteInfrastructures(r.getEffet());
                                    break;
                                case HABITATS:
                                    gestionaireEffet.ajouterFiabiliteHabitats(r.getEffet());
                                    break;
                                case AGRICULTURE_SPATIALE:
                                    gestionaireEffet.ajouterFiabiliteAgricultureSpatiale(r.getEffet());
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case QUALITE:
                            switch (r.getSousType()) {
                                case CONSTRUCTION:
                                    gestionaireEffet.ajouterQualiteConstruction(r.getEffet());
                                    break;
                                /*
                                 * case RECHERCHE:
                                 * gestionaireEffet.ajouterQualiteRecherche(r.getEffet());
                                 * break;
                                 */
                                case HANGAR_ASSEMBLAGE:
                                    gestionaireEffet.ajouterQualiteAssemblage(r.getEffet());
                                    break;
                                case MOTEURS:
                                    gestionaireEffet.ajouterQualiteMoteurs(r.getEffet());
                                    break;
                                case CAPTEURS:
                                    gestionaireEffet.ajouterQualiteCapteurs(r.getEffet());
                                    break;
                                case PANNEAUX_SOLAIRES:
                                    gestionaireEffet.ajouterQualitePanneauxSolaires(r.getEffet());
                                    break;
                                case REACTEURS:
                                    gestionaireEffet.ajouterQualiteReacteurs(r.getEffet());
                                    break;
                                case BATTERIES:
                                    gestionaireEffet.ajouterQualiteBatteries(r.getEffet());
                                    break;
                                case AUTOMATISATION:
                                    gestionaireEffet.ajouterQualiteAutomatisation(r.getEffet());
                                    break;
                                /*
                                 * case SYSTEMES_AUTONOMES:
                                 * gestionaireEffet.ajouterQualiteSystemesAutonomes(r.getEffet());
                                 * break;
                                 */
                                case SYSTEMES_DE_TRANSPORT:
                                    gestionaireEffet.ajouterQualiteSystemesDeTransport(r.getEffet());
                                    break;
                                case VIE_SPATIALE:
                                    gestionaireEffet.ajouterQualiteVieSpatiale(r.getEffet());
                                    break;
                                case INFRASTRUCTURES:
                                    gestionaireEffet.ajouterQualiteInfrastructures(r.getEffet());
                                    break;
                                case HABITATS:
                                    gestionaireEffet.ajouterQualiteHabitats(r.getEffet());
                                    break;
                                case AGRICULTURE_SPATIALE:
                                    gestionaireEffet.ajouterQualiteAgricultureSpatiale(r.getEffet());
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case RENTABILITE:
                            switch (r.getSousType()) {
                                case CONSTRUCTION:
                                    gestionaireEffet.ajouterRentabiliteConstruction(r.getEffet());
                                    break;
                                /*
                                 * case RECHERCHE:
                                 * gestionaireEffet.ajouterRentabiliteRecherche(r.getEffet());
                                 * break;
                                 */
                                case HANGAR_ASSEMBLAGE:
                                    gestionaireEffet.ajouterRentabiliteAssemblage(r.getEffet());
                                    break;
                                case MOTEURS:
                                    gestionaireEffet.ajouterRentabiliteMoteurs(r.getEffet());
                                    break;
                                case FUEL:
                                    gestionaireEffet.ajouterRentabiliteFuel(r.getEffet());
                                    break;
                                case CAPTEURS:
                                    gestionaireEffet.ajouterRentabiliteCapteurs(r.getEffet());
                                    break;
                                case PANNEAUX_SOLAIRES:
                                    gestionaireEffet.ajouterRentabilitePanneauxSolaires(r.getEffet());
                                    break;
                                case REACTEURS:
                                    gestionaireEffet.ajouterRentabiliteReacteurs(r.getEffet());
                                    break;
                                case BATTERIES:
                                    gestionaireEffet.ajouterRentabiliteBatteries(r.getEffet());
                                    break;
                                case AUTOMATISATION:
                                    gestionaireEffet.ajouterRentabiliteAutomatisation(r.getEffet());
                                    break;
                                /*
                                 * case SYSTEMES_AUTONOMES:
                                 * gestionaireEffet.ajouterRentabiliteSystemesAutonomes(r.getEffet());
                                 * break;
                                 */
                                case SYSTEMES_DE_TRANSPORT:
                                    gestionaireEffet.ajouterRentabiliteSystemesDeTransport(r.getEffet());
                                    break;
                                case VIE_SPATIALE:
                                    gestionaireEffet.ajouterRentabiliteVieSpatiale(r.getEffet());
                                    break;
                                case INFRASTRUCTURES:
                                    gestionaireEffet.ajouterRentabiliteInfrastructures(r.getEffet());
                                    break;
                                case HABITATS:
                                    gestionaireEffet.ajouterRentabiliteHabitats(r.getEffet());
                                    break;
                                case AGRICULTURE_SPATIALE:
                                    gestionaireEffet.ajouterRentabiliteAgricultureSpatiale(r.getEffet());
                                    break;
                                case STOCKAGE:
                                    gestionaireEffet.ajouterRentabiliteStockage(r.getEffet());
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case ACCELERATION:
                            switch (r.getSousType()) {
                                case CONSTRUCTION:
                                    gestionaireEffet.ajouterAccelerationConstruction(r.getEffet());
                                    break;
                                /*
                                 * case RECHERCHE:
                                 * gestionaireEffet.ajouterAccelerationRecherche(r.getEffet());
                                 * break;
                                 */
                                case HANGAR_ASSEMBLAGE:
                                    gestionaireEffet.ajouterAccelerationAssemblage(r.getEffet());
                                    break;
                                case MOTEURS:
                                    gestionaireEffet.ajouterAccelerationMoteurs(r.getEffet());
                                    break;
                                case FUEL:
                                    gestionaireEffet.ajouterAccelerationFuel(r.getEffet());
                                    break;
                                case CAPTEURS:
                                    gestionaireEffet.ajouterAccelerationCapteurs(r.getEffet());
                                    break;
                                case PANNEAUX_SOLAIRES:
                                    gestionaireEffet.ajouterAccelerationPanneauxSolaires(r.getEffet());
                                    break;
                                case REACTEURS:
                                    gestionaireEffet.ajouterAccelerationReacteurs(r.getEffet());
                                    break;
                                case BATTERIES:
                                    gestionaireEffet.ajouterAccelerationBatteries(r.getEffet());
                                    break;
                                case AUTOMATISATION:
                                    gestionaireEffet.ajouterAccelerationAutomatisation(r.getEffet());
                                    break;
                                /*
                                 * case SYSTEMES_AUTONOMES:
                                 * gestionaireEffet.ajouterAccelerationSystemesAutonomes(r.getEffet());
                                 * break;
                                 */
                                case SYSTEMES_DE_TRANSPORT:
                                    gestionaireEffet.ajouterAccelerationSystemesDeTransport(r.getEffet());
                                    break;
                                case VIE_SPATIALE:
                                    gestionaireEffet.ajouterAccelerationVieSpatiale(r.getEffet());
                                    break;
                                case INFRASTRUCTURES:
                                    gestionaireEffet.ajouterAccelerationInfrastructures(r.getEffet());
                                    break;
                                case AGRICULTURE_SPATIALE:
                                    gestionaireEffet.ajouterAccelerationAgricultureSpatiale(r.getEffet());
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case DEBLOCAGE:
                            ((RechercheDeblocage) r).getObjectAchetable().debloquer();
                            break;

                        default:
                            break;
                    }
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