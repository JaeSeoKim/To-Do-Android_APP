package dev.jaeseokim.todo;

public class ToDoData {
    private String title, desc, time;
    private int id, textColor, check, archive;

    public void ToDoData(){

    }

    public void ToDoData(String title,String desc, String time,
                         int id, int textColor, int check, int archive){
        this.time = title;
        this.desc = desc;
        this.time = time;
        this.id = id;
        this.textColor = textColor;
        this.check = check;
        this.archive = archive;
    }

    public void setArchive(int archive) {
        this.archive = archive;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getArchive() {
        return archive;
    }

    public int getCheck() {
        return check;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

}
