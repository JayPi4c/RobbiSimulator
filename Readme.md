# Jonas Pohl Robbi Simulator

In diesem Repository ist der Code für die Mini-Programmierwelt (MPW) "Robbi Simulator".
Da die Builds im Workflow auf einem Linux-Server erstellt werden, sind die Jars aufgrund von JavaFX nur auf Linux ausführbar. Für Windows und Mac muss der Simulator auf dem entsprechenden System gebaut werden.

### Aktuelle TODOs

- (In Stockpile.java) Es ist zu überprüfen, auf welche Weise die Items vom Lager entfernt und hinzugefügt werden sollen. Abhängig davon, wie die Daten gespeichert werden unterscheidet sich die Arbeit mit dem Lager. Im speziellen ist hier die Frage zu stellen, ob FIFO oder LIFO verwendet werden soll. Zusätzlich stellt sich die Frage, ob Robbi die Items in der Tasche unterscheiden können soll.
- (In MainStageController.java) Der Slider soll eine Beschriftung bekommen, sodass zu erkennen ist, welche Richtung schneller und welcher langsamer bedeutet und wofür der Slider überhaupt ist.
- (ProgramController.java) Das Programm soll auch kompilieren, wenn der Code im Editor nicht mit void main(){...} beginnt. Attribute sollten ganz oben stehen dürfen und Methoden sollten auch in jeder Reihenfolge stehen dürfen und jeden Modifier haben. ([Stackoverflow](https://stackoverflow.com/a/23663748/13670629))



## Informationen zum siebten Übungszettel

Die Aufgaben des siebten Übungszettel sind abschließend bearbeitet!
Sollten beim Ausführen der Main-Methode Exceptions auftreten, werden diese in einem Alert angezeigt. Ein Warnsound wurde absichtlich nicht implementiert, da dies meiner Meinung nach nicht Angebracht ist für den Simulator.