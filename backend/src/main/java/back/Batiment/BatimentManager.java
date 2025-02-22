package back.Batiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import back.Metaux.Materiaux;
import back.fusee.Piece.PieceFusee;
import back.fusee.moteur.Ergol;
import java.util.Collections;

public class BatimentManager {
    private final Map<String, List<IBatiment>> batimentMap;
    private final List<IBatiment> batimentsPossedes;
    private GestionaireStockage GestionaireStockage;

    public BatimentManager() {
        this.batimentMap = new HashMap<>();
        this.batimentsPossedes = new ArrayList<>();
        GestionaireStockage = new GestionaireStockage();
        initializeBatiments();
    }
    
    public Map<String, List<IBatiment>> getBatimentMap(){
        return batimentMap;
    }

    public GestionaireStockage getGestionaireStockage() {
        return GestionaireStockage;
    }

    public List<BatimentStockage> getBatimentsStockage() {
        List<BatimentStockage> stockage = new ArrayList<>();

        for(IBatiment b : batimentsPossedes){
            if(b instanceof BatimentStockage){
                stockage.add((BatimentStockage) b);
            }
        }

        return stockage;
    }

    public void ajouterPieceParJour() {
        // Récupérer toutes les usines opérationnelles
        List<UsineProduction> usinesOperationnelles = getUsineProduction().stream()
            .filter(IBatiment::estOperationnel)
            .collect(Collectors.toList());

        // Pour chaque usine, calculer et ajouter la production
        for (UsineProduction usine : usinesOperationnelles) {
            if (usine.estOperationnel()) {
                PieceFusee piece = usine.getPieceProduite();
                double productionJournaliere = usine.getProductionParJour();
                int quantiteAajouter = (int) Math.floor(productionJournaliere);

                if (quantiteAajouter > 0) {
                    distribuerPieceAuxStockages(piece, quantiteAajouter);
                }
            }
        }
    }

    private void distribuerPieceAuxStockages(PieceFusee piece, int quantite) {
        int restantAAjouter = quantite;
        List<BatimentStockage> stockages = getBatimentsStockage();
        
        System.out.println("Bâtiments de stockage actuels : " + GestionaireStockage.getBatimentsStockage());
    
        for (BatimentStockage stockage : stockages) {
            System.out.println("Stockage : " + stockage.getNom() + " - Capacité : " + stockage.getCapaciteStockage());
        }
        
        for (BatimentStockage stockage : stockages) {
            if (restantAAjouter <= 0) break;
            if (!stockage.estOperationnel()) continue;
    
            int espaceDisponible = stockage.getCapaciteStockage() - stockage.getStockageActuel();
            if (espaceDisponible > 0) {
                int quantiteStockage = Math.min(restantAAjouter, espaceDisponible);
                stockage.ajouterPiece(piece, quantiteStockage);
                restantAAjouter -= quantiteStockage;
            }
        }
    
        if (restantAAjouter > 0) {
            System.out.println("⚠️ Impossible de stocker " + restantAAjouter + " unités de " + piece.name() + " !");
        }
    }
    

    // Modifier la méthode getProductionParJour pour ne compter que les usines opérationnelles
    public double getProductionParJour(PieceFusee piece) {
        return getUsineProduction().stream()
            .filter(IBatiment::estOperationnel)
            .filter(usine -> usine.getPieceProduite() == piece)
            .mapToDouble(UsineProduction::getProductionParJour)
            .sum();
    }

    public List<UsineProduction> getUsineProduction() {
        List<UsineProduction> usines = new ArrayList<>();

        for(IBatiment b : batimentsPossedes){
            if(b instanceof UsineProduction){
                usines.add((UsineProduction) b);
            }
        }

        return usines;
    }
    
    public List<UsineProductionCarburant> getUsineCarburants() {
        List<UsineProductionCarburant> usines = new ArrayList<>();

        for(IBatiment b : batimentsPossedes){
            if(b instanceof UsineProductionCarburant){
                usines.add((UsineProductionCarburant) b);
            }
        }

        return usines;
    }

    public List<HangarAssemblage> getHangarAssemblage() {
        List<HangarAssemblage> hangars = new ArrayList<>();

        for(IBatiment b : batimentsPossedes){
            if(b instanceof HangarAssemblage){
                hangars.add((HangarAssemblage) b);
            }
        }

        return hangars;
    }

    public void entrainerAstronautes() {
        for (CentreDentrainement centre : getCentreEntrainement()) {
            centre.entrainerAstronautes();
        }
    }

    public List<CentreDentrainement> getCentreEntrainement() {
        List<CentreDentrainement> usines = new ArrayList<>();

        for(IBatiment b : batimentsPossedes){
            if(b instanceof CentreDentrainement){
                usines.add((CentreDentrainement) b);
            }
        }

        return usines;
    }
    
    private void initializeBatiments() {
        initializeHangarAssemblage();
        initializeProductionCarburant();
        initializeUsineProduction();
        initCentreEntrainement();
        initBatiementStockage();
    }

    public void initBatiementStockage(){
        BatimentStockage batimentStockage = new BatimentStockage("Stockage nv1", 100, 100, 100);
        batimentStockage.debloquer();
        GestionaireStockage.ajouterBatimentStockage(batimentStockage);

        GestionaireStockage.ajouterBatimentStockage(new BatimentStockage("Stockage nv2", 150, 12, 200));
        GestionaireStockage.ajouterBatimentStockage(new BatimentStockage("Stockage nv3", 200, 14, 300));
        GestionaireStockage.ajouterBatimentStockage(new BatimentStockage("Stockage nv4", 250, 16, 400));
        GestionaireStockage.ajouterBatimentStockage(new BatimentStockage("Stockage nv5", 300, 18, 500));

        batimentMap.put("batimentStockages", new ArrayList<>(getGestionaireStockage().getBatimentsStockage()));
    }

    private void initializeProductionCarburant() {
        
        List<IBatiment> usinesCarburants = new ArrayList<>();
        
        usinesCarburants.add(new UsineProductionCarburant(
            "SpaceX Propellant", 
            100,  
            12,   
            5.0, 
            Ergol.METHANES,
            0.8   
        ));

        usinesCarburants.add(new UsineProductionCarburant(
            "Oxygen", 
            100,  
            20,   
            10.0, 
            Ergol.OXYGEN,
            0.9   
        ));
        
        usinesCarburants.add(new UsineProductionCarburant(
            "Air Liquide H2", 
            100,  
            24,   
            7.0, 
            Ergol.HYDROGENE,
            0.95  
        ));
        
        usinesCarburants.add(new UsineProductionCarburant(
            "NASA Fuel Research", 
            150,  
            18,   
            6.0, 
            Ergol.AZOTE,
            0.7   
        ));
        
        usinesCarburants.add(new UsineProductionCarburant(
            "BP Aviation Fuel", 
            200,  
            20,   
            8.0, 
            Ergol.KEROSENE,
            0.9   
        ));
        
        ajouterTypeBatiment("usineProductionCarburant", usinesCarburants);

        ajouterBatimentPossede(new UsineProductionCarburant("1", 100, 10, 1, Ergol.OXYGEN, 0.9));
     }

    private void initCentreEntrainement() {
        List<IBatiment> centreEntrainement = new ArrayList<>();

        centreEntrainement.add(new CentreDentrainement("Centre d'entrainement niveau 1", 300, 10, 10));
        centreEntrainement.add(new CentreDentrainement("Centre d'entrainement niveau 2", 400, 15, 20));
        centreEntrainement.add(new CentreDentrainement("Centre d'entrainement niveau 3", 500, 20, 30));

        batimentMap.put("centreEntrainement", centreEntrainement);
    }

    private void initializeUsineProduction() {
        List<IBatiment> usineProduction = new ArrayList<>();

        usineProduction.add(new UsineProduction("Usine niveau 1", 100, 12, Materiaux.FER, PieceFusee.MOTEUR, 50));
        usineProduction.add(new UsineProduction("Usine niveau 2", 150, 15, Materiaux.ALUMINIUM, PieceFusee.RESERVOIR, 70));
        usineProduction.add(new UsineProduction("Usine niveau 3", 200, 20, Materiaux.TITANE, PieceFusee.COQUE, 100));
        usineProduction.add(new UsineProduction("Usine niveau 4", 300, 15, Materiaux.ALUMINIUM, PieceFusee.RESERVOIR, 150));
        usineProduction.add(new UsineProduction("Usine niveau 5", 400, 20, Materiaux.TITANE, PieceFusee.COQUE, 200));

        batimentMap.put("usineProduction", usineProduction);
    }

    private void initializeHangarAssemblage() {
        List<IBatiment> batimentsAssemblage = new ArrayList<>();

        IBatiment tente = new HangarAssemblage("Tente", 100, 1, 5, 10);
        tente.debloquer();

        batimentsAssemblage.add(tente);
        batimentsAssemblage.add(new HangarAssemblage("HighBay", 200, 3, 10, 40 ));
        batimentsAssemblage.add(new HangarAssemblage("MegaBay", 300, 5, 12, 80));
        batimentsAssemblage.add(new HangarAssemblage("GigaBay", 1000, 20, 24, 120));

        batimentMap.put("assemblage", batimentsAssemblage);
    }

   /*  public double getProductionParJour(PieceFusee piece) {
        double production = 0;
        for (IBatiment batiment : batimentsPossedes) {
            if (batiment instanceof UsineProduction) {
                if(((UsineProduction) batiment).getPieceProduite() == piece){
                    production += ((UsineProduction) batiment).getProductionParJour();
                }
            }
        }
        return production;
    } */
    
    public List<IBatiment> getBatimentsParType(String type) {
        return Collections.unmodifiableList(
            batimentMap.getOrDefault(type, new ArrayList<>())
        );
    }
    
    public boolean ajouterBatimentPossede(IBatiment batiment) {
        if (!batimentsPossedes.contains(batiment)) {
            return batimentsPossedes.add(batiment);
        }
        return false;
    }

    public IBatiment getBatiment(String n) {
        for (List<IBatiment> batiments : batimentMap.values()) {
            for (IBatiment batiment : batiments) {
                if (batiment.getNom().equals(n)) {
                    return batiment;
                }
            }
        }
        return null; 
    }
    
    public List<IBatiment> getBatimentsPossedes() {
        return Collections.unmodifiableList(batimentsPossedes);
    }
    
    public boolean ajouterTypeBatiment(String type, List<IBatiment> batiments) {
        if (!batimentMap.containsKey(type)) {
            batimentMap.put(type, new ArrayList<>(batiments));
            return true;
        }
        return false;
    }

}