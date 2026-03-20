public class Teacher extends Instructor{


    public Teacher(String name, char gender, int age){
        super (name,  gender,  age);

    }

    @Override
    public boolean canTeach(Subject subject) {
        if(subject==null){
            return false;
        }
        int[] specialisms=new int[]{1,2};

        for(int specialism :specialisms){
            if(subject.getSpecialism()==specialism){
                return true;
            }
        }
        return false;
    }

}
