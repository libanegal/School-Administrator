import java.util.ArrayList;


public class School {
    private final ArrayList<Student> students;
    private final ArrayList<Instructor> instructors;
    private final ArrayList<Subject> subjects;
    private final ArrayList<Course> courses;
    private String name;

    public School(String name){
        this.name = name;
        this.students = new ArrayList<>();
        this.instructors = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    public void addStudent(Student student){
        if (student != null) {
            this.students.add(student);
        }
    }

    public void removeStudent(Student student) {
        if (student != null) {
            this.students.remove(student);
        }
    }

    public ArrayList<Student> getStudents() {
        return this.students;
    }


    private void assignInstructor() {//loop through courses and check if they have instructor
        for (Course course : courses) {
            if (!course.hasInstructor() && !course.isCancelled() && course.getStatus() < 0) {
                for (Instructor instructor : instructors) {//if courses doesnt have an instructor then
                    if (instructor.getAssignedCourse() == null &&
                            instructor.canTeach(course.getSubject())) {//check if insructor doesnt already teach a course and that he can also teach that specific course
                        course.setInstructor(instructor);
                        break;
                    }
                }
            }
        }
    }
    public void addInstructor(Instructor instructor) {
        if (instructor != null) {
            this.instructors.add(instructor);
        }
    }

    public void removeInstructor(Instructor instructor){
        if (instructor != null) {
            this.instructors.remove(instructor);
        }
    }

    public ArrayList<Instructor> getInstructors() {
        return this.instructors;
    }

    public void addSubject(Subject subject){
        if (subject != null) {
            this.subjects.add(subject);
        }
    }

    public void removeSubject(Subject subject){
        if (subject != null){
            this.subjects.remove(subject);

        }

    }

    public ArrayList<Subject> getSubjects() {
        return this.subjects;
    }

    public void addCourse(Course course){
        if (course != null) {
            this.courses.add(course);
        }
    }

    public void removeCourse(Course course){
        if (course == null) {
            return;
        }
        else if (course.isCancelled()|| course.getStatus()==0) {
            this.courses.remove(course);
        }
    }

    public ArrayList<Course> getCourses() {
        return this.courses;
    }

    public String getName() {
        return this.name;
    }

    public void aDayAtSchool() {
        createNewCourse();
        assignInstructor();
        assignStudent();

        for (Course course : this.courses) {
            course.aDayPasses();
        }

        this.courses.removeIf(course -> course.isCancelled() || course.getStatus() == 0);

    }


    private void createNewCourse() {
        for (Subject subject : this.subjects) {
            if (findOpenCourse(subject) == null) {
                Course course = new Course(subject, 2);
                this.courses.add(course);
            }
        }
    }

    private Course findOpenCourse(Subject subject) {
        for (Course course : this.courses) {
            if (!course.isCancelled()
                    && course.getStatus() != 0
                    && course.getSubject() == subject) {
                return course;
            }
        }
        return null;
    }



    private void assignStudent() {
        for (Student student : students) {
            if (student.course == null) {
                for (Course course : courses) {
                    if (!course.isCancelled() && course.getStatus() < 0) {
                        Subject subject = course.getSubject();

                        if (!student.hasCertificate(subject) && course.enrolStudent(student)) {
                            break;
                        }
                    }
                }
            }
        }
    }
    @Override
    public String toString() {
        return "School{name='" + name + "', students=" + students.size() + ", instructors=" + instructors.size() + ", subjects=" + subjects.size() + ", courses=" + courses.size() + "}";
    }

}
