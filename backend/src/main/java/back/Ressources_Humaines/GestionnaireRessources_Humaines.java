package back.Ressources_Humaines;

import java.util.*;

public class GestionnaireRessources_Humaines {

    private Map<String, List<Personne>> personnesParType;
    private Map<String, List<Personne>> employes;
    private Map<String, List<Personne>> marcheEmploi;

    public GestionnaireRessources_Humaines() {
        personnesParType = new HashMap<>();
        employes = new HashMap<>();
        marcheEmploi = new HashMap<>();
        initMarcheEmploie();
    }

    public void ajouterPersonne(Personne personne, Map<String, List<Personne>> list) {
        String type = personne.getClass().getSimpleName();
        list.putIfAbsent(type, new ArrayList<>());
    
        List<Personne> personnes = list.get(type);
        
        if (personnes.size() < 20) {
            personnes.add(personne);
        }
    }    

    public void embaucherPersonne(Personne personne) {
        String type = personne.getClass().getSimpleName();
        if (marcheEmploi.containsKey(type) && marcheEmploi.get(type).remove(personne)) {
            employes.putIfAbsent(type, new ArrayList<>());
            employes.get(type).add(personne);
        } else {
            System.out.println("Cette personne n'est pas disponible dans le marché de l'emploi" + type);
        }
    }

    public void licencierPersonne(Personne personne) {
        String type = personne.getClass().getSimpleName();
        if (employes.containsKey(type) && employes.get(type).remove(personne)) {
            marcheEmploi.putIfAbsent(type, new ArrayList<>());
            marcheEmploi.get(type).add(personne);
        } else {
            System.out.println("Cette personne n'est pas un employé de type " + type);
        }
    }

    public List<Personne> getEmployesParType(String type) {
        return employes.getOrDefault(type, new ArrayList<>());
    }

    public Personne retrouverEmployeParId(int clePrimaire) {
        for (List<Personne> personnes : employes.values()) {
            for (Personne personne : personnes) {
                if (personne.getClePrimaire() == clePrimaire) {
                    return personne;
                }
            }
        }

        for (List<Personne> personnes : marcheEmploi.values()) {
            for (Personne personne : personnes) {
                if (personne.getClePrimaire() == clePrimaire) {
                    return personne;
                }
            }
        }

        System.out.println("Aucun employé trouvé avec l'id " + clePrimaire);

        return null;
    }

    public void afficherPersonnes() {
        for (Map.Entry<String, List<Personne>> entry : personnesParType.entrySet()) {
            for (Personne personne : entry.getValue()) {
                System.out.println(personne.toString());
            }
        }
    }

    public List<Personne> getEmployes() {
        List<Personne> tousLesEmployes = new ArrayList<>();
        for (List<Personne> liste : employes.values()) {
            tousLesEmployes.addAll(liste);
            System.out.println("Employes : " + liste);
        }
        return tousLesEmployes;
    }
    
    public List<Personne> getPersonnesParType(String type) {
        return personnesParType.getOrDefault(type, new ArrayList<>());
    }

    public Map<String, List<Personne>> getMarcheEmploie() {
        return marcheEmploi;
    }

    public Map<String, List<Personne>> getEmployeMap() {
        return employes;
    }

    public Map<String, List<Personne>> getPersonnesParTypeMap() {
        return personnesParType;
    }

    public void ajouterPersonneAnuel() {
        ajouterPersonne(new Astronaute(), marcheEmploi);
    }

    public void ajouterPersonneMensuel() {
        for (int i = 0; i < 5; i++) {
            ajouterPersonne(new PersonneSimple(), marcheEmploi);
        }

        for (int i = 0; i < 2; i++) {
            ajouterPersonne(new Ouvrier(), marcheEmploi);
        }

        ajouterPersonne(new Ingenieur(), marcheEmploi);
        ajouterPersonne(new Scientifique(), marcheEmploi);
    }

    private void initMarcheEmploie() { 
        for (int i = 0; i < 3; i++) {
            ajouterPersonne(new Astronaute(), marcheEmploi);
        }

        for (int i = 0; i < 3; i++) {
            ajouterPersonne(new Ouvrier(), marcheEmploi);
        }

        for (int i = 0; i < 3; i++) {
            ajouterPersonne(new Scientifique(), marcheEmploi);
        }

        for (int i = 0; i < 3; i++) {
            ajouterPersonne(new Ingenieur(), marcheEmploi);
        }

    }

}
