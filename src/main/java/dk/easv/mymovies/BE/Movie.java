package dk.easv.mymovies.BE;

import java.util.List;

public class Movie {
    private int id;
    private String name;
    private float pRating;
    private String fileLink;
    private String lastView;
    private float iRating;
    private String posterLink;
    private List<Category> categories;

    public Movie(int id, String name, float pRating, String fileLink, String lastView, float iRating, String posterLink, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.pRating = pRating;
        this.fileLink = fileLink;
        this.lastView = lastView;
        this.iRating = iRating;
        this.posterLink = posterLink;
        this.categories = categories;
    }

    public Movie(String name, float pRating, String fileLink, String lastView, float iRating, String posterLink) {
        this.name = name;
        this.pRating = pRating;
        this.fileLink = fileLink;
        this.lastView = lastView;
        this.iRating = iRating;
        this.posterLink = posterLink;
    }

    public Movie(int id, String name, Float pRating, String fileLink, String lastView, Float iRating, String posterLink) {
        this.id = id;
        this.name = name;
        this.pRating = pRating;
        this.fileLink = fileLink;
        this.lastView = lastView;
        this.iRating = iRating;
        this.posterLink = posterLink;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public float getpRating() {
        return this.pRating;
    }
    public void setpRating(float value) {
        this.pRating = value;
    }

    public String getFileLink() {
        return this.fileLink;
    }

    public void setFileLink(String value) {
        this.fileLink = value;
    }

    public String getLastView() {
        return this.lastView;
    }

    public void setLastView(String value) {
        this.lastView = value;
    }
    public float getiRating() {
        return this.iRating;
    }
    public void setiRating(float value) {
        this.iRating = value;
    }

    public String getPosterLink() {
        return this.posterLink;
    }
    public void setPosterLink(String value) {
        this.posterLink = value;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pRating='" + pRating + '\'' +
                ", fileLink='" + fileLink + '\'' +
                ", lastView='" + lastView + '\'' +
                ", iRating='" + iRating + '\'' +
                ", posterLink='" + posterLink + '\'' +
                '}';

    }

}
