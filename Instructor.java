public abstract class Instructor extends Person{
    private Course assignedCourse;
    public Instructor(String name, char gender, int age) {
        super(name, gender, age);

    }

    public Course getAssignedCourse() {
        return assignedCourse;
    }

    public void assignCourse(Course assignedCourse) {
        this.assignedCourse = assignedCourse;
    }
    public void unassignCourse(){
        this.assignedCourse=null;
    }
    public abstract boolean canTeach(Subject subject);

}
