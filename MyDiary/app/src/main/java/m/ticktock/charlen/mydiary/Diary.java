package m.ticktock.charlen.mydiary;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Diary extends DataSupport implements Serializable{
    private int id=0;
    private String title="";
    private String date="";
    private String diaryContent="";

    public Diary() {
    }

    public Diary(String title, String date, String diaryContent) {
        this.date = date;
        this.diaryContent = diaryContent;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDiaryContent(String diaryContent) {
        this.diaryContent = diaryContent;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

