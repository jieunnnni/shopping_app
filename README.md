# 쇼핑 앱

### 프로젝트 설명
상품과 그 상품의 상세 화면을 구성하고, 마음에 드는 상품을 찜하고 공유할 수 있으며, 장바구니에 담고 주문을 할 수 있다.



![392D094B-093E-43F6-8AD9-AEF9C34BBC39-58326-0000186AE5050160](https://user-images.githubusercontent.com/120105216/208847042-f16b4c07-2f15-4210-b017-1be98fe077a8.JPG)
상품 리스트와 상품 상세 페이지, 스크롤시 상품이름이 상단바로 움직임

![96D96B49-B2C3-4BCB-8F49-A5B9CDAD513D-58326-0000186E9137B166](https://user-images.githubusercontent.com/120105216/208848397-de3d283f-0d5d-4451-a648-248615c5001a.JPG)
찜 기능 및 장바구니 기능


<img width="1282" alt="스크린샷 2022-12-20 오후 9 34 54" src="https://user-images.githubusercontent.com/120105216/208847270-a75195eb-5773-47ac-a13f-03afcb14088e.png">
주문 완료하면 FirebaseStorage에서 주문 건을 확인할 수 있음


![D9C5842C-B4C2-47BE-8991-39EE8D1FBC06-58326-0000186BFF0310BE](https://user-images.githubusercontent.com/120105216/208847170-4bf57d4e-3eaa-4d26-ab5c-abb9d987c134.JPG)
공유하기 기능


![32E0F8E6-116D-455B-BE20-460D3CF4101B-58326-0000186C44ED7D4C](https://user-images.githubusercontent.com/120105216/208847208-87cdd10c-e86e-405f-ba89-59c5c8fbee51.JPG)
구글 로그인 및 로그인 안 된 경우 장바구니 기능과 찜 기능 사용 불가

---

### 주요 기능
- MVVM 
- UseCase
- State 패턴
- Koin으로 DI 셋업
- Mock 아이템 데이터로 아이템 리스트 구현
- 아이템 상세화면
- Room 이용해 DB 구성
- 찜 및 공유하기 기능
- 장바구니 추가 기능
- Firebase 이용해 구글 로그인 연동
- CollapsingToolbarLayout를 이용해 움직이는 상단바 구현

---

### 사용 기술
- Glide
- Retrofit2
- OkHttp
- Coroutine
- LiveData
- Koin
- Room
- Swipe Refresh Layout
- Firebase auth
- Firebase storage
