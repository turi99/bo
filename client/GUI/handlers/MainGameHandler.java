package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import it.polimi.ingsw.client.GUI.ViewGUI;
import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionsNoSuchTowers;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.ModelView;
import it.polimi.ingsw.model.ModelViewPro;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Main Game
 */
public class MainGameHandler implements SceneHandler {
    private final String imagePath = "/Images/";
    protected final int NUM_STUD_PER_LANE = 10;
    protected final int NUM_OF_LANES = 5;
    protected final int TOT_TOWERS_IN_BOARD = 8;
    protected final int TOT_STARTING_ISLANDS = 12;

    protected ClientGUI clientGUI;
    private ModelView model;
    private ViewGUI viewGui;

    protected ImageView currentImage = null;
    private ImageView usedImage = null;
    private ObservableList<Node> playerCards;
    private int numOfPlayers;
    private int studentsPerCloud;
    private boolean islandIsUp = false;
    private boolean setUsedCardImage = true;
    protected boolean showBoardHint = true;

    private int[] towers = {0,0,0,0,0,0,0,0,0,0,0,0};

    @FXML
    private Pane islands;
    @FXML
    private Pane clouds;
    @FXML
    protected Pane board0;
    @FXML
    private Pane board1;
    @FXML
    private Pane board2;
    @FXML
    private Pane board3;

    @FXML
    private Button useCardButton;
    @FXML
    private HBox playerCardsContainer;
    @FXML
    protected Text errorMessage;
    @FXML
    private Text currentPlayer;
    @FXML
    private Text currentStatus;
    @FXML
    protected Text currentStatusInfo;
    @FXML
    private Text turnInfoText;
    @FXML
    private Text playerID;
    @FXML
    private Text textInfoMovements;
    @FXML
    private Text maxMovements;
    @FXML
    protected Label boardHint;

    public void setPlayerCards(ObservableList<Node> passedCards) {
        playerCards = passedCards;
    }

    @Override
    public void setClientGui(ClientGUI c) {
        clientGUI = c;
    }

    @Override
    public void setParameters(SceneHandler v) {
    }

    /**
     * Shows and hide corrects island
     * @param islandIterator current pos of island
     * @param skipIsland island required to skip
     * @param numOfIsland total number of aggregation of islands
     * @return
     */
    private boolean checkIslandPrintValidity(int islandIterator, int skipIsland, int numOfIsland) {
        if(numOfIsland >= (TOT_STARTING_ISLANDS*1.0/2))
            return islandIterator!=skipIsland;
        else
            return islandIterator==skipIsland;
    }

    /**
     * Prints other players boards and show the right amount of boards
     */
    public void printOtherPlayersInfo() {
        ArrayList<Pane> boards = new ArrayList<>();
        int numOfPlayers = clientGUI.getNum();
        if(numOfPlayers == 2)
            boards.add(board2);
        else if (numOfPlayers == 3) {
            boards.add(board1);
            boards.add(board3);
            islands.setTranslateY(-180);
        } else {
            boards.add(board1);
            boards.add(board2);
            boards.add(board3);
        }

        int i = 0;
        for (Player p : model.getOtherPlayers()) {
            boards.get(i).setVisible(true);
            printBoardInfo(p, boards.get(i));
            if(model.getState().getName().equals("playCard")) setUsedCardImage = true;
            if(setUsedCardImage)
                printPlayedCards(p, boards.get(i));
            i++;
        }
    }

    /**
     * Print the card played by each player
     */
    private void printPlayedCards(Player p, Pane board) {
        boolean foundImage = false;
        for (PlayedCard c : model.getPlayedCard()) {
            if(c.getPlayer().getID() == p.getID()){
                if(c.getCard() != null && c.getCard().getVal() != 0) {
                    ((ImageView) ((Pane) board.getChildren().get(1)).getChildren().get(1)).setImage(new Image(imagePath + "Cards/Assistente" + c.getCard().getVal() + ".png"));
                    ((Pane) board.getChildren().get(1)).getChildren().get(1).setVisible(true);
                    foundImage = true;
                }
            }
        }
        if(!foundImage)
            ((Pane) board.getChildren().get(1)).getChildren().get(1).setVisible(false);
        if(!model.getState().getName().equals("playCard"))
            setUsedCardImage = false;
    }

    /**
     * Print the board of the passed player
     *
     * @param player the player in the ModelView
     * @param board the board on which print the player
     */
    private void printBoardInfo(Player player, Pane board) {
        //set PlayerBoard

        //set name and team color
        switch (player.getTeam().getTeamColor()) {
            case BLACK -> ((Circle)((Pane) board.getChildren().get(2)).getChildren().get(1)).setFill(Color.BLACK);
            case WHITE -> ((Circle)((Pane) board.getChildren().get(2)).getChildren().get(1)).setFill(Color.WHITE);
            case GREY -> ((Circle)((Pane) board.getChildren().get(2)).getChildren().get(1)).setFill(Color.GREY);
        }
        ((TextField)((Pane) board.getChildren().get(2)).getChildren().get(0)).setText((player.getUserName()));

        //set students on board
        int studPos = 0;
        for(Student s : player.getOwnBoard().getStudents()) {
            String color = s.getColor().name().toLowerCase(Locale.ROOT);
            ((ImageView)((GridPane)((Pane) board.getChildren().get(0)).getChildren().get(4)).getChildren().get(studPos)).setImage(new Image(imagePath+"Pawns/Students/"+color+"Student.png"));
            studPos++;
        }
        while(studPos < 9){
            ((ImageView)((GridPane)((Pane) board.getChildren().get(0)).getChildren().get(4)).getChildren().get(studPos)).setImage(null);
            studPos++;
        }

        //set Towers
        int towerPos = 0;
        for(Tower t : player.getOwnBoard().getTowers()) {
            ((ImageView)((GridPane)((Pane) board.getChildren().get(0)).getChildren().get(1)).getChildren().get(towerPos)).setImage(new Image(imagePath+"Towers/"+t.getColor().name().toLowerCase(Locale.ROOT)+"Tower.png"));
            towerPos++;
        }
        while(towerPos < TOT_TOWERS_IN_BOARD) {
            (((GridPane)((Pane) board.getChildren().get(0)).getChildren().get(1)).getChildren().get(towerPos)).setVisible(false);
            towerPos++;
        }

        //set students in lanes
        int numOfStudent;
        for(int i = 0; i <  NUM_OF_LANES; i++) {
            numOfStudent = 0;
            Col_Pawn color = null;
            switch (i) {
                case 0 -> color = Col_Pawn.GREEN;
                case 1 -> color = Col_Pawn.RED;
                case 2 -> color = Col_Pawn.YELLOW;
                case 3 -> color = Col_Pawn.PINK;
                case 4 -> color = Col_Pawn.BLUE;
            }
            try {
                numOfStudent = player.getOwnBoard().getNumStudLaneByColor(color);
            } catch (ExceptionLaneNotFound e) {
                e.printStackTrace();
            }

            for(int j = 0; j < NUM_STUD_PER_LANE; j++) {
                //add students
                if(numOfStudent > 0) {
                    ((GridPane) ((Pane) board.getChildren().get(0)).getChildren().get(3)).getChildren().get(i * NUM_STUD_PER_LANE + j).setVisible(true);
                    numOfStudent--;
                } else {
                    ((GridPane) ((Pane) board.getChildren().get(0)).getChildren().get(3)).getChildren().get(i * NUM_STUD_PER_LANE + j).setVisible(false);
                }
            }
        }

        //set Professors
        GridPane professors = (GridPane) ((Pane)board.getChildren().get(0)).getChildren().get(2);
        int i = 0;
        while (i<Col_Pawn.values().length) {
            professors.getChildren().get(i).setVisible(false);
            i++;
        }
        for (Professor p : player.getOwnBoard().getProfessors()) {
            switch (p.getColor()) {
                case GREEN -> professors.getChildren().get(0).setVisible(true);
                case RED -> professors.getChildren().get(1).setVisible(true);
                case YELLOW -> professors.getChildren().get(2).setVisible(true);
                case PINK -> professors.getChildren().get(3).setVisible(true);
                case BLUE -> professors.getChildren().get(4).setVisible(true);
            }
        }
    }

    /**
     * Prints the island and hides the ones that are not used and set each island student value and mother nature
     */
    private void printIslandsInfo() {
        //set islands iteration variables
        int islandIterator = 0;
        int modelIslandIterator = 0;

        double result;
        int skipIsland;
        //set island skip iteration variables

        //System.out.println("Aggregazioni isole: " + model.getAggIslands().size());
        //System.out.println("Isole mancanti: " + (TOT_STARTING_ISLANDS - model.getAggIslands().size()));

        if(model.getAggIslands().size() >= TOT_STARTING_ISLANDS*1.0/2) {
            result = (TOT_STARTING_ISLANDS * 1.00) / (TOT_STARTING_ISLANDS - model.getAggIslands().size());
        } else {
            result = (TOT_STARTING_ISLANDS * 1.00) / (model.getAggIslands().size());
        }
        skipIsland = ((int) Math.floor(result)) - 1;
        result = Math.ceil(result);

        //set islands
        for (Node islandNode:islands.getChildren()) {
            //if it is an island
            if(!islandNode.getId().equals("clouds")) {
                if (islandIterator < TOT_STARTING_ISLANDS && checkIslandPrintValidity(islandIterator, skipIsland, model.getAggIslands().size())) {
                    islandNode.setVisible(true);

                    Pane imageContainer = (Pane) ((Pane) islandNode).getChildren().get(0);

                    AggIsland modelIsland = model.getAggIslands().get(modelIslandIterator);
                    islandNode.setId("island"+modelIslandIterator);
                    modelIslandIterator++;

                    int numOfTowers = 0;
                    //set Towers
                    if (modelIsland.isConquered()) {
                        try {
                            String color = modelIsland.getTower().getColor().name().toLowerCase(Locale.ROOT);
                            ((ImageView) imageContainer.getChildren().get(1)).setImage(new Image(imagePath + "Towers/" + color + "Tower.png"));

                            numOfTowers = modelIsland.getNumOfTower();
                            Text t = ((Text)imageContainer.getChildren().get(2));
                            if(numOfTowers <= 1)
                                t.setVisible(false);
                            else {
                                t.setText("x" + numOfTowers);
                                t.setVisible(true);
                            }
                        } catch (ExceptionsNoSuchTowers e) {
                            ((ImageView) imageContainer.getChildren().get(1)).setImage(null);
                        }

                    } else {
                        ((ImageView) imageContainer.getChildren().get(1)).setImage(null);
                        (imageContainer.getChildren().get(2)).setVisible(false);
                    }

                    //change image based on num of islands per aggregation
                    if(numOfTowers != towers[islandIterator]) {
                        if(numOfTowers>1) {
                            //load new image of aggregation of islands
                            int randomIslandImg;
                            if(numOfTowers<8)
                                randomIslandImg = ((int) (Math.random() * 2.0)) + 1;
                            else
                                randomIslandImg = 1;
                            //System.out.println("Island: " + numOfTowers + "island" + randomIslandImg);
                            ((ImageView)imageContainer.getChildren().get(0)).setImage(new Image(imagePath + "Islands/" + numOfTowers + "island" + randomIslandImg + ".png"));
                        } else {
                            if(numOfTowers<towers[islandIterator]) {
                                int randomIslandImg = ((int) (Math.random() * 3.0)) + 1;
                                //System.out.println("info" + randomIslandImg);
                                ((ImageView) imageContainer.getChildren().get(0)).setImage(new Image(imagePath + "Islands/island" + randomIslandImg + ".png"));
                            }
                        }
                        towers[islandIterator] = numOfTowers;
                    }

                    //set students amount
                    GridPane studentsAmount = (GridPane) ((Pane) islandNode).getChildren().get(1);
                    for (int i = 5; i < 10; i++) {
                        String value = String.valueOf(modelIsland.getNumStudents(Col_Pawn.values()[i - 5]));
                        ((TextField) studentsAmount.getChildren().get(i)).setText(value);
                    }

                    //add motherNature if island has mother nature
                    if (modelIsland.hasMotherNature()) {
                        ((Pane) islandNode).getChildren().get(2).setVisible(true);
                    } else {
                        ((Pane) islandNode).getChildren().get(2).setVisible(false);
                    }

                    if((model.getAggIslands().size() < TOT_STARTING_ISLANDS*1.0/2))
                        skipIsland = Math.min(skipIsland+(int)result, 11);;
                } else {
                    islandNode.setVisible(false);
                    if((model.getAggIslands().size() >= TOT_STARTING_ISLANDS*1.0/2))
                        skipIsland = Math.min(skipIsland+(int)result, 11);
                }
                islandIterator++;
            }
        }
    }

    /**
     * Print each cloud and hides the ones not used
     */
    private void printCloudInfo() {
        int i = 0;
        for(Node cloudNode : clouds.getChildren()) {

            if(i<numOfPlayers) {
                Cloud modelCloud = model.getClouds().get(i);

                //turn invisible if it has been used
                cloudNode.setVisible(!modelCloud.toString().equals("Empty"));

                //set students per cloud
                Pane layout = (Pane) ((Pane) cloudNode).getChildren().get(studentsPerCloud - 2);
                layout.setVisible(true);
                if (!modelCloud.toString().equals("Empty")) {
                    for (int j = 0; j < studentsPerCloud; j++) {
                        try {
                            String color = modelCloud.getStudents().get(j).getColor().name().toLowerCase(Locale.ROOT);
                            ((ImageView) layout.getChildren().get(j)).setImage(new Image(imagePath + "Pawns/Students/" + color + "Student.png"));
                        } catch (IndexOutOfBoundsException e) {
                            layout.setVisible(false);
                        }
                    }
                }
            }
            i++;
        }
    }

    /**
     * Prints all info texts on player board
     */
    protected void printTexts() {
        errorMessage.setVisible(false);
        currentPlayer.setText("Current player: " + model.getCurrentPlayer());
        if(model.getCurrentPlayer()==model.getId())
            turnInfoText.setText("It's your turn!");
        else
            turnInfoText.setText("Wait other players turn");
        currentStatus.setText("Current status: " + model.getState().getName());

        if(model.getCurrentPlayer()==model.getId()) {
            switch (model.getState().getName()) {
                case "moveMotherNature" -> {
                    currentStatusInfo.setText("Max mother nature movement: " + model.getPlayedCardPlayer().getCard().getMov());
                    currentStatusInfo.setVisible(true);
                }
                case "turn" -> {
                    if(numOfPlayers == 3)
                        currentStatusInfo.setText("Move 4 students to lane or islands");
                    else
                        currentStatusInfo.setText("Move 3 students to lane or islands");
                    currentStatusInfo.setVisible(true);
                }
                case "playCard" -> {
                    currentStatusInfo.setText("Select a card and press the use card button");
                    currentStatusInfo.setVisible(true);
                }
                case "selectCloud" -> {
                    currentStatusInfo.setText("Select one of the clouds to get its students");
                    currentStatusInfo.setVisible(true);
                }
                default -> currentStatusInfo.setVisible(false);
            }
        } else {
            currentStatusInfo.setVisible(false);
        }
        if(!model.getState().getName().equals("playCard")) {
            maxMovements.setText(String.valueOf(model.getPlayedCardPlayer().getCard().getMov()));

            textInfoMovements.setVisible(true);
            maxMovements.setVisible(true);
        } else {
            textInfoMovements.setVisible(false);
            maxMovements.setVisible(false);
        }
    }

    /**
     * Prints the elements for the first time
     */
    public void startView() {
        //initialize all values and images
        playerID.setText("ID : "+model.getPlayer().getID());
        printTexts();

        //set students per cloud
        numOfPlayers = clientGUI.getNum();
        if(numOfPlayers == 2 || numOfPlayers == 4) {
            studentsPerCloud = 3;
        } else {
            studentsPerCloud = 4;
        }

        //set islands
        //set islands random islandImage and towers to null
        for (Node islandNode:islands.getChildren()) {
            Pane imageContainer = (Pane) ((Pane) islandNode).getChildren().get(0);
            //if it is an island
            if (!islandNode.getId().equals("clouds")) {
                //set random islandImage
                AggIsland modelIsland = model.getAggIslands().get(Integer.parseInt(islandNode.getId().split("d")[1]));
                int randomIslandImg = ((int) (Math.random() * 3.0)) + 1;
                ((ImageView) imageContainer.getChildren().get(0)).setImage(new Image(imagePath + "Islands/island" + randomIslandImg + ".png"));
                ((ImageView) imageContainer.getChildren().get(1)).setImage(null);
            }
        }
        //set all others island info
        printIslandsInfo();

        //set clouds
        int i = 0;
        for(Node cloudNode : clouds.getChildren()) {
            if(i<numOfPlayers) {
                //show cloud
                cloudNode.setVisible(true);

                //choose cloudImage
                int randomCloudImg = ((int)(Math.random()*4.0))+1;
                ((ImageView)((Pane)cloudNode).getChildren().get(0)).setImage( new Image(imagePath+"Clouds/"+studentsPerCloud+"sCloud"+randomCloudImg+".png"));

                //select which layout to use based on number of students per cloud
                if(studentsPerCloud==3) {
                    ((Pane) cloudNode).getChildren().get(2).setVisible(false);
                } else if (studentsPerCloud == 4){
                    ((Pane) cloudNode).getChildren().get(1).setVisible(false);
                }
            } else {
                cloudNode.setVisible(false);
            }
            i++;
        }
        printCloudInfo();

        //set PlayerBoard
        printBoardInfo(model.getPlayer(), board0);

        //set other players boards
        printOtherPlayersInfo();
    }

    /**
     * Updates all elements each turn
     */
    public void update() {
        System.out.println("Updating view");
        printTexts();

        //check cards used
        if(usedImage != null) {
            boolean removed = true;
            for (Card c : model.getPlayer().getOriginalDeck().getCards()) {
                if (c.getVal() == (usedImage.getId().charAt(3) - '0') + 1) {
                    removed = false;
                    break;
                }
            }
            if(removed) {
                playerCards.remove(usedImage);
            } else {
                errorMessage.setText("Card already used");
                errorMessage.setVisible(true);
            }
        }

        printBoardInfo(model.getPlayer(), board0);
        printIslandsInfo();
        printCloudInfo();
        printOtherPlayersInfo();
    }

    /**
     * Initialises all values
     */
    @Override
    public void init() {
        viewGui = clientGUI.getViewGui();
        model = clientGUI.getViewGui().getModel();

        viewGui.setHandler(this);

        playerCards = playerCardsContainer.getChildren();
        startView();
    }


    /**
     * Allow teh scroll of the cards
     *
     */
    @FXML
    private void ScrollHorizzontally(ScrollEvent e) {
        ((ScrollPane)e.getSource()).setHvalue(((ScrollPane)e.getSource()).getHvalue()+(e.getDeltaY()*0.0015));
    }


    /**
     * Allow the board to move up when mouse over on it
     */
    @FXML
    protected void moveElementUp(MouseEvent e) {
        Node node = (Node) e.getSource();
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(20));
        translate.setToY(-225);
        translate.play();
    }

    /**
     * Allow the board to move up when mouse over from it
     */
    @FXML
    protected void moveElementDown(MouseEvent e) {
        Node node = (Node) e.getSource();
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(10));
        translate.setToY(0);
        translate.play();

        if(showBoardHint){
            showBoardHint = false;
            boardHint.setVisible(false);
        }
    }


    /**
     * Select the student clicked and highlights it
     */
    @FXML
    protected void selectStudentMouseClick(MouseEvent e)  {
        if(currentImage!=null) {
            currentImage.setEffect(null);
            currentImage = null;
        }

        if(model.getState().getName().equals("turn") && model.getCurrentPlayer()==model.getId()) {
            currentImage = (ImageView) e.getSource();

            DropShadow dropShadow = new DropShadow();
            dropShadow.setBlurType(BlurType.GAUSSIAN);
            dropShadow.setRadius(15.0);
            dropShadow.setSpread(0.7);
            dropShadow.setColor(Color.color(0.4, 0.5, 0.7));
            currentImage.setEffect(dropShadow);
        }
    }

    /**
     * Moves the clicked student to lane
     */
    @FXML
    private void studentToLane(MouseEvent e) {
        if(currentImage!=null  && model.getState().getName().equals("turn") && model.getCurrentPlayer()==model.getId()) {
            String posString = currentImage.getId();
            try {
                int position = Integer.parseInt(String.valueOf(posString.charAt(7)));
                //System.out.println(position);
                int color;
                color = model.getPlayer().getOwnBoard().getStudents().get(position).getColor().ordinal();

                viewGui.studentToLane(color);
            } catch (NumberFormatException exception) {
                System.err.println("Image unaccepted");
            }
            currentImage.setEffect(null);
            currentImage = null;
        }
    }

    /**
     * Moves mother nature or selected students to clicked island
     */
    @FXML
    private void elementToIsland(MouseEvent e) {
        //which island has been selected
        String posIslandString = ((Pane)e.getSource()).getId();
        int posIsland = Integer.parseInt(posIslandString.split("d")[1]);
        //System.out.println(posIslandString);

        if(currentImage!=null && model.getState().getName().equals("turn") && model.getCurrentPlayer()==model.getId()) {
            String posString = currentImage.getId();
            try {
                int position = Integer.parseInt(String.valueOf(posString.charAt(7)));
                //System.out.println(position);
                int color;
                color = model.getPlayer().getOwnBoard().getStudents().get(position).getColor().ordinal();

                viewGui.studentToIsland(posIsland, color);
            } catch (NumberFormatException exception) {
                System.err.println("Image unaccepted");
            }
            currentImage.setEffect(null);
            currentImage = null;
        } else if (model.getState().getName().equals("moveMotherNature") && model.getCurrentPlayer()==model.getId()) {
            viewGui.moveMotherNature(posIsland);
        }
    }


    /**
     * Allow selection of elements
     */
    @FXML
    protected void selectCardMouseClick(MouseEvent e) {
        if(currentImage!=null) {
            currentImage.setEffect(null);
        }
        currentImage = (ImageView) e.getSource();

        DropShadow dropShadow = new DropShadow();
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setRadius(15.0);
        dropShadow.setSpread(0.7);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.7));
        currentImage.setEffect(dropShadow);

        useCardButton.setDisable(false);
    }

    /**
     * Allow the use of the selected card
     */
    @FXML
    private void useCardButtonPress(ActionEvent e) {
        if(currentImage!=null && model.getState().getName().equals("playCard") && model.getCurrentPlayer()==model.getId()) {
            usedImage = currentImage;
            int cardValue = (usedImage.getId().charAt(3) - '0');
            viewGui.playCard(cardValue+1);
            //playerCards.remove(usedImage);
        } else if (model.getCurrentPlayer()!=model.getId()){
            errorMessage.setText("Not your turn!");
            errorMessage.setVisible(true);
        } else if (!model.getState().getName().equals("playCard")) {
            errorMessage.setText("Cannot play cards now");
            errorMessage.setVisible(true);
        }

        currentImage.setEffect(null);
        currentImage = null;
        useCardButton.setDisable(true);
    }

    /**
     * Chooses the selected cloud
     */
    @FXML
    private void selectCloudMouseClick(MouseEvent e) {
        if(model.getState().getName().equals("selectCloud") && model.getCurrentPlayer()==model.getId()) {
            int cloudPos = Integer.parseInt(((Pane) e.getSource()).getId().split("d")[1]);
            ((Pane) e.getSource()).setVisible(false);
            viewGui.selectCloud(cloudPos);
        }
    }


    @FXML
    protected void mouseOverEnter(MouseEvent e) {
        ((Node) e.getSource()).setTranslateY(-2);
    }

    @FXML
    protected void mouseOverExit(MouseEvent e) {
        ((Node) e.getSource()).setTranslateY(2);
    }

    @FXML
    private void mouseOverEnterIsland(MouseEvent e) {
        if(model.getState().getName().equals("turn") || model.getState().getName().equals("moveMotherNature")) {
            ((Node) e.getSource()).setTranslateY(2);
            islandIsUp = true;
        }
    }

    @FXML
    private void mouseOverExitIsland(MouseEvent e) {
        if(islandIsUp) {
            ((Node) e.getSource()).setTranslateY(-2);
            islandIsUp = false;
        }
    }
}
