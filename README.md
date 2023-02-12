# 사용자 간 최적 만남 장소 추천 서비스(System and Method for recommending location) - 알고리즘#7(Algorithm Part7)
## 목적(Purpose)
* 모임 장소를 정할 때 시간이 오래 걸리는 문제점 해결(Solved the problem of taking a long time to select a meeting place)
* 한강 위, 산속 등 실제 갈 수 없는 장소를 추천하는 유사 서비스의 문제점 해결(Resolving the problem of similar services that recommend places that cannot be actually visited, such as on the Han River or in the mountains)
## 기술스택(Skills)
* Java
* OS : Android
* Server : AWS EC2, Node.js
* Database : AWS RDS, MySQL
## 시스템 구성도(System architecture)
![image](https://user-images.githubusercontent.com/26674094/103957717-a88dc000-518e-11eb-835d-361711139882.png)
## 알고리즘(Algorithm)
> 지하철 노선도를 기반으로 사용자에게 최적의 만남 장소를 추천하는 BFS 기반 알고리즘 개발(특허 출원 (10-2019-0066162))
> Development of a BFS-based algorithm that recommends the optimal meeting place to users based on the subway map(Patent(10-2019-0066162))
1. 사용자가 입력한 지하철 역의 주변 역들을 하나하나 탐색하며 code set에 저장한다.(Searches for stations around the subway station entered by the user one by one and stores them in a code set.)
![image](https://user-images.githubusercontent.com/26674094/103951314-c7d22080-5181-11eb-8862-e1835e210121.png)
2. 각 사용자 별 code set에 담겨있는 지하철 역들의 공통 역을 common set에 저장한다.(The common station of the subway stations contained in the code set for each user is stored in the common set.)
![image](https://user-images.githubusercontent.com/26674094/103951748-9ad23d80-5182-11eb-87e2-79c324b78e53.png)
3. common set에 대해 각 사용자가 선호하는 방식에 따라 정렬한다.(Sort the common set according to each user's preference)
* 사용자 A : 최단시간 선호 -> 사용자 A가 입력한 역으로부터 가장 적은 시간이 걸리는 역 순으로 정렬 (User A : shortest time preferred -> Sort from the station entered by user A in the order that takes the least amount of time.)
* 사용자 B : 최소환승 선호 -> 사용자 B가 입력한 역으로부터 가장 환승이 적은 역 순으로 정렬 (User B : Minimal transfer preference -> Sort from the station entered by user B in descending order with the fewest transfers)
* 사용자 C : 최소환승 선호 -> 사용자 C가 입력한 역으로부터 가장 환승이 적은 역 순으로 정렬 (User C : Minimal transfer preference -> Sort from the station entered by user C in descending order with the fewest transfers)
![image](https://user-images.githubusercontent.com/26674094/103951774-a6256900-5182-11eb-8435-25023aae812c.png)

## 관련 깃헙 링크 (Related Links)
* 서버 파트(Server) : <https://github.com/smulionOong/networking_graduate>
* 알고리즘#1(Algorithm part1) : <https://github.com/smulionOong/project_Rain>
* 알고리즘#2(Algorithm part2) : <https://github.com/smulionOong/android_graduate_rain>
* 알고리즘#3(Algorithm part3) : <https://github.com/smulionOong/graduate_algorithm_final>
* 알고리즘#4(Algorithm part4) : <https://github.com/smulionOong/algorithm_odsay>
* 알고리즘#5(Algorithm part5) : <https://github.com/smulionOong/odsay_hs>
* 알고리즘#6(Algorithm part6) : <https://github.com/smulionOong/algorithm_odsay2_showroute>
* 알고리즘#7(Algorithm part7) : <https://github.com/smulionOong/algorithm_odsay2>
* Mobile application(Android) part1 : <https://github.com/smulionOong/Goodplace>
* Mobile application(Android) part2 : <https://github.com/smulionOong/Goodplace2_MAP>
