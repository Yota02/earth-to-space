package back.politique;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PolitiqueManager {

    private List<Subvention> subventions;
    private List<Objectif> objectifs;
    private List<Subvention> subventionsPosssede;

    public PolitiqueManager() {
        this.subventions = new ArrayList<>();
        this.objectifs = new ArrayList<>();
        this.subventionsPosssede = new ArrayList<>();
        initSubventions();
        initObjectifs();
    }

    public Subvention findSubventionParId(int id){
        for(Subvention s : subventions){
            if(s.getId() == id){
                return s;
            }
        }

        return null;
    }

    public int activateSubvention(Subvention subvention){
        ajouterSubventions(subvention);
        return subvention.getQuantite();
    }

    private void initSubventions() {
        subventions.add(new Subvention("Recherche Spatiale", 5000000, 5));
        subventions.add(new Subvention("Développement de Fusées", 10000000, 7));
        subventions.add(new Subvention("Exploration Lunaire", 20000000, 10));
        subventions.add(new Subvention("Colonisation Martienne", 50000000, 15));
        subventions.add(new Subvention("Développement de Satellites", 8000000, 6));
        subventions.add(new Subvention("Mission d'Astéroïde", 25000000, 12));
        subventions.add(new Subvention("Programme de Vol Habité", 30000000, 10));
        subventions.add(new Subvention("Exploration des Lunes de Jupiter", 60000000, 20));
        subventions.add(new Subvention("Station Spatiale Avancée", 75000000, 25));
    }

    private void initObjectifs() {
        objectifs.add(new Objectif("Lancement de la première fusée",
                "Test de lancement spatial pour valider les technologies.",
                LocalDateTime.of(2025, 12, 1, 0, 0),
                new Effet(3, 6, 7, 2, 4),
                Proprietaire.NASA,
                1));

        objectifs.add(new Objectif("Exploration lunaire",
                "Poser un rover sur la Lune pour récolter des données.",
                LocalDateTime.of(2027, 12, 1, 0, 0),
                new Effet(5, 8, 9, 3, 6),
                Proprietaire.ESA,
                2));

        objectifs.add(new Objectif("Base lunaire permanente",
                "Construire une base scientifique et industrielle sur la Lune.",
                LocalDateTime.of(2030, 12, 1, 0, 0),
                new Effet(8, 9, 10, 4, 7),
                Proprietaire.SPACEX,
                3));

        objectifs.add(new Objectif("Colonisation de Mars",
                "Établir une présence humaine permanente sur Mars.",
                LocalDateTime.of(2035, 12, 1, 0, 0),
                new Effet(10, 10, 10, 6, 9),
                Proprietaire.CNSA,
                4));
    }

    private void ajouterSubventions(Subvention subvention) {
        subventionsPosssede.add(subvention);
    }

    public List<Subvention> getSubventions() {
        return subventions;
    }

    public List<Subvention> getSubventionsPosseder() {
        return subventionsPosssede;
    }

}