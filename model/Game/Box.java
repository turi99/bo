package it.polimi.ingsw.model.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class Box implements Serializable {
    private ArrayList<Professor> professors;

    public Box(){
        initializeProfessorsStart();
    }


    private void initializeProfessorsStart(){
        this.professors = new ArrayList<>();
        this.professors.add(new Professor(Col_Pawn.BLUE));
        this.professors.add(new Professor(Col_Pawn.YELLOW));
        this.professors.add(new Professor(Col_Pawn.GREEN));
        this.professors.add(new Professor(Col_Pawn.PINK));
        this.professors.add(new Professor(Col_Pawn.RED));
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(ArrayList<Professor> professors) {
        this.professors = professors;
    }
}
