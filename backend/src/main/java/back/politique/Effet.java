package back.politique;

public class Effet {
    private int economie;     // Impact économique (échelle 1-10)
    private int technologie;  // Impact technologique (échelle 1-10)
    private int science;      // Impact scientifique (échelle 1-10)
    private int environnement; // Impact environnemental (échelle 1-10)
    private int politique;    // Impact géopolitique (échelle 1-10)

    // Constructeur
    public Effet(int economie, int technologie, int science, int environnement, int politique) {
        this.economie = economie;
        this.technologie = technologie;
        this.science = science;
        this.environnement = environnement;
        this.politique = politique;
    }

    // Getters
    public int getEconomie() { return economie; }
    public int getTechnologie() { return technologie; }
    public int getScience() { return science; }
    public int getEnvironnement() { return environnement; }
    public int getPolitique() { return politique; }

    // Affichage de l'effet sous forme de texte
    @Override
    public String toString() {
        return "Effet [Économie=" + economie + ", Technologie=" + technologie + 
               ", Science=" + science + ", Environnement=" + environnement + 
               ", Politique=" + politique + "]";
    }
}

