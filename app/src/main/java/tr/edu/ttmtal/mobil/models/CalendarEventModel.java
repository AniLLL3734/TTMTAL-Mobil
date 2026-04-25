package tr.edu.ttmtal.mobil.models;

public class CalendarEventModel {
    private String id;
    private String title;
    private String description;
    private String date;    // e.g. "2026-05-15"
    private String type;    // "sınav", "etkinlik", "tatil" etc.

    public CalendarEventModel() {} // Required for Firebase

    public CalendarEventModel(String id, String title, String description, String date, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getType() { return type; }
}
