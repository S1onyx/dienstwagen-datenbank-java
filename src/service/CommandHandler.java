package service;

import utils.ArgParserUtils;

public class CommandHandler {
    private final RadarTrapService radarTrapService;
    private final LostAndFoundService lostAndFoundService;
    private final DriverSearchService driverSearchService;
    private final CarSearchService carSearchService;

    public CommandHandler(
            RadarTrapService radarTrapService,
            LostAndFoundService lostAndFoundService,
            DriverSearchService driverSearchService,
            CarSearchService carSearchService
    ) {
        this.radarTrapService = radarTrapService;
        this.lostAndFoundService = lostAndFoundService;
        this.driverSearchService = driverSearchService;
        this.carSearchService = carSearchService;
    }

    public void handle(String arg) {
        if (arg.startsWith("--fahrersuche=")) {
            String keyword = ArgParserUtils.extractValue(arg);
            driverSearchService.searchByName(keyword);
        } else if (arg.startsWith("--fahrzeugsuche=")) {
            String keyword = ArgParserUtils.extractValue(arg);
            carSearchService.searchByKeyword(keyword);
        } else if (arg.startsWith("--fahrerZeitpunkt=")) {
            radarTrapService.findDriverAtTime(ArgParserUtils.extractValue(arg));
        } else if (arg.startsWith("--fahrerDatum=")) {
            lostAndFoundService.findOtherDrivers(ArgParserUtils.extractValue(arg));
        } else if (arg.equals("--help") || arg.isEmpty()) {
            printHelp();
        } else {
            System.out.println("Unbekannte Option: " + arg);
        }
    }

    private static void printHelp() {
        System.out.println("Verfügbare Optionen:");
        System.out.println("--fahrersuche=<Suchbegriff>        : Suche nach Fahrern mit dem angegebenen Suchbegriff.");
        System.out.println("--fahrzeugsuche=<Suchbegriff>      : Suche nach Fahrzeugen mit dem angegebenen Suchbegriff.");
        System.out.println("--fahrerZeitpunkt=<Kennz.;Zeit>    : Finde Fahrer zum Zeitpunkt (Format: S-XX-1234;2024-01-01T14:00:00).");
        System.out.println("--fahrerDatum=<FahrerID;Datum>     : Finde andere Fahrer desselben Fahrzeugs am Tag (Format: F001;2024-01-01).");
        System.out.println("--help                              : Zeige diese Hilfe an.");
    }
}