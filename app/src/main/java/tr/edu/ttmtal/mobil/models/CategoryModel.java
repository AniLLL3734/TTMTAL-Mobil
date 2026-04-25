package tr.edu.ttmtal.mobil.models;

public class CategoryModel {
    private String id;
    private String name;
    private String imageUrl;
    private String content;

    public CategoryModel() {} // Required for Firebase

    public CategoryModel(String id, String name, String imageUrl, String content) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getContent() { return content; }
}
