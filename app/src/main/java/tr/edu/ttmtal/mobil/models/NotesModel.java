package tr.edu.ttmtal.mobil.models;

public class NotesModel {
    private String id;
    private String title;
    private String content;
    private String date;

    public NotesModel() {}

    public NotesModel(String id, String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getDate() { return date; }
}
