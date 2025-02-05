package back.Batiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class BatimentManager {
    private final Map<String, List<IBatiment>> batimentMap;
    private final List<IBatiment> batimentsPossedes;
    
    public BatimentManager() {
        this.batimentMap = new HashMap<>();
        this.batimentsPossedes = new ArrayList<>();
        initializeBatiments();
    }
    
    public Map<String, List<IBatiment>> getBatimentMap(){
        return batimentMap;
    }

    private void initializeBatiments() {
        List<IBatiment> batimentsAssemblage = new ArrayList<>();
        
        batimentsAssemblage.add(new HangarAssemblage("Tente", 100, 1, 5, 10));
        batimentsAssemblage.add(new HangarAssemblage("HighBay", 200, 3, 10, 40));
        batimentsAssemblage.add(new HangarAssemblage("MegaBay", 300, 5, 12, 80));
        batimentsAssemblage.add(new HangarAssemblage("GigaBay", 1000, 20, 24, 120));
        
        batimentMap.put("assemblage", batimentsAssemblage);
    }
    
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