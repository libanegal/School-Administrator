import java.util.ArrayList;

public class Student extends Person {
    protected  Course course;
    protected ArrayList<Integer> certificates;

    public Student(String name, char gender, int age) {
        super(name, gender, age);
        this.certificates = new ArrayList<>();
    }
    public void graduate(Subject subject){
        certificates.add(subject.getID());
    }
    public boolean hasAllCertificates( School school) {
        for (Subject subject : school.getSubjects()) {
            if (!this.hasCertificate(subject)) {
                return false;
            }
        }
        return true;
    }
    public Course getCourse(){
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    public ArrayList<Integer> getCertificates() {
        return certificates;
    }
    public void setCertificates(ArrayList<Integer> certificates) {
        this.certificates = certificates;
    }
    public boolean hasCertificate(Subject subject){
        return certificates.contains(subject.getID());
    }

}
