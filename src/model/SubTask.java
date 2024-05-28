package model;

public class SubTask extends Task {
    private int idByEpic;


    public SubTask(String name, Status status, String description, int idByEpic) {
        super(name, status, description);
        this.id = getId();
        this.idByEpic = idByEpic;
    }

    public SubTask(int id, String name, Status status, String description, int idByEpic) {
        super(id, name, status, description);
        this.idByEpic = idByEpic;
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
    }

    @Override
    public Integer getEpicId() {
        return idByEpic;
    }

    public void setEpic(int idByEpic) {
        this.idByEpic = idByEpic;
    }

}
