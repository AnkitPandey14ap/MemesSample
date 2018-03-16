package space.apple.three.memes.model;

/**
 * Created by ankit on 16/3/18.
 */

public class Meme {
    private String url;
    private String like;

    public Meme(String url, String like) {
        this.url = url;
        this.like = like;
    }

    public Meme() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
