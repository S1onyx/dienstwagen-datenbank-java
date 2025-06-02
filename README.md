# dienstwagen-datenbank-java
Java-Anwendung zur Verwaltung von Dienstwagen, Fahrern und Fahrten – inklusive Kommandozeilen-Interface, Dateibasierendem Datenimport, und Unit-Tests. Projektarbeit im Rahmen des Sommersemesters 2025 an der DHBW Stuttgart (TINF24).

## 🧩 UML-Diagramm

```mermaid
classDiagram
    %% =======================
    %% == Datenmodell-Teil ==
    %% =======================

    class Driver {
        - String id
        - String firstName
        - String lastName
        - String licenseClass
        + String getFullName()
    }

    class Car {
        - String id
        - String manufacturer
        - String model
        - String licensePlate
        + String getDisplayName()
    }

    class Trip {
        - int startMileage
        - int endMileage
        - LocalDateTime startTime
        - LocalDateTime endTime
        - Driver driver
        - Car car
        + long getDistance()
        + Duration getDuration()
        + boolean includesTime(LocalDateTime timestamp)
        + boolean isOnDate(LocalDate date)
    }

    %% ========================
    %% == Service-Klassen ====
    %% ========================

    class ImportService {
        + void importFromFile(String path)
        - void parseDriver(String[] fields)
        - void parseCar(String[] fields)
        - void parseTrip(String[] fields)
    }

    class SearchService {
        + List~Driver~ searchDrivers(String namePart)
        + List~Car~ searchCars(String keyword)
    }

    class BlitzService {
        + Optional~Driver~ getDriverAtTime(String licensePlate, LocalDateTime timestamp)
    }

    class LostAndFoundService {
        + List~String~ findOtherDrivers(String driverId, LocalDate date)
    }

    %% ========================
    %% == Kommando-Handling ==
    %% ========================

    class CommandLineHandler {
        + void parseArgs(String[] args)
        - void handleFahrersuche(String term)
        - void handleFahrzeugsuche(String term)
        - void handleBlitz(String input)
        - void handleLiegenlassen(String input)
    }

    class Main {
        + void main(String[] args)
    }

    %% ========================
    %% == Beziehungen =========
    %% ========================

    Trip --> Driver : assigned to
    Trip --> Car : uses
    ImportService --> Driver : creates
    ImportService --> Car : creates
    ImportService --> Trip : creates

    Main --> CommandLineHandler : uses
    CommandLineHandler --> SearchService : uses
    CommandLineHandler --> BlitzService : uses
    CommandLineHandler --> LostAndFoundService : uses
```
