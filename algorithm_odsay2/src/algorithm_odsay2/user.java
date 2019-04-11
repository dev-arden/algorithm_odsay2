package algorithm_odsay2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

//각 user버퍼를 관리하기 위함,,,,
//그럼 여기에는 어떤 변수들과 함수들이 올 수 있을 것인가,,,,,,,,,
//일단 user객체 당 버퍼가 만들어져야 함,,, 버퍼를 만들자!!!! 그러면 어떤 형태의 버퍼를 만들 것인가,,,,,
//버퍼에는 지하철 역들의 코드가 담길 것임,,,일단 arraylist로 갑시다,,,,
public class user {
	public Vector<Integer> codestore;
	public Vector<Integer> check;
	public db checkTransfer;
	public boolean sopt; // 최단거리 최소환승 부분임
	public Vector<rank> rankResult;
	public Vector<String> routestore;

	public user() {
		this.codestore = new Vector<Integer>();
		this.check = new Vector<Integer>();
		this.checkTransfer = new db();
		this.sopt = true;
		this.rankResult = new Vector<rank>();
		this.routestore = new Vector<String>();
	}

	
	static Vector<rank> rankstore = new Vector<rank>();
	public void findcode(int tt) throws Exception {
		// 이 부분은 userinfo의 코드를 받아오면 그 코드의 전역, 다음역의 코드를 api를 이용해서 json으로 받아오는 것
		// userinfo의 첫번째 행의 이름으로 code 검색
		JSONParser jsonparser = new JSONParser();
		JSONObject jsonobject = (JSONObject) jsonparser.parse(readUrl(tt));
		JSONObject json = (JSONObject) jsonobject.get("result");
		JSONObject json2 = (JSONObject) json.get("prevOBJ");// 전역
		JSONObject json3 = (JSONObject) json.get("nextOBJ");// 다음역
		JSONArray array = (JSONArray) json2.get("station");// 전역의 정보들
		JSONArray array2 = (JSONArray) json3.get("station");// 다음역의 정보들
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject entity = (JSONObject) array.get(i);
				// JSONObject entity2 = (JSONObject) array2.get(i);

				long code = (long) entity.get("stationID");// 전역의 코드 담기
				/*
				 * 여기서 (int)code에 해당하는 디비 뒤지기.... 만약 환승 호선이 여러개인 경우도 있을 수 있으니까 그거를 담는 경우도
				 * vector로 담고 그래서 밑에 중복검사하고 담길ㄸ도 포문으로 돌려야 함
				 */
				int codesize = codestore.size();
				int issame = 0; // int issame2 = 0;
				// 여기서 코드스토어 중복 검사가 필요함
				// 오류가 나는이유,,,일일이 다 검사를 하면 같지 않을때마다 계속 하게 되니까 담기게 됨,,,음,,,,,,
				for (int j = 0; j < codesize; j++) {
					if (codestore.get(j) == (int) code) {
						issame++;
					}
				}
				if (issame == 0) {
					checkTransfer.isTransfer(code);
				} else {
					issame = 0;// issame초기화
				}
				// checkTransfer.isTransfer(code);
				// 벡터를 리턴함
				for (int j = 0; j < checkTransfer.transfer.size(); j++) {
					for (int k = 0; k < codesize; k++) {
						if (codestore.get(k) == checkTransfer.transfer.get(j)) {
							issame++;
						}
					}
					if (issame == 0) {
						codestore.add(checkTransfer.transfer.get(j));
					}
				}

				checkTransfer.transfer.removeAllElements();
			}
		}
		// System.out.print(codestore.get(0) + "," +codestore.get(1));//전역이랑 다음역 코드보기
		if (array2 != null) {
			for (int i = 0; i < array2.size(); i++) {
				// JSONObject entity = (JSONObject) array.get(i);
				JSONObject entity2 = (JSONObject) array2.get(i);

				// long code = (long) entity.get("stationID");// 전역의 코드 담기
				long code2 = (long) entity2.get("stationID");// 다음역의 코드 담기
				// codestore.add((int)code);
				// codestore.add((int)code2);
				int codesize = codestore.size();
				// int issame = 0;
				int issame2 = 0;
				// 여기서 코드스토어 중복 검사가 필요함
				// 오류가 나는이유,,,일일이 다 검사를 하면 같지 않을때마다 계속 하게 되니까 담기게 됨,,,음,,,,,,
				for (int j = 0; j < codesize; j++) {
					if (codestore.get(j) == (int) code2) {
						issame2++;
					}
				}
				if (issame2 == 0) {
					checkTransfer.isTransfer(code2);
				} else {
					issame2 = 0;// issame초기화
				}
				// checkTransfer.isTransfer(code);
				// 벡터를 리턴함
				for (int j = 0; j < checkTransfer.transfer.size(); j++) {
					for (int k = 0; k < codesize; k++) {
						if (codestore.get(k) == checkTransfer.transfer.get(j)) {
							issame2++;
						}
					}
					if (issame2 == 0) {
						codestore.add(checkTransfer.transfer.get(j));
					}
				}

				checkTransfer.transfer.removeAllElements();
			}
		}
		// 여기서 또 새로운 함수를 호출해야함 - 어떤 함수냐하면 새롭게 코드스토어에 담긴 코드들에 해당하는 지하철이 환승역인지 아닌지 알아내서
		// 환승역이면 다른 호선에 해당하는 코드를 담아야함
		// 지금 머릿속에 드는 생각은 코드스토어에 새롭게 담긴 코드두개에 대해서 subwaynogada에 있는 디비에서 이름을 뽑고, 또 그 이름에
		// 해당하는 나머지 코드가 잇는지를 검사하는거지
		// 애초에 저기 담을 때 중복 검사를 하기 때문에 일단 한번 시도해보자,,,,

		// System.out.print(codestore.get(0) + "," +codestore.get(1));//전역이랑 다음역 코드보기
	}

	private String readUrl(int parameter) throws Exception {
		BufferedReader reader = null;

		try {
			URL url = new URL("https://api.odsay.com/v1/api/subwayStationInfo?lang=0&" + "stationID="
					+ codestore.get(parameter) + "&apiKey=9wndy8Mwrj6EeQZKf1Z9kusSZjU+vBpEdeDwCokXgy0");
			// WcVpRfZ6U%2BAuKf8AgOTZapx9edixkIvmJLWnT9KgiaE-하이드아웃
			// 15XH4EhsIQGTKIwZAjii5dwtmXtv%2BdVulD4QWniB%2Bjg-히수집
			// 9loymI1RM20ytIKmWKFe0x8arsNpYKoPSgHLoGhzANE-은비집
			// FKNgHXbbPDpB2qoqgvkmA3DAKApfxjOfbp%2Fz%2F0gWnOU-학교

			reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

			StringBuffer buffer = new StringBuffer();

			String str;

			while ((str = reader.readLine()) != null) {
				buffer.append(str);
			}

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public Vector<rank> makeRanking(Vector<String> commonResult) throws Exception {
		// Vector<rank> rank1 = new Vector<rank>();
		
		// rank test = new rank();
		// rank test2 = new rank();
		// Vector<rank> rank2 = new Vector<rank>();
		rankstore.clear();
		Vector<Integer> tempCommonSet = new Vector<Integer>();
		db noduplicate = new db();

		// 여기서 받아온 commonResult의 요소들에 대하여 해당하는 코드를 tempConnonset에 넣어주면 됨
		// 환승호선인 경우도 그냥 상관없이 무조건 받아오는 개수는 한개로하자
		for (int i = 0; i < commonResult.size(); i++) {
			// 디비안에 함수를 새로 만들자
			noduplicate.onlyonecode(commonResult.get(i));
		}

		// System.out.println(noduplicate.onlyone.size());

		for (int i = 0; i < noduplicate.onlyone.size(); i++) {
			tempCommonSet.add(noduplicate.onlyone.get(i));
		}

		noduplicate.onlyone.removeAllElements();

		for (int i = 0; i < tempCommonSet.size(); i++) {
			rank tempRank = new rank();

			if (codestore.get(0).equals(tempCommonSet.get(i)) != true) {// 만약 다르다면
				tempRank.id = tempCommonSet.elementAt(i);
				tempRank.count = makeRoute(tempCommonSet.get(i), commonResult.get(i)).count;
				tempRank.change = makeRoute(tempCommonSet.get(i), commonResult.get(i)).change;
				tempRank.time = makeRoute(tempCommonSet.get(i), commonResult.get(i)).time;
				tempRank.idName = makeRoute(tempCommonSet.get(i), commonResult.get(i)).idName;
				// System.out.println("idName :"+ tempRank.idName);

			} else {
				tempRank.id = tempCommonSet.elementAt(i);
				tempRank.count = 0;
				tempRank.change = 0;
				tempRank.time = 0;
				tempRank.idName = commonResult.get(i);
			}
			rankstore.add(tempRank);
		}

		// for(int i=0;i<rankstore.size();i++) {
		// System.out.println("------------------------------------------------");
		// System.out.println(rankstore.get(i).count+", "+rankstore.get(i).id);
		// }

		for (int i = 0; i < rankstore.size(); i++) {
			if (rankstore.get(i).key == false)// 최단거리
				rankstore.get(i).setTotal0();
			else // 최소환승
				rankstore.get(i).setTotal1();
		}

		for (int i = 0; i < rankstore.size(); i++) {
			for (int j = 0; j < rankstore.size() - 1; j++) {
				if (rankstore.get(j).total > rankstore.get(j + 1).total) {
					// System.out.print(rankstore.get(j).count + ",");

					rank test = new rank();
					rank test2 = new rank();
					test.id = rankstore.elementAt(j).id;
					test.idName = rankstore.elementAt(j).idName;
					test.count = rankstore.elementAt(j).count;
					test.change = rankstore.elementAt(j).change;
					test.key = rankstore.elementAt(j).key;
					test.time = rankstore.elementAt(j).time;
					test.total = rankstore.elementAt(j).total;

					test2.id = rankstore.elementAt(j + 1).id;
					test2.idName = rankstore.elementAt(j + 1).idName;
					test2.count = rankstore.elementAt(j + 1).count;
					test2.change = rankstore.elementAt(j + 1).change;
					test2.key = rankstore.elementAt(j + 1).key;
					test2.time = rankstore.elementAt(j + 1).time;
					test2.total = rankstore.elementAt(j + 1).total;

					rankstore.get(j).id = test2.id;
					rankstore.get(j).count = test2.count;

					rankstore.get(j).idName = test2.idName;
					rankstore.get(j).change = test2.change;
					rankstore.get(j).key = test2.key;
					rankstore.get(j).time = test2.time;
					rankstore.get(j).total = test2.total;

					rankstore.get(j + 1).id = test.id;
					rankstore.get(j + 1).count = test.count;
					rankstore.get(j + 1).idName = test.idName;
					rankstore.get(j + 1).change = test.change;
					rankstore.get(j + 1).key = test.key;
					rankstore.get(j + 1).time = test.time;
					rankstore.get(j + 1).total = test.total;

					// rankstore.get(j).id = test2.id;
					// System.out.print(rankstore.get(j).id + ",");
					// rankstore.get(j).count = test2.count;
					// System.out.println(rankstore.get(j).id);
					// rankstore.get(j + 1).id = test.id;
					// System.out.print(rankstore.get(j).id+",");
					// rankstore.get(j + 1).count = test.count;
					// System.out.println(rankstore.get(j).id);
					// rankstore.set(j, rankstore.elementAt(j + 1));
					// rankstore.set(j + 1, test);
					// System.out.println(rank.get(j).count+", "+rank.get(j).id+",
					// "+rank.get(j+1).count+", "+rank.get(j+1).id);

				}
			}
		}

		// true가 최소환승
		
		if(rankstore.size() >=4) {
			for (int i = 0; i < 4; i++) {
				System.out.println("★TOP★" + (i + 1));
				String name = noduplicate.rankResult(rankstore.get(i).id);
				System.out.println(rankstore.get(i).idName + "역 , total :" + rankstore.get(i).total); // System.out.print(name
			} 
		}
		else {
			for (int i = 0; i < rankstore.size(); i++) {
				System.out.println("★TOP★" + (i + 1));
				String name = noduplicate.rankResult(rankstore.get(i).id);
				System.out.println(rankstore.get(i).idName + "역 , total :" + rankstore.get(i).total); // System.out.print(name
			} 
			
		}
		
		
//			for (int i = 0; i < 3; i++) {
//				System.out.println("★TOP★" + (i + 1));
//				String name = noduplicate.rankResult(rankstore.get(i).id);
//				System.out.println(rankstore.get(i).idName + "역 , total :" + rankstore.get(i).total); // System.out.print(name
//			} 
	
		

		return rankstore;
	}

	public rank makeRoute(int commonSet, String name) throws Exception { // 첫번쨰로 뭐가 담기던지 어짜피 api에서 알아서 환승역인지 검사해서
		// 상관업승ㅁ!!

		int commonSetCount = 0;
		BufferedReader reader = null;

		JSONParser jsonparser = new JSONParser();
		JSONObject jsonobject = (JSONObject) jsonparser.parse(readUrl2(commonSet));
		JSONObject json = (JSONObject) jsonobject.get("result");
		JSONObject stationSetJson = (JSONObject) json.get("stationSet");
		JSONArray stationArray = (JSONArray) stationSetJson.get("stations");// 다음역의 정보들
		// long globalTravelTime = (long) json.get("globalTravelTime");
		long globalStationCount = (long) json.get("globalStationCount");
		long globalTravelTime = (long) json.get("globalStationCount");

		rank tempRank = new rank();
		tempRank.idName = name;

		try {
			System.out.print("[" + name + "]");
			// 여기서 commonset에 해당하는 역들별로 얼마나 걸리는지 보여짐......
			// 코드가 중복인 경우에 어떻게 할 것인가,,,,,
			// 코드가 들어오게 되면 일단 그걸 이름으로 바꿔 그리고
			if (this.sopt == false) // 최단거리 -> 칸수랑 분 더하는걸로 하기
			{
				/*
				 * for (int j = 0; j < stationSetJson.size(); j++)
				 * System.out.print(stationArray.get(j) + "\t"); System.out.println();
				 * System.out.println("이동한 칸 수 : " + globalStationCount); //int
				 * intGlobalStationCount = Integer.parseInt(globalStationCount);
				 */
				System.out.println((int) globalStationCount + "칸 이동 " + (int) globalTravelTime + "분 소요");
				tempRank.time = (int) globalStationCount;
				tempRank.count = (int) globalStationCount;
				tempRank.key = false;

				return tempRank;
			}

			else { // 최소환승 : 환승횟수 * 칸수 + 시간
				if (json.containsKey("exChangeInfoSet") == true) {
					JSONObject json2 = (JSONObject) json.get("exChangeInfoSet");
					JSONArray array1 = (JSONArray) json2.get("exChangeInfo");// 전역의 정보들
					// 환승구간에 대한 정보 제공
					System.out.println(array1.size() + "번 환승 " + (int) globalTravelTime + "분 소요");
					tempRank.time = (int) globalStationCount;
					tempRank.count = (int) globalStationCount;
					tempRank.change = (int) array1.size();
					tempRank.key = true;

					return tempRank;
				} else {
					System.out.println("0번 환승" + (int) globalTravelTime + "분 소요");
					tempRank.time = (int) globalStationCount;
					tempRank.count = (int) globalStationCount;
					tempRank.change = 0;
					tempRank.key = true;
					return tempRank;
				}
			}
		} finally {
			if (reader != null)
				reader.close();
		}

	}

	private String readUrl2(int commonSet) throws Exception {
		BufferedReader reader = null;

		try {
			int tempFlag = 0;
			if (this.sopt == false)
				tempFlag = 1;
			else
				tempFlag = 2;

			// if(codestore.get(0) != commonSet) {
			URL url = new URL("https://api.odsay.com/v1/api/subwayPath?lang=0&CID=1000&SID=" + codestore.get(0)
					+ "&EID=" + commonSet + "&Sopt=" + tempFlag
					+ "&apiKey=9wndy8Mwrj6EeQZKf1Z9kusSZjU+vBpEdeDwCokXgy0");
			// WcVpRfZ6U%2BAuKf8AgOTZapx9edixkIvmJLWnT9KgiaE-하이드아웃
			// 15XH4EhsIQGTKIwZAjii5dwtmXtv%2BdVulD4QWniB%2Bjg-히수집
			// 9loymI1RM20ytIKmWKFe0x8arsNpYKoPSgHLoGhzANE-은비집
			// FKNgHXbbPDpB2qoqgvkmA3DAKApfxjOfbp%2Fz%2F0gWnOU-학교

			reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

			StringBuffer buffer = new StringBuffer();

			String str;

			while ((str = reader.readLine()) != null) {
				buffer.append(str);
			}

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}
