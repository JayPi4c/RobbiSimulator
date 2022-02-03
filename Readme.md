# Jonas Pohl Robbi Simulator

In diesem Repository ist der Code für die Mini-Programmierwelt (MPW) "Robbi Simulator".
Da die verwendete Bibliothek JavaFX Plattform-Abhängige Resourcen verwendet, muss für jedes Betriebssystem eine eigene Version gebaut werden. Unter Releases können Versionen für Windows, MacOS und Linux heruntergeladen werden.


## Aufgabenspezifische Anmerkungen

Aufgabe 13 auf Aufgabenblatt 6 fordert die Implementierung eines Warnsounds, falls der Akteur einen Fehler macht. Im Robbi Simulator ist das Abspielen des Tons standartmäßig deaktiviert. In der MenuBar unter Fenster gibt es jedoch die Mögichkeit, den Ton zu aktivieren.

Die freiwillige Aufgabe 17.1, das Drucken des Editorinhalts, des Aufgabenblattes 8 wurde nicht realisiert.

### Zukünftige TODOs

- (In Stockpile.java) Es ist zu überprüfen, auf welche Weise die Items vom Lager entfernt und hinzugefügt werden sollen. Abhängig davon, wie die Daten gespeichert werden unterscheidet sich die Arbeit mit dem Lager. Im speziellen ist hier die Frage zu stellen, ob FIFO oder LIFO verwendet werden soll. Zusätzlich stellt sich die Frage, ob Robbi die Items in der Tasche unterscheiden können soll.
- (In MainStageController.java) Der Slider soll eine Beschriftung bekommen, sodass zu erkennen ist, welche Richtung schneller und welcher langsamer bedeutet und wofür der Slider überhaupt ist.

### Acknowledgement

Der Warnsound wurde von [hier](https://freesound.org/s/507906/) heruntergeladen.
Die meisten Kontroll-Grafiken stammen von Dibo.