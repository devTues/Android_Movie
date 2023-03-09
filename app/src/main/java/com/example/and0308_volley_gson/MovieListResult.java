package com.example.and0308_volley_gson;

import java.util.ArrayList;

public class MovieListResult {
    // 박스오피스 검색 결과 전체를 저장할 MovieListResult 클래스
    String boxofficeType;
    String showRange;

    ArrayList<Movie> dailyBoxOfficeList = new ArrayList<Movie>();
}
