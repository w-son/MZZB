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
docker run --name {컨테이너이름} -p {포트번호}:{포트번호} -e POSTGRES_PASSWORD={비밀번호} -d postgres

> PostgreSQL 실행, 종료
docker start {컨테이너이름}
docker stop {컨테이너이름}

> 컨테이너 접속
docker exec -i -t {컨테이너이름} bash

> PostgreSQL 접속
psql -d postgres -U postgres

> 테이블 조회
\dt
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

## JAR

- 배포 요약 
```
> mvn 설치
sudo apt-get install maven

> 패키징
mvn package

> target 내 jar 실행
nohup java -jar {jar 파일} &
```

## Notes

Rest Docs 제대로 동작 안할 시 Lifecycle 의 package 다시 빌드