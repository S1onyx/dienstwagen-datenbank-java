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

- **CommandHandlerFahrersucheTest**  
  Testet ob Fahrersuche über die Kommandozeile funktioniert.

- **CommandHandlerFahrzeugsucheTest**  
  Testet ob Fahrzeugsuche korrekt filtert.

- **LostAndFoundServiceTest**  
  Findet andere Fahrer desselben Fahrzeugs am selben Tag. Testet positive Fälle, kein Match und falsches Format.

- **RadarTrapServiceTest**  
  Testet Fahrerermittlung anhand von Kennzeichen und Uhrzeit. Behandelt auch Zeit außerhalb der Fahrt sowie fehlerhafte Eingaben.

---

## 🧱 Aufbau (Klassendiagramm)

```mermaid
classDiagram
%% === Main Entry Point ===
class Main {
    +main(String[]): void
}
Main --> ImportService
Main --> RadarTrapService
Main --> LostAndFoundService
Main --> CommandHandler

%% === Exceptions ===
class RuntimeException
class DuplicateEntityException {
    +DuplicateEntityException(String)
}
class EntityNotFoundException {
    +EntityNotFoundException(String)
}
class InvalidInputException {
    +InvalidInputException(String)
}
DuplicateEntityException --|> RuntimeException
EntityNotFoundException --|> RuntimeException
InvalidInputException --|> RuntimeException

%% === Models ===
class Driver {
    +String id()
    +String firstName()
    +String lastName()
    +LicenseClass licenseClass()
    +getFullName(): String
    +toString(): String
}
class Car {
    +String id()
    +String manufacturer()
    +String model()
    +String licensePlate()
    +toString(): String
}
class Trip {
    +String driverId()
    +String carId()
    +int startKm()
    +int endKm()
    +LocalDateTime startTime()
    +LocalDateTime endTime()
    +getDistance(): long
    +getDuration(): Duration
    +includesTime(LocalDateTime): boolean
    +overlapsWithDate(LocalDate): boolean
    +toString(): String
}
class LicenseClass {
    <<enum>>
    +fromString(String): LicenseClass
}
Driver --> LicenseClass
Trip --> Driver : driverId
Trip --> Car : carId

%% === Services ===
class ImportService {
    +loadData(String): void
    +getDrivers(): List<Driver>
    +getCars(): List<Car>
    +getTrips(): List<Trip>
    -detectEntityType(String): String
    -addDriver(String[], String): void
    -addCar(String[], String): void
    -addTrip(String[], String): void
}
ImportService --> Driver
ImportService --> Car
ImportService --> Trip

class RadarTrapService {
    +findDriverAtTime(String): void
}
RadarTrapService --> Trip
RadarTrapService --> Car
RadarTrapService --> Driver

class LostAndFoundService {
    +findOtherDrivers(String): void
}
LostAndFoundService --> Trip
LostAndFoundService --> Car
LostAndFoundService --> Driver

class CommandHandler {
    +handle(String): void
    -handleFahrersuche(String): void
    -handleFahrzeugsuche(String): void
    -printHelp(): void
}
CommandHandler --> RadarTrapService
CommandHandler --> LostAndFoundService
CommandHandler --> ArgParserUtils

%% === Utilities ===
class ArgParserUtils {
    +extractValue(String): String
}
class DateParser {
    +parseDateTime(String): LocalDateTime
    +parseDate(String): LocalDate
}
class EntityFinder {
    +findDriverById(List<Driver>, String): Driver
    +findCarById(List<Car>, String): Car
    +findCarByLicensePlate(List<Car>, String): Car
}
RadarTrapService --> DateParser
RadarTrapService --> EntityFinder
LostAndFoundService --> DateParser
LostAndFoundService --> EntityFinder

%% === Tests ===
class CommandHandlerFahrersucheTest {
    +testFahrersucheFindetTreffer(): void
}
class CommandHandlerFahrzeugsucheTest {
    +testFahrzeugsucheFindetTreffer(): void
}
class ImportServiceTest {
    +testSuccessfulImport(): void
    +testDuplicateDriverSkipped(): void
    +testInvalidDateSkipped(): void
    +testUnknownDriverInTripSkipped(): void
    +testEndBeforeStartSkipped(): void
}
class LostAndFoundServiceTest {
    +setUp(): void
    +testFindOtherDriversOnSameDay(): void
    +testInvalidFormatThrows(): void
    +testNoMatchFound(): void
}
class RadarTrapServiceTest {
    +setUp(): void
    +testFindDriverAtTime(): void
    +testFindDriverAtTimeNoMatch(): void
    +testInvalidFormatThrows(): void
}
class ProjektTester {
    +main(String[]): void
    -passedTestNetzwerk(String, String): boolean
}

CommandHandlerFahrersucheTest --> CommandHandler
CommandHandlerFahrzeugsucheTest --> CommandHandler
ImportServiceTest --> ImportService
LostAndFoundServiceTest --> LostAndFoundService
RadarTrapServiceTest --> RadarTrapService
ProjektTester --> Main
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
