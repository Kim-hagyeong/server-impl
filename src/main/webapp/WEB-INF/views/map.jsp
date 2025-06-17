<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Tmap 지도</title>
    <!-- Tmap v1 스크립트 -->
    <script src="https://apis.openapi.sk.com/tmap/js?version=1&appKey=여기에_발급받은_키"></script>
    <style>
      body {
        font-family: "Noto Sans KR", sans-serif;
        background: #f5f5f5;
      }
      .search-container {
        max-width: 500px;
        margin: 20px auto;
        display: flex;
        gap: 10px;
      }
      .map-container {
        display: flex;
        gap: 20px;
        max-width: 1200px;
        margin: 0 auto;
      }
      .search-results {
        width: 30%;
        background: white;
        border-radius: 12px;
        padding: 20px;
      }
      #map_div {
        width: 60%;
        height: 610px;
        border-radius: 12px;
      }
    </style>
  </head>
  <body>
    <div class="search-container">
      <input
        type="text"
        id="searchKeyword"
        name="searchKeyword"
        value="서울시"
        placeholder="검색어를 입력하세요"
      />
      <button id="btn_select">검색</button>
    </div>

    <div class="map-container">
      <div class="search-results">
        <div class="title"><strong>Search</strong> Results</div>
        <div class="rst_wrap">
          <div class="rst">
            <ul id="searchResult" name="searchResult">
              <li>검색결과</li>
            </ul>
          </div>
        </div>
      </div>
      <div id="map_div"></div>
    </div>

    <script>
      var contextPath = "${pageContext.request.contextPath}";
      let map, markerLayer;
      let markers = [];

      function initTmap() {
        // 지도 생성
        map = new Tmap.Map("map_div", {
          center: new Tmap.LonLat(126.98702028, 37.5652045).transform(
            "EPSG:4326",
            "EPSG:3857"
          ),
          width: "100%",
          height: "100%",
          zoom: 17,
        });

        // 마커 레이어 생성 및 지도에 추가
        markerLayer = new Tmap.Layer.Markers();
        map.addLayer(markerLayer);

        // 검색 버튼 이벤트
        document
          .getElementById("btn_select")
          .addEventListener("click", function () {
            const searchKeyword =
              document.getElementById("searchKeyword").value;

            fetch(contextPath + "/searchLocation.map", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({ keyword: searchKeyword }),
            })
              .then((response) => response.json())
              .then((data) => {
                // 기존 마커 삭제
                clearMarkers();

                // 검색 결과 리스트 생성
                let innerHtml = "";
                data.forEach((poi, index) => {
                  // 커스텀 마커 이미지 경로 (0~9까지만)
                  let iconUrl =
                    typeof index === "number" && index >= 0 && index <= 9
                      ? contextPath +
                        "/upload/tmap/marker/pin_b_m_" +
                        index +
                        ".png"
                      : null;
                  addMarker(poi.lon, poi.lat, poi.name, iconUrl);
                  if (iconUrl) {
                    innerHtml += `<li><img src='${iconUrl}' style='width:24px;height:38px;vertical-align:middle;'/> <span>${poi.name}</span></li>`;
                  } else {
                    innerHtml += `<li><span>${poi.name}</span></li>`;
                  }
                });
                document.getElementById("searchResult").innerHTML = innerHtml;
              })
              .catch((error) => console.error("Error:", error));
          });
      }

      // 마커 추가 함수 (커스텀 이미지 지원)
      function addMarker(lon, lat, name, iconUrl) {
        const lonlat = new Tmap.LonLat(lon, lat).transform(
          "EPSG:4326",
          "EPSG:3857"
        );
        let marker;
        if (iconUrl) {
          // 커스텀 마커 이미지
          marker = new Tmap.Marker(
            lonlat,
            iconUrl,
            new Tmap.Size(24, 38),
            new Tmap.Pixel(-(24 / 2), -38) // offset: 이미지 하단이 좌표에 맞게
          );
        } else {
          // 기본 마커
          marker = new Tmap.Marker(lonlat);
        }
        markerLayer.addMarker(marker);
        markers.push(marker);
      }

      // 마커 전체 삭제 함수
      function clearMarkers() {
        for (let marker of markers) {
          markerLayer.removeMarker(marker);
        }
        markers = [];
      }

      document.addEventListener("DOMContentLoaded", initTmap);
    </script>
  </body>
</html>
