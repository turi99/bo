package it.polimi.ingsw.model;

import it.polimi.ingsw.client.ViewCli;
import it.polimi.ingsw.model.Game.*;
import it.polimi.ingsw.model.Game.State.*;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardBlockDecorator;
import it.polimi.ingsw.model.GamePro.CharacterCards.Decorators.CardInfluencePlusTwoDecorator;
import it.polimi.ingsw.model.GamePro.StatePro.StateProMoveMotherNature;
import it.polimi.ingsw.model.GamePro.StatePro.StateProPlayCard;
import it.polimi.ingsw.model.GamePro.StatePro.StateProSelectCloud;
import it.polimi.ingsw.model.GamePro.StatePro.StateProTurn;
import it.polimi.ingsw.utils.observer.Observable;
import it.polimi.ingsw.utils.observer.Observer;

import java.util.ArrayList;

public class ModelView extends Observable<Object> implements Observer<Object> {
    private final int id;
    private State state;
    private Player player;
    private Team winner;
    private Col_Tower myColor;
    private ArrayList<AggIsland> aggIslands;
    private ArrayList<Cloud> clouds;
    private MotherNature motherNature;
    private Box box;
    private ArrayList<PlayedCard> playedCard;
    private int currentPlayerPos;
    private  ArrayList<Player> otherPlayers;

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    public Team getWinner() {
        if(winner !=  null)
            return winner;
        else
            return null;
    }


    public ModelView(int id, Model model){
        this.id=id;
        winner=model.getTeamWinner();
        player=model.getPlayers()[id];
        myColor=player.getTeam().getTeamColor();
        otherPlayers=new ArrayList<>();
        for (Player p: model.getPlayers()){
            if(p.equals(player))continue;
            otherPlayers.add(p.withoutDeck());
        }
        clouds=new ArrayList<>();
        for(Cloud c: model.getClouds()){
            clouds.add(c);
        }
        aggIslands=model.getIslands();
        state= model.getState();
        motherNature=model.getMotherNature();
        box=model.getBox();
        playedCard=model.getPlayedCards();
        currentPlayerPos= model.getCurrentPlayerPos();


    }

    /**
     *
     * @return myColor
     */
    public Col_Tower getMyColor() {
        return myColor;
    }

    /**
     *
     * @return state
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return aggIsland
     */
    public ArrayList<AggIsland> getAggIslands() {
        return aggIslands;
    }

    /**
     *
     * @return clouds
     */
    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    /**
     *
     * @return motherNature
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     *
     * @return box.getProfessors()
     */
    public ArrayList<Professor> getProfessors() {
        return box.getProfessors();
    }

    /**
     *
     * @return playedCard
     */
    public ArrayList<PlayedCard> getPlayedCard() {
        return playedCard;
    }

    /**
     *
     * @return currentPlayerPos
     */
    public int getCurrentPlayerPos() {
        return currentPlayerPos;
    }

    /**
     *
     * @return otherPlayers
     */
    public ArrayList<Player> getOtherPlayers() {
        return otherPlayers;
    }

    /**
     *
     * @return null, if there isn't a winner. Color of the winner team, if it's present
     */
    public Col_Tower getColorTeamWinner(){
        if(winner==null)return null;
        else return winner.getTeamColor();
    }

    /**
     *
     * @return currentPlayerPos
     */
    public int getCurrentPlayer(){
        return currentPlayerPos;
    }

    /**
     *
     * @return true if exist a played card with card!=null, else false
     */
    public boolean thereIPlayedCard() {
        for (PlayedCard p : playedCard) {
            if (p.getCard() != null) return true;
        }
        return false;
    }

    /**
     *
     * @return playedCard of this player
     */
    public PlayedCard getPlayedCardPlayer(){
        return playedCard.get(id);
    }

    @Override
    public void update(Object message) {
        if(message.getClass()==ArrayList.class){
            Object o=((ArrayList<?>) message).get(0);
            if(o.getClass()==Cloud.class){
                clouds=(ArrayList<Cloud>) message;
            }
            if(o.getClass()==AggIsland.class || o.getClass()==CardBlockDecorator.class || o.getClass()==CardInfluencePlusTwoDecorator.class){
                aggIslands=(ArrayList<AggIsland>) message;
            }
            if(o.getClass()==Player.class){
                player=((ArrayList<Player>) message).get(id);
                ArrayList<Player> pl=new ArrayList<>();
                for(int i=0;i<((ArrayList<Player>) message).size();i++){
                    if(i==id)continue;
                    pl.add((Player) ((ArrayList<?>) message).get(i));
                }
                otherPlayers=pl;
            }
            if(o.getClass()==PlayedCard.class){
                playedCard=(ArrayList<PlayedCard>) message;
            }
        }else{
            if(message.getClass()==Integer.class){
                currentPlayerPos=(Integer)message;
            }
            if(message.getClass()==StatePlayCard.class || message.getClass()==StateTurn.class
            || message.getClass()==StateSelectCloud.class || message.getClass()==StateMoveMotherNature.class
            || message.getClass()==StateWin.class || message.getClass()== StateProPlayCard.class || message.getClass()== StateProTurn.class
                    || message.getClass()== StateProSelectCloud.class || message.getClass()== StateProMoveMotherNature.class ){
                state=(State) message;
                notify(null);
            }
            if(message.getClass()==Team.class){
                winner=(Team) message;
            }
            if(message.getClass()==MotherNature.class){
                motherNature=(MotherNature) message;
            }
            if(message.getClass()==String.class) {
                String s = (String) message;
                if (s.contains(",")) {
                    String[] error = ((String) message).split(",");
                    try {
                        int i = Integer.parseInt(error[0]);
                        if (i == getId()) {
                            System.out.println(error[1]);
                            notify(null);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(s);
                    if (((String) message).contains("lost")) {
                        notify(true);
                    } else{
                        System.out.println(s);
                        notify(null);
                    }
                }
            }
            if(message.getClass()==Box.class || message.getClass()==BoxPro.class){
                box= (Box) message;
            }
        }
        
    }

    /**
     * update the state
     * @param state new state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * update the player
     * @param player new player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * update the winner
     * @param winner new winner
     */
    public void setWinner(Team winner) {
        this.winner = winner;
    }

    /**
     * update the myColor
     * @param myColor new myColor
     */
    public void setMyColor(Col_Tower myColor) {
        this.myColor = myColor;
    }

    /**
     * update the aggIslands
     * @param aggIslands new aggIslands
     */
    public void setAggIslands(ArrayList<AggIsland> aggIslands) {
        this.aggIslands = aggIslands;
    }

    /**
     * update the clouds
     * @param clouds new clouds
     */
    public void setClouds(ArrayList<Cloud> clouds) {
        this.clouds = clouds;
    }

    /**
     * update the motherNature
     * @param motherNature new motherNature
     */
    public void setMotherNature(MotherNature motherNature) {
        this.motherNature = motherNature;
    }

    /**
     * update the box
     * @param box new box
     */
    public void setBox(Box box) {
        this.box = box;
    }

    /**
     * update the playedCard
     * @param playedCard new playedCard
     */
    public void setPlayedCard(ArrayList<PlayedCard> playedCard) {
        this.playedCard = playedCard;
    }

    /**
     * update the currentPlayerPos
     * @param currentPlayerPos new currentPlayerPos
     */
    public void setCurrentPlayerPos(int currentPlayerPos) {
        this.currentPlayerPos = currentPlayerPos;
    }

    /**
     * update the otherPlayers
     * @param otherPlayers new otherPlayers
     */
    public void setOtherPlayers(ArrayList<Player> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }
}
