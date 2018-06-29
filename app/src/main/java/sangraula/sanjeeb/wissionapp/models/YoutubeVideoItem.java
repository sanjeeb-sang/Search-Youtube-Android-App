package sangraula.sanjeeb.wissionapp.models;

public class YoutubeVideoItem {

    public String videoId;
    public String title;
    public String thumbnailUrl;
    public int likes;


    public YoutubeVideoItem(String title, String videoId, String thumbnailUrl, int likes) {

        this.title = title;
        this.videoId = videoId;
        this.thumbnailUrl = thumbnailUrl;
        this.likes = likes;
    }

}
