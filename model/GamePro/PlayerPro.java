package it.polimi.ingsw.model.GamePro;

import it.polimi.ingsw.model.Exceptions.ExceptionLaneNotFound;
import it.polimi.ingsw.model.Game.Col_Pawn;
import it.polimi.ingsw.model.Game.Player;

public class PlayerPro extends Player {
    private int coins;

    public PlayerPro(Player originPlayer) {
        super(originPlayer);
        coins=0;
    }

    /**
     *
     * @return coins of the player
     */
    public int getCoins(){
        return coins;
    }

    /**
     *
     * @param coins coins you want to add
     */
    public void addCoins(int coins){
        this.coins+=coins;
    }

    /**
     *
     * @param coins number of coins
     * @return this.coins>=coin
     */
    public boolean canPay(int coins){
        return this.coins>=coins;
    }

    /**
     * remove the given coins, and return it
     * @param coins number of coins to remove
     * @return number of coins removed
     */
    public int payCoins(int coins){
            this.coins-=coins;
            return coins;
    }

    /**
     *
     * @param colorLane color of the lane of interest
     * @return true, if the player has a number of students in the lane multiple of 3 and different from 0. else, false
     * @throws ExceptionLaneNotFound if there isn't a lane with color equals to colorLane
     */
    public boolean checkStudentsForCoin(Col_Pawn colorLane) throws ExceptionLaneNotFound {
        return getOwnBoard().getNumStudLaneByColor(colorLane)%3==0 && getOwnBoard().getNumStudLaneByColor(colorLane)!=0;
    }

    @Override
    public String toString() {
        return super.toString()+"\n"+"Coins : "+coins;
    }
}
