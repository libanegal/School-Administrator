public class Subject {
    private int ID;
    private int specialism;
    private int duration;
    private String description;
    public Subject(int ID, int specialism, int duration, String description){
        this.ID=ID;
        this.specialism=specialism;
        this.description=description;
        this.duration=duration;
    }

    public int getID() {
        return ID;
    }

    public int getDuration() {
        return duration;
    }

    public int getSpecialism() {
        return specialism;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
