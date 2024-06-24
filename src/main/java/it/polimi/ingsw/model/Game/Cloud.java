package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionCloudYetChoose;
import it.polimi.ingsw.model.Exceptions.ExceptionWrongNumOfStud;

import java.io.Serializable;
import java.util.ArrayList;

public class Cloud  implements Serializable {
    private ArrayList<Student> students;
    private int dim;


    public Cloud(int dim){
        students=new ArrayList<>();
        this.dim=dim;
    }

    /**
     *
     * @return list of all student in the cloud
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     *
     * @return dimension of the cloud
     */
    public int getDim(){
        return dim;
    }

    /**
     *
     * @param students list of student you want to add
     * @throws ExceptionWrongNumOfStud if getDim()<student.size
     */
    public void addStudents(ArrayList<Student> students) throws ExceptionWrongNumOfStud {
        if(this.getDim() == students.size()) this.students = students;
        else throw new ExceptionWrongNumOfStud();
    }

    /**
     *
     * @return if students.size>0, return the list of all student on the cloud
     * @throws ExceptionCloudYetChoose students.size>0
     */
    public ArrayList<Student> removeStudents() throws ExceptionCloudYetChoose {
        if(this.students.size()==0) throw new ExceptionCloudYetChoose();
        else{
            ArrayList<Student> x=students;

            students = new ArrayList<Student>();

            return x;
        }
    }

    @Override
    public String toString(){

        String s;
        if(students.size()==0){
            s="Empty";
        }
        else {


            int[] stud = new int[5];
            for (Student studO : students) {
                stud[studO.getColor().ordinal()] += 1;
            }
            s = "\t" +Col_Pawn.values()[0] + " : " + stud[0];
            for (int i = 1; i < 5; i++) {
                s = "\t" +Col_Pawn.values()[i] + " : " + stud[i] + "\n" + s;
            }
        }
        return s;
    }
}
