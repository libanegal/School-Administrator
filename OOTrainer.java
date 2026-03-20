public class OOTrainer extends Teacher{

    public OOTrainer(String name,char gender,int age){
        super(name, gender, age);


    }

    @Override
    public boolean canTeach(Subject subject) {
        if(subject==null){//if subject is null then return false because we can't iterate if empty

            return false;
        }

        int[] specialisms=new int[]{1,2,3};//putting all the specialisms that OOTrainer can teach in an array so that I can loop it against a subjects specialism

        for(int specialism :specialisms){
            if(subject.getSpecialism()==specialism){
                return true;
            }
        }
        return false;//return false if the subject's specialism doesn't match any of the specialisms that OOTrainer can teach
    }

}
