package com.howaboutthisway.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MapServiceImpl implements MapService {
    private static final String TMAP_API_KEY = "SG5XFnAWzC1kj9f58OoQR3iAlDLG99Lm393Tu0DS";

    @Override
    public void saveLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 요청 본문 읽기
        StringBuilder buffer = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        }

        String body = buffer.toString();
        System.out.println("📍 받은 좌표 JSON: " + body);

        // 응답
        try (PrintWriter out = response.getWriter()) {
            out.print("좌표 수신 완료!");
        }
    }

    @Override
    public void getLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            out.print("좌표 조회 기능은 아직 구현되지 않았습니다.");
        }
    }

    @Override
    public void searchLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder buffer = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        }
        System.out.println("받은 데이터: " + buffer.toString());

        JSONObject requestJson = new JSONObject(buffer.toString());
        String keyword = requestJson.getString("keyword");

        // Tmap API 호출
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tmapRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://apis.openapi.sk.com/tmap/pois?version=1&format=json&searchKeyword=" + keyword + "&resCoordType=EPSG3857&reqCoordType=WGS84GEO&count=10"))
                .header("appKey", TMAP_API_KEY)
                .GET()
                .build();

        try {
            HttpResponse<String> tmapResponse = client.send(tmapRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("Tmap 응답: " + tmapResponse.body());
            JSONObject tmapJson = new JSONObject(tmapResponse.body());
            JSONArray pois = tmapJson.getJSONObject("searchPoiInfo").getJSONObject("pois").getJSONArray("poi");

            List<JSONObject> resultList = new ArrayList<>();
            for (int i = 0; i < pois.length(); i++) {
                JSONObject poi = pois.getJSONObject(i);
                JSONObject result = new JSONObject();
                result.put("name", poi.getString("name"));
                result.put("lat", poi.getDouble("noorLat"));
                result.put("lon", poi.getDouble("noorLon"));
                resultList.add(result);
            }

            try (PrintWriter out = response.getWriter()) {
                out.print(new JSONArray(resultList).toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IOException("Tmap API 호출 중 오류 발생", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("기타 오류 발생", e);
        }
    }
}
