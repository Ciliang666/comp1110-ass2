package comp1110.ass2;

import java.util.ArrayList;
import java.util.Random;

public class RailroadInk {
    /**
     * Determine whether a tile placement string is well-formed:
     * - it consists of exactly 5 characters;
     * - the first character represents a die A or B, or a special tile S
     * - the second character indicates which tile or face of the die (0-5 for die A and special tiles, or 0-2 for die B)
     * - the third character represents the placement row A-G
     * - the fourth character represents the placement column 0-6
     * - the fifth character represents the orientation 0-7
     *
     * @param tilePlacementString a candidate tile placement string
     * @return true if the tile placement is well formed
     */
    public static boolean isTilePlacementWellFormed(String tilePlacementString) {
        // FIXME Task 2: determine whether a tile placement is well-formed
        //Check the length
        if (tilePlacementString.length()!=5){
            return false;
        }

        //Check the first character
        char fst = tilePlacementString.charAt(0);
        if (fst!='A' && fst!='B' && fst !='S'){
            return false;
        }

        //Check the second character
        //A and S
        char snd = tilePlacementString.charAt(1);
        if (fst=='A' || fst=='S'){
            if (snd>='6' || snd<'0'){
                return false;
            }
        }
        //B
        if (fst=='B'){
            if (snd>='3' || snd<'0'){
                return false;
            }
        }

        //Check the third character
        char trd = tilePlacementString.charAt(2);
        if (trd<'A' || trd>'G'){
            return false;
        }

        //Check the fourth character
        char frth = tilePlacementString.charAt(3);
        if (frth>'6' || frth<'0'){
            return false;
        }

        //Check the fifth character
        char fif = tilePlacementString.charAt(4);
        return !(fif>'7' || fif<'0');
    }

    /**
     * Determine whether a board string is well-formed:
     * - it consists of exactly N five-character tile placements (where N = 1 .. 31);
     * - each piece placement is well-formed
     * - no more than three special tiles are included
     *
     * @param boardString a board string describing the placement of one or more pieces
     * @return true if the board string is well-formed
     */
    public static boolean isBoardStringWellFormed(String boardString) {
        // FIXME Task 3: determine whether a board string is well-formed
        //Check if the string is empty
        if (boardString == null || boardString.equals("")){
            return false;
        }

        //Check if the length is divisible by 5 and within the range
        int length = boardString.length();
        if (length%5!=0 || length>155){
            return false;
        }

        //Check if every piece is valid
        int index = 0;
        while(index < length){
            String piece = boardString.substring(index, index + 5);
            if(!isTilePlacementWellFormed(piece)){
                return false;
            }
            index += 5;
        }

        //check special tiles
        int k = length/5;
        int cnt = 0;
        for (int i = 0;i<k;i++){
            if (boardString.charAt(i*5)=='S'){
                cnt++;
            }
        }
        return cnt <= 3;
    }


    /**
     * Determine whether the provided placements are neighbours connected by at least one validly connecting edge.
     * For example,
     * - areConnectedNeighbours("A3C10", "A3C23") would return true as these tiles are connected by a highway edge;
     * - areConnectedNeighbours("A3C23", "B1B20") would return false as these neighbouring tiles are disconnected;
     * - areConnectedNeighbours("A0B30", "A3B23") would return false as these neighbouring tiles have an
     * invalid connection between highway and railway; and
     * areConnectedNeighbours("A0B30", "A3C23") would return false as these tiles are not neighbours.
     *
     * @return true if the placements are connected neighbours
     */
    public static boolean areConnectedNeighbours(String tilePlacementStringA, String tilePlacementStringB) {
        // FIXME Task 5: determine whether neighbouring placements are connected
        Tile fst = new Tile(tilePlacementStringA);
        Tile snd = new Tile(tilePlacementStringB);
        return Tile.twoTileConnected(fst, snd);
    }

    /**
     * Given a well-formed board string representing an ordered list of placements,
     * determine whether the board string is valid.
     * A board string is valid if each tile placement is legal with respect to all previous tile
     * placements in the string, according to the rules for legal placements:
     * - A tile must be placed such that at least one edge connects to either an exit or a pre-existing route.
     *   Such a connection is called a valid connection.
     * - Tiles may not be placed such that a highway edge connects to a railway edge;
     *   this is referred to as an invalid connection.
     *   Highways and railways may only join at station tiles.
     * - A tile may have one or more edges touching a blank edge of another tile;
     *   this is referred to as disconnected, but the placement is still legal.
     *
     * @param boardString a board string representing some placement sequence
     * @return true if placement sequence is valid
     */
    public static boolean isValidPlacementSequence(String boardString) {
        // FIXME Task 6: determine whether the given placement sequence is valid
        int i = 0;
        boolean validity;
        Board b = new Board();
        while(i < boardString.length()){
            String thisPiece = boardString.substring(i, i+5);
            validity = b.addValidTile(thisPiece);
            if(!validity) return false;
            i += 5;
        }
        return true;
    }


    /**
     * Generate a random dice roll as a string of eight characters.
     * Dice A should be rolled three times, dice B should be rolled once.
     * Die A has faces numbered 0-5.
     * Die B has faces numbered 0-2.
     * Each die roll is composed of a character 'A' or 'B' representing the dice,
     * followed by a digit character representing the face.
     *
     * @return a String representing the die roll e.g. A0A4A3B2
     */
    public static String generateDiceRoll() {
        // FIXME Task 7: generate a dice roll
        Random rand = new Random();
        int fstA = rand.nextInt(6);
        int sndA = rand.nextInt(6);
        int trdA = rand.nextInt(6);
        int fstB = rand.nextInt(3);
        String result = "A"+fstA+"A"+sndA+"A"+trdA+"B"+fstB;
        return result;
    }

    /**
     * Given the current state of a game board, output an integer representing the sum of all the following factors
     * that contribute to the player's final score.
     * <p>
     * * Number of exits mapped
     * * Number of centre tiles used
     * * Number of dead ends in the network
     *
     * @param boardString a board string representing a completed game
     * @return integer (positive or negative) for score *not* considering longest rail/highway
     */
    public static int getBasicScore(String boardString) {
        // FIXME Task 8: compute the basic score
        Board b = new Board(boardString);
        return b.basicScore();
    }

    /**
     * Given a valid boardString and a dice roll for the round,
     * return a String representing an ordered sequence of valid piece placements for the round.
     * @param boardString a board string representing the current state of the game as at the start of the round
     * @param diceRoll a String representing a dice roll for the round
     * @return a String representing an ordered sequence of valid piece placements for the current round
     * @see RailroadInk#generateDiceRoll()
     */
    public static String generateMove(String boardString, String diceRoll) {
        // FIXME Task 10: generate a valid move
        Board b = new Board(boardString);
        ArrayList<String> placements = b.movingOptions(diceRoll, false, false);

        if(placements.isEmpty()){
            return "";
        }

        String result = placements.get(0);
        return result;
    }


    /**
     * Given the current state of a game board, output an integer representing the sum of all the factors contributing
     * to `getBasicScore`, as well as those attributed to:
     * <p>
     * * Longest railroad
     * * Longest highway
     *
     * @param boardString a board string representing a completed game
     * @return integer (positive or negative) for final score (not counting expansion packs)
     */
    public static int getAdvancedScore(String boardString) {
        // FIXME Task 12: compute the total score including bonus points
        Board b = new Board(boardString);
        int basic = b.basicScore();
        int bonus = b.bonusScore();
        return (basic + bonus);
    }
}

