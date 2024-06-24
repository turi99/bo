package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionWrongStudentColor;

import java.io.Serializable;
import java.util.ArrayList;

public class Lane  implements Serializable {
    private final ArrayList<Student> students=new ArrayList<>();
    private final Col_Pawn colorLane;

    public Lane(Col_Pawn colorLane) {
        this.colorLane=colorLane;
    }

    /**
     *
     * @return list of all students in the lane
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     *
     * @return number of students in the lane
     */
    public int getNumStudents() {
        return students.size();
    }

    /**
     *
     * @return color of the lane
     */
    public Col_Pawn getColor() {
        return colorLane;
    }

    /**
     *
     * @param student student you want to add
     * @throws ExceptionWrongStudentColor if colorLane!=student.color
     */
    public void addStudent(Student student) throws ExceptionWrongStudentColor {
        if(student.getColor()==colorLane)
            students.add(student);
        else
            throw new ExceptionWrongStudentColor();

    }

    /**
     *
     * @return students.size()>=10
     */
    public boolean full(){
        int dim = 10;
        return students.size()>= dim;
    }

    @Override
    public String toString(){
        return colorLane+" : "+students.size();
    }
}