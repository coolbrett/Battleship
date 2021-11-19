package server;

/**
 * Enum for Symbol mappings in Battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 */
public enum Symbol {

    CARRIER('C', "Carrier",  2),
    BATTLESHIP('B', "Battleship", 3),
    CRUISER('R', "Cruiser", 4),
    SUBMARINE('S', "Submarine", 3),
    DESTROYER('D', "Destroyer", 5),
    HIT('X', "Hit", 1),
    MISS('O', "Miss", 1),
    EMPTY(' ', "Empty", 1);

    /** Symbol field for each Symbol */
    private final char symbol;
    /** Name field for each Symbol */
    private final String name;
    /** Size field for each Symbol */
    private final int size;

    /**
     * Constructor for ship symbol enums
     * @param symbol symbol associated with the ship
     * @param size size of ship on the grid
     */
    Symbol(char symbol, String name, int size) {
        this.symbol = symbol;
        this.name = name;
        this.size = size;
    }

    /**
     * Getter for symbol field
     * @return symbol field
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Getter method for name field
     * @return name field
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for size field
     * @return size field
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the ship type as a String output.
     * @return The ShipType as a String.
     */
    @Override
    public String toString(){
        return this.symbol + "";
    }
}
