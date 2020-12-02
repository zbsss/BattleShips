# Dokumentacja
# Statki
Projekt jest realizowany w ramach laboratorium z technologii obiektowych na semestrze V kierunku Informatyka.



# *Autorzy projektu:*
- *Michał Kurleto*
- *Oliwia Masiarek*
- *Tomasz Rosiek*

# Odpowiedzialności w projekcie:
- *Michał Kurleto*:
  - tworzenie modelu
  - implementacja modelu

- *Oliwia Masiarek*:
  - tworzenie modelu
  - opracowanie mechaniki dodawania statków
  - dokumentacja

- *Tomasz Rosiek*:
  - tworzenie modelu
  - opracowanie mechaniki dodawania statków oraz implementacja


# Cel projektu
Celem projektu jest stworzenie gry *Statki*.
Ogólne zasady dostępne są na wikipedii: [Zasady](https://pl.wikipedia.org/wiki/Okr%C4%99ty)

# Uruchomienie aplikacji
W terminalu w katalogu głównym należy wykonać następujące komendy:

```
chmod u+x gradlew
```
```
./gradlew build
```

```
./gradlew run
```

![](gifs/uruchomienie.gif)


# Architektura aplikacji
Aplikacja budowana jest w oparciu o wzorzec Model, View, Controller.

# Model
## Diagram klas modelu

![](image/Package%20game.png)

### Podstawowe klasy

#### AbstractPlayer
Klasa abstrakcyjna, która reprezentuje gracza. Każdy gracz posiada dwie plansze - własną oraz przeciwnika. Na własnej planszy będzie ustawiał własne statki, a na przeciwnika rozgrywał. Każdy gracz ma również dostępne statki, które musi rozmieścić na planszy. Gracz może wykonywać ruchy.

### Board
Klasa planszy, która składa się z komórek (klasa Cell). Na planszy możemy atakować oraz ustawiać statki.

### Cell
Klasa komórki - reprezentuje pojedyncze pole planszy. Każda komórka ma obserwatorów oraz reprezentuje konkretny stan.

### Game
Klasa reprezentująca rozgrywkę. Skupia dwóch graczy, którzy rozgrywają partię gry.

### Position
Klasa, która reprezentuje połóżenie.

### Ship
Klasa reprezentująca statek. Każdy statek posiada informacje o swojej długości, położeniu na planszy oraz swoim stanie.

### PlayerInfo
Klasa, która posiada podstawowe informacje o graczu oraz umożliwia uzyskanie statystyk danego gracza.

### PlayerRanking
Klasa skupiająca wszystkich graczy.

# GUI
Po uruchomieniu aplikacji mamy dwie plansze oraz statki do rozstawienia.
Plansza lewa - Enemy Board służy do rozgrywki, natomiast plansza prawa - Player Board służy do rozstawienia statków oraz w trakcie rozgrywki widzimy stan naszej planszy po ruchach przeciwnika.

![](image/aplikacja.png)

## Rozstawianie statków
Rozstawienie statków odbywa się poprzez przeciągnięcie myszką po odpowiedniej ilości pól na planszy. Dostępne statki widzimy po prawej stronie od planszy playera. Statki muszą być układane w linii prostej. Jeżeli chcemy zakończyć ustawienie statku to kursor myszki musi być na danym polu.

![](gifs/rozstawienie_statkow.gif)




## Użyte wzorce

### Observer
Wzorzec użyty w klasie *Ship*. Każda komórka *Cell* posiada obserwatorów, którzy są informowani po zmianie statusu komórki.

```java
public interface CellObserver {
    /**
     * calls the observer after status of the cell was changed
     * @param newStatus the updated cell status
     */
    void update(CellStatus newStatus);
}
```

```java
public class Cell {
    /***/

    private final Collection<CellObserver> observers = new ArrayList<>();

    /***/

    /**
     * adds a new observer to the cell
     * @param observer new observer to be added
     */
    public void addObserver(CellObserver observer){
        observers.add(observer);
    }

    /**
     * notifies all observers of the cell
     */
    private void notifyObservers(){
        for (CellObserver observer: observers) {
            observer.update(status);
        }
    }
}
```

```java
public class Ship implements CellObserver{
    private Collection<Cell> cells;

    /***/

    /**
     * for each cell sets it's status as SHIP and adds the ship as an observer
     * @param cells collection of cells that represent the ship
     */
    public void setCells(Collection<Cell> cells) {

         /***/

        this.cells = cells;
        for (Cell cell : cells) {
            cell.setStatus(CellStatus.SHIP);
            cell.addObserver(this);
        }
    }

    /***/


    /**
     * if the cell was hit, check if the ship has sunk
     * @param newStatus the updated cell status
     */
    @Override
    public void update(CellStatus newStatus) {
        if(newStatus == CellStatus.HIT)
            checkIfSunk();
    }
}
```
