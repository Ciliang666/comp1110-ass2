package comp1110.ass2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Board {

//Initialization of Board class is based on the group framework, which is done by three.

    String[][] boardState;
    ArrayList<Tile> tiles;
    public String boardString = "";
    int score;
    ArrayList<ArrayList<Tile>> routes = new ArrayList<>();

    //exits coordinates
    int[][] exits = new int[][]{
            {0,1}, {0,3}, {0,5},
            {1,6}, {3,6}, {5,6},
            {6,1}, {6,3}, {6,5},
            {1,0}, {3,0}, {5,0}
    };



    //Constructor: Form a boardState, Initialize tiles
    public Board(){
        this.boardState = new String[7][7];
        this.tiles = new ArrayList<>();
    }

    //Another constructor
    public Board(String boardString){
        this.boardState = new String[7][7];
        this.tiles = new ArrayList<>();
        int i = 0;
        while(i < boardString.length()){
            String tileStr = boardString.substring(i, i+5);
            addValidTile(tileStr);
            i += 5;
        }
        this.routes = this.routes();
    }



//addValidTile, neighbors, placeable are done by Junming Zhao(u6633756)

    //Adjust addValidTile function:
    //some exit tiles doesn't have to be connected to exit
    //Check if a tile can be added to the current board
    //If valid return true and update current board state
    //If not, return false and nothing will be updated
    public boolean addValidTile(String tilePiece){
        Tile temp = new Tile(tilePiece);
        int row = temp.row;
        int col = temp.col;

        //If duplicate
        if(this.boardState[row][col] != null){
            return false;
        }

        //Get neighboring four grids
        String[] neighborGrids = neighbors(row, col);
        ArrayList<Tile> neighborTile = new ArrayList<>();
        boolean isIsolated = true;
        for(String s:neighborGrids){
            if(s != null){
                neighborTile.add(new Tile(s));
                isIsolated = false;
            }
        }

        //If no neighbor grids or not exits, then invalid
        boolean exit = temp.isExit();
        if(isIsolated && !exit){
            return false;
        }

        //Wrong connection on exit
        if(exit && !temp.exitValid()){
            return false;
        }

        //Check connection to other pieces
        boolean realIsolation = true;
        if(!isIsolated){
            for(Tile neighbor:neighborTile){
                if(temp.twoHaveConnection(temp, neighbor)){
                    boolean validConnection = Tile.twoTileConnected(temp, neighbor);
                    if(!validConnection) return false;
                    realIsolation = false;
                }
            }
        }

        if(realIsolation && exit && temp.exitConnection() == 'n') {
            return false;
        }

        //If has valid connection or a valid exit
        if(!realIsolation || exit){
            this.tiles.add(temp);
            this.boardState[row][col] = tilePiece;
            this.boardString += tilePiece;
            return true;
        }

        return false;
    }


    //Get neighboring four tiles for a given coordinate
    String[] neighbors(int x, int y){
        String up = null;
        String down = null;
        String left = null;
        String right = null;
        if(x > 0) up = this.boardState[x-1][y];
        if(x < 6) down = this.boardState[x+1][y];
        if(y > 0) left = this.boardState[x][y-1];
        if(y < 6) right = this.boardState[x][y+1];
        return new String[]{up, down, left, right};
    }


    //Get placeable coordinates for current board
    public Set<String> placeable(){
        Set<String> result = new HashSet<>();
        //Neighbor of existing tile
        for(Tile t : this.tiles){
            if(t.col!=0){
                if(boardState[t.row][t.col-1] == null){
                    result.add(""+Tile.correspondingRowToChar(t.row)+(t.col-1));
                }
            }
            if(t.row!=0){
                if(boardState[t.row-1][t.col] == null){
                    result.add(""+Tile.correspondingRowToChar((t.row-1))+t.col);
                }
            }
            if(t.row!=6){
                if(boardState[t.row+1][t.col] == null){
                    result.add(""+Tile.correspondingRowToChar((t.row+1))+t.col);
                }
            }
            if(t.col!=6){
                if(boardState[t.row][t.col+1] == null){
                    result.add(""+Tile.correspondingRowToChar(t.row)+(t.col+1));
                }
            }
        }
        //exits
        for(int[] exit : exits){
            int row = exit[0];
            int col = exit[1];
            if(boardState[row][col] == null){
                result.add(""+Tile.correspondingRowToChar(row)+""+col);
            }
        }
        return result;
    }


//routes(and helper), addNeighborTiles, exitsToScore, exitScore, centreScore, errorScore(and helper),
//basicScore are done by Junming Zhao(u6633756)

    //Get routes as a list of routes
    ArrayList<ArrayList<Tile>> routes(){
        ArrayList<ArrayList<Tile>> result = new ArrayList<>();
        for(Tile t : this.tiles){
            //Start a new route
            if(t.isExit() && t.exitConnection() != 'n' && !t.isMarked) {
                HashSet<Tile> route = new HashSet<>();
                char exitType = t.exitConnection();
                int exitDir = t.exitDirection();
                addNeighborTiles(t, route, exitType, exitDir);
                ArrayList<Tile> routeList = new ArrayList<>(route);
                result.add(routeList);
            }
        }
        return result;
    }


    //Add neighbor tiles for the current tile
    void addNeighborTiles(Tile t, HashSet<Tile> myRoute, char type, int preDir){
        myRoute.add(t);
        t.isMarked = true;

        //Non-station, only specific rail/highway can be connected
        if(!t.isStation()){
            if(preDir != 0 && t.connections[0] == type && t.col != 0){
                routeHelper(myRoute, t.row, (t.col - 1), type, 2, t);
            }
            if(preDir != 1 && t.connections[1] == type && t.row != 0){
                routeHelper(myRoute, (t.row - 1), t.col, type, 3, t);
            }
            if(preDir != 2 && t.connections[2] == type && t.col != 6){
                routeHelper(myRoute, t.row, (t.col + 1), type, 0, t);
            }
            if(preDir != 3 && t.connections[3] == type && t.row != 6){
                routeHelper(myRoute, (t.row + 1), t.col, type, 1, t);
            }
        }

        //Station, every connection can be added
        else{
            for(char c : t.connections){
                if(preDir != 0 && t.col != 0 && c!= 'n'){
                    routeHelper(myRoute, t.row, (t.col - 1), t.connections[0], (2), t);
                }
                //If up can be connected
                if(preDir != 1 && t.row != 0 && c!= 'n'){
                    routeHelper(myRoute, (t.row - 1), t.col, t.connections[1], (3), t);
                }
                //If right can be connected
                if(preDir != 2 && t.col != 6 && c!= 'n'){
                    routeHelper(myRoute, t.row, (t.col + 1), t.connections[2], (0), t);
                }
                //If down can be connected
                if(preDir != 3 && t.row != 6 && c!= 'n'){
                    routeHelper(myRoute, (t.row + 1), t.col, t.connections[3], (1), t);
                }
            }
        }
    }


    //Recursion helper function to continue to add tiles
    void routeHelper(HashSet<Tile> myRoute, int nrow, int ncol, char type, int nDir, Tile t){
        for(Tile t1 : this.tiles){
            if(t1.row == nrow && t1.col == ncol){
                if(!t1.isMarked){
                    if(Tile.twoTileConnected(t, t1)){
                        addNeighborTiles(t1, myRoute, type, nDir);
                    }
                } else if(t1.isCross() && !t1.isMarked2){
                    if(Tile.twoTileConnected(t, t1)){
                        t1.isMarked2 = true;
                        addNeighborTiles(t1, myRoute, type, nDir);
                    }
                }
            }
        }
    }


    //Calculate corresponding score
    static int exitsToScore(int exits){
        if(exits < 12){
            return (exits - 1)*4;
        }
        else{
            return 45;
        }
    }


    //Count the total score

    //Exit Scores
    int exitScore(){
        int score = 0;
        for(ArrayList<Tile> route : this.routes){
            int exits = 0;
            for(Tile t : route){
                if(t.isExit() && t.isMarked){
                    t.isMarked = false;
                    exits ++;
                }
            }
            score += exitsToScore(exits);
        }
        return score;
    }


    //Centre Scores
    int centreScore(){
        int result = 0;
        for(int i = 2; i < 5; i ++){
            for(int j = 2; j < 5; j ++){
                if(boardState[i][j] != null) {
                    result ++;
                }
            }
        }
        return result;
    }


    //Error Scores
    int errorScore(boolean permanent){
        int number = 0;
        for(Tile t : this.tiles){
            int row = t.row;
            int col = t.col;

            if(t.connections[0] != 'n' && col != 0){
                number = errorHelper(t, row, (col-1), number, permanent);
            }
            if(t.connections[1] != 'n' && row != 0){
                number = errorHelper(t, (row-1), col, number, permanent);
            }
            if(t.connections[2] != 'n' && col != 6){
                number = errorHelper(t, row, (col+1), number, permanent);
            }
            if(t.connections[3] != 'n' && row != 6){
                number = errorHelper(t, (row+1), col, number, permanent);
            }
        }
        return number;
    }


    //Check if there is an empty connection for a given tile
    int errorHelper(Tile t, int nrow, int ncol, int number, boolean permanent){
        if(this.boardState[nrow][ncol] == null){
            if(!permanent){
                number ++;
            }
        }
        else {
            for(Tile t1 : this.tiles){
                if(t1.row == nrow && t1.col == ncol){
                    if(!Tile.twoTileConnected(t, t1)){
                        number ++;
                    }
                }
            }
        }
        return number;
    }


    public int basicScore(){
        int score = this.exitScore() + this.centreScore() - this.errorScore(false);
        this.score = score;
        return score;
    }


//bonusScore, counts(and helper) are done by Junming Zhao(u6633756)

    public int bonusScore(){
        ArrayList<int[]> results = new ArrayList<>();
        for(ArrayList<Tile> route : this.routes){
            results.add(counts(route));
        }
        int longestRail = 0;
        int longestHighway = 0;
        //Select the longest railway and highway from all routes
        for(int[] counts : results){
            int rail = counts[0];
            int highway = counts[1];
            if(rail > longestRail) longestRail = rail;
            if(highway > longestHighway) longestHighway = highway;
        }
        return (longestRail + longestHighway);
    }



    //Generate the longest railway and highway for each route
    int[] counts(ArrayList<Tile> route){
        int maxR = 0;
        int maxH = 0;
        for(Tile t : route){
            //The starting point to count roads in a route is either exits or stations
            if(t.isExit() || t.isStation()){
                ArrayList<Integer> rails    = new ArrayList<>();
                ArrayList<Integer> highways = new ArrayList<>();
                ArrayList<Tile> counted1    = new ArrayList<>();
                ArrayList<Tile> counted2    = new ArrayList<>();
                counted1.add(t);
                counted2.add(t);
                countsHelper(route, counted1, t, 0, 'r', rails);
                countsHelper(route, counted2, t, 0, 'h', highways);
                //Get the longest one from all generated roads
                for(int r : rails){
                    if(r > maxR) maxR = r;
                }
                for(int h : highways){
                    if(h > maxH) maxH = h;
                }
            }
        }
        return new int[]{maxR, maxH};
    }


    //Helper function to count roads, using recursion to iterate through the route
    //Get different counting when it comes to crosses
    //Recursion method is hint by boogle game on lecture
    void countsHelper(ArrayList<Tile> route, ArrayList<Tile> counted, Tile current, int count, char type, ArrayList<Integer> counts){
        boolean connectToOthers = false;
        for(Tile t : route){
            int countT = count;
            ArrayList<Tile> countedT = new ArrayList<>(counted);
            countedT.add(current);
            //If in the route the other tile is connected to it, skip tiles that are already in the route
            if(!countedT.contains(t) && Tile.twoTileConnected(current, t)){
                //Same type, do recursion to iterate through the route
                if(Tile.twoTileConnectionType(current, t) == type){
                    connectToOthers = true;
                    countsHelper(route, countedT, t, (countT + 1), type, counts);
                    //If it is an exit, can count as one road option
                    if(t.isExit()){
                        countT += 2;
                        counts.add(countT);
                    }
                }
            }
        }
        //If no connection or the connection type is different,
        //then this road is ended and can be counted into road option
        if(!connectToOthers){
            int countT = count;
            if(countT != 0){
                countT ++;
                counts.add(countT);
            }
        }
    }



//movingOptions, validPlacement, placeOneTile, placeFixedTile are done by Junming Zhao(u6633756)

    //Get all placement options for a given board and diceRoll,
    //concerning all diceRoll combinations
    public ArrayList<String> movingOptions(String diceRoll, boolean simple, boolean manyOptions){
        ArrayList<String> options = new ArrayList<>();

        //Separate dices
        String[] dices = new String[4];
        for(int i = 0; i < 8; i += 2){
            String dice = diceRoll.substring(i, i+2);
            dices[i/2] = dice;
        }

        //Many orders of placing
        Set<String[]> combinations = new HashSet<>();
        permute(combinations, dices, (0));

        //For each order, get solutions
        for(String[] combination : combinations){
            if(manyOptions){
                if(options.size() > 20000){
                    break;
                }
            }
            ArrayList<String> moves = new ArrayList<>();
            validPlacement(moves, (""), combination, (0), (this));
            for(String s : moves){
                if(!options.contains(s)){
                    options.add(s);
                    if(s.length() == 20){
                        if(simple){
                            return options;
                        }
                    }
                }
            }
        }

        Collections.sort(options, (String a, String b) -> b.length() - a.length());

        //Get rid of non-complete solutions
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < options.size(); i ++){
            String current = options.get(i);
            int currentLength = current.length();
            boolean isSolution = true;
            for(int j = 0; j < i; j ++){
                if(options.get(j).substring(0, currentLength).equals(current)){
                    isSolution = false;
                    break;
                }
            }
            if(isSolution){
                result.add(current);
            }
        }
        return result;
    }



    //Recursion helper function to continue to add
    //tilesIrritating dice tiles using recursion. Update board, current solution each time.
    //Add solution into result if four tiles placed or no valid placement
    void validPlacement(ArrayList<String> result, String current, String[] tiles, int pointer, Board b){
        //If four tiles placed, add to result
        if(pointer == tiles.length){
            if(!result.contains(current)){
                result.add(current);
            }
        }

        else{
            String ctemp = current;
            ArrayList<String> placementsChoices = placeOneTile(ctemp, tiles[pointer], b);

            //If cannot place any, add to result
            if(placementsChoices.isEmpty()){
                if(!result.contains(current)){
                    result.add(current);
                }
            }
            //Get solutions for each current placement
            else{
                for(String eachPlacement : placementsChoices){
                    Board tempB = new Board(b.boardString);
                    if(tempB.addValidTile(eachPlacement.substring(eachPlacement.length()-5))){
                        validPlacement(result, eachPlacement, tiles, (pointer+1), tempB);
                    }
                }
            }
        }
    }


    //Get solutions for one tile, with coordinates possibilities
    ArrayList<String> placeOneTile(String current, String tile, Board b){
        ArrayList<String> result = new ArrayList<>();
        for(String coordinate : b.placeable()){
            String fixedTile = tile + coordinate;
            String temp = current;
            Board tempB = new Board(b.boardString);
            ArrayList<String> thisResult = placeFixedTile(temp, fixedTile, tempB);
            result.addAll(thisResult);
        }
        return result;
    }


    //Get solutions for one tile with fixed coordinate and orientations possibilities
    ArrayList<String> placeFixedTile(String current, String tile, Board b){
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < 8; i ++){
            String temp = tile;
            String piece = Tile.fixOrientationStr((temp + i));
            if(b.addValidTile(piece)){
                String temp1 = current + piece;
                if(!result.contains(temp1)) result.add(temp1);
            }
        }
        return result;
    }


//AI related functions

    //Heuristic for advanced AI (Jingsheng Deng (u6847863))
    public int myScore(int numOfRound){
        int score = 0;
        int error;
        int centre = centreScore();
        int exit = exitScore();
        int bonus = bonusScore();
        if(numOfRound < 5){
            error = errorScore(true);
            score -= 20*error;
            score += 5*centre;
            score += exit;
            score += 15*bonus;
        } else{
            error = errorScore(false);
            score -= 5*error;
            score += centre;
            score += 7*exit;
            score += bonus;
        }
        return score;
    }


    //Select a best placement from a list according to numOfRound and the heuristic (Jingsheng Deng (u6847863))
    public String placement(ArrayList<String> movingOptions, int numOfRound){
        Board temp = new Board(boardString);
        String first = movingOptions.get(0);
        int i = 0;
        while(i < first.length()){
            String tileStr = first.substring(i, i + 5);
            temp.addValidTile(tileStr);
            i += 5;
        }
        int score = temp.myScore(numOfRound);
        String placement = first;

        for(String s : movingOptions){
            temp = new Board(boardString);
            i = 0;
            while(i < s.length()){
                String tileStr = s.substring(i, i + 5);
                temp.addValidTile(tileStr);
                i += 5;
            }
            int tempScore = temp.myScore(numOfRound);
            if(tempScore > score){
                score = tempScore;
                placement = s;
            }
        }
        return placement;
    }


//Helper function permute relies on the hint of stackoverflow

    //Helper function -- get permutations for a four-tile diceRoll
    private void permute(Set<String[]> result, String[] arr, int pointer) {
        if (pointer == arr.length) {
            result.add(arr);
            return;
        }
        for (int i = pointer; i < arr.length; i++) {
            String[] permutation = arr.clone();
            permutation[pointer] = arr[i];
            permutation[i] = arr[pointer];
            permute(result, permutation, pointer + 1);
        }
    }



    @Override
    public String toString() {
        // Get the name of the tile
        // e.g. "A3C23", "B1B20"
        return this.boardString;
    }
}
