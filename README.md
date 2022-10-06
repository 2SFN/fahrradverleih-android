# Fahrradverleih

Implementierung des Fahrradverleihs mit Native Android.

# Umgebungsvariablen

Parameter, welche von der Entwicklungsumgebung abhängig sind, z.B. die URL der Rad-API oder der
API-Key für Google Maps, werden in der Datei `local.properties` im Root-Verzeichnis des Projekts
abgelegt.

Folgende Keys sollten nach dem Klonen angepasst werden:
- `RAD_API_URL` mit der URL des Fahrradverleih-Backends
- `MAPS_API_KEY` mit dem API-Key der GCP für Maps

# JSON Deserialisierung

Für die Deserialisierung der API-Antworten wird in diesem Projekt
[Moshi](https://github.com/square/moshi) eingesetzt, welches hier keine direkten
Änderungen an den Model-Klassen erfordert.
