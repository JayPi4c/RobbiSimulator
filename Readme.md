# Jonas Pohl Robbi Simulator

In diesem Repository ist der Code für die Mini-Programmierwelt (MPW) "Robbi Simulator".
Da die verwendete Bibliothek JavaFX Plattform-Abhängige Resourcen verwendet, muss für jedes Betriebssystem eine eigene Version gebaut werden. Unter Releases können Versionen für Windows, MacOS und Linux heruntergeladen werden.


## Aufgabenspezifische Anmerkungen

Aufgabe 13 auf Aufgabenblatt 6 fordert die Implementierung eines Warnsounds, falls der Akteur einen Fehler macht. Im Robbi Simulator ist das Abspielen des Tons standartmäßig deaktiviert. In der MenuBar unter Fenster gibt es jedoch die Mögichkeit, den Ton zu aktivieren.

Die freiwillige Aufgabe 17.1, das Drucken des Editorinhalts, des Aufgabenblattes 8 wurde nicht realisiert.

Zum Aufgabenblatt 10 (speichern eines Beispiels in einer Datenbank) wurde angemerkt, dass beim Laden der in der Datenbank gespeicherte Editorcode nicht in die TextArea geschrieben wird. Dieses Verhalten kann ich leider nicht reproduzieren. Meine Tests unter Ubuntu 20.04.3 LTS erzeugen nicht diesen Fehler.

### Logging Level

Um das Programm besser debuggen zu können, werden viele Informationen mit dem Level debug geloggt. Diese Informationen sind für den Nutzer am Ende jedoch nicht von Interesse. Um während der Entwicklung jedoch die Informationen mit debug Level zu bekommen, kann auf dem Enwticklungssystem die Umgebungsvariable logging.level auf einen gültigen log-level gesetzt werden. Alternativ kann auch der JVM die folgende Option mitegegeben werden: ``-Dlogging.level=DEBUG``. Ist diese Option nicht gesetzt wird das Log-Level auf Info gesetzt, andernfalls wird die entsprechende Konfiguration übernommen. In Eclipse kann in den Run Configurations unter Arguments die Option für dieses Projekt gesetzt werden, sodass die Option immer gesetzt ist.

### Zukünftige TODOs

- (In Stockpile.java) Es ist zu überprüfen, auf welche Weise die Items vom Lager entfernt und hinzugefügt werden sollen. Abhängig davon, wie die Daten gespeichert werden unterscheidet sich die Arbeit mit dem Lager. Im speziellen ist hier die Frage zu stellen, ob FIFO oder LIFO verwendet werden soll. Zusätzlich stellt sich die Frage, ob Robbi die Items in der Tasche unterscheiden können soll.
- (In MainStageController.java) Der Slider soll eine Beschriftung bekommen, sodass zu erkennen ist, welche Richtung schneller und welcher langsamer bedeutet und wofür der Slider überhaupt ist.

## Coverage
![coverage](../badges/jacoco.svg)
![branches coverage](../badges/branches.svg)

Die Coverage wurde nach diesem [Template](https://github.com/cicirello/examples-jacoco-badge-generator) umgesetzt.

### Acknowledgement

Der Warnsound wurde von [hier](https://freesound.org/s/507906/) heruntergeladen.
Die meisten Kontroll-Grafiken stammen von Dibo.