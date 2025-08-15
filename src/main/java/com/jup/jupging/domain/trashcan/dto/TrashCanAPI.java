package com.jup.jupging.domain.trashcan.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrashCanAPI {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
        private String type;
    }

    @Data
    public static class Body {
        private List<Item> items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Item {
        private String instlPlcNm;      // 설치 장소명
        private String ctpvNm;          // 시도명
        private String sggNm;           // 시군구명
        private String lctnRoadNm;      // 도로명 주소
        private String lctnLotnoAddr;   // 지번 주소
        private String lat;             // 위도
        private String lot;             // 경도
        private String trashCanKnd;     // 쓰레기통 종류
        private String mngInstNm;       // 관리 기관명
        private String mngInstTelno;    // 관리 기관 전화번호
        private String actlPstn;
        private String crtrYmd;
        private String insttCode;
        private String insttNm;


    }

}
