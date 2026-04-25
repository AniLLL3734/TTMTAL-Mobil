package tr.edu.ttmtal.mobil.models;

import java.util.List;

public class GalleryModel {
    private String id;
    private String title;
    private String authorName;
    private String date;
    private List<String> imageUrls; // unlimited photos per album

    public GalleryModel() {} // Required for Firebase

    public GalleryModel(String id, String title, String authorName, String date, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.date = date;
        this.imageUrls = imageUrls;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorName() { return authorName; }
    public String getDate() { return date; }
    public List<String> getImageUrls() { return imageUrls; }
}
