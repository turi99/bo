package it.polimi.ingsw.model.GamePro.CharacterCards;

import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Exceptions.ExceptionNoTowerInBoard;
import it.polimi.ingsw.model.Game.Player;
import it.polimi.ingsw.model.GamePro.PlayerPro;
import it.polimi.ingsw.model.ModelPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;

public abstract class CharacterCard implements Serializable {

    private final int idCard;
    private final String description;
    private int cost;
    private boolean active;
    private Player player;
    protected ModelPro model;

    public CharacterCard(String description, int cost, int idCard, ModelPro model) {
        this.idCard=idCard;
        this.description = description;
        this.cost = cost;
        this.model = model;
        active=false;
    }


    /**
     *
     * @return this model
     */
    public ModelPro getModel() {
        return model;
    }

    /**
     * replace the model
     * @param model new model
     */
    public void setModel(ModelPro model){
        this.model = model;
    }

    /**
     *
     * @return idCard
     */
    public int getIdCard() {
        return idCard;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * increase the cost of the card by 1 unit
     */
    public void increaseCost() {
        cost=cost+1;
        System.out.println(cost);
    }


    /**
     * set who played the card, remove the necessary coins from that player and increase the cost of the card
     * @param parameters parameters are ignored
     * @throws ExceptionNoTowerInBoard exception from the override of sons
     * @throws ExceptionLaneNotFound exception from the override of sons
     */
    public void activate(String parameters) throws ExceptionNoTowerInBoard, ExceptionLaneNotFound {
        PlayerPro player=(PlayerPro) model.getCurrentPlayer();
        if(player.canPay(cost)){
            model.playerPayCoins(cost);
            this.increaseCost();
            this.active=true;
            this.player=player;
            model.notify(model.getBox());
            model.notify(model.arrayToArrayList(model.getPlayers()));
            model.notify(model.getActiveGameCards());

            System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwww carta attivata "+idCard);
        }else model.notify(model.getCurrentPlayerPos()+",You haven't enough coins");
    }

    /**
     * deactivate the card and set the player=null
     */
    public void deactivate() {
        this.active = false;
        this.player = null;
    }

    /**
     * return if the parameters of activation are correct
     * @param parameters parameters of activation
     * @return if parameters are correct for the specific card
     * @throws ExceptionLaneNotFound if the lane is not found
     */
    public abstract boolean checkParameters(String parameters) throws ExceptionLaneNotFound;

    /**
     *
     * @return a model pro with necessary elements decorated
     */
    public abstract ModelPro decorate();

    @Override
    public String toString() {
        return "Id : "+idCard+"\nDescription :\n"+description+"\nCost : "+cost+"\nActive : "+active;
    }

    /**
     * ask the user to insert the necessary parameters
     * @param bufferedReader parameters are red from this buffer
     * @param thread to check that the game isn't closed
     * @return string of parameters
     * @throws IOException exception while reading
     */
    public abstract String activationParameters(BufferedReader bufferedReader,Thread thread) throws IOException;
}
