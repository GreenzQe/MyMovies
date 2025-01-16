package dk.easv.mymovies.BE;

import java.util.HashMap;
import java.util.List;

public class Movie {
    private int id;
    private String name;
    private double pRating;
    private String fileLink;
    private String lastView;
    private double iRating;
    private String posterLink;
    private HashMap<String, Category> categories;

    public Movie(int id, String name, double pRating, String fileLink, String lastView, double iRating, String posterLink, HashMap<String, Category> categories) {
        this.id = id;
        this.name = name;
        this.pRating = pRating;
        this.fileLink = fileLink;
        this.lastView = lastView;
        this.iRating = iRating;
        this.posterLink = posterLink;
        this.categories = categories;
    }

    public Movie(String name, double pRating, String fileLink, String lastView, double iRating, String posterLink) {
        this.name = name;
        this.pRating = pRating;
        this.fileLink = fileLink;
        this.lastView = lastView;
        this.iRating = iRating;
        this.posterLink = posterLink;
    }

    public Movie(int id, String name, double pRating, String fileLink, String lastView, double iRating, String posterLink) {
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

    public double getpRating() {
        return this.pRating;
    }
    public void setpRating(double value) {
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
    public double getiRating() {
        return this.iRating;
    }
    public void setiRating(double value) {
        this.iRating = value;
    }

    public String getPosterLink() {
        return this.posterLink;
    }
    public void setPosterLink(String value) {
        this.posterLink = value;
    }

    public HashMap<String, Category> getCategories() {
        return categories;
    }

    public Category getCategory(String name) {
        return categories.get(name);
    }

    public void setCategories(HashMap<String, Category> categories) {
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
