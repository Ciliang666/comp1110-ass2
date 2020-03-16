Reviewer: Ciliang Ma (u6803148)
Component: <Board.addValidTile(String tilePiece)>
Author: Junming Zhao (u6633756)

Review Comments:

1. This method is used to determine whether the tile is placed legally according to the rules.
If it is legal, then add the tile placement string to the current board string. In order to 
implement this method, different conditions are taken into consideration:
a)if the tile is isolated
b)if the tile is linked to an exit
c)if the tile is linked to another tile(rail to rail, highway to highway)
2. A two-dimension array is created to help implement the above method in the Board Class:
boardState[row][col], if valid, update boardState plus board string
3. Other methods in Tile Class are used too:
a)isExit() // if the tile is next to an exit
b)exitValid() // if the exit is legal
c)twoHaveConnection(Tile fst, Tile snd) // if two tiles are neighbours
d)twoTileConnected(Tile fst, Tile snd) // if two tiles are legal neighbours
4.The code is well-formed. The program decomposition is appropriate. It follows Java code 
conventions, and the style is consistent throughout. 