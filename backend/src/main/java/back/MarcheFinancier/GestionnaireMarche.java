package back.MarcheFinancier;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireMarche {
    
    private Entreprise monEntreprise;
    private List<Entreprise> marche;
    private List<MarcheFinancier> marcheFinanciers;

    public GestionnaireMarche() {
        this.marche = new ArrayList<>();
        this.marcheFinanciers = new ArrayList<>();
        initMarche();
    }

    private void initMarche() {
        initEntreprises();
        ajouterMarchesFinanciers();
    }

    private void ajouterMarchesFinanciers() {
        ajouterMarche("Europe", new int[]{0, 1}, new double[]{0.6, 0.4});
        ajouterMarche("Amérique du Nord", new int[]{2, 3, 4, 5}, new double[]{0.3, 0.4, 0.2, 0.1});
        ajouterMarche("Amérique du Sud", new int[]{6}, new double[]{1.0});
        ajouterMarche("Afrique", new int[]{7}, new double[]{1.0});
        ajouterMarche("Asie", new int[]{8, 9, 10}, new double[]{0.3, 0.4, 0.3});
        ajouterMarcheMondial();
    }

    private void ajouterMarche(String nom, int[] indices, double[] parts) {
        MarcheFinancier marcheFinancier = new MarcheFinancier(nom);
        for (int i = 0; i < indices.length; i++) {
            marcheFinancier.ajouterEntreprise(marche.get(indices[i]), parts[i]);
        }
        marcheFinanciers.add(marcheFinancier);
    }

    private void ajouterMarcheMondial() {
        MarcheFinancier marcheMondial = new MarcheFinancier("Mondial");
        double totalCapitalisation = marche.stream().mapToDouble(Entreprise::getCapitalisationBoursiere).sum();
        for (Entreprise entreprise : marche) {
            double part = entreprise.getCapitalisationBoursiere() / totalCapitalisation;
            marcheMondial.ajouterEntreprise(entreprise, part);
        }
        marcheFinanciers.add(marcheMondial);
    }

    public void recalculerPartsMarche() {
        for (MarcheFinancier marche : marcheFinanciers) {
            double totalCapitalisation = marche.getPartMarche().keySet().stream()
                    .mapToDouble(Entreprise::getCapitalisationBoursiere)
                    .sum();

            if (monEntreprise != null && marche.getNom().equals(monEntreprise.getPays())) {
                totalCapitalisation += monEntreprise.getCapitalisationBoursiere();
                marche.ajouterEntreprise(monEntreprise, monEntreprise.getCapitalisationBoursiere() / totalCapitalisation);
            }

            for (Entreprise entreprise : marche.getPartMarche().keySet()) {
                double nouvellePart = entreprise.getCapitalisationBoursiere() / totalCapitalisation;
                marche.modifierPart(entreprise, nouvellePart);
            }
        }
    }

    private void initEntreprises() {
        marche.add(new Entreprise("ArianeGroup", "France", 2500000000L, 120.5, 2.8, 1.2));
        marche.add(new Entreprise("Reaction Engines", "Royaume-Uni", 500000000, 45.3, 1.5, -0.8));
        marche.add(new Entreprise("SpaceX", "États-Unis", 150000000000L, 950.7, 0.0, 5.4));
        marche.add(new Entreprise("Blue Origin", "États-Unis", 10000000000L, 150.2, 0.0, 2.1));
        marche.add(new Entreprise("United Launch Alliance", "États-Unis", 3000000000L, 85.6, 2.0, -1.5));
        marche.add(new Entreprise("MDA Space", "Canada", 1200000000, 30.4, 1.2, 0.3));
        marche.add(new Entreprise("Visiona Tecnologia Espacial", "Brésil", 200000000, 10.8, 0.5, 0.2));
        marche.add(new Entreprise("SCS Aerospace Group", "Afrique du Sud", 150000000, 5.4, 0.3, -0.1));
        marche.add(new Entreprise("ISRO Commercial Arm", "Inde", 5000000000L, 60.3, 1.8, 2.6));
        marche.add(new Entreprise("CASC", "Chine", 100000000000L, 450.9, 3.2, 4.1));
        marche.add(new Entreprise("Mitsubishi Heavy Industries", "Japon", 40000000000L, 320.1, 2.5, 1.9));
    }

    public List<MarcheFinancier> getMarcheFinanciers() {
        return marcheFinanciers;
    }

    public List<Entreprise> getEntreprises() {
        return marche;
    }
}
