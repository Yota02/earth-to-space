package back.recherche;
import org.json.JSONObject;

public class Recherche {

    static int nextId;
    private int id;
    private int prix;
    private String nom;
    private double temps;
    private String description;
    private SousTypeRecherche sousType;  // Nouveau champ pour sous-type
    private CategorieRecherche categorie;
    private int niveau;
    private int etat;
    private double progression;

    public Recherche(int prix, String nom, double temps, String description, CategorieRecherche categorie, SousTypeRecherche sousType, int niveau) {
        this.id = nextId++;
        this.prix = prix;
        this.nom = nom;
        this.temps = temps;
        this.description = description;
        this.categorie = categorie;
        this.sousType = sousType;  // Initialisation du sous-type
        this.niveau = niveau;
        this.etat = 0;
        this.progression = 0;
    }

    public Recherche(int prix, String nom, double temps, String description, CategorieRecherche categorie, int niveau) {
        this.id = nextId++;
        this.prix = prix;
        this.nom = nom;
        this.temps = temps;
        this.description = description;
        this.categorie = categorie;
        this.sousType = null;  
        this.niveau = niveau;
        this.etat = 0;
        this.progression = 0;
    }

    public void ajouterProgression(int quantite){
        progression = Math.min(progression + quantite, 100);
    }

    public SousTypeRecherche getSousType() {
        return sousType;
    }

    public void setSousType(SousTypeRecherche sousType) {
        this.sousType = sousType;
    }

    public int getId() {
        return id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTemps() {
        return temps;
    }

    public void setTemps(double temps) {
        this.temps = temps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategorieRecherche getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieRecherche categorie) {
        this.categorie = categorie;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public double getProgression() {
        return progression;
    }

    public void setProgression(double progression) {
        this.progression = progression;
    }

    public int getNbprogressionParMoi(){
        return 5;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("prix", this.prix);
        json.put("nom", this.nom);
        json.put("temps", this.temps);
        json.put("description", this.description);
        json.put("sousCategorie", this.sousType);
        json.put("categorie", this.categorie);
        json.put("niveau", this.niveau);
        json.put("etat", this.etat);
        json.put("progression", this.progression);
        return json;
    }
}
