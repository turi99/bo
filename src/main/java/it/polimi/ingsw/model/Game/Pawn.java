package it.polimi.ingsw.model.Game;

import java.io.Serializable;

public class Pawn implements Serializable {
    private Col_Pawn color;

    public Pawn(Col_Pawn color) {
        this.color = color;
    }

    public Pawn(Pawn pawn) {
        this.color = pawn.color;
    }


    public Col_Pawn getColor() {
        return color;
    }
}
