package back.recherche;

import back.objectAchetable.ObjectAchetable;
import back.recherche.Recherche;

import java.util.*;

public class GestionnaireRecherche {
    private final List<Recherche> rechercheTotal = new ArrayList<>();
    private final Map<String, Recherche> recherchesMap = new HashMap<>();

    public void initialiserRecherches() {
        Recherche recherche1 = new Recherche.Builder()
                .setPrix(1000)
                .setNom("Recherche de Propulsion Avancée")
                .setTemps(50.0)
                .setDescription("Améliore la technologie de propulsion des vaisseaux spatiaux.")
                .setType("Propulsion")
                .setCategorie("Propulsion")
                .setNiveau(1)
                .setEtat("Non commencé")
                .setProgression(0.0)
                .build();

        Recherche recherche2 = new Recherche.Builder()
                .setPrix(2500)
                .setNom("Optimisation des Moteurs Ionique")
                .setTemps(500.0)
                .setDescription("Améliore l'efficacité des moteurs ioniques pour les voyages spatiaux à longue distance.")
                .setType("Propulsion")
                .setCategorie("Propulsion")
                .setNiveau(2)
                .setEtat("Non commencé")
                .setProgression(0.0)
                .build();


        ajouterObjectAchetable(recherche1);
        ajouterObjectAchetable(recherche2);
    }

    public Recherche getRecherche(String nom) {
        return recherchesMap.get(nom);
    }

    private void ajouterObjectAchetable(Recherche object) {
        rechercheTotal.add(object);
        recherchesMap.put(object.getNom(), object);
    }

    public List<Recherche> getRecherches() {
        return Collections.unmodifiableList(rechercheTotal);
    }
}