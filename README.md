# MZZB Server
맛집족보 데이터베이스 백엔드 서버입니다

## DB Resource

- Docker
```
> ubuntu 18.04 기준
sudo apt-get install docker
sudo apt-get install docker.io
```
- PostgreSQL
```
> DB 이미지 생성
sudo docker run --name {컨테이너이름} -p {포트번호}:{포트번호} -e POSTGRES_PASSWORD={비밀번호} -d postgres

> PostgreSQL 실행, 종료
sudo docker start {컨테이너이름}
sudo docker stop {컨테이너이름}

> 컨테이너 접속
sudo docker exec -i -t {컨테이너이름} bash

> PostgreSQL 접속
psql -d postgres -U postgres

> 테이블 조회
\dt
```

## 배포

- 요약 
```
> mvn 설치
sudo apt-get install maven

> 패키징
sudo mvn package

> target 내 jar 실행
sudo nohup java -jar {jar 파일} &
```
- 배포 후 application.properties의 DB 설정 제대로 되었는지 확인
- 서버 중지 시 'java'라고 되어 있는 프로세스 종료

## Run Tests

```
> 패키지 내 모든 테스트 실행
sudo mvn clean test

> 특정 테스트 클래스 실행
sudo mvn clean test -Dtest=com.son.mzzb.tmi.{클래스이름}

> 특정 테스트 클래스의 특정 메서드 실행
sudo mvn clean test -Dtest=com.son.mzzb.matzip.{클래스이름}#{메서드이름}
```

## HTTPS 설정

- keystore 생성 
```
> 프로젝트 디렉토리 내에서 다음 실행
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 4000
```
- properties 파일에 다음 내용 추가
```
server.ssl.key-store=keystore.p12
server.ssl.key-store-password={비밀번호}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
```

## Notes

- Rest Docs 제대로 동작 안할 시 Lifecycle 의 package 다시 빌드
- 리눅스 단어 치환 명령어 
```
:%s/old/new/g
```