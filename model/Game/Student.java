package it.polimi.ingsw.model.Game;

public class Student extends Pawn{

    public Student(Col_Pawn color) {
        super(color);
    }


    public Student(Pawn pawn) {
        super(pawn);
    }

    @Override
    public boolean equals(Object obj) {
        return getColor() == ((Student)obj).getColor();
    }
}
