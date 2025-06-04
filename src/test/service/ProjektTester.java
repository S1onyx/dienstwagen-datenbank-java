package test.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class ProjektTester {
    // Konfiguration eurer Hauptklasse. Bitte vollqualifizierten Klassennamen plus Paket angeben.
    // Z.b. die Klasse MeinProjekt im Paket dhbw.java, muss lauten: 'dhbw.java.MeinProjekt'
    private static final String MAIN_CLASS = "Main";
    private static String line;

    public static void main(String[] args) {
        // Tests are passing
        boolean passed = true;

        // geblitzt
        passed = passedTestNetzwerk("--fahrerZeitpunkt=\"S-MN-9932;2024-02-14T13:57:43\"", "Fahrer: Paul Becker");
        passed = passedTestNetzwerk("--fahrerZeitpunkt=\"S-BC-4566;2024-01-25T18:25:09\"", "Fahrer: Tom Mueller");
        passed = passedTestNetzwerk("--fahrerZeitpunkt=\"S-BC-4566;2024-03-13T00:08:13\"", "Fahrer: Tom Schmidt");
        // liegen gelassen
        passed = passedTestNetzwerk("--fahrerDatum=\"F003;2024-08-13\"", "Ben Wagner (S-GH-3277), Mia Hoffmann (S-GH-3277)");
        passed = passedTestNetzwerk("--fahrerDatum=\"F004;2024-08-13\"", "Ben Mueller (S-UV-4535), Tom Mueller (S-UV-4535)");
        passed = passedTestNetzwerk("--fahrerDatum=\"F008;2024-01-13\"", "Ben Wagner (S-LM-7677), Max Hoffmann (S-LM-7677), Mia Wagner (S-LM-7677)");


        if (passed) {
            System.out.println("Alle Tests bestanden á••( á› )á•—");
        } else {
            System.out.println("Leider nicht alle Tests bestanden.");
        }
    }

    /**
     * @param arg          Programmargument
     * @param outputEquals Die erwartete Ausgabe
     * @return
     */
    private static boolean passedTestNetzwerk(String arg, String outputEquals) {
        // Der System.out Stream muss umgebogen werden, damit dieser spÃ¤ter Ã¼berprÃ¼ft werden kann.
        PrintStream normalerOutput = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        String[] args = {arg};
        try {
            // MainClass mittels Reflection bekommen und main Methode aufrufen
            Class<?> mainClass = Class.forName(MAIN_CLASS);
            Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Main Klasse konnte nicht geladen werden, bitte Konfiguration prÃ¼fen.");
            System.exit(1);
        } finally {
            // System.out wieder zurÃ¼cksetzen
            System.setOut(normalerOutput);
        }

        // Ergebnisse Ã¼berprÃ¼fen.
        String matrixOutput = baos.toString();
        String[] lines = matrixOutput.split(System.lineSeparator());
        // wir testen nur die erste ausgabe zeile auf das erwartete Ergebnis
        boolean passed = outputEquals.equals(lines[0]);
        if (!passed) {
            System.out.println("Erwartet: " + outputEquals);
            System.out.println("Berechnet: " + lines[0]);
            for (String line : lines) {
                System.out.println(line);
            }
        }
        return passed;
    }
}