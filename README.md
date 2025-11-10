# Restaurant Reservation Information Extractor

Java-Service zur Extraktion von Reservierungsinformationen (Name, Datum, Uhrzeit, Personenanzahl) aus deutschem Text.

## Voraussetzungen

- Java 11 oder höher
- Maven 3.6 oder höher

## Anwendung starten

```bash
# Erstellen und ausführen
mvn package
java -jar target/reservation-extractor-1.0.0.jar

# Tests ausführen
mvn test
```

**Hinweis:** Bei Encoding-Problemen mit Umlauten unter Windows:
```cmd
chcp 65001
java -jar target/reservation-extractor-1.0.0.jar
```

## Services

### Kern-Services

- **`ReservationExtractorService`** - Hauptservice, koordiniert alle Extraktions-Services
- **`CustomerNameExtractionService`** - Extrahiert Kundennamen aus Grußformeln
- **`DateExtractionService`** - Extrahiert Datum in Formaten wie `19.3.`, `9. April`
- **`TimeExtractionService`** - Extrahiert Uhrzeit in Formaten wie `20:00 Uhr`, `9 Uhr abends`
- **`PeopleCountExtractionService`** - Extrahiert Personenanzahl (gibt erste Übereinstimmung zurück)
- **`GermanNumberParserService`** - Parst deutsche Zahlwörter (`zwei`, `sechs`, etc.)

### Model

- **`Reservation`** - Datenmodell mit extrahierten Informationen (Name, Datum, Uhrzeit, Personenanzahl)

## Beispiel

Eingabe:
```
Hallo, bitte für zwei Personen einen Tisch am 19.3. um 20:00 Uhr, Vielen Dank Klaus Müller
```

Ausgabe:
```
(Klaus Müller, 19.03., 20:00, 2)
```