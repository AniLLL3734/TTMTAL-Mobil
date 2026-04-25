package tr.edu.ttmtal.mobil.models;

public class NewsModel {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String date;
    private String content;

    public NewsModel() {} // Required for Firebase

    public NewsModel(String id, String title, String description, String imageUrl, String date, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.content = content;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getDate() { return date; }
    public String getContent() { return content; }
}
