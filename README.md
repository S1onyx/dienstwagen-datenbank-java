# Dienstwagendatenbank – Java Projekt DHBW 2025

**Projektname:** Dienstwagendatenbank Java  
**Autor:** Simon Riedinger  
**Kurs:** DHBW Stuttgart – Informatik  
**Modul:** Programmieren 2 (Java)  
**Abgabejahr:** 2025

---

## 🧭 Überblick

Dieses Konsolenprogramm verwaltet eine einfache Datenbank aus Fahrern, Fahrzeugen und deren Fahrten.  
Ziel ist es, Informationen zu verwalten und gezielt abzufragen – z. B. welcher Fahrer ein Auto zu einem bestimmten Zeitpunkt fuhr oder wer nach einem bestimmten Fahrer ein Fahrzeug genutzt hat.

Das Projekt wurde vollständig in Java (Version 24.0.1) ohne externe Libraries entwickelt und manuell kompiliert.

---

## ⚙️ Funktionsweise

Beim Start der Anwendung wird eine strukturierte Datei (`dienstwagenprojekt2025.db`) eingelesen, welche Fahrer, Fahrzeuge und Fahrten beschreibt.

Das Programm bietet verschiedene Kommandozeilenoptionen zur Analyse der Daten:

```bash
java Main --fahrersuche=<Suchbegriff>
java Main --fahrzeugsuche=<Suchbegriff>
java Main --fahrerZeitpunkt=S-XX-1234;2024-01-01T14:00:00
java Main --fahrerDatum=F001;2024-01-01
```

### Unterstützte Befehle

| Option                     | Beschreibung |
|---------------------------|--------------|
| `--fahrersuche=<Text>`    | Sucht Fahrer anhand von Namensteilen |
| `--fahrzeugsuche=<Text>`  | Sucht Fahrzeuge anhand von Marke/Modell/Kennzeichen |
| `--fahrerZeitpunkt=…`     | Gibt Fahrer aus, die ein Fahrzeug zum angegebenen Zeitpunkt gefahren haben |
| `--fahrerDatum=…`         | Listet alle anderen Fahrer, die am gleichen Tag dasselbe Fahrzeug wie ein bestimmter Fahrer genutzt haben |
| `--help`                  | Zeigt Hilfeübersicht an |

---

## 📂 Datei: dienstwagenprojekt2025.db

Die Import-Datei ist eine textbasierte `.db`-Datei mit folgendem Format:

```txt
New_Entity: fahrerId, vorname, nachname, klasse
F001, Anna, Muster, B

New_Entity: fahrzeugId, hersteller, modell, kennzeichen
C001, BMW, 320i, S-XX-1234

New_Entity: fahrerId, fahrzeugId, startKM, endKM, startzeit, endzeit
F001, C001, 10000, 10100, 2024-01-01T08:00:00, 2024-01-01T09:00:00
```

Ungültige Zeilen oder doppelte IDs werden ignoriert.

---

## 🧪 JUnit 5 Tests

Die wichtigsten Funktionen wurden mit Unit-Tests abgesichert:

- **ImportServiceTest**  
  Prüft Datenimport, Fehlerbehandlung bei Duplikaten, Datumsformaten und Referenzen.

- **DriverSearchServiceTest**  
  Testet ob Fahrersuche über die Kommandozeile funktioniert.

- **CarSearchServiceTest**  
  Testet ob Fahrzeugsuche korrekt filtert.

- **LostAndFoundServiceTest**  
  Findet andere Fahrer desselben Fahrzeugs am selben Tag. Testet positive Fälle, kein Match und falsches Format.

- **RadarTrapServiceTest**  
  Testet Fahrerermittlung anhand von Kennzeichen und Uhrzeit. Behandelt auch Zeit außerhalb der Fahrt sowie fehlerhafte Eingaben.

---

## 🧱 Aufbau (Klassendiagramm)

```mermaid
classDiagram
class Main {
  +main(args: String[]): void
}

class Driver {
  String id
  String firstName
  String lastName
  LicenseClass licenseClass
  +getFullName(): String
  +toString(): String
}

class Car {
  String id
  String manufacturer
  String model
  String licensePlate
  +toString(): String
}

class Trip {
  String driverId
  String carId
  int startKm
  int endKm
  LocalDateTime startTime
  LocalDateTime endTime
  +getDistance(): long
  +getDuration(): Duration
  +includesTime(timestamp: LocalDateTime): boolean
  +overlapsWithDate(date: LocalDate): boolean
  +toString(): String
}

class LicenseClass {
  <<enum>>
  +fromString(value: String): LicenseClass
}

class ImportService {
  -List~Driver~ drivers
  -List~Car~ cars
  -List~Trip~ trips
  +loadData(filePath: String): void
  +getDrivers(): List~Driver~
  +getCars(): List~Car~
  +getTrips(): List~Trip~
  -detectEntityType(header: String): String
  -addDriver(parts: String[], rawLine: String): void
  -addCar(parts: String[], rawLine: String): void
  -addTrip(parts: String[], rawLine: String): void
}

class CommandHandler {
  -RadarTrapService radarTrapService
  -LostAndFoundService lostAndFoundService
  -DriverSearchService driverSearchService
  -CarSearchService carSearchService
  +handle(arg: String): void
  -printHelp(): void
}

class RadarTrapService {
  -List~Driver~ drivers
  -List~Car~ cars
  -List~Trip~ trips
  +findDriverAtTime(input: String): void
}

class LostAndFoundService {
  -List~Driver~ drivers
  -List~Car~ cars
  -List~Trip~ trips
  +findOtherDrivers(input: String): void
}

class DriverSearchService {
  -List~Driver~ drivers
  +searchByName(keyword: String): void
}

class CarSearchService {
  -List~Car~ cars
  +searchByKeyword(keyword: String): void
}

class ArgParserUtils {
  +extractValue(arg: String): String
}

class DateParserUtils {
  +parseDateTime(input: String): LocalDateTime
  +parseDate(input: String): LocalDate
}

class EntityFinderUtils {
  +findDriverById(drivers: List~Driver~, id: String): Driver
  +findCarById(cars: List~Car~, id: String): Car
  +findCarByLicensePlate(cars: List~Car~, licensePlate: String): Car
}

Main --> ImportService
Main --> RadarTrapService
Main --> LostAndFoundService
Main --> DriverSearchService
Main --> CarSearchService
Main --> CommandHandler

CommandHandler --> RadarTrapService
CommandHandler --> LostAndFoundService
CommandHandler --> DriverSearchService
CommandHandler --> CarSearchService

RadarTrapService --> Driver
RadarTrapService --> Car
RadarTrapService --> Trip

LostAndFoundService --> Driver
LostAndFoundService --> Car
LostAndFoundService --> Trip

ImportService --> Driver
ImportService --> Car
ImportService --> Trip

CommandHandler --> ArgParserUtils
RadarTrapService --> DateParserUtils
RadarTrapService --> EntityFinderUtils
LostAndFoundService --> DateParserUtils
LostAndFoundService --> EntityFinderUtils
```

---

## ✅ Voraussetzungen

- Java 24.0.1
- Kommandozeile (z. B. `Terminal`, `CMD` oder `PowerShell`)
- Datei: `dienstwagenprojekt2025.db` im `src/` Verzeichnis

---

## 🧠 Hinweise

- Fehlerhafte Eingaben (z. B. unbekannte IDs, falsches Datum) werden robust behandelt.
- Die Anwendung ignoriert fehlerhafte Zeilen im Datenimport, gibt aber Hinweise in der Konsole aus.
- Es wird keine externe Bibliothek verwendet – nur Standard-Java (SE).

---

© 2025 Simon Riedinger – DHBW Stuttgart