package comp1110.ass2;

import java.util.Arrays;

public class Tile{

//Initialization of Tile class is based on the group framework, which is done by three.

    private String init;
    public int row;
    public int col;
    public String coordinate;
    public int orientation;

    //rail -- 'r'
    //highway -- 'w'
    //null -- 'n'
    //Connections are in clockwise direction a-w-d-s
    //   w
    // a   d
    //   s
    char[] connections;

    boolean isMarked = false;


    //Constructor: set init and connections by checking enum values
    public Tile(){}
    public Tile(String piece){
        this.init = piece.substring(0, 2);
        this.connections = new char[4];
        for(DefaultTiles t:DefaultTiles.values()){
            if(this.init.equals(t.name())){
                this.connections = Arrays.copyOf(t.defaultConnections, 4);
                break;
            }
        }
        this.setCoordinate(piece.substring(2, 4));
        this.setOrientation(piece.substring(4));
    }


//-------- Some initializing methods
    public static int correspondingRowToNum(char r){
        if(r == 'A') return 0;
        if(r == 'B') return 1;
        if(r == 'C') return 2;
        if(r == 'D') return 3;
        if(r == 'E') return 4;
        if(r == 'F') return 5;
        if(r == 'G') return 6;
        return 0;
    }

    public static char correspondingRowToChar(int i){
        if(i == 0) return 'A';
        if(i == 1) return 'B';
        if(i == 2) return 'C';
        if(i == 3) return 'D';
        if(i == 4) return 'E';
        if(i == 5) return 'F';
        if(i == 6) return 'G';
        return 'A';
    }

    //Convert corresponding row letters to integers
    public int[] getCoordinate(String coordinate){
        int row = correspondingRowToNum(coordinate.charAt(0));
        int col = Character.digit(coordinate.charAt(1), 10);
        return new int[]{row, col};
    }

    void setCoordinate(String coordinate){
        this.coordinate = coordinate;
        int[] temp = getCoordinate(coordinate);
        this.row = temp[0];
        this.col = temp[1];
    }


    //Rotate 90 degrees in clockwise direction
    void rotate90(){
        char[] temp = Arrays.copyOf(this.connections, 4);
        this.connections[0] = temp[3];
        this.connections[1] = temp[0];
        this.connections[2] = temp[1];
        this.connections[3] = temp[2];
    }

    //Horizontally mirroring
    void mirror(){
        char[] temp = Arrays.copyOf(this.connections, 4);
        this.connections[0] = temp[2];
        this.connections[2] = temp[0];
    }


    //Set orientation and change connections accordingly
    void setOrientation(String orientation){
        this.orientation = Integer.parseInt(orientation);
        this.fixOrientation();

        //Orientation 1-3 -- rotate
        if(this.orientation > 0 && this.orientation <4){
            for(int i = 0; i < this.orientation; i ++){
                rotate90();
            }
        }
        //Orientation 4 -- mirror
        else if(this.orientation == 4){
            mirror();
        }
        //Orientation 5-7 -- mirror and rotate
        else if(this.orientation > 4 && this.orientation < 8){
            mirror();
            for(int i = 4; i < this.orientation; i ++) {
                rotate90();
            }
        }
    }


    //fix orientation for some tiles to avoid repetition
    void fixOrientation(){
        if(this.init.equals("S0") || this.init.equals("S1") || this.init.equals("B0")){
            if(this.orientation >= 4) this.orientation -= 4;
        }
        if(this.init.equals("S2") || this.init.equals("S3")){
            this.orientation = 0;
        }
        if(this.init.equals("S5") || this.init.equals("A1") ||
                this.init.equals("A4") || this.init.equals("B2")){
            if(this.orientation >= 2) this.orientation %= 2;
        }
        if(this.init.equals("S4") || this.init.equals("A0") || this.init.equals("A5")){
            if(this.orientation == 7) this.orientation = 0;
            if(this.orientation == 4) this.orientation = 1;
            if(this.orientation == 5) this.orientation = 2;
            if(this.orientation == 6) this.orientation = 3;
        }
        if(this.init.equals("A2") || this.init.equals("A3")){
            if(this.orientation == 6) this.orientation = 0;
            if(this.orientation == 7) this.orientation = 1;
            if(this.orientation == 4) this.orientation = 2;
            if(this.orientation == 5) this.orientation = 3;
        }
    }


    static String fixOrientationStr(String piece){
        if(piece.length() != 5){
            return piece;
        }
        String init = piece.substring(0,2);
        String coor = piece.substring(2,4);
        int orientation = Integer.parseInt(piece.substring(4));
        if(init.equals("SO") || init.equals("S1") || init.equals("B0")){
            if(orientation>=4) orientation -= 4;
        }
        if(init.equals("S2") || init.equals("S3")){
            orientation = 0;
        }
        if(init.equals("S5") || init.equals("A1") ||
                init.equals("A4") || init.equals("B2")){
            if(orientation >= 2) orientation %= 2;
        }
        if(init.equals("S4") || init.equals("A0") || init.equals("A5")){
            if(orientation == 7) orientation = 0;
            if(orientation == 4) orientation = 1;
            if(orientation == 5) orientation = 2;
            if(orientation == 6) orientation = 3;
        }
        if(init.equals("A2") || init.equals("A3")){
            if(orientation == 6) orientation = 0;
            if(orientation == 7) orientation = 1;
            if(orientation == 4) orientation = 2;
            if(orientation == 5) orientation = 3;
        }
        return init + coor + orientation;
    }


//-------- End of initializing methods


//relativeDirection, twoHaveConnection, twoTileConnectionType, isExit, exitConnection,
//exitDirection, exitValid, isStation, isCross are done by Junming Zhao(u6633756)

    //Get the relative direction for the snd tile in terms of the fst tile
    public static char relativeDirection(Tile fst, Tile snd){
        int fstRow = fst.row;
        int fstCol = fst.col;
        int sndRow = snd.row;
        int sndCol = snd.col;

        //Not neighbors
        if(fstRow != sndRow && fstCol != sndCol) {
            return 'n';
        }

        //Same row, compare columns
        if(fstRow == sndRow){
            //snd tile is to the right of fst
            if(fstCol == sndCol - 1){
                return 'd';
            }
            //snd tile is to the left of fst
            else if(fstCol == sndCol + 1){
                return 'a';
            }
        }
        //Same column, compare rows
        else{
            //snd tile is to the top of the fst
            if(fstRow == sndRow + 1){
                return 'w';
            }
            //snd tile is to the bottom of fst
            else if(fstRow == sndRow - 1) {
                return 's';
            }
        }
        return 'n';
    }



    //If there is any actual road to road connection between two tiles
    public boolean twoHaveConnection(Tile fst, Tile snd){
        char relative = relativeDirection(fst, snd);
        if(relative == 'n') return false;
        else if(relative == 'a'){
            //snd is to the left of fst
            char fsta = fst.connections[0];
            char sndd = snd.connections[2];
            return (fsta != 'n' && sndd != 'n');
        } else if(relative == 'w'){
            //snd is to the top fst
            char fstw = fst.connections[1];
            char snds = snd.connections[3];
            return (fstw != 'n' && snds != 'n');
        } else if(relative == 'd'){
            //snd is to the right of fst
            char fstd = fst.connections[2];
            char snda = snd.connections[0];
            return (fstd != 'n' && snda != 'n');
        } else if(relative == 's'){
            //snd is to the bottom of fst
            char fsts = fst.connections[3];
            char sndw = snd.connections[1];
            return (fsts != 'n' && sndw != 'n');
        }
        return false;
    }



    //If there is any valid connection between two tiles
    static boolean twoTileConnected(Tile fst, Tile snd){
        char relative = relativeDirection(fst, snd);
        if(relative == 'n') return false;
         if(relative == 'a'){
            //snd is to the left of fst
            char fsta = fst.connections[0];
            char sndd = snd.connections[2];
            return (fsta == sndd && fsta != 'n');
        } else if(relative == 'w'){
            //snd is to the top fst
            char fstw = fst.connections[1];
            char snds = snd.connections[3];
            return (fstw == snds && fstw != 'n');
        } else if(relative == 'd'){
            //snd is to the right of fst
            char fstd = fst.connections[2];
            char snda = snd.connections[0];
            return (fstd == snda && fstd != 'n');
        } else if(relative == 's'){
            //snd is to the bottom of fst
            char fsts = fst.connections[3];
            char sndw = snd.connections[1];
            return (fsts == sndw && fsts != 'n');
        }
        return false;
    }


    static char twoTileConnectionType(Tile fst, Tile snd){
        char relativePos = relativeDirection(fst, snd);
        if(relativePos == 'a') return fst.connections[0];
        if(relativePos == 'w') return fst.connections[1];
        if(relativePos == 'd') return fst.connections[2];
        if(relativePos == 's') return fst.connections[3];
        return 'n';
    }



    //Tile on exit position of a board
    boolean isExit(){
        int row = this.row;
        int col = this.col;

        boolean one = (row == 1 || row == 3 || row == 5) &&
                (col == 0 || col == 6);
        boolean two = (col == 1 || col == 3 || col == 5) &&
                (row == 0 || row == 6);
        return one || two;
    }


    char exitConnection(){
        if(this.isExit()){
            if(this.col == 0) return this.connections[0];
            if(this.row == 0) return this.connections[1];
            if(this.col == 6) return this.connections[2];
            if(this.row == 6) return this.connections[3];
        }
        return 'n';
    }



    int exitDirection(){
        if(this.isExit()){
            if(this.col == 0) return 0;
            if(this.row == 0) return 1;
            if(this.col == 6) return 2;
            if(this.row == 6) return 3;
        }
        return 0;
    }


    //Tile is a valid exit -- corresponding road type matching
    boolean exitValid(){
        int row = this.row;
        int col = this.col;
        if(row == 0 && (col == 1 || col == 5)){
            if(this.connections[1] == 'r') return false;
        }
        if(row == 0 && col == 3) {
            if(this.connections[1] == 'h') return false;
        }
        if(row == 6 && (col == 1 || col == 5)){
            if(this.connections[3] == 'r') return false;
        }
        if(row == 6 && col == 3) {
            if(this.connections[3] == 'h') return false;
        }
        if(col == 6 && (row == 1 || row == 5)){
            if(this.connections[2] == 'h') return false;
        }
        if(col == 6 && (row == 3)){
            if(this.connections[2] == 'r') return false;
        }
        if(col == 0 && (row == 1 || row == 5)){
            if(this.connections[0] == 'h') return false;
        }
        if(col == 0 && (row == 3)){
            if(this.connections[0] == 'r') return false;
        }
        return true;
    }


    //Some tiles are station so it can connect to
    //another type of road in the same route
    boolean isStation(){
        return  this.init.equals("B0") || this.init.equals("B1") ||
                this.init.equals("S0") || this.init.equals("S1") ||
                this.init.equals("S4") || this.init.equals("S5");
    }

    boolean isCross(){
        return this.init.equals("B2");
    }
    boolean isMarked2 = false;



    //Debug helper function
    @Override
    public String toString() {
        // Get the name of the tile
        // e.g. "A3C23", "B1B20"
        return this.init + this.coordinate + this.orientation;
    }
}
