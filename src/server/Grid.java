package server;

import java.util.*;

/**
 * This class represents the logic for a single board of Battleship
 *
 * @author Brett Dale
 * @author Katherine Blanton
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
        //System.out.println("Grid built!");
    }

    /**
     * Method to place ships on grid
     */
    private void placeShips(){
        int shipCount = determineShipCount();
        //System.out.println("\nGoing to place " + shipCount + " ships...");
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
                    if ((y - ship.getSize()) < size && y - ship.getSize() > -1) {
                        if (putShip(x, y, direction, ship)) {
                            shipPlaced = true;
                        }else{
                            //System.out.println("Failed to place " + ship.getSize());
                            directions.remove(Integer.valueOf(direction));
                        }
                    }else {
                        directions.remove(Integer.valueOf(direction));
                    }
                    //Right
                } else if (direction == 2) {
                    if (x + ship.getSize() < size && x + ship.getSize() > -1) {
                        if (putShip(x, y, direction, ship)) {
                            shipPlaced = true;
                        }else{
                            //System.out.println("Failed to place " + ship.getName());
                            directions.remove(Integer.valueOf(direction));
                        }
                    }else {
                        directions.remove(Integer.valueOf(direction));
                    }
                    //Down
                } else if (direction == 3) {
                    if ((y + ship.getSize()) < size && y + ship.getSize() > -1) {
                        if (putShip(x, y, direction, ship)) {
                            shipPlaced = true;
                        }else{
                            //System.out.println("Failed to place " + ship.getName());
                            directions.remove(Integer.valueOf(direction));
                        }
                    }else {
                        directions.remove(Integer.valueOf(direction));
                    }
                    //Left
                } else if (direction == 4) {
                    if (x - ship.getSize() < size && x - ship.getSize() > -1) {
                        if (putShip(x, y, direction, ship)) {
                            shipPlaced = true;
                        }else{
                            //System.out.println("Failed to place " + ship.getName());
                            directions.remove(Integer.valueOf(direction));
                        }
                    }else {
                        directions.remove(Integer.valueOf(direction));
                    }
                }
            }
            if (!shipPlaced) {
                //System.out.printf("%s could not be placed", ship.getName());
                //try and re-place the ship
                i--;
            }
        }
    }

    private void tryUp(int x, int y, Symbol ship, int direction, ArrayList<Integer> directions, boolean shipPlaced){
        if ((y - ship.getSize()) < size && y - ship.getSize() > -1) {
            if (putShip(x, y, direction, ship)) {
                shipPlaced = true;
            }else{
                //System.out.println("Failed to place " + ship.getSize());
                directions.remove(Integer.valueOf(direction));
            }
        }else {
            directions.remove(Integer.valueOf(direction));
        }
    }

    /**
     * Helper method to place a single ship
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
            //System.out.printf("\nTrying to place %s going UP\n", ship.getName());
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y-i][x] == Symbol.EMPTY){
                    temp[y-i][x] = ship;
                    //System.out.printf("\n%s placed at [%x][%x]\n", ship.getName(), y-i, x);
                }else {
                    return false;
                }
            }
            grid = makeDeepClone(temp);
        }//Right
        else if (direction == 2) {
            //System.out.printf("\nTrying to place %s going RIGHT\n", ship.getName());
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y][x+i] == Symbol.EMPTY){
                    temp[y][x+i] = ship;
                    //System.out.printf("\n%s placed at [%x][%x]\n", ship.getName(), y, x+i);

                }else {
                    return false;
                }
            }
            grid = makeDeepClone(temp);

        }//Down
        else if (direction == 3){
            //System.out.printf("\nTrying to place %s going DOWN\n", ship.getName());
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y+i][x] == Symbol.EMPTY){
                    temp[y+i][x] = ship;
                    //System.out.printf("\n%s placed at [%x][%x]\n", ship.getName(), y+i, x);
                }else {
                    return false;
                }
            }
            grid = makeDeepClone(temp);

        }//Left
        else if (direction == 4){
            //System.out.printf("\nTrying to place %s going LEFT\n", ship.getName());
            for (int i = 0; i < ship.getSize(); i++){
                if (grid[y][x-i] == Symbol.EMPTY){
                    temp[y][x-i] = ship;
                    //System.out.printf("\n%s placed at [%x][%x]\n", ship.getName(), y, x-i);

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
     * @return grid field
     */
    public Symbol[][] getGrid() {
        return this.grid;
    }

    /**
     * Getter method for size of grid
     * @return size of grid
     */
    public int getSize() {
        return this.size;
    }

    /**
     * toString method to nicely format the grid
     * @return nicely formatted grid
     */
    public String toString(Symbol[][] grid) {
        String board = "";
        String space = " ";
        String columnHeader = " ";
        for (int i = 0; i < size; i++) {
            columnHeader = columnHeader + "   " + String.valueOf(i);
        }
        board += username + "'s board:\n" + columnHeader + "\n";

        String body = "  ";
        String inBetween = "+---";
        String lines = "| ";
        for (int i = 0; i < size; i++) {
            body = body + (inBetween);
        }
        body += "+";
        board += body + "\n";

        for (int i = 0; i < grid.length; i++){
            board += i + space;
            for (int j = 0; j < grid.length; j++){
                board += lines + grid[j][i].getSymbol() + space;
            }
            board += lines + "\n" + body + "\n";
        }
        return board;
    }

    /**
     * Helper method to create a deep clone of a 2D array
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

    public String getUsername() {
        return username;
    }


    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Symbol[][] getAltGrid() {
        return altGrid;
    }

}
