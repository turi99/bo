package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionPlayerCantPay;

public class BoxPro extends Box{
    int coins=20;

    public BoxPro(){
        super();
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins){
        this.coins += coins;
    }


    /**
     *
     * @param coins coins you want to remove
     * @return if this.coins>=coins, return the number of coins removed
     * @throws ExceptionPlayerCantPay if this.coins<coins
     */
    public int removeCoins(int coins) throws ExceptionPlayerCantPay {
        if(this.coins<coins){
            throw new ExceptionPlayerCantPay();
        }
        this.coins -= coins;
        return coins;
    }
}
