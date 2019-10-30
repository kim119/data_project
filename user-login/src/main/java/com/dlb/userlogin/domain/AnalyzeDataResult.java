package com.dlb.userlogin.domain;

import java.io.Serializable;
import java.util.List;

public class AnalyzeDataResult implements Serializable {




    private Integer id;
    private List<MovieBean> movies;
    private List<MovieTypeBean> movieTypes;
    private List<ProvinceBean> provinces;

    public List<MovieBean> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieBean> movies) {
        this.movies = movies;
    }

    public List<MovieTypeBean> getMovieTypes() {
        return movieTypes;
    }

    public void setMovieTypes(List<MovieTypeBean> movieTypes) {
        this.movieTypes = movieTypes;
    }

    public List<ProvinceBean> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvinceBean> provinces) {
        this.provinces = provinces;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}
