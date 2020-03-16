package comp1110.ass2;

// Default tiles and their states
// Quote from assignment rules and done by three group members
public enum DefaultTiles {
    //'h' - Highway
    //'r' - Rail
    //'n' - Null
    S0(new char[]{'h', 'h', 'h', 'r'}),
    S1(new char[]{'r', 'h', 'r', 'r'}),
    S2(new char[]{'h', 'h', 'h', 'h'}),
    S3(new char[]{'r', 'r', 'r', 'r'}),
    S4(new char[]{'h', 'h', 'r', 'r'}),
    S5(new char[]{'r', 'h', 'r', 'h'}),
    A0(new char[]{'r', 'r', 'n', 'n'}),
    A1(new char[]{'n', 'r', 'n', 'r'}),
    A2(new char[]{'n', 'r', 'r', 'r'}),
    A3(new char[]{'n', 'h', 'h', 'h'}),
    A4(new char[]{'n', 'h', 'n', 'h'}),
    A5(new char[]{'h', 'h', 'n', 'n'}),
    B0(new char[]{'n', 'h', 'n', 'r'}),
    B1(new char[]{'n', 'h', 'r', 'n'}),
    B2(new char[]{'r', 'h', 'r', 'h'});

    public final char[] defaultConnections;

    DefaultTiles(char[] connection){
        this.defaultConnections = connection;
    }

}
