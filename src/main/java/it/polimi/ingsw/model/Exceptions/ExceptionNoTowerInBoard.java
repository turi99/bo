package it.polimi.ingsw.model.Exceptions;

public class ExceptionNoTowerInBoard extends Exception{

    public ExceptionNoTowerInBoard(){
        super("ERROR : no tower in board");
    }
}
