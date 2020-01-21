package com.example.hamadaelsha3r.the_movie_application;

class MovieTrailer {

    String trailer_name;
    String trailer_key;
    private String trailer_id;

    public MovieTrailer(String trailer_name, String trailer_key, String trailer_id) {
        this.trailer_name = trailer_name;
        this.trailer_key = trailer_key;
        this.trailer_id = trailer_id;
    }


    public String getTrailer_name() {
        return trailer_name;
    }

    public void setTrailer_name(String trailer_name) {
        this.trailer_name = trailer_name;
    }

    public String getTrailer_key() {
        return trailer_key;
    }

    public void setTrailer_key(String trailer_key) {
        this.trailer_key = trailer_key;
    }

    public String getTrailer_id() {
        return trailer_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }
}
