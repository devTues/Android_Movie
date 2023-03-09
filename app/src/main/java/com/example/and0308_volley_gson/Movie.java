package com.example.and0308_volley_gson;

/*
* JSON 객체의 각 파라미터에 해당하는 멤버변수를 선언하고
* 파싱된 데이터를 저장하는 용도의 클래스 = DTO(JavaBean) 클래스 역할
* (=> 1개 영화 정보를 저장하는 객체)
* 주의! JSON 객체의 파라미터 이름과 변수명이 동일해야함
* https://www.kobis.or.kr/ 응답구조 부분 참조
* */
public class Movie {
    String audiAcc;
    String audiChange;
    String audiCnt;
    String audiInten;
    String movieCd;
    String movieNm;
    String openDt;
    String rank;
    String rankInten;
    String rankOldAndNew;
    String rnum;
    String salesAcc;
    String salesAmt;
    String salesChange;
    String salesInten;
    String salesShare;
    String scrnCnt;
    String showCnt;

    // Naver API 포스터 용도
    String image;
}
