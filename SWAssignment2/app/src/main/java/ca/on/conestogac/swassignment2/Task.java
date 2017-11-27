package ca.on.conestogac.swassignment2;

/**
 * Created by Swry6766 on 11/27/2017.
 */

public class Task {
    private int taskId;
    private int listId;
    private String name;
    private String notes;
    private String completed;
    private String hidden;

    public Task() {
        name = "";
        notes = "";
        completed = "0";
        hidden = "0";
    }

    public Task(int listId, String name, String notes, String completed, String hidden) {
        this.listId = listId;
        this.name = name;
        this.notes = notes;
        this.completed = completed;
        this.hidden = hidden;
    }

    public Task(int taskId, int listId, String name, String notes, String completed, String hidden) {
        this.taskId = taskId;
        this.listId = listId;
        this.name = name;
        this.notes = notes;
        this.completed = completed;
        this.hidden = hidden;
    }

    public int getId() {
        return taskId;
    }

    public void setId(int taskId) {
        this.taskId = taskId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCompleted() {
        return completed;
    }

    public long getCompletedMillis() {
        return Long.valueOf(completed);
    }

    public void setCompletedDate(String completed) {
        this.completed = completed;
    }

    public void setCompletedDate(long millis) {
        this.completed = Long.toString(millis);
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
}
