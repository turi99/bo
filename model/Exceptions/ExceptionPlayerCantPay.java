package it.polimi.ingsw.model.Exceptions;

public class ExceptionPlayerCantPay extends Exception{

    public ExceptionPlayerCantPay(){
        super("ERROR : player can't pay the card");
    }
}