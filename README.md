# dienstwagen-datenbank-java
Java-Anwendung zur Verwaltung von Dienstwagen, Fahrern und Fahrten – inklusive Kommandozeilen-Interface, Dateibasierendem Datenimport, und Unit-Tests. Projektarbeit im Rahmen des Sommersemesters 2025 an der DHBW Stuttgart (TINF24).

## 🧩 UML-Diagramm

```mermaid
classDiagram
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
        + boolean isOnDate(LocalDate date)
        + boolean includesTime(LocalDateTime timestamp)
    }

    class ImportService {
        + void loadFromFile(String path)
        - List~String~ readLines(Path path)
        - void parseEntities(List~String~ lines)
    }

    class SearchService {
        + List~Driver~ findDrivers(String nameFragment)
        + List~Car~ findCars(String keyword)
    }

    class BlitzService {
        + Optional~Driver~ findDriverByLicensePlateAndTime(String licensePlate, LocalDateTime timestamp)
    }

    class LostAndFoundService {
        + List~String~ findOtherDrivers(String driverId, LocalDate date)
    }

    class Main {
        + void main(String[] args)
        - void handleArguments(String[] args)
    }

    Trip --> Driver : uses
    Trip --> Car : uses
    ImportService --> Driver : creates
    ImportService --> Car : creates
    ImportService --> Trip : creates
```
