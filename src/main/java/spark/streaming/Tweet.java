package spark.streaming;

import twitter4j.HashtagEntity;

import java.util.Date;


public class Tweet {
    private String user;
    private String text;
    private Date createdAt;
    private HashtagEntity[] hashTags;
    private Double[] geolocation;

    public Tweet(String name, String text, Date createdAt, Double[] geoLocation) {
        this.user = name;
        this.text = text;
        this.createdAt = createdAt;
        this.geolocation = geoLocation;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public HashtagEntity[] getHashTags() {
        return hashTags;
    }

    public void setHashTags(HashtagEntity[] hashTags) {
        this.hashTags = hashTags;
    }

    public Double[] getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Double[] geolocation) {
        this.geolocation = geolocation;
    }
}
