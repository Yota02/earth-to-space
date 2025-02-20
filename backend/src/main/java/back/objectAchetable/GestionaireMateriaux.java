package back.objectAchetable;

import java.util.ArrayList;
import java.util.List;

import back.Metaux.Materiaux;

public class GestionaireMateriaux {
    
    private List<MateriauxAchetable> materiauxAchetables;

    GestionaireMateriaux() {
        materiauxAchetables = new ArrayList<>();
        initialiserMateriaux();
    }

    public MateriauxAchetable getMateriaux(String nom){
        for (MateriauxAchetable materiauxAchetable : materiauxAchetables) {
            if (materiauxAchetable.getNom().equals(nom)) {
                return materiauxAchetable;
            }
        }
        return null;
    }

    private void initialiserMateriaux() {
        // Prix de base pour les différents matériaux
        final int PRIX_BASE = 1000;
        
        // Création des matériaux achetables
        MateriauxAchetable aluminium = new MateriauxAchetable.Builder()
                .setNom(Materiaux.ALUMINIUM.getNom())
                .setPrix((int) (PRIX_BASE * 1.5))
                .setMateriau(Materiaux.ALUMINIUM)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable titane = new MateriauxAchetable.Builder()
                .setNom(Materiaux.TITANE.getNom())
                .setPrix((int) (PRIX_BASE * 3))
                .setMateriau(Materiaux.TITANE)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable acier = new MateriauxAchetable.Builder()
                .setNom(Materiaux.ACIER.getNom())
                .setPrix(PRIX_BASE)
                .setMateriau(Materiaux.ACIER)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable carbone = new MateriauxAchetable.Builder()
                .setNom(Materiaux.CARBONE.getNom())
                .setPrix((int) (PRIX_BASE * 2.5))
                .setMateriau(Materiaux.CARBONE)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable fer = new MateriauxAchetable.Builder()
                .setNom(Materiaux.FER.getNom())
                .setPrix((int) (PRIX_BASE * 0.8))
                .setMateriau(Materiaux.FER)
                .setEstAchetable(true)
                .build();

        fer.debloquer();

        MateriauxAchetable inox = new MateriauxAchetable.Builder()
                .setNom(Materiaux.INOX.getNom())
                .setPrix((int) (PRIX_BASE * 1.2))
                .setMateriau(Materiaux.INOX)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable inconel = new MateriauxAchetable.Builder()
                .setNom(Materiaux.INCONEL.getNom())
                .setPrix(PRIX_BASE * 4)
                .setMateriau(Materiaux.INCONEL)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable cuivre = new MateriauxAchetable.Builder()
                .setNom(Materiaux.CUIVRE.getNom())
                .setPrix((int) (PRIX_BASE * 1.3))
                .setMateriau(Materiaux.CUIVRE)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable nickel = new MateriauxAchetable.Builder()
                .setNom(Materiaux.NICKEL.getNom())
                .setPrix(PRIX_BASE * 2)
                .setMateriau(Materiaux.NICKEL)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable berillium = new MateriauxAchetable.Builder()
                .setNom(Materiaux.BERILLIUM.getNom())
                .setPrix(PRIX_BASE * 5)
                .setMateriau(Materiaux.BERILLIUM)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable tantale = new MateriauxAchetable.Builder()
                .setNom(Materiaux.TANTALE.getNom())
                .setPrix(PRIX_BASE * 6)
                .setMateriau(Materiaux.TANTALE)
                .setEstAchetable(true)
                .build();

        // Ajout de tous les matériaux à la liste
        ajouterObjectAchetable(aluminium);
        ajouterObjectAchetable(titane);
        ajouterObjectAchetable(acier);
        ajouterObjectAchetable(carbone);
        ajouterObjectAchetable(fer);
        ajouterObjectAchetable(inox);
        ajouterObjectAchetable(inconel);
        ajouterObjectAchetable(cuivre);
        ajouterObjectAchetable(nickel);
        ajouterObjectAchetable(berillium);
        ajouterObjectAchetable(tantale);
    }

    private void ajouterObjectAchetable(MateriauxAchetable object) {
        materiauxAchetables.add(object);
    }

    public List<MateriauxAchetable> getMateriaux() {
        return materiauxAchetables;
    }

}
