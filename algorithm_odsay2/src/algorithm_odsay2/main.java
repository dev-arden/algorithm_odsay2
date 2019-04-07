package algorithm_odsay2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import java.io.BufferedInputStream;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


//API 3번 호출할거니까 세번 반복
//다른 유저 시작

public class main {
	// static Vector<Integer> ID = new Vector<Integer>();//dbID의 코드를 main에서 사용하기 위해
	// 복사
	// static Vector<Integer> ID2 = new Vector<Integer>();//dbID의 코드의 전역,다음역의 코드를 담기
	// 위한 것

	public main() throws Exception {
		// userinfo의 행 개수만큼 각 user 버퍼 만들기
		int userCount = 0;
		Vector<Integer> commonset = new Vector<Integer>();// 각 유저 버퍼에 담긴 역들중 공통 역들을 담는 버퍼

		db test = new db();
		
		test.deletemakeroute();
		test.namecopy();
		test.userroutecopy();
		test.userinfocopy();//나중에 경로를 보여주기 위해서 함수가 실행되면 데이터가 사라질 userinfo를 카피함
		
		test.userCount();
		userCount = test.a;
		test.userRouteChoice();
		
		test.Delete2();
		user[] user = new user[userCount];// userCount에 따라 각 user객체들을 담는 배열
		for (int i = 0; i < userCount; i++) {
			user[i] = new user();// userCount만큼 user객체 생성
		}
		for (int i = 0; i < userCount; i++) {
			// 이제 객체 생성은 다 됐으니까 유저 카운트만큼 일단 돌릴거야,,,,
			// 각 user원소의 지하철역에 해당하는 코드를 뽑고 그 코드의 전역,다음역 코드를 뽑는 함수를 돌려버려,,,,
			// 일단 이걸 한번 해보자,,,,,,,,
			// test.userinfoSelect(i + 1);
			test.userinfoSelect(i + 1);
			user[i].codestore.addAll(test.dbID);
			// System.out.println("코드개수: "+user[i].codestore.size());

			int tt = user[i].codestore.size();
			user[i].check.add(tt);// 맨처음 코드스토어 개수 추가
			user[i].sopt = test.routeFlag.get(i);

			for (int j = 0; j < tt; j++) {
				user[i].findcode(j);
			}
			user[i].check.add(user[i].codestore.size());// 그 다음 코드스토어 사이즈 추가
			System.out.println("user" + (i + 1) + "의 코드 집합: " + user[i].codestore);
			if (user[i].sopt == true) {
				System.out.println("user" + (i + 1) + "의 선호도 : 최소환승 ");
			} else
				System.out.println("user" + (i + 1) + "의 선호도 : 최단거리 ");
			test.dbID.removeAllElements();

			// 그럼 이제 두번째 탐색을 할때 어케할것인가
			// 두번째 탐색을 할때는 check에 인덱스가 0이랑 1밖에 없는 상태
			// 그로면은 codestore.get(check.get(0)) ~ codestore.get(check.get(2)-1)이거를 탐색해야함
			// 이렇게 따지면은 세번째 탐색을 할때는
			// codestore.get(check.get(1))~codestore.get(check.get(3)-2)이거를 탐색해야함
			// 그럼 이걸 변수로 정리를 해보면은
			// 일단 탐색을 할 횟수를 변수로 정의해야함
		}
		// 이제 여기서 담는 것은 일단 성공을 했으니까 중복없이 담는 것을 한번 해봅시다
		// 그리고나서 이거 자체를 함수로 만들자
		// findresult를 commonset의 결과의 개수에 따라서 도는게 달라지도록 만들자
		findresult(userCount, user, commonset);
		// System.out.println(makeCommonset(userCount, user, commonset));
		// while(true)
		// {
		// findresult(userCount, user, commonset);
		// int commonsize = makeCommonset(userCount, user, commonset);
		// if(commonsize >= 1)
		// {
		// break;
		// }
		// }
		// for(int i=0;i<commonset.size();i++)
		// {
		// System.out.println(commonset.get(i)+", ");
		// }
		showCommonset(commonset, test);

		for (int i = 0; i < user.length; i++) {
			System.out.println("<user" + (i + 1) + ">");
			user[i].rankResult.addAll(0, user[i].makeRanking(test.commonResult));
		}

		int count = 1;
		if (user[1].rankstore.size() > 2) {
			for (int i = 0; i < user.length; i++) {
				for (int j = 0; j < 3; j++) {
					// test.rankSave(count, user, rank, code);
					test.rankSave(count, i + 1, j + 1, user[i].rankResult.elementAt(j).id,
							user[i].rankResult.elementAt(j).idName);
					count++;
				}

			}
		} else {
			for (int i = 0; i < user.length; i++) {
				for (int j = 0; j < user[1].rankstore.size(); j++) {
					// test.rankSave(count, user, rank, code);
					test.rankSave(count, i + 1, j + 1, user[i].rankResult.elementAt(j).id,
							user[i].rankResult.elementAt(j).idName);
					count++;
				}

			}
		}
		test.useridcopy();

		for (int i = 1; i <= userCount; i++) {
			int dep = test.departure(i);
			//System.out.println(dep);
			int des = test.destination(i);
			//System.out.println(des);
			int prefer = test.prefer(i);
			//System.out.println(prefer);

			JSONParser jsonparser = new JSONParser();
			JSONObject jsonobject = (JSONObject) jsonparser.parse(readUrl(dep, des, prefer));
			JSONObject json = (JSONObject) jsonobject.get("result");
			JSONObject json2 = (JSONObject) json.get("stationSet");
			JSONArray array = (JSONArray) json2.get("stations");

			for (int j = 0; j < array.size(); j++) {
				JSONObject entity = (JSONObject) array.get(j);

				String sname = (String) entity.get("startName");
				if(checkduplicate(sname, user[i - 1]) == 0) {
					user[i-1].routestore.add(sname);
				}
				

				String ename = (String) entity.get("endName");
				if(checkduplicate(ename, user[i - 1]) == 0) {
					user[i-1].routestore.add(ename);
				}

			}

			for (int k = 0; k < user[i - 1].routestore.size(); k++) {
				test.makeroute(i,user[i-1].routestore.get(k));
			}
			test.useridcopy2();
		}
		test.Delete1();
		test.Delete3();
		test.deleteuserinfocopy();

	}

	// findresult에 x 조건을 주지 않아서 생기는 문제임,,,그렇다면 findresult안에 commonset을 집어넣는게 답인듯
	public void findresult(int userCount, user[] user, Vector<Integer> commonset) throws Exception {
		// int times = 1;// 3번 탐색을 할 것임
		for (int x = 0;; x++) {
			for (int y = 0; y < userCount; y++) {
				int start = user[y].check.get(x);
				int end = user[y].check.get(x + 1) - 1;
				// System.out.println(start+","+end);
				for (int z = start; z <= end; z++) {
					user[y].findcode(z);
				}
				user[y].check.add(user[y].codestore.size());// 그 다음 코드스토어 사이즈 추가
				System.out.println("user" + (y + 1) + "의 코드 집합: " + user[y].codestore);
			}
			int commonsize = makeCommonset(userCount, user, commonset);
			if (commonsize >= 6) {
				break;
			}
		}
	}

	// makeCommonset의 결과가 3이 나올때까지 돌린다고 치자.....
	// makeCommonset의 인자로 무엇이 와야할까,,,,
	// 각 user의 코드스토어가 와야겠쥬,,그러니까 인자로 각 user를 보내야겠쥬
	public int makeCommonset(int userCount, user[] user, Vector<Integer> commonset) {
		// user[0]의 코드 하나하나를 나머지 user들의 코드랑 비교를 해가는 과정이 필요할 듯
		// 만약에 같은 코드가 발견될 때마다 ++을 주고 그 개수가 userCount - 1이라면 commonset에 추가하면 되겠쥬
		int flag = 0;
		int firstsize = user[0].codestore.size();
		for (int i = 0; i < firstsize; i++) {
			flag = 0;
			int compare = user[0].codestore.get(i);
			for (int j = 1; j < userCount; j++) {
				for (int k = 0; k < user[j].codestore.size(); k++) {
					if (compare == user[j].codestore.get(k)) {
						flag++;
					}
				}
			}
			if (flag == userCount - 1) {
				commonset.add(compare);
			}
		}

		return commonset.size();
	}

	public void showCommonset(Vector<Integer> commonset, db test) {
		for (int i = 0; i < commonset.size(); i++) {
			test.showCommonset(commonset.get(i));
		}

		System.out.println(test.commonResult);

	}
	
	private String readUrl(int departure, int destination, int prefer) throws Exception {
		BufferedReader reader = null;

		try {
			// https://api.odsay.com/v1/api/subwayPath?lang=0&CID=1000&SID=201&EID=222

			URL url = new URL("https://api.odsay.com/v1/api/subwayPath?lang=0&CID=1000&" + "SID=" + departure + "&EID="
					+ destination + "&Sopt=" + prefer + "&apiKey=9wndy8Mwrj6EeQZKf1Z9kusSZjU%2BvBpEdeDwCokXgy0");
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

	public int checkduplicate(String route, user user) {
		int check = 0;

		for (int j = 0; j < user.routestore.size(); j++) {
			if (user.routestore.get(j).contains(route)) {
				check++;
			}
		}

		return check;
	}

	public static void main(String[] args) {
		try {
			new main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}