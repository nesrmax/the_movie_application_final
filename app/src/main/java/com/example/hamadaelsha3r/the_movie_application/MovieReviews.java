package com.example.hamadaelsha3r.the_movie_application;


class MovieReviews {

    private String author;
    private String conrent;
    private String id;
    private String link;


    public MovieReviews(String author, String conrent, String id, String link) {
        this.author = author;
        this.conrent = conrent;
        this.id = id;
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getConrent() {
        return conrent;
    }

    public void setConrent(String conrent) {
        this.conrent = conrent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
