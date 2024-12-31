package back.fusee;

public class fuseeTier1 extends Fusee {

    public fuseeTier1(Builder builder) {
        super(builder);
    }

    @Override
    public void decoler() {
            System.out.println("Décollage de la fusée " + nom + "...");
        }

    @Override
    public void exploser() {
        System.out.println("Boom... Fusée " + nom + " a explosé...");
    }

    @Override
    public void atterir() {
        System.out.println("Fusée " + nom + " a atterri...");
    }

    @Override
    public void orbite() {
        System.out.println("La fusée " + nom + " est en orbite.");
    }
}
