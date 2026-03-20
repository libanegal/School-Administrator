import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Administrator {

    private School school;
    private Random random;

    public Administrator(){
        this.random = new Random();
    }
    public void load(String filename){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename)); // create file reader
            String line;

            while ((line = reader.readLine()) != null) { // while loop will continue until the end of the file
                line = line.trim(); // remove extra spaces at the beginning and end of the line

                if (line.isEmpty()) { // if the line is empty skip it
                    continue;
                }

                String lowerLine = line.toLowerCase();

                if (lowerLine.startsWith("school:")) { // if the beginning of the line contains School then
                    String[] parts = line.split(":", 2); // use a different split logic because there are only two groups of string, one before the colon and one after
                    school = new School(parts[1].trim());
                }

                else if (lowerLine.startsWith("subject:")) {//retrieve all the information from subject needed to create the object
                    String rest = line.substring(8).trim(); // remove "Subject:" from the line
                    String[] parts = rest.split(",", 4); // file format is comma-separated
                    String description = parts[0];
                    int specialism = Integer.parseInt(parts[1]);
                    int id = Integer.parseInt(parts[2]);
                    int duration = Integer.parseInt(parts[3]);
                    school.addSubject(new Subject(id, specialism, duration, description));
                }

                else if (lowerLine.startsWith("student:")) { // if the line starts with Student then create a student object
                    String rest = line.substring(8).trim();
                    String[] parts = rest.split(",");

                    String name = parts[0];
                    char gender = parts[1].charAt(0);
                    int age = Integer.parseInt(parts[2]);

                    school.addStudent(new Student(name, gender, age));
                }

                else if (lowerLine.startsWith("teacher:") || lowerLine.startsWith("demonstrator:")
                        || lowerLine.startsWith("ootrainer:") || lowerLine.startsWith("guitrainer:")) { // if the line starts with Instructor then create the correct instructor object
                    String[] lineParts = line.split(":", 2);
                    String type = lineParts[0].trim();
                    String[] parts = lineParts[1].trim().split(","); // split the rest of the line by commas

                    String name = parts[0];
                    char gender = parts[1].charAt(0);
                    int age = Integer.parseInt(parts[2]);
                    //check all the instruction types and create the correct object

                    if (type.equals("Teacher")) {
                        school.addInstructor(new Teacher(name, gender, age));
                    }
                    else if (type.equals("OOTrainer")) {
                        school.addInstructor(new OOTrainer(name, gender, age));
                    }
                    else if (type.equals("GUITrainer")) {
                        school.addInstructor(new GUITrainer(name, gender, age));
                    }
                    else if (type.equals("Demonstrator")) {
                        school.addInstructor(new Demonstrator(name, gender, age));
                    }
                }

            }
           reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){//admit students and instructors to the school so it can be used in the day at school method
        admitStudents();
        admitInstructors();

        school.aDayAtSchool();

        endOfDayUpdates();//

        report();
    }

    public void run(int days){
        for(int i = 0; i < days; i++){

            admitStudents();
            admitInstructors();

            school.aDayAtSchool();

            endOfDayUpdates();

            report();
        }
    }

    private void admitStudents(){
        int n = random.nextInt(3);

        for(int i = 0; i < n; i++){
            Student s = new Student("S" + random.nextInt(1000), 'M', 18 + random.nextInt(10));
            school.addStudent(s);
        }
    }

    private void admitInstructors() {
        for (int i = 0; i < 1; i++) { // one admission attempt per run
            if (Math.random() > 0.3) {
                continue; // if greater then 30 then skip the admission of an instructor
            }

            int type = random.nextInt(4);
            Instructor instructor;

            switch (type) {
                case 0:
                    instructor = new Teacher("T" + random.nextInt(1000), 'M', 30);
                    break;
                case 1:
                    instructor = new OOTrainer("OO" + random.nextInt(1000), 'F', 35);
                    break;
                case 2:
                    instructor = new GUITrainer("GUI" + random.nextInt(1000), 'M', 40);
                    break;
                default:
                    instructor = new Demonstrator("D" + random.nextInt(1000), 'F', 25);
                    break;
            }

            school.addInstructor(instructor);
        }
    }


    private void endOfDayUpdates(){

        //using remove-if instead of removeing from a foreach loop beacuse of risk of concurrent modification error
        //removing instructors and students if they have no course and use random 10% to leave school, also removing students if they have all certificates
        school.getInstructors().removeIf(instructor -> instructor.getAssignedCourse() == null && Math.random() <= 0.1
        );

        school.getStudents().removeIf(student -> student.hasAllCertificates(school) || (student.course == null && Math.random() <= 0.05)
        );
    }

    public void report(){
        System.out.println("----- REPORT -----");
        System.out.println("School: " + school.getName());
        System.out.println("Students: " + school.getStudents().size());
        System.out.println("Instructors: " + school.getInstructors().size());
        System.out.println("Courses: " + school.getCourses().size());

        for (Course course : school.getCourses()) {
            //status of course returns a number so I'll use that number so it makes sense to a user when printed
            if(course.getStatus()<0){
                System.out.println("Course: " + course.getSubject().getDescription() + " | Status: " + -course.getStatus() + " days until start | Students: " + course.getSize() + " | Instructor: " + (course.hasInstructor() ? "Yes" : "No"));
            }
            else if(course.getStatus()>0){//if the course has started but not finished then print the days to run
                System.out.println("Course: " + course.getSubject().getDescription() + " | Status: " + course.getStatus() + " days left to run | Students: " + course.getSize() + " | Instructor: " + (course.hasInstructor() ? "Yes" : "No"));
            }
            else{//anything else so 0 can mean either cancelled or the course is completed
                System.out.println("Course: " + course.getSubject().getDescription() + " | Status: Cancelled or Completed | Students: " + course.getSize() + " | Instructor: " + (course.hasInstructor() ? "Yes" : "No"));
            }

        }
        System.out.println("------------------\n");
    }
    //extension
    //saving a simulation
    public void saveSimulation(String filename){//saving the state of school into a file
        String outputFile=filename+".save.txt";//add txt to the name
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            //saving the school
            writer.write("School: " + school.getName() + "\n");
            //saving the state of the subjects
            for(Subject subject:school.getSubjects()){
                writer.write("Subject:"+ subject.getDescription()+"," + subject.getID() + "," + subject.getSpecialism() + "," + subject.getDuration() + ","  + "\n");
            }

            ArrayList<Student> students = school.getStudents();
            ArrayList<Instructor> instructors = school.getInstructors();
            ArrayList<Course> courses = school.getCourses();
            //i can't directly save the objects so i have to save the information of the objects and then when i load the file i can create the objects again using that information
            for (Student student : students) {
                StringBuilder certs = new StringBuilder();
                for (int i = 0; i < student.getCertificates().size(); i++) {//splitting the certificates with | because they are in an arraylist and i need to save them in one line
                    if (i > 0) certs.append("|");
                    certs.append(student.getCertificates().get(i));
                }

                int courseIndex = -1;//i cant save getCourse() so I can save which course in the list it is
                if (student.getCourse() != null) {
                    courseIndex = courses.indexOf(student.getCourse());
                }

                writer.write("student:" +student.getName() + "," +student.getGender() + "," +student.getAge() + "," + "certificates=" + certs + "," + "course=" + courseIndex+ "\n");
            }

            for (Instructor instructor : instructors) {
                int courseIndex = -1;
                if (instructor.getAssignedCourse() != null) {
                    courseIndex = courses.indexOf(instructor.getAssignedCourse());
                }

                writer.write(instructor.getClass().getSimpleName() + ":" +instructor.getName() + "," + instructor.getGender() + "," + instructor.getAge() + "," + "course=" + courseIndex+"\n");
            }

            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);

                int instructorIndex = -1;
                if (course.getInstructor() != null) {
                    instructorIndex = instructors.indexOf(course.getInstructor());
                }

                StringBuilder studentIndexes = new StringBuilder();
                Student[] enrolled = course.getStudents();
                boolean first = true;
                for (Student s : enrolled) {
                    if (s != null) {
                        if (!first) studentIndexes.append("|");
                        studentIndexes.append(students.indexOf(s));
                        first = false;
                    }
                }

                writer.write("course:" + i + "," + course.getSubject().getID() + "," + course.getDaysUntilStarts() + "," + course.getDaysToRun() + "," + course.isCancelled() + "," + "instructor=" + instructorIndex + "," + "students=" + studentIndexes+"\n");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) {
        System.out.println("test");
        if (args.length < 2) {
            System.out.println("Usage: java Administrator <file> <days>");
            return;
        }

        String filename = args[0];
        int days = Integer.parseInt(args[1]);

        Administrator admin = new Administrator();

        admin.load(filename);
        admin.run(days);
        admin.saveSimulation(filename);
    }

}
