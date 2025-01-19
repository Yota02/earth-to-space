package back.Ressources_Humaines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GestionnaireRessources_Humaines {

    private Map<String, List<Personne>> personnesParType;


    public GestionnaireRessources_Humaines() {
        personnesParType = new HashMap<>();
    }

    public void ajouterPersonne(Personne personne) {
        String type = personne.getClass().getSimpleName();
        personnesParType.putIfAbsent(type, new ArrayList<>());
        personnesParType.get(type).add(personne);
    }

    public void afficherPersonnes() {
        for (Map.Entry<String, List<Personne>> entry : personnesParType.entrySet()) {
            System.out.println("Type: " + entry.getKey());
            for (Personne personne : entry.getValue()) {
                System.out.println(personne.toString());
            }
        }
    }

    public List<Personne> getPersonnesParType(String type) {
        return personnesParType.getOrDefault(type, new ArrayList<>());
    }

    public Map<String, List<Personne>> getPersonnesParTypeMap() {
        return personnesParType; 
    }
}
