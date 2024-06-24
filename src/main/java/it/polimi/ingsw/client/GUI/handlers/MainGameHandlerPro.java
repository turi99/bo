package it.polimi.ingsw.client.GUI.handlers;

import it.polimi.ingsw.client.GUI.ClientGUI;
import it.polimi.ingsw.client.GUI.SceneHandler;
import it.polimi.ingsw.client.GUI.ViewGUI;
import it.polimi.ingsw.client.GUI.ViewGUIPro;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardBlockDecorator;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardBlock;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardMoveLane;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardStudentOnIsland;
import it.polimi.ingsw.model.GamePro.CharacterCards.SpecificCharacterCard.CardSwitchBoard;
import it.polimi.ingsw.model.GamePro.PlayerPro;
import it.polimi.ingsw.model.ModelPro;
import it.polimi.ingsw.model.ModelView;
import it.polimi.ingsw.model.ModelViewPro;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Main Game Pro
 */
public class MainGameHandlerPro extends MainGameHandler implements SceneHandler {
    private final String imagePath = "/Images/";

    EventHandler<MouseEvent> ccDestinationEvent;
    EventHandler<MouseEvent> ccOriginEvent;

    private ClientGUI clientGUI;
    private ModelView model;
    private ViewGUI viewGui;

    private ArrayList<Node> allCCActiveInteractionDestinationElements;
    private ArrayList<Node> allCCActiveInteractionOriginElements;
    private Node ccActiveElement = null;
    private int ccActiveElementPosInCard = -1;
    private ArrayList<Node> ccStudentsToMoveOrigin = new ArrayList<>();
    private ArrayList<Integer> ccStudentsToMoveOriginColor = new ArrayList<>();
    private ArrayList<Node> ccStudentsToMoveDest  = new ArrayList<>();
    private ArrayList<Integer> ccStudentsToMoveDestColor = new ArrayList<>();

    private int activeCharacterCardPos = -1;
    private int selectedCharacterCardPos = -1;
    private boolean initPrint = true;
    private boolean canPerformOtherAction = true;
    private boolean gameIsUsingBlocks = false;
    private boolean showInfoCharacterCards;
    private boolean showCCHint= true;
    private boolean ccTurnAlreadyUsed = false;

    @FXML
    private Pane islands;
    @FXML
    private HBox characterCardsContainer;
    @FXML
    private Pane playerCoins;
    @FXML
    private Button characterCardButton;
    @FXML
    private GridPane ccInfo;
    @FXML
    private Label ccHint;

    /**
     * Activate the passed Character card
     * @param idCard the id of the Character Card used
     * @param parameters String of the parameters required by the Card
     */
    private void activateCard(int idCard, String parameters) {
        ((ViewGUIPro) viewGui).activateCard(idCard, parameters);

        printTexts();
        removeCCInteractionElements();
        enableOtherAction();
        ccTurnAlreadyUsed = true;
    }

    /**
     * Disable all actions when player is using a character card
     */
    private void disableOtherAction() {
        if (currentImage != null)
            currentImage.setEffect(null);
        currentImage = null;
        canPerformOtherAction = false;
    }

    /**
     * Reanables all other actions
     */
    private void enableOtherAction() {
        canPerformOtherAction = true;
    }

    /**
     * Set quick info text of the character cards
     * @param cardId id of the card used
     * @return
     */
    private String getCharacterCardInfoText(int cardId) {
        switch (cardId) {
            case 1, 8 -> {
                return "choose destination island";
            }
            case 2, 5 -> {
                return "choose student color";
            }
            case 6 -> {
                return "choose student to move to lane from card";
            }
            case 9 -> {
                return "choose student, then island";
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Set all texts of the Character cards
     */
    private void setCharacterCardsInfo() {
        int numOfCards = ((ModelViewPro)model).getCards().size();
        ccInfo.getParent().setVisible(false);

        int i = 0;
        for (CharacterCard c : ((ModelViewPro)model).getCards()) {
            //show image
            ((ImageView)ccInfo.getChildren().get(i)).setImage(new Image(imagePath + "CharacterCards/character_" + c.getIdCard() + ".jpg"));
            //show description
            ((Label)ccInfo.getChildren().get(i+numOfCards)).setText(c.getDescription());
            i++;
        }
    }

    /**
     * removes interaction of the elements influenced by the character cards
     */
    private void removeCCInteractionElements() {
        if (!allCCActiveInteractionDestinationElements.isEmpty()) {
            for (Node n : allCCActiveInteractionDestinationElements) {
                n.removeEventHandler(MouseEvent.MOUSE_CLICKED, ccDestinationEvent);
                try {wait(1000);} catch (Exception e ) {e.printStackTrace();}
                n.setEffect(null);
            }
            allCCActiveInteractionDestinationElements.clear();
        }
        if (!allCCActiveInteractionOriginElements.isEmpty()) {
            for (Node n : allCCActiveInteractionOriginElements) {
                n.removeEventHandler(MouseEvent.MOUSE_CLICKED, ccOriginEvent);
                try {wait(1000);} catch (Exception e ) {e.printStackTrace();}
                n.setEffect(null);
            }
            allCCActiveInteractionOriginElements.clear();
        }
        if (activeCharacterCardPos != -1) {
            characterCardsContainer.getChildren().get(activeCharacterCardPos).setEffect(null);
        }
        activeCharacterCardPos = -1;
        if (ccActiveElement != null) {
            ccActiveElement = null;
            ccActiveElementPosInCard = -1;
        }

        if(!ccStudentsToMoveOrigin.isEmpty()) {
            for (Node n:ccStudentsToMoveOrigin) {
                n.setEffect(null);
            }
        }

        if(!ccStudentsToMoveDest.isEmpty()) {
            for (Node n:ccStudentsToMoveDest) {
                n.setEffect(null);
            }
        }
        ccStudentsToMoveOriginColor.clear();
        ccStudentsToMoveOrigin.clear();
        ccStudentsToMoveDestColor.clear();
        ccStudentsToMoveDest.clear();
    }

    /**
     * actives interaction of the elements influenced by the character cards
     */
    private void activateCCInteractionElements(int cardId) {
        switch (cardId) {
            case 1, 8 -> {
                //set action
                ccDestinationEvent = mouseEvent -> {
                    int posIsland = Integer.parseInt(((Pane) mouseEvent.getSource()).getId().split("d")[1]);

                    activateCard(cardId, String.valueOf(posIsland));
                };
                //set action elements
                for (Node islandNode : islands.getChildren()) {
                    if (islandNode.isVisible() && !islandNode.getId().equals("clouds")) {
                        islandNode.addEventHandler(MouseEvent.MOUSE_CLICKED, ccDestinationEvent);
                        allCCActiveInteractionDestinationElements.add(islandNode);
                    }
                }
            }

            case 2, 5 -> {
                //set action
                ccOriginEvent = mouseEvent -> {
                    String colorString = ((Label) mouseEvent.getSource()).getText();
                    int color = (Col_Pawn.valueOf(colorString)).ordinal();

                    activateCard(cardId, String.valueOf(color));
                };
                //set action elements
                VBox box = (VBox) ((Pane) characterCardsContainer.getChildren().get(activeCharacterCardPos)).getChildren().get(2);
                for (Node colorNode : box.getChildren()) {
                    colorNode.addEventHandler(MouseEvent.MOUSE_CLICKED, ccOriginEvent);
                    allCCActiveInteractionOriginElements.add(colorNode);
                }
            }

            case 6 -> {
                //set action
                ccOriginEvent = mouseEvent -> {
                    if (ccActiveElement != null)
                        ccActiveElement.setEffect(null);

                    ccActiveElement = (Node) mouseEvent.getSource();
                    int posInCard = ((Node) mouseEvent.getSource()).getId().charAt(1) - '0';
                    int color = ((CardMoveLane) ((ModelViewPro) model).getCards().get(activeCharacterCardPos)).getStudents().get(posInCard).getColor().ordinal();

                    //add selection effect
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setBlurType(BlurType.GAUSSIAN);
                    dropShadow.setRadius(15.0);
                    dropShadow.setSpread(0.7);
                    dropShadow.setColor(Color.color(0.4, 0.7, 0.5));
                    ccActiveElement.setEffect(dropShadow);

                    activateCard(cardId, String.valueOf(color));
                };
                //set action elements
                GridPane pane = (GridPane) ((Pane) characterCardsContainer.getChildren().get(activeCharacterCardPos)).getChildren().get(1);
                for (Node element : pane.getChildren()) {
                    if (element.isVisible()) {
                        element.addEventHandler(MouseEvent.MOUSE_CLICKED, ccOriginEvent);
                        allCCActiveInteractionOriginElements.add(element);
                    }
                }
            }

            case 9 -> {
                //set action
                ccOriginEvent = mouseEvent -> {
                    if (ccActiveElement != null)
                        ccActiveElement.setEffect(null);

                    ccActiveElement = (Node) mouseEvent.getSource();

                    //add selection effect
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setBlurType(BlurType.GAUSSIAN);
                    dropShadow.setRadius(15.0);
                    dropShadow.setSpread(0.7);
                    dropShadow.setColor(Color.color(0.4, 0.7, 0.5));
                    ccActiveElement.setEffect(dropShadow);

                    ccActiveElementPosInCard = ((Node) mouseEvent.getSource()).getId().charAt(1) - '0';
                };
                ccDestinationEvent = mouseEvent -> {
                    int posIsland = Integer.parseInt(((Pane) mouseEvent.getSource()).getId().split("d")[1]);
                    int color = ((CardStudentOnIsland) ((ModelViewPro) model).getCards().get(activeCharacterCardPos)).getStudents().get(ccActiveElementPosInCard).getColor().ordinal();

                    String parameters = (color + "," + posIsland);
                    activateCard(9, parameters);
                };
                //set action elements
                for (Node islandNode : islands.getChildren()) {
                    if (islandNode.isVisible() && !islandNode.getId().equals("clouds")) {
                        islandNode.addEventHandler(MouseEvent.MOUSE_CLICKED, ccDestinationEvent);
                        allCCActiveInteractionDestinationElements.add(islandNode);
                    }
                }
                GridPane pane = (GridPane) ((Pane) characterCardsContainer.getChildren().get(activeCharacterCardPos)).getChildren().get(1);
                for (Node element : pane.getChildren()) {
                    if (element.isVisible()) {
                        element.addEventHandler(MouseEvent.MOUSE_CLICKED, ccOriginEvent);
                        allCCActiveInteractionOriginElements.add(element);
                    }
                }
            }

            case 10 -> {
                //set action
                ccOriginEvent = mouseEvent -> {
                    //from Card
                    if (ccStudentsToMoveOrigin.size() < 3) {
                        Node source = (Node) mouseEvent.getSource();
                        if (ccStudentsToMoveOrigin.isEmpty() || !ccStudentsToMoveOrigin.contains(source)) {
                            ccStudentsToMoveOrigin.add(source);

                            //add selection effect
                            DropShadow dropShadow = new DropShadow();
                            dropShadow.setBlurType(BlurType.GAUSSIAN);
                            dropShadow.setRadius(15.0);
                            dropShadow.setSpread(0.7);
                            dropShadow.setColor(Color.color(0.4, 0.7, 0.5));
                            source.setEffect(dropShadow);

                            //color as ordinal of Col_Pawn
                            int posInCard = source.getId().charAt(1)-'0';
                            ccStudentsToMoveOriginColor.add(((CardSwitchBoard)((ModelViewPro)model).getCards().get(activeCharacterCardPos)).getStudents().get(posInCard).getColor().ordinal());
                        }
                    }
                };
                //to Hall
                ccDestinationEvent = mouseEvent -> {
                    if(ccStudentsToMoveDest.size()<3 && ccStudentsToMoveOrigin.size() > 0) {
                        Node source = (Node)mouseEvent.getSource();
                        if(ccStudentsToMoveDest.isEmpty() || !ccStudentsToMoveDest.contains(source)) {
                            ccStudentsToMoveDest.add(source);

                            //add selection effect
                            DropShadow dropShadow = new DropShadow();
                            dropShadow.setBlurType(BlurType.GAUSSIAN);
                            dropShadow.setRadius(15.0);
                            dropShadow.setSpread(0.7);
                            dropShadow.setColor(Color.color(0.4, 0.7, 0.5));
                            source.setEffect(dropShadow);

                            //color as ordinal of Col_Pawn
                            int posInHall = source.getId().charAt(7)-'0';
                            ccStudentsToMoveOriginColor.add(model.getPlayer().getOwnBoard().getStudents().get(posInHall).getColor().ordinal());

                            if(ccStudentsToMoveDest == ccStudentsToMoveOrigin) {
                                String param = "";
                                for(Integer color : ccStudentsToMoveOriginColor) {
                                    param = param+color;
                                }
                                param = param+",";
                                for(Integer color : ccStudentsToMoveDestColor) {
                                    param = param+color;
                                }
                                activateCard(cardId, param);
                            }
                        }
                    }
                };

                //set Action elements
                //origin: card
                for(Node n : ((GridPane)((Pane)characterCardsContainer.getChildren().get(activeCharacterCardPos)).getChildren().get(1)).getChildren()) {
                    n.addEventHandler(MouseEvent.MOUSE_CLICKED, ccOriginEvent);
                    allCCActiveInteractionOriginElements.add(n);
                }
                //destination: hall
                for(int i = 1; i<model.getPlayer().getOwnBoard().getStudents().size(); i++) {
                    Node n = ((GridPane)((Pane)board0.getChildren().get(0)).getChildren().get(4)).getChildren().get(i);
                    n.addEventHandler(MouseEvent.MOUSE_CLICKED, ccOriginEvent);
                    ccStudentsToMoveOrigin.add(n);
                    allCCActiveInteractionDestinationElements.add(n);
                }
            }

            case 11 -> {
                //set action
                ccOriginEvent = mouseEvent -> {
                    //from Hall
                    if (ccStudentsToMoveOrigin.size() < 2) {
                        Node source = (Node) mouseEvent.getSource();
                        if (ccStudentsToMoveOrigin.isEmpty() || !ccStudentsToMoveOrigin.contains(source)) {
                            ccStudentsToMoveOrigin.add(source);

                            //add selection effect
                            DropShadow dropShadow = new DropShadow();
                            dropShadow.setBlurType(BlurType.GAUSSIAN);
                            dropShadow.setRadius(15.0);
                            dropShadow.setSpread(0.7);
                            dropShadow.setColor(Color.color(0.4, 0.7, 0.5));
                            source.setEffect(dropShadow);

                            //color as ordinal of Col_Pawn
                            ccStudentsToMoveOriginColor.add(model.getPlayer().getOwnBoard().getStudents().get(Integer.parseInt(source.getId().split("d")[1])).getColor().ordinal());
                            //ccStudentsToMoveOriginPos.add(Integer.parseInt(((Node) mouseEvent.getSource()).getId().split("d")[1]));
                        }
                    }
                };
                //to Lane
                ccDestinationEvent = mouseEvent -> {
                    if(ccStudentsToMoveDest.size() < 2 && ccStudentsToMoveOrigin.size()>0) {
                        Node source = (Node) mouseEvent.getSource();
                        ccStudentsToMoveDest.add(source);

                        //add selection effect
                        DropShadow dropShadow = new DropShadow();
                        dropShadow.setBlurType(BlurType.GAUSSIAN);
                        dropShadow.setRadius(15.0);
                        dropShadow.setSpread(0.7);
                        dropShadow.setColor(Color.color(0.4, 0.7, 0.5));
                        source.setEffect(dropShadow);

                        int colorLane = source.getId().charAt(1)-'0';
                        ccStudentsToMoveDestColor.add(colorLane);

                        if(ccStudentsToMoveOrigin.size() == ccStudentsToMoveDest.size()) {
                            String params = "";
                            for (Integer color: ccStudentsToMoveOriginColor) {
                                params = params+color;
                            }
                            params = params+",";
                            for (Integer color: ccStudentsToMoveDestColor) {
                                params = params+color;
                            }

                            activateCard(cardId, params);
                        }
                    }
                };

                //set Action elements
                for(int i = 1; i<model.getPlayer().getOwnBoard().getStudents().size(); i++) {
                    Node elem = ((GridPane)((Pane)board0.getChildren().get(0)).getChildren().get(4)).getChildren().get(i);
                    elem.addEventHandler(MouseEvent.MOUSE_CLICKED, ccOriginEvent);

                    allCCActiveInteractionOriginElements.add(elem);
                }
                for(int i = 0; i<NUM_STUD_PER_LANE; i++)
                    for(int j = 0; j<NUM_OF_LANES; j++){
                        Node elem = ((GridPane)((Pane)board0.getChildren().get(0)).getChildren().get(3)).getChildren().get(j*NUM_STUD_PER_LANE + i);
                        elem.addEventHandler(MouseEvent.MOUSE_CLICKED, ccDestinationEvent);

                        allCCActiveInteractionDestinationElements.add(elem);
                }
            }

            default -> {
                activateCard(cardId, "");
            }
        }
    }

    /**
     * turn off all the effects caused by the character card activation
     */
    private void turnOffCCCardsActivation() {
        characterCardButton.setDisable(true);
        if (selectedCharacterCardPos != -1)
            characterCardsContainer.getChildren().get(selectedCharacterCardPos).setEffect(null);
        if (activeCharacterCardPos != -1)
            characterCardsContainer.getChildren().get(activeCharacterCardPos).setEffect(null);

        activeCharacterCardPos = -1;
        selectedCharacterCardPos = -1;
    }

/*
    private void drawIslandsBlocks() {
        if (false && gameIsUsingBlocks) {
            for (Node islandNode : islands.getChildren()) {
                if (islandNode.isVisible() && !islandNode.getId().equals("clouds")) {
                    int islandPos = Integer.parseInt(islandNode.getId().split("d")[1]);
                    if (((CardBlockDecorator) model.getAggIslands().get(islandPos)).getBan()) {
                        ((Pane) ((Pane) islandNode).getChildren().get(0)).getChildren().get(3).setVisible(true);
                    } else {
                        ((Pane) ((Pane) islandNode).getChildren().get(0)).getChildren().get(3).setVisible(false);
                    }
                }
            }
        }
    }

 */

    /**
     * Print all the character cards elements
     * @param card image to print to
     * @param c character card to print elements about
     */
    private void printCardElements(Pane card, CharacterCard c) {
        ImageView element;
        switch (c.getIdCard()) {
            case 1 -> {
                int i = 0;
                for (i = 0; i < ((CardBlock) c).getBlockCard(); i++) {
                    element = ((ImageView) ((GridPane) card.getChildren().get(1)).getChildren().get(i));
                    if (initPrint)
                        element.setImage(new Image(imagePath + "block.png"));
                    element.setVisible(true);
                }

                while (i < 6) {
                    ((GridPane) card.getChildren().get(1)).getChildren().get(i).setVisible(false);
                    i++;
                }
            }

            case 2, 5 -> {
                card.getChildren().get(1).setVisible(false);
                card.getChildren().get(2).setVisible(true);
            }

            case 6 -> {
                int i = 0;
                for (Student s : ((CardMoveLane) c).getStudents()) {
                    element = ((ImageView) ((GridPane) card.getChildren().get(1)).getChildren().get(i));
                    element.setImage(new Image(imagePath + "Pawns/Students/" + s.getColor().name().toLowerCase(Locale.ROOT) + "Student.png"));
                    element.setVisible(true);
                    i++;
                }

                while (i < 6) {
                    ((GridPane) card.getChildren().get(1)).getChildren().get(i).setVisible(false);
                    i++;
                }
            }

            case 9 -> {
                int i = 0;
                for (Student s : ((CardStudentOnIsland) c).getStudents()) {
                    element = ((ImageView) ((GridPane) card.getChildren().get(1)).getChildren().get(i));
                    element.setImage(new Image(imagePath + "Pawns/Students/" + s.getColor().name().toLowerCase(Locale.ROOT) + "Student.png"));
                    element.setVisible(true);
                    i++;
                }

                while (i < 6) {
                    ((GridPane) card.getChildren().get(1)).getChildren().get(i).setVisible(false);
                    i++;
                }
            }

            case 10 -> {
                int i = 0;
                for (Student s : ((CardSwitchBoard) c).getStudents()) {
                    element = ((ImageView) ((GridPane) card.getChildren().get(1)).getChildren().get(i));
                    if (initPrint)
                        element.setImage(new Image(imagePath + "Pawns/Students/" + s.getColor().name().toLowerCase(Locale.ROOT) + "Student.png"));
                    element.setVisible(true);
                    i++;
                }

                while (i < 6) {
                    ((GridPane) card.getChildren().get(1)).getChildren().get(i).setVisible(false);
                    i++;
                }
            }

            default -> {
                card.getChildren().get(1).setVisible(false);
                card.getChildren().get(2).setVisible(false);
            }
        }
    }

    /**
     * Prints the character cards images and elements
     */
    private void printCharacterCards() {
        Pane singleCharCard;
        int i = 0;
        for (CharacterCard c : ((ModelViewPro) model).getCards()) {
            singleCharCard = (Pane) characterCardsContainer.getChildren().get(i);
            if (initPrint) {
                ((ImageView) singleCharCard.getChildren().get(0)).setImage(new Image(imagePath + "CharacterCards/character_" + c.getIdCard() + ".jpg"));

                //if there is CardBlock card
                if (c.getIdCard() == 1) {
                    gameIsUsingBlocks = true;
                }
            }

            //set cost
            ((Text) ((Pane) singleCharCard.getChildren().get(3)).getChildren().get(1)).setText(String.valueOf(c.getCost()));

            printCardElements(singleCharCard, c);
            i++;
        }
        initPrint = false;
    }

    /**
     * Updates all elements with data from Server after each action
     */
    public void update() {
        System.out.println(((ModelViewPro) model).getCards());

        super.update();
        System.out.println("Pro Update");
        System.out.println("");

        //update coins
        ((Text) playerCoins.getChildren().get(1)).setText(String.valueOf(((PlayerPro) model.getPlayer()).getCoins()));

        //update cards
        printCharacterCards();
        //stop players from using charactersCards when they cant
        if (!model.getState().getName().equals("turn")) {
            ccTurnAlreadyUsed = false;
            if(model.getCurrentPlayer() == model.getId()) {
                turnOffCCCardsActivation();
            }
        }

        //drawIslandsBlocks();
        removeCCInteractionElements();
        enableOtherAction();
    }

    /**
     * Initialises all values
     */
    public void init() {
        super.init();
        allCCActiveInteractionDestinationElements = new ArrayList<>();
        allCCActiveInteractionOriginElements = new ArrayList<>();
        clientGUI = super.clientGUI;
        viewGui = clientGUI.getViewGui();
        model = clientGUI.getViewGui().getModel();


        ((Text) playerCoins.getChildren().get(1)).setText(String.valueOf(((PlayerPro) (model).getPlayer()).getCoins()));
        printCharacterCards();
        setCharacterCardsInfo();
    }


    @FXML
    protected void moveElementUp(MouseEvent e) {
        Node node = (Node) e.getSource();
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(20));
        translate.setToY(-235);
        translate.play();
    }

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
     * Allow players to select a character cards and adds highlights
     * @param e mouseClick
     */
    @FXML
    protected void characterCardSelected(MouseEvent e) {
        //if there are no active cards
        if (activeCharacterCardPos == -1) {
            if (selectedCharacterCardPos != -1) {
                characterCardsContainer.getChildren().get(selectedCharacterCardPos).setEffect(null);
                selectedCharacterCardPos = -1;
            }

            //if there are no character cards active
            if (activeCharacterCardPos == -1) {
                int pos = Integer.parseInt(((Pane) e.getSource()).getId().split("d")[1]);
                System.out.println(pos);

                DropShadow dropShadow = new DropShadow();
                dropShadow.setBlurType(BlurType.GAUSSIAN);
                dropShadow.setRadius(15.0);
                dropShadow.setSpread(0.7);
                dropShadow.setColor(Color.color(0.4, 0.7, 0.5));

                selectedCharacterCardPos = pos;
                ((Pane) e.getSource()).setEffect(dropShadow);

                characterCardButton.setDisable(!model.getState().getName().equals("turn"));
            }
        }
    }

    /**
     * Uses the selected Character card
     * @param e mouseClick
     */
    @FXML
    private void useCharacterCardButtonPress(ActionEvent e) {
        if (model.getCurrentPlayer() == model.getId() && !ccTurnAlreadyUsed) {
            if (selectedCharacterCardPos != -1 && model.getState().getName().equals("turn")) {
                //if can pay
                if (((PlayerPro) model.getPlayer()).canPay(((ModelViewPro) model).getCards().get(selectedCharacterCardPos).getCost())) {
                    disableOtherAction();
                    ccTurnAlreadyUsed = true;

                    //set effect for card selected
                    characterCardsContainer.getChildren().get(selectedCharacterCardPos).setEffect(null);

                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setBlurType(BlurType.GAUSSIAN);
                    dropShadow.setRadius(15.0);
                    dropShadow.setSpread(0.7);
                    dropShadow.setColor(Color.color(0.8, 0.75, 0.5));

                    characterCardsContainer.getChildren().get(selectedCharacterCardPos).setEffect(dropShadow);
                    activeCharacterCardPos = selectedCharacterCardPos;

                    //print quick info
                    currentStatusInfo.setText(getCharacterCardInfoText(((ModelViewPro) model).getCards().get(selectedCharacterCardPos).getIdCard()));

                    //activate the card
                    activateCCInteractionElements(((ModelViewPro) model).getCards().get(selectedCharacterCardPos).getIdCard());
                } else {
                    errorMessage.setText("You don't have enough coins!");
                    errorMessage.setVisible(true);
                    activeCharacterCardPos = -1;
                }
            }
        } else {
            if(ccTurnAlreadyUsed){
                super.errorMessage.setText("Cant play more than one");
                super.errorMessage.setVisible(true);
                activeCharacterCardPos = -1;
            } else {
                super.errorMessage.setText("Not your turn!");
                super.errorMessage.setVisible(true);
                activeCharacterCardPos = -1;
            }
            turnOffCCCardsActivation();
        }


        selectedCharacterCardPos = -1;
        characterCardButton.setDisable(true);
    }

    @FXML
    private void ccElementClicked(MouseEvent e) {

    }

    /**
     * Moves slightly the elements of the activated character card when mouse enters it
     * @param e mouseOver
     */
    @FXML
    private void ccMouseOverEnter(MouseEvent e) {
        if (activeCharacterCardPos != -1) {
            if (((Node) e.getSource()).getParent().getId().equals("p" + activeCharacterCardPos)) {
                ((Node) e.getSource()).setTranslateY(-1);
            }
        }
    }

    /**
     * Moves slightly the elements of the activated character card when mouse exits it
     * @param e mouseOver
     */
    @FXML
    private void ccMouseOverExit(MouseEvent e) {
        if (activeCharacterCardPos != -1) {
            if (((Node) e.getSource()).getParent().getId().equals("p" + activeCharacterCardPos)) {
                ((Node) e.getSource()).setTranslateY(1);
            }
        }
    }

    protected void selectStudentMouseClick(MouseEvent e) {
        if (canPerformOtherAction)
            super.selectStudentMouseClick(e);
    }

    protected void mouseOverEnter(MouseEvent e) {
        if (canPerformOtherAction) {
            super.mouseOverEnter(e);
        }
    }

    protected void mouseOverExit(MouseEvent e) {
        if (canPerformOtherAction) {
            super.mouseOverExit(e);
        }
    }

    /**
     * Show or hides Pane that shows all info texts of the character cards
     * @param e
     */
    @FXML
    private void ccInfoClicked(MouseEvent e){
        showInfoCharacterCards = !showInfoCharacterCards;
        ccInfo.getParent().setVisible(showInfoCharacterCards);

        //hide hint only after when player close cc description for the first time
        if(showCCHint && !showInfoCharacterCards) {
            showCCHint = false;
            ccHint.setVisible(false);
        }
    }
}
