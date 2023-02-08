package com.project.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// 추후에 void를 list 형태로 리턴해서 컨트롤러에서 DB에 넣을 수 있도록 수정 예정
// i값, j값, 선택자, 변수명 등은 사이트별로 맞게 조절해서 사용할 것
// 얻어와야할 값: 지점명, 주소, 무슨 편의점인지(CU, GS 등...) => MapVO 참고
// 지도는 지점명 정보를 얻어오기 위해 시도 정보, 구군 정보도 추가로 크롤링해서 반복문으로 넣어줘야 함
public class MapManager {
	
	String targetSite = "";
	Document doc = null;

	public ArrayList<MapVO> getGS25Map() {
		
		ArrayList<MapVO> list = new ArrayList<MapVO>();
		
		targetSite = "http://gs25.gsretail.com/gscvs/ko/store-services/locationList?CSRFToken=8f35eb2a-1562-44be-9ff0-390b02611142";
		
		try {
			int page = 0;
			while(true) {
				page++;
				// 이번에는 data가 많이 필요할 것으로 예상이 되어 맵 클래스를 이용해준다.
				Map<String, String> data = new HashMap<String, String>();
				data.put("pageNum", page+"");
				data.put("pageSize", "300"); // 원래 사이트 내 pageSize는 5라 5개씩 나오는데 300으로 늘리니까 300개로 잘 나온다!
				data.put("searchShopName", "");
				data.put("searchTypeService", "");
				data.put("searchSido", "");
				data.put("searchGugun", "");
				data.put("searchDong", "");
				data.put("searchType", "");
				data.put("searchTypeService", "0");
				data.put("searchTypeToto", "0");
				data.put("searchTypeCafe25", "0");
				data.put("searchTypeInstant", "0");
				data.put("searchTypeDrug", "0");
				data.put("searchTypeSelf25", "0");
				data.put("searchTypePost", "0");
				data.put("searchTypeATM", "0");
				data.put("searchTypeWithdrawal", "0");
				data.put("searchTypeTaxrefund", "0");
				data.put("searchTypeSmartAtm", "0");
				data.put("searchTypeSelfCookingUtensils", "0");
				data.put("searchTypeDeliveryService", "0");
				
				// gs 매장검색페이지 접속
				Thread.sleep(500); 
				Connection.Response response = Jsoup.connect(targetSite)
						.timeout(3000)
						.header("Origin", "http://gs25.gsretail.com")
						.header("Referer", "http://gs25.gsretail.com/gscvs/ko/store-services/locations")
						.header("Accept", "application/json, text/javascript, */*; q=0.01")
						.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
						.header("Accept-Encoding", "gzip, deflate")
						.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
						.data(data)
						.ignoreContentType(true)
						.method(Connection.Method.GET)
						.execute();
				
				// 페이지에 접속했으면 파싱 후 값을 doc에 전달한다.
				doc = response.parse();
				
				// text() 메소드를 이용해서 내용을 문자열로 옮겨준다.
				String str = doc.select("body").text().replaceAll("\\\\", "").substring(1); // "\" 제거, 첫번째 글자 제거
				
				str = str.substring(0, str.length() - 1); // 마지막 글자 제거
				
				// JSON데이터 형태로 만든 문자열 str을 넣어 JSON Object 로 만들어 준다.
				JSONObject jsonObj = (JSONObject) new JSONParser().parse(str);
				
				// JSON Object에서 필요한 정보가 있는, 즉 results 배열을 추출한다.
				JSONArray jsonArr = (JSONArray) jsonObj.get("results");	
				
				if(jsonArr.size() == 0) {
					break;
				}
				
				// 배열의 사이즈만큼 배열을 돌려준다.
				for(int i=0; i<jsonArr.size(); i++) {
					// 추출한 배열을 새로운 JSON Object에 넣어주고, 필요한 정보들을 빼온다.
					JSONObject gs25Object = (JSONObject) jsonArr.get(i);
					
					String storeName = gs25Object.get("shopName").toString().replace("GS25", ""); // 지점명
					String address = gs25Object.get("address").toString(); // 주소
					
					MapVO mapVO = new MapVO();
					mapVO.setStoreName(storeName);
					mapVO.setWhichCVS("GS25");
					mapVO.setAddress(address.replace("충청북도", "충북").replace("강원도", "강원").replace("경기도", "경기").replace("경상남도", "경남").replace("경상북도", "경북")
							.replace("광주광역시", "광주").replace("대구광역시", "대구").replace("대구시", "대구").replace("대전광역시", "대전").replace("부산광역시", "부산").replace("부산시", "부산")
							.replace("서울특별시", "서울").replace("서울시", "서울").replace("세종특별자치시", "세종").replace("울산광역시", "울산").replace("인천광역시", "인천").replace("인천시", "인천")
							.replace("전라남도", "전남").replace("전라북도", "전북").replace("제주도", "제주").replace("제주특별자치도", "제주").replace("충청남도", "충남"));
					
					list.add(mapVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<MapVO> getCUMap() {
		
		ArrayList<MapVO> list = new ArrayList<MapVO>();		
		targetSite = "https://cu.bgfretail.com/store/list_Ajax.do";
		
		try {
			int page = 0;
			while(true) {
				page++;
				Map<String, String> data = new HashMap<String, String>();
				data.put("pageIndex", page+"");
				data.put("listType", "");
				data.put("jumpoCode", "");
				data.put("jumpoLotto", "");
				data.put("jumpoToto", "");
				data.put("jumpoCash", "");
				data.put("jumpoHour", "");
				data.put("jumpoCafe", "");
				data.put("jumpoDelivery", "");
				data.put("jumpoBakery", "");
				data.put("jumpoFry", "");
				data.put("jumpoMultiDevice", "");
				data.put("jumpoPosCash", "");
				data.put("jumpoBattery", "");
				data.put("jumpoAdderss", "");
				data.put("jumpoSido", "");
				data.put("jumpoGugun", "");
				data.put("jumpodong", "");
				data.put("user_id", "");
				data.put("sido", "");
				data.put("Gugun", "");
				data.put("jumpoName", "");
				
				Thread.sleep(500); 
				doc = Jsoup.connect(targetSite)
						.data(data)
						.post();
				
//				System.out.println(doc.select("div.detail_info > address"));
				
				Elements spans = doc.select("span.name");
				Elements addresses = doc.select("div.detail_info > address");
				
				for(int i=0; i<spans.size(); i++) {
					String storeName = spans.get(i).text();
					String address = addresses.get(i).text();
					
					MapVO mapVO = new MapVO();
					mapVO.setStoreName(storeName);
					mapVO.setWhichCVS("CU");
					mapVO.setAddress(address.replace("충청북도", "충북").replace("강원도", "강원").replace("경기도", "경기").replace("경상남도", "경남").replace("경상북도", "경북")
							.replace("광주광역시", "광주").replace("대구광역시", "대구").replace("대구시", "대구").replace("대전광역시", "대전").replace("부산광역시", "부산").replace("부산시", "부산")
							.replace("서울특별시", "서울").replace("서울시", "서울").replace("세종특별자치시", "세종").replace("울산광역시", "울산").replace("인천광역시", "인천").replace("인천시", "인천")
							.replace("전라남도", "전남").replace("전라북도", "전북").replace("제주도", "제주").replace("제주특별자치도", "제주").replace("충청남도", "충남"));
					
					list.add(mapVO);
				}
				
				if(spans.size() == 0) {
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<MapVO> getSevenElevenMap() {

	ArrayList<MapVO> list = new ArrayList<MapVO>();
	targetSite = "https://www.7-eleven.co.kr/util/storeLayerPop.asp";
	
	try {
		doc = Jsoup.connect(targetSite).get();
		Elements sidoList = doc.select("select#storeLaySido > option"); // 시도를 가져온다.
		for(int i=1; i<sidoList.size(); i++) { // 가져온 시도만큼 반복을 돌려준다.(i=1인 이유는 맨 첨 옵션값이 시/도이기 때문)
			String sido = sidoList.get(i).text();
			
			targetSite = "https://www.7-eleven.co.kr/library/asp/StoreGetGugun.asp";
			doc = Jsoup.connect(targetSite)
					.data("Sido", sido) // 가져온 시도값을 데이터로 넣어준다.
					.data("selName", "storeLayGu")
					.post();
			Elements gugunList = doc.select("select > option"); // 구군리스트를 가져온다.
			for(int j=1; j<gugunList.size(); j++) { // 가져온 구군만큼 반복을 돌려준다.(i=1인 이유는 위와 동일)
				String gugun = gugunList.get(j).text();
				
				targetSite = "https://www.7-eleven.co.kr/util/storeLayerPop.asp";
				Thread.sleep(500);
				doc = Jsoup.connect(targetSite)
						.data("storeLaySido", sido) // 시도값과
						.data("storeLayGu", gugun) // 구군값을 데이터로 넣어준다.
						.data("hiddentext", "none")
						.post();
				Elements storeList = doc.select(".type02 a");
				for(int k=0; k<storeList.size(); k++) {
					Element element = storeList.get(k);
					Elements spanElement = element.select("span");
					
					String storeName = spanElement.get(0).text();
					String address = spanElement.get(1).text();
					
					MapVO mapVO = new MapVO();
					mapVO.setStoreName(storeName);
					mapVO.setWhichCVS("세븐일레븐");
					mapVO.setAddress(address);
					mapVO.setAddress(address.replace("충청북도", "충북").replace("강원도", "강원").replace("경기도", "경기").replace("경상남도", "경남").replace("경상북도", "경북")
							.replace("광주광역시", "광주").replace("대구광역시", "대구").replace("대구시", "대구").replace("대전광역시", "대전").replace("부산광역시", "부산").replace("부산시", "부산")
							.replace("서울특별시", "서울").replace("서울시", "서울").replace("세종특별자치시", "세종").replace("울산광역시", "울산").replace("인천광역시", "인천").replace("인천시", "인천")
							.replace("전라남도", "전남").replace("전라북도", "전북").replace("제주도", "제주").replace("제주특별자치도", "제주").replace("충청남도", "충남"));
					list.add(mapVO);
				}
			}
		}
	} catch(Exception e) {
		e.printStackTrace();
	}

	return list;
}
	
	public ArrayList<MapVO> getEmart24Map() {
		ArrayList<MapVO> list = new ArrayList<MapVO>();
		
		try {
			int page = 0;
			while(true) {
				page++;
				targetSite = "http://emart24.co.kr/api1/store?page="+page;
				
				Thread.sleep(500);
				doc = Jsoup.connect(targetSite)
						.ignoreContentType(true)
						.get();
				
				String str = doc.select("body").text();
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(str);
				JSONArray jsonArray = (JSONArray) jsonObject.get("data");
				
				if(jsonArray.size() == 0) {
					break;
				}
				
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject emartObject = (JSONObject) jsonArray.get(i);
					String storeName = emartObject.get("TITLE").toString();
					String address = emartObject.get("ADDRESS").toString();
					
					MapVO mapVO = new MapVO();
					mapVO.setStoreName(storeName);
					mapVO.setWhichCVS("이마트24");
					mapVO.setAddress(address.replace("충청북도", "충북").replace("강원도", "강원").replace("경기도", "경기").replace("경상남도", "경남").replace("경상북도", "경북")
							.replace("광주광역시", "광주").replace("대구광역시", "대구").replace("대구시", "대구").replace("대전광역시", "대전").replace("부산광역시", "부산").replace("부산시", "부산")
							.replace("서울특별시", "서울").replace("서울시", "서울").replace("세종특별자치시", "세종").replace("울산광역시", "울산").replace("인천광역시", "인천").replace("인천시", "인천")
							.replace("전라남도", "전남").replace("전라북도", "전북").replace("제주도", "제주").replace("제주특별자치도", "제주").replace("충청남도", "충남"));
					
					list.add(mapVO);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}	
	
	public ArrayList<MapVO> getMinistopMap() {
		ArrayList<MapVO> list = new ArrayList<MapVO>();

		HashMap<String, String> data = new HashMap<String, String>();
		targetSite = "https://www.ministop.co.kr/MiniStopHomePage/page/store/store.do";

		try {
			doc = Jsoup.connect(targetSite).get();
			Elements sido = doc.select("#area1 > option");
//				System.out.println(sido);

			for (int i = 1; i <= sido.size() - 1; i++) {
				Element indexSido = sido.get(i);
				Elements optionSido = indexSido.select("option");
				String storeLaySido = optionSido.text();
//				System.out.println(storeLaySido);
				data.put("pageId", "store/store");
				data.put("sqlnum", "3");
				data.put("paramInfo", i + ":-1:-1:");
				data.put("pageNum", "1");
				data.put("sortGu", "");
				data.put("tm", "1673417832007");

				Thread.sleep(500);
				targetSite = "https://www.ministop.co.kr/MiniStopHomePage/page/querySimple.do";
				doc = Jsoup.connect(targetSite).data(data).ignoreContentType(true).post();
				String str = doc.select("body").text();
//							System.out.println(str);
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(str);
				JSONArray jsonArray = (JSONArray) jsonObject.get("recordList");

				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject msObject = (JSONObject) jsonArray.get(j);
//								System.out.println(msObject);
					String map = msObject.get("fields").toString();

					map = map.replaceAll("\"", "");
					map = map.replaceAll("\\[", "");
					map = map.replaceAll("\\]", "");
//								System.out.println(map);
					String mapArr[] = map.split(",");
					String storeName = mapArr[0];
					String address = mapArr[1];
//								System.out.println(storeName);
//								System.out.println(address);
					address.replace("충청북도", "충북");
					
					MapVO mapVO = new MapVO();
					mapVO.setStoreName(storeName);
					mapVO.setWhichCVS("ministop");
					mapVO.setAddress(address.replace("충청북도", "충북").replace("강원도", "강원").replace("경기도", "경기").replace("경상남도", "경남").replace("경상북도", "경북")
							.replace("광주광역시", "광주").replace("대구광역시", "대구").replace("대구시", "대구").replace("대전광역시", "대전").replace("부산광역시", "부산").replace("부산시", "부산")
							.replace("서울특별시", "서울").replace("서울시", "서울").replace("세종특별자치시", "세종").replace("울산광역시", "울산").replace("인천광역시", "인천").replace("인천시", "인천")
							.replace("전라남도", "전남").replace("전라북도", "전북").replace("제주도", "제주").replace("제주특별자치도", "제주").replace("충청남도", "충남"));

					list.add(mapVO);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public ArrayList<MapVO> getCspaceMap() {
		
		ArrayList<MapVO> list = new ArrayList<MapVO>();
		for(int i=1; i<=21; i++) {
			targetSite = 
					String.format("https://www.cspace.co.kr/intro/store.html?address_sido_s=&address_gugun_s=&store_name_s="
							+ "&provided_services_s=&id_position_move=calSelId&cpage=%d", i);
			try {
				Thread.sleep(500); 
				doc = Jsoup.connect(targetSite).get();
				Elements elements = doc.select("tr.store-list-item");
				// System.out.println(elements);
				for(Element element : elements) {
					Elements ele = element.select("td");
					// System.out.println(ele); // 1번 지점명, 2번 주소
					String storeName = ele.get(1).text(); // 지점명
					String address = ele.get(2).text(); // 주소
					
					// 가져온 값을 MapVO에 넣어준다. 
					MapVO mapVO = new MapVO();
					mapVO.setStoreName(storeName);
					mapVO.setWhichCVS("C·SPACE");
					mapVO.setAddress(address.replace("충청북도", "충북").replace("강원도", "강원").replace("경기도", "경기").replace("경상남도", "경남").replace("경상북도", "경북")
							.replace("광주광역시", "광주").replace("대구광역시", "대구").replace("대구시", "대구").replace("대전광역시", "대전").replace("부산광역시", "부산").replace("부산시", "부산")
							.replace("서울특별시", "서울").replace("서울시", "서울").replace("세종특별자치시", "세종").replace("울산광역시", "울산").replace("인천광역시", "인천").replace("인천시", "인천")
							.replace("전라남도", "전남").replace("전라북도", "전북").replace("제주도", "제주").replace("제주특별자치도", "제주").replace("충청남도", "충남"));
					
					list.add(mapVO);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
}
