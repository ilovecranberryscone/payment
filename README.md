# kakaopay Payment Project
결제 요청을 받아 카드사와 통신하는 인터페이스를 제공하는 결제 시스템

## 개발 프레임워크
Spring boot 2.3.1.RELEASE

## Prerequisites
jdk1.8.0_231 <br/>
Lombok plugin <br/>

## Dependencies
spring-boot-starter-data-jpa
spring-boot-starter-web
spring-boot-starter-validation
com.h2database:h2
lombok
spring-boot-starter-test
spring-boot-maven-plugin

## API
성공시 errCode는 S0, 실패시 errCode는 "E"로 시작하게 설계하였다.
|Action|API|Parameter|Body|
|------|------|------|------|
|Pay by Card|POST /payment||{"cardNumber": "111122223333","installmentMonth": "03","validityPeriod": "0522","cvc": "527","price": 1000}|
|Cancel(Include Partitial Cancel)|DELETE /cancel||{"managementNumber": "20200707020622464001","price": 1000}|
|Retrieve|GET /history/{managementId}|managementId={String}||

## 테이블 설계
|Payment(테이블명)|
|------|
|**managementNumber (pk)**|
|type|
|encryptedCardNumber|
|installmentMonth|
|encryptedValidityPeriod|
|encryptedCvc|
|price|
|vat|
|originalManagementNumber|
|encryptCardInfomation|
|createDate|

## 문제해결 전략
### 중복 결제 방지
결제 저장 직전에 checkDuplication이라는 함수를 구현하여 같은 카드 번호에 같은 년/월/일/시간/분/초로 들어온 결제는 중복 결제로 간주함
### 카드사와 통신하는 부분 string 데이터 저장
AES256 암호화 공통 유틸 개발하여 카드번호|유효기간|cvc 로 암호화.
해당 암호화 유틸로 카드번호, 유효기간, cvc도 각각 암호화하여 DB 저장
### 필수구현 API를 개발하고 단위테스트로 각 기능 검증
PayUnitTest.java로 pay 기능 검증
CancelUnitTest.java로 cancel 기능 검증
HistoryUnitTest.java로 조회 기능 검증
### Exception 처리
GlobalExceptionHandler를 구현하여 에러 처리를 분기함
### 추가 선택 문제
TestCase1, TestCase2, TestCase3를 구현하여 결과값이 맞게 나오는 것을 확인함

## 빌드 및 실행 방법
maven update 후 spring boot app 으로 기동.

