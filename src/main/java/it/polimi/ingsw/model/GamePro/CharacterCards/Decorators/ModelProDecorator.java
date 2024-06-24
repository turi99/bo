package it.polimi.ingsw.model.GamePro.CharacterCards.Decorators;

import it.polimi.ingsw.model.Exceptions.*;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.Game.State.State;
import it.polimi.ingsw.model.GamePro.CharacterCards.CharacterCard;
import it.polimi.ingsw.model.GamePro.PlayerPro;
import it.polimi.ingsw.model.ModelPro;
import it.polimi.ingsw.utils.observer.Observable;
import it.polimi.ingsw.utils.observer.Observer;

import java.util.ArrayList;

public abstract class ModelProDecorator extends ModelPro {
    protected ModelPro modelPro;
    protected CharacterCard card;

    public ModelProDecorator(ModelPro modelPro, CharacterCard card) {
        this.modelPro = modelPro;
        this.card = card;
        modelPro.addObservers(this);
    }

    /**
     * call the same method of the internal object
     */
    @Override
    public ModelPro initializeModelPro(ModelPro model) {
        return modelPro.initializeModelPro(model);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public CharacterCard getActiveGameCard(int idCard) throws Exception {
        return modelPro.getActiveGameCard(idCard);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getCoins() {
        return modelPro.getCoins();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void addCoins(int coins) {
        modelPro.addCoins(coins);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int removeCoins(int coins) throws ExceptionPlayerCantPay {
        return modelPro.removeCoins(coins);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<CharacterCard> getActiveGameCards() {
        return modelPro.getActiveGameCards();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void playerPayCoins(int coins) {
        modelPro.playerPayCoins(coins);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void giveCoinsToPlayer(PlayerPro player, int coins) throws ExceptionPlayerCantPay {
        modelPro.giveCoinsToPlayer(player, coins);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void moveStudentToLane(Col_Pawn colorStudent) throws ExceptionLaneNotFound, ExceptionStudentNotFound, ExceptionPlayerCantPay {
        modelPro.moveStudentToLane(colorStudent);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void activateCard(int idCard, String parameters) throws Exception {
        modelPro.activateCard(idCard, parameters);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void actionActivateCard(int idCard, String parameters) {
        modelPro.actionActivateCard(idCard, parameters);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setState(State state) {
        modelPro.setState(state);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public State getState() {
        return modelPro.getState();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Team[] getTeams() {
        return modelPro.getTeams();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Team getTeamByPlayer(Player player) throws ExceptionTeamNotFound {
        return modelPro.getTeamByPlayer(player);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Team getTeamByColor(Col_Tower colTeam) throws ExceptionTeamNotFound {
        return modelPro.getTeamByColor(colTeam);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Cloud[] getClouds() {
        return modelPro.getClouds();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Cloud getCloud(int indexCloud) throws ExceptionCloudNotFound {
        return modelPro.getCloud(indexCloud);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<AggIsland> getIslands() {
        return modelPro.getIslands();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public AggIsland getIsland(int indexIsland) throws ExceptionIslandNotFound {
        return modelPro.getIsland(indexIsland);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Bag getBag() {
        return modelPro.getBag();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public MotherNature getMotherNature() {
        return modelPro.getMotherNature();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<Professor> getProfessors() {
        return modelPro.getProfessors();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Deck[] getDecks() {
        return modelPro.getDecks();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Player[] getPlayers() {
        return modelPro.getPlayers();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<PlayedCard> getPlayedCards() {
        return modelPro.getPlayedCards();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getCurrentPlayerPos() {
        return modelPro.getCurrentPlayerPos();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Player getCurrentPlayer() {
        return modelPro.getCurrentPlayer();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public PlayedCard selectPlayedCardByPlayer(Player player) {
        return modelPro.selectPlayedCardByPlayer(player);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public PlayedCard selectPlayedCardByIdPlayer(int idPlayer) {
        return modelPro.selectPlayedCardByIdPlayer(idPlayer);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void newOrder() {
        modelPro.newOrder();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getMaxMov() {
        System.out.println("maxMuve from dentro");
        return modelPro.getMaxMov();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void fillCloud() {
        modelPro.fillCloud();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public int getPosMotherNature() {
        return modelPro.getPosMotherNature();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void checkProf(Col_Pawn colProf) {
        modelPro.checkProf(colProf);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean compareNumberStudent(int x, int y) {
        return modelPro.compareNumberStudent(x, y);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void moveMotherNature(int indexIsland, Team[] teams) throws ExceptionNoTowerInBoard, ExceptionIslandNotFound, ExceptionsNoSuchTowers {
        modelPro.moveMotherNature(indexIsland, teams);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<Island> getAllIslands() {
        return modelPro.getAllIslands();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void nextPlayer() {
        modelPro.nextPlayer();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean isThisLastPlayer() {
        return modelPro.isThisLastPlayer();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ModelPro upgradeToTablePro() throws Exception {
        return modelPro.upgradeToTablePro();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setTeams(Team[] teams) {
        modelPro.setTeams(teams);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setClouds(Cloud[] clouds) {
        modelPro.setClouds(clouds);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setIslands(ArrayList<AggIsland> islands) {
        modelPro.setIslands(islands);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setBag(Bag bag) {
        modelPro.setBag(bag);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setMotherNature(MotherNature motherNature) {
        modelPro.setMotherNature(motherNature);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setProfessors(ArrayList<Professor> professors) {
        modelPro.setProfessors(professors);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setDecks(Deck[] decks) {
        modelPro.setDecks(decks);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setPlayers(Player[] players) {
        modelPro.setPlayers(players);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setPosCurrentPlayer(int posCurrentPlayer) {
        modelPro.setPosCurrentPlayer(posCurrentPlayer);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void setPlayedCards(ArrayList<PlayedCard> playedCards) {
        modelPro.setPlayedCards(playedCards);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public ArrayList<Object> arrayToArrayList(Object[] objects) {
        return modelPro.arrayToArrayList(objects);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void deactivateAllActiveGameCards() {
        modelPro.deactivateAllActiveGameCards();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void playCard(int valCard) throws Exception {
        modelPro.playCard(valCard);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void moveStudentToIsland(int indexIsland, Col_Pawn colorStudent) throws ExceptionStudentNotFound, ExceptionIslandNotFound {
        modelPro.moveStudentToIsland(indexIsland, colorStudent);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void moveMotherNature(int newPos) throws ExceptionNoTowerInBoard, ExceptionIslandNotFound, ExceptionMotherNatureIllegalMovement, ExceptionsNoSuchTowers {
        modelPro.moveMotherNature(newPos);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void chooseCloud(int indexCloud) throws ExceptionCloudYetChoose, ExceptionCloudNotFound {
        modelPro.chooseCloud(indexCloud);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void actionPlayCard(int valCard) {
        modelPro.actionPlayCard(valCard);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void actionMoveStudentToLane(Col_Pawn colorStudent) {
        modelPro.actionMoveStudentToLane(colorStudent);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void actionMoveStudentToIsland(int indexIsland, Col_Pawn colorStudent) {
        modelPro.actionMoveStudentToIsland(indexIsland, colorStudent);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void actionMoveMotherNature(int newPos) {
        modelPro.actionMoveMotherNature(newPos);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void actionChooseCloud(int indexCloud) {
        modelPro.actionChooseCloud(indexCloud);
    }

    //----------------------------------------//
    /**
     * call the same method of the internal object
     */
    @Override
    public int getPosCurrentPlayer() {
        return modelPro.getPosCurrentPlayer();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Team getTeamWinner() {
        return modelPro.getTeamWinner();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean isLastTurn() {
        return modelPro.isLastTurn();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void moveProf(Professor prof) {
        modelPro.moveProf(prof);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void moveProf(Player player, Col_Pawn colProf) {
        modelPro.moveProf(player, colProf);
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public Box getBox() {
        return modelPro.getBox();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void boxToPro() {
        modelPro.boxToPro();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public void win() throws ExceptionsNoSuchTowers {
        modelPro.win();
    }


    /**
     * call the same method of the internal object
     */
    @Override
    public boolean isFlagCard() {
        return modelPro.isFlagCard();
    }
    /**
     * call the same method of the internal object
     */
    @Override
    public boolean isFlagBagEmpty() {
        return modelPro.isFlagBagEmpty();
    }

    /**
     * call the same method of the internal object
     */
    @Override
    public void setFlagBagEmpty(boolean flagBagEmpty) {
        modelPro.setFlagBagEmpty(flagBagEmpty);
    }

    /**
     * call the same method of the internal object
     */
    @Override
    public boolean isFlagTowerOrIsland() {
        return modelPro.isFlagTowerOrIsland();
    }



    /**
     * call the same method of the internal object
     */
    @Override
    public void update(Object message) {
        notify(message);
    }

    /**
     * call the same method of the internal object
     */
    @Override
    public String toString() {
        return modelPro.toString();
    }
}

    //---------------------

