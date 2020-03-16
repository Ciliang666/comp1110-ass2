package comp1110.ass2.gui;

import comp1110.ass2.Board;
import comp1110.ass2.RailroadInk;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import comp1110.ass2.Tile;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Set;
import javafx.scene.effect.DropShadow;


/**
 * A very simple viewer for tile placements in the Railroad Ink game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various tile placements.
 */
public class Viewer extends Application {
    /* board layout */
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;
    private static int TILE_SIDE;
    private static final int SINGLE_X = 267;
    private static final int SINGLE_Y = 170;
    private static final int MULTI_X1 = 50;
    private static final int MULTI_Y = 170;
    private static final int MULTI_X2 = 562;
    private static final String URI_BASE = "assets/";
    private static final String TITLE_FONT = "Herculanum";
    private static final String DEFAULT_FONT = "Skia";
    private static final String RULE_FONT = "Hannotate TC";
    private static final String START_SCENE_FONT = "Copperplate";
    private static final Color BOARD_COLOR1 = Color.rgb(255, 255, 240);
    private static final Color BOARD_COLOR2 = Color.rgb(240, 245, 245);
    private static final Color CORRECT_COLOR = Color.rgb(160, 210, 170);
    private static final Color WRONG_COLOR = Color.rgb(215, 90, 90);
    private final ImageView BACKGROUND =
            new ImageView(new Image(getClass().getResourceAsStream(URI_BASE + "Background" + ".png")));


    private Group root     = new Group();
    private Group controls = new Group();
    private Group board    = new Group();
    private Group tiles    = new Group();
    private Group buttons  = new Group();
    private Group scores   = new Group();


    private Board b = new Board();
    private Board ai = new Board();
    private DropShadow shadow = new DropShadow();
    private int numOfRound = 0;
    private int numOfSpecialTiles = 0;
    private int[] rotateCount = {0,0,0,0};
    private int[] specialRotateCount = {0,0,0,0,0,0};
    private String dices;
    private ImageView[] dicesTiles = new ImageView[4];
    private String[] tempTiles = new String[4];
    private TextField textField;
    private Rectangle highlighted = null;
    private Rectangle[][] rectangles = new Rectangle[7][7];

    private double fixedX = 0;
    private double fixedY = 0;


    private static Text basic = new Text("Basic Score:");
    private static Text advanced = new Text("Advanced Score:");
    private static Text round = new Text("Num of Rounds:");
    private static Text AIbasic = new Text("AI Basic Score:");
    private static Text AIadvanced = new Text("AI Advanced Score:");
    private Text basicScore = new Text("0");
    private Text advancedScore = new Text("0");
    private Text numrounds = new Text("0");
    private Text AIbasicScore = new Text("0");
    private Text AIadvancedScore = new Text("0");



    //Get all special tiles to display at the top
    private ImageView[] specialTiles = new ImageView[6];
    private String[] specialTileName = {"S0","S1","S2","S3","S4","S5"};
    void getSpecialTiles(){
        for(int i = 0; i < 6; i ++){
            String name = specialTileName[i];
            Image special = new Image(getClass().getResourceAsStream(URI_BASE + name + ".png"));
            ImageView img = new ImageView(special);
            specialTiles[i] = img;
        }
    }


    //An initial board with grids and exits (Junming Zhao u6633756)
    void getBoard(int startPointX, int startPointY, Color color, boolean player){
        //Exits
        Image highway = new Image(getClass().getResourceAsStream(URI_BASE + "HighExit" + ".png"));
        Image railway = new Image(getClass().getResourceAsStream(URI_BASE + "RailExit" + ".png"));

        ImageView[] exits = new ImageView[12];
        for(int i = 0; i < 12; i ++){
            if(i % 2 == 0){
                exits[i] = new ImageView(highway);
            } else{
                exits[i] = new ImageView(railway);
            }
        }
        for(int i = 0; i < 3; i ++){
            exits[i].setTranslateX(startPointX + TILE_SIDE + i * 2 * TILE_SIDE);
                exits[i].setTranslateY(startPointY - 0.73 * TILE_SIDE);
        }

        for(int i = 3; i < 6; i ++){
            exits[i].setRotate(90);
            exits[i].setTranslateX(startPointX + 7 * TILE_SIDE - 0.286 * TILE_SIDE);
            exits[i].setTranslateY(startPointY + TILE_SIDE + (i - 3) * 2 * TILE_SIDE);
        }

        for(int i = 6; i < 9; i ++){
            exits[i].setRotate(180);
            exits[i].setTranslateX(startPointX + TILE_SIDE + (i - 6) * 2 * TILE_SIDE);
            exits[i].setTranslateY(startPointY + 7 * TILE_SIDE - 0.286 * TILE_SIDE);
        }

        for(int i = 9; i < 12; i ++){
            exits[i].setRotate(270);
            exits[i].setTranslateX(startPointX - 0.714 * TILE_SIDE);
            exits[i].setTranslateY(startPointY + TILE_SIDE + (i - 9) * 2 * TILE_SIDE);
        }

        for(ImageView i : exits){
            i.setFitHeight(TILE_SIDE);
            i.setFitWidth(TILE_SIDE);
            board.getChildren().add(i);
        }

        //Outside broader
        Rectangle bigGrid = new Rectangle(startPointX, startPointY, TILE_SIDE * 7, TILE_SIDE * 7);
        Rectangle fill = new Rectangle(startPointX, startPointY, TILE_SIDE * 7, TILE_SIDE * 7);
        fill.setFill(color);
        bigGrid.setFill(Color.TRANSPARENT);
        bigGrid.setStroke(Color.BLACK);
        bigGrid.setStrokeWidth(7);
        bigGrid.toFront();
        board.getChildren().addAll(bigGrid, fill);


        //Inner grids
        for(int i = 0; i < 7; i ++){
            for (int j = 0; j < 7; j ++){
                Rectangle r = new Rectangle(startPointX + TILE_SIDE * i,startPointY + TILE_SIDE * j, TILE_SIDE, TILE_SIDE);
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.BLACK);
                if(player) {
                    rectangles[i][j] = r;
                }
                board.getChildren().add(r);
            }
        }


        //Centre
        Rectangle centre = new Rectangle(startPointX + 2 * TILE_SIDE, startPointY + 2 * TILE_SIDE , TILE_SIDE * 3, TILE_SIDE * 3);
        centre.setFill(Color.TRANSPARENT);
        centre.setStroke(Color.RED);
        centre.setStrokeWidth(3);
        board.getChildren().add(centre);
    }



    //display(done by Ciliang Ma u6803148)
    public void display(int startPointX, int startPointY, boolean single){
        //display special tiles
        for (int i = 0; i < 6; i ++) {
            specialTiles[i].setFitHeight(TILE_SIDE);
            specialTiles[i].setFitWidth(TILE_SIDE);
            specialTiles[i].setX(240 + 90*i);
            specialTiles[i].setY(10);
            board.getChildren().add(specialTiles[i]);
        }

        //drag special tiles onto the board
        for (ImageView t : specialTiles){
            drag(t, startPointX, startPointY, single);
        }

        Text textSpecial = new Text("Special Tiles:\nOnly three per game");
        textSpecial.setX(30);
        textSpecial.setY(40);
        textSpecial.setFont(Font.font(DEFAULT_FONT, FontWeight.BOLD, FontPosture.REGULAR, 20));
        board.getChildren().add(textSpecial);

        //creat buttons that can rotate special tiles
        for(int i = 0; i < 6; i ++){
            Button special = new Button();
            roundButton(special);
            int temp = i;
            EventHandler<MouseEvent> rotate = mouseEvent -> rotateSpecial(specialTiles[temp],temp);
            special.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,rotate);
            special.setLayoutX(227 + TILE_SIDE/2 + i * 90);
            special.setLayoutY(85);
            special.setPrefWidth(TILE_SIDE);
            buttons.getChildren().add(special);
        }

        //creat four blocks that display 4 normal tiles each round
        for (int i = 0; i < 4; i++){
            Rectangle temp = new Rectangle(TILE_SIDE, TILE_SIDE);
            if(single){
                temp.setX(SINGLE_X + 7*TILE_SIDE + 90);
                temp.setY(SINGLE_Y + i*TILE_SIDE + 150);
            } else{
                temp.setX(VIEWER_WIDTH/2 + (i-2)*TILE_SIDE);
                temp.setY(SINGLE_Y + 7*TILE_SIDE + 40);
            }
            temp.setFill(Color.TRANSPARENT);
            temp.setStroke(Color.BLACK);
            board.getChildren().add(temp);
        }
    }


    //drag tiles onto the board and get update board string(done by Ciliang Ma u6803148)
    public void drag(ImageView imageView, int startPointX, int startPointY, boolean single){
        imageView.setPickOnBounds(true);
        imageView.setOnMouseDragged(e -> {
            imageView.setX(e.getSceneX() - (double) TILE_SIDE/2);
            imageView.setY(e.getSceneY() - (double) TILE_SIDE/2);


            if(e.getSceneX() > startPointX && e.getSceneX() < startPointX + 7*TILE_SIDE
                    && e.getSceneY() > startPointY && e.getSceneY() < startPointY + 7*TILE_SIDE){
                char c = (char) ('A' + (e.getSceneY() - startPointY)/TILE_SIDE);
                int r = (int) ((e.getSceneX() - startPointX)/TILE_SIDE);
                findNearest(c, r);
                highlight(imageView, c, r);
            }
        });


        imageView.setOnMouseReleased(e -> {
            char r = (char) ('A' + (e.getSceneY() - startPointY)/TILE_SIDE);
            int c = (int) ((e.getSceneX() - startPointX)/TILE_SIDE);

            //Drag normal tiles
            for (int i = 0; i < 4; i ++){
                if (imageView.equals(dicesTiles[i])){
                    String tileString = tilePlacementString(i) + r + c + rotateCount[i];
                    //Outside the board
                    if (e.getSceneX() < startPointX || e.getSceneX() > startPointX + 7*TILE_SIDE
                            || e.getSceneY() < startPointY || e.getSceneY() > startPointY + 7*TILE_SIDE){
                        normalHelper2(single, imageView, i, startPointX, startPointY);
                    }
                    //Invalid placement
                    else if (!b.addValidTile(tileString)){
                        normalHelper2(single, imageView, i, startPointX, startPointY);
                    }
                    else {
                        fixedX = e.getSceneX() - (e.getSceneX() - startPointX) % TILE_SIDE;
                        fixedY = e.getSceneY() - (e.getSceneY() - startPointY) % TILE_SIDE;
                        fixTiles(imageView, i);
                        rotateCount[i] = 0;
                    }
                }
                highlighted.setFill(Color.TRANSPARENT);
            }

            //Drag special tiles
            for (int i = 0; i < 6; i ++){
                if (imageView.equals(specialTiles[i])){
                    String tileString = specialTilePlacementString(i)+ r + c + specialRotateCount[i];
                    //Outside the board
                    if (e.getSceneX() < startPointX || e.getSceneX() > startPointX + 7 * TILE_SIDE
                            || e.getSceneY() < startPointY || e.getSceneY() > startPointY + 7 * TILE_SIDE){
                        imageView.setX(240 + 90*i);
                        imageView.setY(10);
                    }
                    // >=3 special tiles
                    else if (numOfSpecialTiles >= 3){
                        imageView.setX(240 + 90*i);
                        imageView.setY(10);
                        //Display an error window
                        smallWindow("You can only place at most three special tiles per game!");
                    }
                    //invalid placement
                    else if (!b.addValidTile(tileString)){
                        imageView.setX(240 + 90*i);
                        imageView.setY(5);
                    }
                    //valid placement
                    else {
                        fixedX = e.getSceneX() - (e.getSceneX() - startPointX) % TILE_SIDE;
                        fixedY = e.getSceneY() - (e.getSceneY() - startPointY) % TILE_SIDE;
                        fixTiles(imageView,i);
                        imageView.setX(240 + 90*i);
                        imageView.setY(10);
                        numOfSpecialTiles ++;
                    }
                }
            }
        });
    }


    //create rotate buttons(done by Ciliang Ma u6803148)
    public void rotateButtons(int startPointX, int startPointY, boolean single, boolean simple){
        //create a button that can generate 4 normal tiles each round
        Button tossDices = new Button("Toss Dices");
        if(single){
            tossDices.setLayoutX(SINGLE_X + 7*TILE_SIDE + 83);
            tossDices.setLayoutY(SINGLE_Y + 7*TILE_SIDE - 40);
        } else{
            tossDices.setLayoutX(MULTI_X1 + 4*TILE_SIDE);
            tossDices.setLayoutY(MULTI_Y + 8*TILE_SIDE);
        }

        buttons.getChildren().add(tossDices);

        EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean allused = true;
                if(numOfRound <= 8 && numOfRound > 0){
                    allused = allUsed(single);
                }
                if(numOfRound < 8){
                    if(!allused){
                        smallWindow("You haven't placed all valid tiles!");
                    } else {
                        clearUnusedTiles(single);
                        if(!single){
                            putAI(simple, MULTI_X2, MULTI_Y);
                        }

                        //Update
                        for (int i = 0; i < 4; i++) {
                            rotateCount[i] = 0;
                        }
                        if(numOfRound > 0){
                            scoreUpdate(single);
                        }

                        //New Round
                        dices = RailroadInk.generateDiceRoll();
                        numOfRound ++;

                        for (int i = 0; i < 4; i++) {
                            Image tile = new Image(getClass().getResourceAsStream(URI_BASE + dices.substring(2 * i, 2 * (i + 1)) + ".png"));
                            dicesTiles[i] = new ImageView(tile);
                            if(single){
                                dicesTiles[i].setX(SINGLE_X + 7 * TILE_SIDE + 90);
                                dicesTiles[i].setY(SINGLE_Y + i * TILE_SIDE + 150);
                            } else {
                                dicesTiles[i].setX(VIEWER_WIDTH / 2 + (i - 2) * TILE_SIDE);
                                dicesTiles[i].setY(MULTI_Y + 7 * TILE_SIDE + 40);
                            }
                            dicesTiles[i].setFitWidth(TILE_SIDE);
                            dicesTiles[i].setFitHeight(TILE_SIDE);
                            board.getChildren().add(dicesTiles[i]);
                            tempTiles[i] = dices.substring(2 * i, 2 * (i + 1));
                            drag(dicesTiles[i], startPointX, startPointY, single);
                        }
                    }
                } else{
                    if(!allused){
                        smallWindow("You haven't placed all valid tiles!");
                    } else{
                        if(!single){
                            putAI(simple, MULTI_X2, MULTI_Y);
                        }
                        scoreUpdate(single);
                        gameOver(single);
                    }
                }
            }
        };
        tossDices.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, eventHandler);


        //creat 4 buttons that can rotate normal tiles
        for(int i = 0; i < 4; i ++){
            Button button = new Button();
            roundButton(button);
            int temp = i;
            EventHandler<MouseEvent> rotate = mouseEvent -> rotate(dicesTiles[temp],temp);
            button.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,rotate);
            if(single){
                button.setLayoutX(SINGLE_X + 8 * TILE_SIDE + 100);
                button.setLayoutY(SINGLE_Y + i * TILE_SIDE + 175);
            } else{
                button.setLayoutX(VIEWER_WIDTH / 2 + (i - 2) * TILE_SIDE + 17.5);
                button.setLayoutY(MULTI_Y + 7 * TILE_SIDE + 55 + TILE_SIDE);
            }
            buttons.getChildren().add(button);
        }
    }


    //replace the legal tile by a fixed one(done by Ciliang Ma u6803148)
    public void fixTiles(ImageView imageView, int i){
        if (imageView.equals(specialTiles[i])) {
            Image tile = new Image(getClass().getResourceAsStream(URI_BASE + specialTileName[i] + ".png"));
            ImageView fixedImageView = new ImageView(tile);
            fixSpecialHelper(fixedImageView, i);
        }
        else if (imageView.equals(dicesTiles[i])){
            Image tile = new Image(getClass().getResourceAsStream(URI_BASE + dices.substring(2*i,2*(i+1)) + ".png"));
            ImageView fixedImageView = new ImageView(tile);
            fixNormalHelper(fixedImageView, i);
        }
    }


    //clear unused tiles when tossing dices(done by Ciliang Ma u6803148)
    public void clearUnusedTiles(boolean single){
        for(int i = 0; i < 4; i ++){
            if(dicesTiles[i] == null){
                return;
            }
        }

        if(single){
            for(int i = 0; i < 4; i ++){
                if (dicesTiles[i].getX() == SINGLE_X + 7*TILE_SIDE + 90){
                    board.getChildren().remove(dicesTiles[i]);
                }
            }
        } else{
            for(int i = 0; i < 4; i ++){
                if (dicesTiles[i].getY() == MULTI_Y + 7*TILE_SIDE + 40){
                    board.getChildren().remove(dicesTiles[i]);
                }
            }
        }
    }


    //Update AI board (Jingsheng Deng (u6847863))
    public void putAI(boolean simple, int startPointX, int startPointY){
        String placementAI;
        if(numOfRound > 0){
            if(simple){
                placementAI = ai.movingOptions(dices, true, false).get(0);
            } else{
                ArrayList<String> movingOptions = ai.movingOptions(dices, false, true);
                placementAI = ai.placement(movingOptions, numOfRound);
            }
            int i = 0;
            while(i < placementAI.length()){
                String tileStr = placementAI.substring(i, i+5);
                ai.addValidTile(tileStr);
                i += 5;
            }
            makePlacement(ai.boardString, startPointX, startPointY);
        }
    }


//Helper functions to manipulate tiles

    //if all tiles that are valid have been placed for this round (Junming Zhao u6633756)
    public boolean allUsed(boolean single){
        Board temp = new Board(b.boardString);
        Set<String> placeable = temp.placeable();

        //haven't used all placeable tiles
        for(int i = 0; i < 4; i ++){
            //check each unused tile
            if(single){
                if(dicesTiles[i].getX() == SINGLE_X + 7 * TILE_SIDE + 90){
                    for(String coordinates : placeable){
                        for(int j = 0; j < 8; j ++){
                            String tileStr = dices.substring(i, i + 2) + coordinates + j;
                            if(temp.addValidTile(tileStr)){
                                return false;
                            }
                        }
                    }
                }
            }
            else {
                if(dicesTiles[i].getY() == MULTI_Y + 7 * TILE_SIDE + 40){
                    for(String coordinates : placeable){
                        for(int j = 0; j < 8; j ++){
                            String tileStr = dices.substring(i, i + 2) + coordinates + j;
                            if(temp.addValidTile(tileStr)){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    //rotate the tile and get the orientation(done by Ciliang Ma u6803148)
    public void rotate(ImageView imageView,int i){
        imageView.setRotate((imageView.getRotate() + 90) % 360);
        rotateCount[i] ++;
        if (rotateCount[i] == 4){
            dicesTiles[i].setScaleX(-1);
        }
        if (rotateCount[i] == 8) {
            dicesTiles[i].setScaleX(1);
            rotateCount[i] = 0;
        }
    }

    //rotate and mirror special tiles(done by Ciliang Ma u6803148)
    public void rotateSpecial(ImageView imageView,int i){
        imageView.setRotate((imageView.getRotate() + 90) % 360);
        specialRotateCount[i] ++;
        if (specialRotateCount[i] == 4){
            imageView.setScaleX(-1);
        }
        if (specialRotateCount[i] == 8) {
            imageView.setScaleX(1);
            specialRotateCount[i] = 0;
        }
    }

    //fix special tile helper(done by Ciliang Ma u6803148)
    public void fixSpecialHelper(ImageView imageView, int i){
        imageView.setX(fixedX);
        imageView.setY(fixedY);
        setTileLength(imageView);
        if (specialRotateCount[i] < 4) {
            imageView.setRotate(specialTiles[i].getRotate());
        } else {
            imageView.setScaleX(-1);
            imageView.setRotate(specialTiles[i].getRotate());
        }
        board.getChildren().add(imageView);
    }

    //fix normal tile helper(done by Ciliang Ma u6803148)
    public void fixNormalHelper(ImageView imageView, int i){
        imageView.setX(fixedX);
        imageView.setY(fixedY);
        setTileLength(imageView);
        if (rotateCount[i]<4){
            imageView.setRotate((90 * rotateCount[i]) % 360);
        }else {
            imageView.setScaleX(-1);
            imageView.setRotate((90 * (rotateCount[i]-4)) % 360);
        }
        board.getChildren().remove(dicesTiles[i]);
        board.getChildren().add(imageView);
    }

    public void normalHelper2(boolean single, ImageView imageView, int i, int startPointX, int startPointY){
        if(single){
            imageView.setX(startPointX + 7*TILE_SIDE + 90);
            imageView.setY(startPointY + i*TILE_SIDE + 150);
        } else{
            imageView.setX(VIEWER_WIDTH/2 + (i-2)*TILE_SIDE);
            imageView.setY(startPointY + 7*TILE_SIDE + 40);
        }
    }

    //set length and width of tiles(done by Ciliang Ma u6803148)
    private void setTileLength(ImageView imageView){
        imageView.setFitHeight(TILE_SIDE);
        imageView.setFitWidth(TILE_SIDE);
    }

    //generate tile placement string(done by Ciliang Ma u6803148)
    private String tilePlacementString(int i){
        String tilePlacement = tempTiles[i];
        return tilePlacement;
    }


    //generate special placement string(done by Ciliang Ma u6803148)
    private String specialTilePlacementString(int i){
        String specialPlacement = specialTileName[i];
        return specialPlacement;
    }


    //Helper function of make placement (Junming Zhao u6633756)
    private ImageView getImage(String placement, int startPointX, int startPointY) {
        //Initialize image, tile
        String imgName = placement.substring(0, 2);
        int orientation = Integer.parseInt(placement.substring(4));
        Image img = new Image(getClass().getResourceAsStream(URI_BASE + imgName + ".png"));

        //Set ImageView for the tile, set size
        ImageView tileImg = new ImageView(img);
        tileImg.setFitHeight(TILE_SIDE);
        tileImg.setFitWidth(TILE_SIDE);

        //Rotate
        if(orientation > 0 && orientation <4){
            tileImg.setRotate(90 * orientation);
        }

        //Mirror
        else if(orientation == 4){
            tileImg.setScaleX(-1);
        }

        //Mirror and rotate
        else if(orientation > 4 && orientation < 8){
            tileImg.setScaleX(-1);
            tileImg.setRotate(90 * (orientation - 4));
        }

        //Translate image
        int row = Tile.correspondingRowToNum(placement.charAt(2));
        int col = Character.digit(placement.charAt(3),10);
        tileImg.setTranslateX(TILE_SIDE*col + startPointX);
        tileImg.setTranslateY(TILE_SIDE*row + startPointY);

        return tileImg;
    }


    //make placement according to string (Junming Zhao u6633756)
    void makePlacement(String placement, int startPointX, int startPointY) {
        //Clear all previously drawn one
        tiles.getChildren().clear();

        //Separate board placements
        int i = 0;
        while(i < placement.length()){
            String tile = placement.substring(i, i + 5);
            //Draw this tile
            tiles.getChildren().add(getImage(tile, startPointX, startPointY));
            i += 5;
        }
        // FIXME Task 4: implement the simple placement viewer
    }



//Helper functions of scenes

    //Find nearest grid (Junming Zhao u6633756)
    private void findNearest(char c, int r){
        int col = Tile.correspondingRowToNum(c);
        highlighted = rectangles[r][col];
    }


    //highlight helper (Junming Zhao u6633756)
    private void setHighlighted(boolean right, Rectangle rectangle){
        if(right){
            rectangle.setFill(CORRECT_COLOR);
        } else{
            rectangle.setFill(WRONG_COLOR);
        }
    }


    //do highlight (Junming Zhao u6633756)
    private void highlight(ImageView imageView, char c, int r){
        //For normal tiles
        for(int index = 0; index < 4; index ++){
            if(imageView.equals(dicesTiles[index])){
                for(Rectangle[] rs : rectangles){
                    for(Rectangle rectangle : rs){
                        if(rectangle.equals(highlighted)){
                            Board temp = new Board(b.boardString);
                            String str =  tilePlacementString(index) + c + r + rotateCount[index];
                            boolean right = temp.addValidTile(str);
                            setHighlighted(right, rectangle);
                        } else{
                            rectangle.setFill(Color.TRANSPARENT);
                        }
                    }
                }
            }
        }
        //For special tiles
        for(int i = 0; i < 6; i ++){
            if(imageView.equals(specialTiles[i])){
                for(Rectangle[] rs : rectangles){
                    for(Rectangle rectangle : rs){
                        if(rectangle.equals(highlighted)){
                            if(numOfSpecialTiles >= 3){
                                rectangle.setFill(WRONG_COLOR);
                            } else{
                                Board temp = new Board(b.boardString);
                                String str =  specialTilePlacementString(i) + c + r + specialRotateCount[i];
                                boolean right = temp.addValidTile(str);
                                setHighlighted(right, rectangle);
                            }
                        } else{
                            rectangle.setFill(Color.TRANSPARENT);
                        }
                    }
                }
            }
        }
    }


    //error window (Junming Zhao u6633756)
    private void smallWindow(String message){
        Stage window = new Stage();
        window.setTitle("Error!");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        Text text = new Text(message);
        text.setFont(Font.font(DEFAULT_FONT, FontWeight.NORMAL, FontPosture.REGULAR, 17));
        VBox layout = new VBox(10);
        layout.getChildren().add(text);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


    //end the game(done by Ciliang Ma u6803148)
    private void gameOver(boolean single) {
        if (numOfRound == 8){
            Stage gm = new Stage();
            gm.setTitle("Game Over");
            gm.initModality(Modality.APPLICATION_MODAL);

            gm.setMinHeight(300);
            gm.setMinWidth(800);
            gm.setMaxHeight(300);
            gm.setMaxWidth(800);

            Button button = new Button("Close the window");
            button.setOnAction(e -> gm.close());

            VBox layout = new VBox(10);
            Text textBasic = new Text();
            Text textAdvanced = new Text();
            Text gameOver = new Text("Game Over! :)");

            if(single){
                textBasic.setText("Basic Scores: " + RailroadInk.getBasicScore(b.boardString));
                textAdvanced.setText("Advanced Scores: " + RailroadInk.getAdvancedScore(b.boardString));
            } else{
                textBasic.setText("Basic: " + RailroadInk.getBasicScore(b.boardString) + " VS " +
                        RailroadInk.getBasicScore(ai.boardString));
                textAdvanced.setText("Advanced : " + RailroadInk.getAdvancedScore(b.boardString) + " VS " +
                        RailroadInk.getAdvancedScore(ai.boardString));
            }

            textBasic.setX(200);
            textBasic.setY(200);
            textBasic.setFont(Font.font(RULE_FONT, FontWeight.BOLD, FontPosture.REGULAR, 50));
            textAdvanced.setX(200);
            textAdvanced.setY(300);
            textAdvanced.setFont(Font.font(RULE_FONT, FontWeight.BOLD, FontPosture.REGULAR, 50));
            gameOver.setX(300);
            gameOver.setY(400);
            gameOver.setFont(Font.font(START_SCENE_FONT, FontWeight.BOLD, FontPosture.REGULAR, 60));
            gameOver.setFill(Color.rgb(80, 80, 80));
            gameOver.setStroke(Color.rgb(51, 51, 51));

            layout.getChildren().addAll(button, textBasic, textAdvanced, gameOver);

            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            gm.setScene(scene);
            gm.showAndWait();
        }
    }


    //Helper function to set up rotate buttons
    private void roundButton(Button button){
        button.setShape(new Circle(1));
        int BUTTON_RADIUS = 25;
        button.setMinWidth(BUTTON_RADIUS);
        button.setMinHeight(BUTTON_RADIUS);
        button.setMaxWidth(BUTTON_RADIUS);
        button.setMaxHeight(BUTTON_RADIUS);
    }


    //set initial scores (Junming Zhao u6633756)
    private void setScoreTitle(boolean single){
        basic.setFont(Font.font(DEFAULT_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        advanced.setFont(Font.font(DEFAULT_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        round.setFont(Font.font(DEFAULT_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        basicScore.setFont(Font.font(RULE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        advancedScore.setFont(Font.font(RULE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        numrounds.setFont(Font.font(RULE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        AIbasic.setFont(Font.font(DEFAULT_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        AIadvanced.setFont(Font.font(DEFAULT_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        AIbasicScore.setFont(Font.font(RULE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        AIadvancedScore.setFont(Font.font(RULE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
        if(single){
            basic.setX(30);
            basic.setY(SINGLE_Y + TILE_SIDE);
            advanced.setX(30);
            advanced.setY(SINGLE_Y + 3*TILE_SIDE);
            round.setX(30);
            round.setY(SINGLE_Y + 5*TILE_SIDE);

            basicScore.setX(35);
            basicScore.setY(SINGLE_Y + TILE_SIDE + 30);
            advancedScore.setX(35);
            advancedScore.setY(SINGLE_Y + 3*TILE_SIDE + 30);
            numrounds.setX(35);
            numrounds.setY(SINGLE_Y + 5*TILE_SIDE + 30);
        } else{
            basic.setX(80);
            basic.setY(MULTI_Y + 8 * TILE_SIDE);
            advanced.setX(80);
            advanced.setY(MULTI_Y + 9.1 * TILE_SIDE);
            round.setX(850);
            round.setY(MULTI_Y + 8 * TILE_SIDE);

            basicScore.setX(85);
            basicScore.setY(MULTI_Y + 8 * TILE_SIDE + 30);
            advancedScore.setX(85);
            advancedScore.setY(MULTI_Y + 9.1 * TILE_SIDE + 30);
            numrounds.setX(855);
            numrounds.setY(MULTI_Y + 8 * TILE_SIDE + 30);

            AIbasic.setX(650);
            AIbasic.setY(MULTI_Y + 8 * TILE_SIDE);
            AIadvanced.setX(650);
            AIadvanced.setY(MULTI_Y + 9.1 * TILE_SIDE);
            AIbasicScore.setX(655);
            AIbasicScore.setY(MULTI_Y + 8 * TILE_SIDE + 30);
            AIadvancedScore.setX(655);
            AIadvancedScore.setY(MULTI_Y + 9.1 * TILE_SIDE + 30);
        }

        board.getChildren().addAll(basic, advanced, round);
        scores.getChildren().addAll(basicScore, advancedScore, numrounds);
        if(!single){
            board.getChildren().addAll(AIbasic,  AIadvanced);
            scores.getChildren().addAll(AIbasicScore, AIadvancedScore);
        }
        board.getChildren().add(scores);
    }


    //Update scores (Junming Zhao u6633756)
    private void scoreUpdate(boolean single){
        //clear before
        scores.getChildren().clear();

        //display scores
        basicScore.setText("" + RailroadInk.getBasicScore(b.boardString));
        advancedScore.setText("" + RailroadInk.getAdvancedScore(b.boardString));
        if(numOfRound >= 8){
            numrounds.setText("7");
        } else{
            numrounds.setText("" + numOfRound);
        }
        scores.getChildren().addAll(basicScore, advancedScore, numrounds);

        if(!single){
            AIbasicScore.setText("" + RailroadInk.getBasicScore(ai.boardString));
            AIadvancedScore.setText("" + RailroadInk.getAdvancedScore(ai.boardString));
            scores.getChildren().addAll(AIbasicScore, AIadvancedScore);
        }
    }



//Start scene set ups (Junming Zhao u6633756)

    //Quit button
    private Button quit = new Button("QUIT");

    //Clear all data when quit
    public void clearAllData(){
        root.getChildren().clear();
        controls.getChildren().clear();
        board.getChildren().clear();
        tiles.getChildren().clear();
        buttons.getChildren().clear();
        scores.getChildren().clear();
        b = new Board();
        ai = new Board();

        numOfRound = 0;
        numOfSpecialTiles = 0;
        rotateCount = new int[]{0,0,0,0};
        specialRotateCount = new int[]{0,0,0,0,0,0};
        dicesTiles = new ImageView[4];
        tempTiles = new String[4];
        rectangles = new Rectangle[7][7];

        basicScore.setText("0");
        advancedScore.setText("0");
        numrounds.setText("0");
        AIbasicScore.setText("0");
        AIadvancedScore.setText("0");
        highlighted = null;
    }

    //Event Handler of quit
    EventHandler<MouseEvent> quitGame = mouseEvent -> {
        clearAllData();
        setStartScene();
    };
    public void setQuit(){
        //set basic features
        quit.setLayoutX(VIEWER_WIDTH - 100);
        quit.setLayoutY(30);
        quit.setMaxHeight(30);
        quit.setMinHeight(30);
        quit.setMaxWidth(60);
        quit.setMaxWidth(60);
        quit.setFont(Font.font(DEFAULT_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
        quit.setStyle("-fx-background-color: #EDEDED; -fx-border-color: #5E5E5F; -fx-border-width: 2px;");

        //events
        quit.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> quit.setEffect(shadow));
        quit.addEventHandler(MouseEvent.MOUSE_EXITED, e -> quit.setEffect(null));

        quit.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, quitGame);
    }


    //Events of the start buttons

    //single player
    private Button singlePlayer = new Button("Single Player");
    private EventHandler<MouseEvent> start1 = mouseEvent -> {
        //clear the previous scene and go to a new scene
        root.getChildren().clear();

        root.getChildren().add(BACKGROUND);
        TILE_SIDE = 70;
        getBoard(SINGLE_X, SINGLE_Y, BOARD_COLOR1, true);
        getSpecialTiles();
        setScoreTitle(true);

        root.getChildren().add(board);
        root.getChildren().add(tiles);
        root.getChildren().add(buttons);

        display(SINGLE_X, SINGLE_Y, true);
        rotateButtons(SINGLE_X, SINGLE_Y, true, true);
        root.getChildren().add(quit);
    };


    //Multi player event
    private Button simpleAI = new Button("Simple AI");
    private EventHandler<MouseEvent> start2 = mouseEvent -> {
        //clear the previous scene and go to a new scene
        root.getChildren().clear();

        root.getChildren().add(BACKGROUND);
        TILE_SIDE = 60;
        getBoard(MULTI_X1, MULTI_Y, BOARD_COLOR1, true);
        getBoard(MULTI_X2, MULTI_Y, BOARD_COLOR2, false);
        getSpecialTiles();
        setScoreTitle(false);

        root.getChildren().add(board);
        root.getChildren().add(tiles);
        root.getChildren().add(buttons);

        display(MULTI_X1,MULTI_Y, false);
        rotateButtons(MULTI_X1, MULTI_Y, false, true);
        root.getChildren().add(quit);
    };


    private Button hardAI = new Button("Hard AI");
    private EventHandler<MouseEvent> start3 = mouseEvent -> {
        //clear the previous scene and go to a new scene
        root.getChildren().clear();

        root.getChildren().add(BACKGROUND);
        TILE_SIDE = 60;
        getBoard(MULTI_X1, MULTI_Y, BOARD_COLOR1, true);
        getBoard(MULTI_X2, MULTI_Y, BOARD_COLOR2, false);
        getSpecialTiles();
        setScoreTitle(false);

        root.getChildren().add(board);
        root.getChildren().add(tiles);
        root.getChildren().add(buttons);

        display(MULTI_X1,MULTI_Y, false);
        rotateButtons(MULTI_X1, MULTI_Y, false, false);
        root.getChildren().add(quit);
    };


    //Rules
    private Button rules = new Button("Game Rules");
    private EventHandler<MouseEvent> rule = mouseEvent -> {
        //clear the previous scene and go to a new scene
        root.getChildren().clear();
        root.getChildren().add(BACKGROUND);

        Text title1 = new Text("*Game Rules");
        Text ruleText = new Text(
                "1. Each game is played over seven rounds;\n" +
                "2. Each round, the four tile dice are rolled to determine the tiles that may be placed for that round;\n" +
                "3. Each round the player may choose to place one of six special tiles, with a maximum of three special tiles per game;\n" +
                "4. Click round button to change orientations of tiles;\n" +
                "5. All four tiles must be placed, unless doing so would result in an illegal placement;\n" +
                "6. After all placement, click toss dice to get into next round;\n" +
                "7. The game ends at the end of the 7th round, and scoring determines the winner.");

        Text title2 = new Text("*Legal Placement");
        Text legalPlacements = new Text(
                "1. A tile must be placed such that at least one edge connects to either an exit or a pre-existing route.\n" +
                "2. Tiles may not be placed such that a highway edge connects to a railway edge;\n" +
                "3. A tile may have one or more edges touching a blank edge of another tile;\n" +
                "4. Routes may not cross over, with the exception of the overpass tile.");

        Text title3 = new Text("*Scoring");
        Text scoring = new Text(
                "1. Each player scores points based on how many exits each of their routes are connected to;\n" +
                "2. -1 point for each 'Error' -- non-connecting edges.\n" +
                "3. +1 point for each of the squares of their centre grid that are covered;\n" +
                "4. The longest Highway gets one bonus point per square the highway covers;\n" +
                "5. The longest Railway gets one bonus point per square the railway covers.\n");

        title1.setFont(Font.font(TITLE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
        title1.setX(100);
        title1.setY(100);
        title2.setFont(Font.font(TITLE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
        title2.setX(100);
        title2.setY(330);
        title3.setFont(Font.font(TITLE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
        title3.setX(100);
        title3.setY(500);

        ruleText.setFont(Font.font(RULE_FONT, FontWeight.BOLD, FontPosture.REGULAR, 15));
        ruleText.setX(100);
        ruleText.setY(140);
        legalPlacements.setFont(Font.font(RULE_FONT, FontWeight.BOLD, FontPosture.REGULAR, 15));
        legalPlacements.setX(100);
        legalPlacements.setY(370);
        scoring.setFont(Font.font(RULE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
        scoring.setX(100);
        scoring.setY(540);

        root.getChildren().addAll(title1, title2, title3, ruleText, legalPlacements, scoring);
        root.getChildren().add(quit);
    };


    //Make placement scene
    private Button makePlacement = new Button("Make Placement");
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(e -> {
            makePlacement(textField.getText(), SINGLE_X, 100);
            textField.clear();
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(275);
        hb.setLayoutY(VIEWER_HEIGHT - 100);
        controls.getChildren().add(hb);

    }
    private EventHandler<MouseEvent> placement = mouseEvent -> {
        //clear the previous scene and go to a new scene
        root.getChildren().clear();
        root.getChildren().add(BACKGROUND);

        TILE_SIDE = 70;
        getBoard(SINGLE_X, 100,  BOARD_COLOR1, false);
        root.getChildren().add(board);
        root.getChildren().add(tiles);
        root.getChildren().add(controls);
        makeControls();
        setQuit();
        root.getChildren().add(quit);
    };


    //Titles of the start scene
    private Text startTitles = new Text("RailroadInk");
    private Text subTitles = new Text("click to start");
    public void setTitles(){
        startTitles.setFont(Font.font(TITLE_FONT, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 90));
        subTitles.setFont(Font.font(DEFAULT_FONT, FontWeight.NORMAL, FontPosture.REGULAR, 40));
        startTitles.setX(VIEWER_WIDTH / 2 - 300);
        subTitles.setX(VIEWER_WIDTH / 2 - 120);
        startTitles.setY(150);
        subTitles.setY(250);
    }


    //Set buttons of the start scene
    private Button[] startButtons = {singlePlayer, simpleAI, hardAI, rules, makePlacement};
    public void setStartButtons(){
        for(int i = 0; i < startButtons.length; i ++){
            Button button = startButtons[i];
            int BUTTON_WIDTH = 400;
            int BUTTON_HEIGHT = 30;
            button.setPrefWidth(BUTTON_WIDTH);
            button.setPrefHeight(BUTTON_HEIGHT);
            button.setLayoutX(VIEWER_WIDTH / 2  - BUTTON_WIDTH / 2);
            button.setLayoutY(300 + i*80);

            button.setFont(Font.font(START_SCENE_FONT, FontWeight.BOLD, FontPosture.REGULAR, 30));
            button.setStyle("-fx-background-color: #CBCBCB; -fx-border-color: #1E1E1E; -fx-border-width: 2px;");

            button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> button.setEffect(shadow));
            button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> button.setEffect(null));
        }

        //button events set up
        singlePlayer.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, start1);
        simpleAI.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, start2);
        hardAI.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, start3);
        makePlacement.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, placement);
        rules.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, rule);
    }


    //display the start scene
    public void setStartScene(){
        root.getChildren().add(BACKGROUND);
        root.getChildren().add(startTitles);
        root.getChildren().add(subTitles);
        for(Button button : startButtons){
            root.getChildren().add(button);
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("RailroadInk Game Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        setTitles();
        setStartButtons();
        setQuit();
        setStartScene();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}