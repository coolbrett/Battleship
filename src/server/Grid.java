package server;

import java.util.*;

/**
 * This class represents the logic for a single board of Battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
 * @version 11/22/2021
 */
public class Grid {

    /** Grid field for Grid class */
    private Symbol[][] grid;

    /** Grid based on grid field, just hits and misses */
    private Symbol[][] altGrid;

    /** Username field */
    private String username;

    /** Size of grid (size being 8 would mean grid is 8x8 */
    private final int size;

    /** Random generator */
    private final Random random = new Random();

    /** The total number of the size of ships on the grid aka hitpoints */
    private int hitPoints = 0;

    /**
     * Constructor for creating a Grid
     * @param size size for grid field to be
     */
    public Grid(int size, String username){
        this.size = size;
        this.username = username;
        this.grid = new Symbol[size][size];
        this.altGrid = new Symbol[size][size];
        fillGrid();
        placeShips();
    }

    /**
     * Method to place ships on grid
     */
    private void placeShips(){
        int shipCount = determineShipCount();
        Symbol[] ships = determineShipsToPlace(shipCount);
        for (int i = 0; i < ships.length; i++) {
            Symbol ship = ships[i];
            //starting position x and y
            int x = random.nextInt(size - 1);
            int y = random.nextInt(size - 1);
            ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

            boolean shipPlaced = false;
            while (!shipPlaced && !directions.isEmpty()) {
                //Up = 1, Right = 2, Down = 3, Left = 4
                int direction = directions.get(random.nextInt(directions.size()));

                    //Up
                if (direction == 1) {
                    shipPlaced = tryUp(x, y, ship, direction, directions);
                    //Right
                } else if (direction == 2) {
                    shipPlaced = tryRight(x, y, ship, direction, directions);
                    //Down
                } else if (direction == 3) {
                    shipPlaced = tryDown(x, y, ship, direction, directions);
                    //Left
                } else if (direction == 4) {
                    shipPlaced = tryLeft(x, y, ship, direction, directions);
                }
            }
            if (!shipPlaced) {
                i--;
            }
        }
    }

    /**
     * Method to try and place ship going up from point
     *
     * @param x Integer for x coordinate
     * @param y Integer for y coordinate
     * @param ship Symbol representation of the ship 
     * @param direction Integer of direction to go
     * @param directions Integer arraylist of all directions to go
     */
    private boolean tryUp(int x, int y, Symbol ship, int direction, 
                          ArrayList<Integer> directions){
        if ((y - ship.getSize()) < size && y - ship.getSize() > -1) {
            if (putShip(x, y, direction, ship)) {
                return true;
            }else{
                directions.remove(Integer.valueOf(direction));
            }
        }else {
            directions.remove(Integer.valueOf(direction));
        }
        return false;
    }
    
  /**
     * Method to try and place ship going right from point
     *
     * @param x Integer for x coordinate
     * @param y Integer for y coordinate
     * @param ship Symbol representation of the ship 
     * @param direction Integer of direction to go
     * @param directions Integer arraylist of all directions to go
     */
    private boolean tryRight(int x, int y, Symbol ship, int direction, 
                             ArrayList<Integer> directions) {
        if (x + ship.getSize() < size && x + ship.getSize() > -1) {
            if (putShip(x, y, direction, ship)) {
                return true;
            }else{
                directions.remove(Integer.valueOf(direction));
            }
        }else {
            directions.remove(Integer.valueOf(direction));
        }
        return false;
    }

      /**
     * Method to try and place ship going down from point
     *
     * @param x Integer for x coordinate
     * @param y Integer for y coordinate
     * @param ship Symbol representation of the ship 
     * @param direction Integer of direction to go
     * @param directions Integer arraylist of all directions to go
     */
    private boolean tryDown(int x, int y, Symbol ship, int direction, 
                            ArrayList<Integer> directions) {
        if ((y + ship.getSize()) < size && y + ship.getSize() > -1) {
            if (putShip(x, y, direction, ship)) {
                return true;
            }else{
                directions.remove(Integer.valueOf(direction));
            }
        }else {
            directions.remove(Integer.valueOf(direction));
        }
        return false;
    }

      /**
     * Method to try and place ship going left from point
     *
     * @param x Integer for x coordinate
     * @param y Integer for y coordinate
     * @param ship Symbol representation of the ship 
     * @param direction Integer of direction to go
     * @param directions Integer arraylist of all directions to go
     */
    private boolean tryLeft(int x, int y, Symbol ship, int direction, ArrayList<Integer> directions) {
        if (x - ship.getSize() < size && x - ship.getSize() > -1) {
            if (putShip(x, y, direction, ship)) {
                return true;
            }else{
                directions.remove(Integer.valueOf(direction));
            }
        }else {
            directions.remove(Integer.valueOf(direction));
        }
        return false;
    }

    /**
     * Helper method to place a single ship
     *
     * @param x x coordinate to start placement
     * @param y y coordinate to start placement
     * @param direction direction to place ship
     * @param ship ship to be placed
     * @return true if ship is placed, false if not
     */
    private boolean putShip(int x, int y, int direction, Symbol ship){
        Symbol[][] temp = makeDeepClone(grid);
        //Up
        if (direction == 1){
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y-i][x] == Symbol.EMPTY){
                    temp[y-i][x] = ship;
                }else {
                    return false;
                }
            }
            grid = makeDeepClone(temp);
        }//Right
        else if (direction == 2) {
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y][x+i] == Symbol.EMPTY){
                    temp[y][x+i] = ship;
                }else {
                    return false;
                }
            }
            grid = makeDeepClone(temp);
        }//Down
        else if (direction == 3){
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y+i][x] == Symbol.EMPTY){
                    temp[y+i][x] = ship;
                }else {
                    return false;
                }
            }
            grid = makeDeepClone(temp);
        }//Left
        else if (direction == 4){
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y][x-i] == Symbol.EMPTY){
                    temp[y][x-i] = ship;
                }else {
                    return false;
                }
            }
            grid = makeDeepClone(temp);
        }
        return true;
    }

    /**
     * Helper method to fill grid spots with Empty symbol
     */
    private void fillGrid(){
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[i][j] = Symbol.EMPTY;
                altGrid[i][j] = Symbol.EMPTY;
            }
        }
    }

    /**
     * Helper method to get number of ships to be placed
     *
     * @return number of ships to be placed
     */
    private int determineShipCount(){
        int MIN;
        int MAX;

        if (size == 9 || size == 8){
            MIN = 3;
            MAX = 5;
        }else if (size == 7 || size == 6){
            MIN = 2;
            MAX = 3;
        }else if (size == 5){
            MIN = 1;
            MAX = 2;
        }else {
            //Assume default size of 10x10
            MIN = 4;
            MAX = 6;
        }
        //Line below gets random number for number of ships to place, based on above if-else tree
        return random.nextInt((MAX - MIN) + 1) + MIN;
    }

    /**
     * Helper method to determine what ships will be placed on the board
     *
     * @return Array containing which ships to place
     */
    private Symbol[] determineShipsToPlace(int shipCount){
        ArrayList<Symbol> shipsToPlace = new ArrayList<>();

        ArrayList<Symbol> possibleShips = new ArrayList<>(Arrays.asList(Symbol.BATTLESHIP,
                Symbol.CARRIER, Symbol.CRUISER, Symbol.DESTROYER, Symbol.SUBMARINE));

        for(int i = 0; i < shipCount; i++){
            Symbol ship = possibleShips.get(random.nextInt((possibleShips.size())));
            shipsToPlace.add(ship);
            hitPoints += ship.getSize();
        }
        return shipsToPlace.toArray(new Symbol[0]);
    }

    /**
     * Getter method for grid field
     *
     * @return grid field
     */
    public Symbol[][] getGrid() {
        return this.grid;
    }

    /**
     * Getter method for size of grid
     *
     * @return size of grid
     */
    public int getSize() {
        return this.size;
    }

    /**
     * toString method to nicely format the grid
     *
     * @return nicely formatted grid
     */
    public String toString(Symbol[][] grid) {
        String board = "";
        String space = " ";
        String columnHeader = " ";
        //to get the numbers at the top of the grid
        for (int i = 0; i < size; i++) {
            columnHeader = columnHeader + "   " + String.valueOf(i);
        }
        board += username + "'s board:\n" + columnHeader + "\n";

        String body = "  ";
        String inBetween = "+---";
        String lines = "| ";
        //to create the border lines in between the rows
        for (int i = 0; i < size; i++) {
            body = body + (inBetween);
        }
        body += "+";
        board += body + "\n";

        //to add the border lines 
        for (int i = 0; i < grid.length; i++){
            board += i + space;
            //To add the rows with numbers at the front
            for (int j = 0; j < grid.length; j++){
                board += lines + grid[j][i].getSymbol() + space;
            }
            board += lines + "\n" + body + "\n";
        }
        return board;
    }

    /**
     * Helper method to create a deep clone of a 2D array
     * 
     * @param array 2D array to clone
     * @return Deep clone of 2D array passed
     */
    private Symbol[][] makeDeepClone(Symbol[][] array){
        Symbol[][] temp = new Symbol[array.length][array.length];
        for (int i = 0; i < array.length; i++){
            temp[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return temp;
    }

    /**
     * A Getter method to get the username
     * 
     * @return username The username associated with a grid of a player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter method to get the hit points
     *
     * @return hitPoints The number of total ship coordinates to hit
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Setter method to set the number of hitpoints a grid has 
     *
     * @param hitPoints The number of ship coordinates to hit
     */
    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     * Getter method to get the alternate version of the grid
     *
     * @return altGrid The alternate version of the board with no ships shown
     */
    public Symbol[][] getAltGrid() {
        return altGrid;
    }

}
