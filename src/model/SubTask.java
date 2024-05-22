package model;

public class SubTask extends Task {
    private int idByEpic;


    public SubTask(String name, Status status, String description, int idByEpic) {
        super(name, status, description);
        this.idByEpic = idByEpic;
        this.type = TypeOfTask.SUBTASK;
    }


    public int getEpicId() {
        return idByEpic;
    }

    public void setEpic(int idByEpic) {
        this.idByEpic = idByEpic;
    }

}
