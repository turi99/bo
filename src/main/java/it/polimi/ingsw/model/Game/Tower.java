package it.polimi.ingsw.model.Game;

import java.io.Serializable;

public class Tower implements Serializable {
    private Col_Tower color;

    public Tower(Col_Tower color) {
        this.color = color;
    }

    public Tower(Tower tower) {
        this.color = tower.color;
    }

    public Col_Tower getColor() {
        return color;
    }


}
