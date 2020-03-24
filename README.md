# 졸업 프로젝트

> 컵 형태로 무게 센서를 이용해서 알코올 섭취량을 개략적으로 측정하는 장치
> 애플리케이션을 통해 경고를 보내거나 개선 방안을 알려 주는 기능

입으로 부는 형태의 측정기보다 정확도는 현저히 떨어지지만, 접근성이 좋고 필요할 때 알림을 해주는 "지원 도구"로 개발


###	컵
-	아두이노에 LED와 Bluetooth, 무게 센서 사용
-	음주 권고량 또는 설정된 값을 넘을 경우 LED로 알림
-	스마트폰과 Bluetooth로 연결하여 알코올 섭취량 전달
-	무게 센서의 증감에 따라 음주량 예측

###	앱
-	블루투스 통신 : 컵에 LED 설정을 전송하고, 컵의 섭취량 데이터 수신
-	로그인 기능 : 온라인으로 데이터를 백업하고 복원
-	지금은 스마트폰 데이터베이스에 저장되도록 함
-	통계 기능 : 매일 기록과 일주일간 합계 기록 표시

## 초기 설계 vs 실제 구현 내용 
[초기 설계 PPT 파일](https://github.com/sdrstone1/Grad_Prj/blob/beta/4모바일ui.ppt)

#### 대부분 구현 완료.
- 스플래시
- 로그인 화면(로그인 인증은 앱내 데이터베이스로 구현함)
- 회원가입 화면
- 메인 화면
- 회원 정보 변경 
- LED 색 / 패턴 변경 

#### 구현 못 했거나 부족한 부분
- 회원 계정 찾기
- 달력 통계 (주간 통계까지만 구현 됨)
- 회원 탈퇴 

#### 설계에 없지만 추가되 부분
- 블루투스 장치 선택 및 자동 연결 기능

