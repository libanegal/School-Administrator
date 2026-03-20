public class Course {
    protected Subject subject;
    private int daysUntilStarts;
    private int daysToRun;
    private Instructor instructor;
    private boolean cancelled;
    protected Student[] enrolledStudents;
    private int capacity=3;

    public Course(Subject subject, int daysUntilStarts){
        this.daysToRun = subject.getDuration();
        this.subject = subject;
        this.daysUntilStarts = daysUntilStarts;
        this.enrolledStudents = new Student[3];
        this.cancelled = false;
    }

    public Subject getSubject(){
        return this.subject;
    }
    public int getDaysUntilStarts() {
        return this.daysUntilStarts;
    }
    public int getDaysToRun() {
        return this.daysToRun;
    }
    public Student[] getStudents(){
        return enrolledStudents;
    }
    public int getStatus(){
        if (this.cancelled) {//0 if cancelled
            return 0;
        }
        if(this.daysUntilStarts>0){
            return -this.daysUntilStarts;
        } else if (this.daysToRun>0) {
            return this.daysToRun;
        }

        return 0;
    }
    public void aDayPasses() {
        int studentCount=0;
        if (this.cancelled) {//if course status us cancelled then exit
            return;
        }

        if (this.daysUntilStarts > 0) {//if the course hasn't started then adjust daysUntilStarts
            this.daysUntilStarts -= 1;


            // On the day the course starts, cancel if no students or no instructor.
            if (this.daysUntilStarts == 0 &&  this.instructor == null) {
                cancelCourse();
            }
            //loop manually through the array because array always has fixed size of 3 so length won't work
            for(Student student:enrolledStudents){
                if(student!=null){
                    studentCount++;
                }
            }
            if (this.daysUntilStarts == 0 && studentCount == 0) {
                cancelCourse();
            }

        }

        else if (this.daysToRun > 0) {//if the course is running then adjust days left
            this.daysToRun -= 1;

            if (this.daysToRun == 0 ) {//if course has reached the end then loop through all the enrolled students in the array and graduate them

                for (Student student : this.enrolledStudents) {
                    //check if student is null because if not I will be graduating an object which doesn't exist
                    if(student!=null){
                        student.graduate(this.subject);
                    }
                }
                this.instructor.unassignCourse();
                this.instructor=null;

            }
        }
    }
    public boolean enrolStudent(Student student) {
        if (student == null) return false;//check if student is null
        if (this.cancelled) return false;//is course cancelled?
        
        if(student.getCourse()!=null){//check if the student is already enrolled in a course if yes return false
            return false;
        }
        // check if course already started or finished
        if (getStatus() >= 0)
        {
            return false;
        }
        //use for loop to check capacity
        int count=0;
        for(Student student1:enrolledStudents){
            if(student1!=null){
                count++;
            }

        }
        if (count==capacity){//check if course is at capacity
            return false;
        }

        for(int i =0;i<3;i++){ //loop through enrolled students array if a space is null add the student there
            if(this.enrolledStudents[i]==null){
                this.enrolledStudents[i]=student;
                break;
            }
        }
        student.course = this;
        return true;
    }

    public int getSize() {
        int count=0;
        for(Student student:enrolledStudents){
            if(student!=null){
                count++;
            }
        }
        return count;
    }

    public boolean setInstructor(Instructor instructor){
        if (this.cancelled || instructor == null) {
            return false;
        }
        if(instructor.canTeach(this.subject)){
            this.instructor = instructor;
            instructor.assignCourse(this);
            return true;
        }
        return false;

    }
    public boolean hasInstructor() {
        return this.instructor != null;
    }
    public boolean isCancelled(){
        return this.cancelled;
    }
    public void setDaysUntilStarts(int daysUntilStarts) {
        this.daysUntilStarts = daysUntilStarts;
    }
    public void setDaysToRun(int daysToRun) {
        this.daysToRun = daysToRun;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


    public void cancelCourse() {
        this.cancelled = true;
        this.daysToRun = 0;

        for (Student student : this.enrolledStudents) {//loop through students and only cancel if student is not null
            if(student!=null){
                student.course = null;
            }
        }

        if (this.instructor != null) {
            this.instructor.unassignCourse();
            this.instructor = null;
        }
    }

    public Instructor getInstructor() {
        return instructor;
    }
}
