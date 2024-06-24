package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Exceptions.ExceptionStudentNotFound;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bag implements Serializable {
    private ArrayList<Student> students;


    public Bag(ArrayList<Student> students) {
        this.students = students;
    }


    /**
     *
     * @return number of student in the bag
     */
    public int getNumberOfStudents(){
        return students.size();
    }

    /**
     * add the given list of students in the bag
     * @param students list of students you want to add
     */
    public void addStudents(List<Student> students){
        this.students.addAll(students);
    }


    /**
     *
     * @param num number of student you want to pick
     * @return list of random student from the bag, list.size == num
     */
    public ArrayList<Student> pickRandomStudent(int num){

        ArrayList<Student> students1 = new ArrayList<Student>();
        if(students.size()>=num) {

            int j;

            Student student;
            for (int i = 0; i < num; i++) {


                j = (int) (Math.random() * this.getNumberOfStudents());

                student = this.students.get(j);

                students1.add(student);
                this.students.remove(student);
            }
        }
        return students1;
    }

    /**
     * remove the given student and return it
     * @param student student you want to remove
     * @return if the given student is present in the bag, return that student
     * @throws ExceptionStudentNotFound if the given student is not present
     */
    public Student removeStudent(Student student) throws ExceptionStudentNotFound {
        if (!students.contains(student)) {
            throw new ExceptionStudentNotFound();
        }
        int i = students.indexOf(student);
        Student s = students.get(i);
        students.remove(i);

        return s;
    }

    /**
     * add the given student in the bag
     * @param student student you want to add
     */
    public void addStudent(Student student) {
        this.students.add(student);
    }
}
