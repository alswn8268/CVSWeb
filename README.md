# 모다: 모두 모으다
편의점 행사 정보 정리 사이트

## 기능 구현
1차
- 메인 페이지 전반 / 행사 상품, 행사, 인기 상품, 인기 게시글 가져오기
- 상품 리스트 페이지 전반 / 상품 리스트 정렬 및 페이징 작업
- 상품 개별 페이지 전반 / 각 상품 정보를 가져와 화면에 띄움
- 신상 게시판 전반 / 최신 상품 목록 리스트를 가져와 상품과 연결함

2차
- 크롤링 전처리, 후처리 / 다른 조원들이 크롤링에만 집중할 수 있도록 틀(class 파일)을 만들고, 크롤링 코드 작성 완료 후 바로 프로젝트에 적용할 수 있도록 컨트롤러, DB 연결
- 인기 검색어 기능 / 검색어를 입력하면 해당 내용을 바로 DB에 입력하고 실시간으로 인기검색어로 띄움
- 찜 기능 / 상품 상세 페이지의 하트를 한 번 클릭하면 찜, 한 번 더 클릭하면 찜을 취소하는 기능 / Toggle + ajax로 실시간으로 데이터를 컨트롤러 + DB로 넘겨주었음. 마이페이지에 가면 찜한 상품 리스트를 볼 수 있음
- 접속위치 기준 근처 편의점 위치를 보여주는 기능 / 현재 접속한 시도/구군 정보를, 기존 크롤링한 편의점 위치 데이터와 조합해서 카카오맵 api를 통해 지도에 마커를 꽂았음
- 같은 상품, 다른 편의점 가격 비교 / 이름이 같은 상품이 다른 편의점에서 가격이 다를 경우를 대비해서 구글 차트 api와 연결해 바로 가격 비교를 할 수 있게 함
- 덤증정 상품 / 행사가 덤증정인 상품은 증정 상품 정보를 추가로 입력함
- 평점을 입력한 상태로 댓글을 달면 댓글 옆에 본인이 등록한 평점이 보이게 함
- 차단 후처리 / 차단당한 회원은 글 작성, 댓글 작성 등을 할 수 없게 함
- 멤버 탈퇴 기능
- 회원가입 양식 / js 정규식을 활용해서 맞는 양식만 회원 가입이 가능하게 함
- 프로필 기능 / 회원 닉네임을 클릭하면 상대가 마이페이지에서 입력한 정보로 프로필이 뜨게 함
- 쪽지 기능 / 회원들끼리 쪽지, 관리자->회원으로 전체 공지 쪽지를 보낼 수 있게 함
- 편의점 찾기 / 편의점 종류, 시도, 구군, 입력한 정보를 ajax로 넘겨서, 크롤링했던 편의점 위치 DB에서 맞는 정보를 찾아와 카카오맵 api에 넘겨서 실시간으로 검색한 모든 편의점이 지도에 마커에 꽂히게 함
- 링크 공유 / 페이스북, 트위터, 카카오톡으로 링크 공유 가능
- TOP 버튼 / 메인 페이지 우하단 TOP 버튼 구현
- 이전, 다음 상품 / 상품 개별 페이지에서 다음 상품, 이전 상품으로 바로 넘어가는 버튼
- 캘린더 개선 / 시각적으로 잘 보이도록(UX) 수정
