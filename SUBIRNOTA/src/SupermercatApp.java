import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

class Producte {
    protected String codiBarres;
    protected String nom;
    protected double preuUnitari;

    public Producte(String codiBarres, String nom, double preuUnitari) {
        this.codiBarres = codiBarres;
        this.nom = nom;
        this.preuUnitari = preuUnitari;
    }

    public double calcularPreu() {
        return preuUnitari;
    }

    public String toString() {
        return "Nom: " + nom + ", Preu Unitari: " + preuUnitari;
    }
}

class Alimentacio extends Producte {
    private Date dataCaducitat;

    public Alimentacio(String codiBarres, String nom, double preuUnitari, Date dataCaducitat) {
        super(codiBarres, nom, preuUnitari);
        this.dataCaducitat = dataCaducitat;
    }

    @Override
    public double calcularPreu() {
        Date dataActual = new Date();
        long diesFinsCaducitat = (dataCaducitat.getTime() - dataActual.getTime()) / (1000 * 60 * 60 * 24);
        return preuUnitari - preuUnitari / (diesFinsCaducitat + 1) + (preuUnitari * 0.1);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return super.toString() + ", Data Caducitat: " + dateFormat.format(dataCaducitat);
    }
}

class Textil extends Producte {
    private String composicioTextil;

    public Textil(String codiBarres, String nom, double preuUnitari, String composicioTextil) {
        super(codiBarres, nom, preuUnitari);
        this.composicioTextil = composicioTextil;
    }

    @Override
    public String toString() {
        return super.toString() + ", Composició Tèxtil: " + composicioTextil;
    }
}

class Electronica extends Producte {
    private int diesGarantia;

    public Electronica(String codiBarres, String nom, double preuUnitari, int diesGarantia) {
        super(codiBarres, nom, preuUnitari);
        this.diesGarantia = diesGarantia;
    }

    @Override
    public double calcularPreu() {
        return preuUnitari + (preuUnitari * (diesGarantia / 365.0) * 0.1);
    }

    @Override
    public String toString() {
        return super.toString() + ", Dies de Garantia: " + diesGarantia;
    }
}

class CarroCompra {
    private ArrayList<Producte> productes;

    public CarroCompra() {
        productes = new ArrayList<>();
    }

    public void afegirProducte(Producte producte) {
        productes.add(producte);
    }

    public void generarTiquet() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        double sumaTotal = 0.0;
        ArrayList<String> productesJaVistos = new ArrayList<>();

        System.out.println("Data de la compra: " + dateFormat.format(new Date()));
        System.out.println("Nom del supermercat: Supermercat XYZ");
        System.out.println("Detall de la compra:");

        for (Producte producte : productes) {
            if (!productesJaVistos.contains(producte.codiBarres)) {
                int quantitat = 1;
                for (Producte p : productes) {
                    if (p.codiBarres.equals(producte.codiBarres) && p.preuUnitari == producte.preuUnitari) {
                        quantitat++;
                    }
                }
                productesJaVistos.add(producte.codiBarres);

                double preuTotal = quantitat * producte.calcularPreu();
                sumaTotal += preuTotal;

                System.out.println("Nom: " + producte.nom + ", Unitats: " + quantitat + ", Preu Unitari: " + producte.preuUnitari + ", Preu Total: " + preuTotal);
            }
        }

        System.out.println("Suma Total a Pagar: " + sumaTotal);
        productes.clear();
    }

    public void mostrarCarroCompra() {
        System.out.println("Carro de la Compra:");
        for (Producte producte : productes) {
            System.out.println(producte);
        }
    }
}

public class SupermercatApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarroCompra carroCompra = new CarroCompra();

        while (true) {
            System.out.println("Menú d'opcions:");
            System.out.println("1. Introduir producte");
            System.out.println("2. Passar per caixa");
            System.out.println("3. Mostrar carro de la compra");
            System.out.println("0. Sortir");

            int opcio = scanner.nextInt();

            switch (opcio) {
                case 1:
                    System.out.println("Quin tipus de producte vols afegir?");
                    System.out.println("1. Alimentació");
                    System.out.println("2. Tèxtil");
                    System.out.println("3. Electrònica");
                    System.out.println("0. Tornar");
                    int tipusProducte = scanner.nextInt();

                    if (tipusProducte == 0) {
                        break;
                    }

                    System.out.println("Entra el codi de barres:");
                    String codiBarres = scanner.next();
                    System.out.println("Entra el nom del producte:");
                    String nom = scanner.next();
                    System.out.println("Entra el preu unitari:");
                    double preuUnitari = scanner.nextDouble();

                    if (tipusProducte == 1) {
                        System.out.println("Entra la data de caducitat (dd/MM/yyyy):");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataCaducitat = null;
                        try {
                            dataCaducitat = dateFormat.parse(scanner.next());
                        } catch (Exception e) {
                            System.out.println("Format de data incorrecte.");
                            continue;
                        }
                        carroCompra.afegirProducte(new Alimentacio(codiBarres, nom, preuUnitari, dataCaducitat));
                    } else if (tipusProducte == 2) {
                        System.out.println("Entra la composició tèxtil:");
                        String composicioTextil = scanner.next();
                        carroCompra.afegirProducte(new Textil(codiBarres, nom, preuUnitari, composicioTextil));
                    } else if (tipusProducte == 3) {
                        System.out.println("Entra els dies de garantia:");
                        int diesGarantia = scanner.nextInt();
                        carroCompra.afegirProducte(new Electronica(codiBarres, nom, preuUnitari, diesGarantia));
                    }

                    break;
                case 2:
                    carroCompra.generarTiquet();
                    break;
                case 3:
                    carroCompra.mostrarCarroCompra();
                    break;
                case 0:
                    scanner.close();
                    System.exit(0);
            }
        }
    }
}
