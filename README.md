# MZZB Server


### DB Resource
Docker Client

PostgreSQL

- DB 이미지 생성
```
docker run --name {컨테이너이름} -p {포트번호}:{포트번호} -e POSTGRES_PASSWORD={비밀번호} -d postgres
```
- PostgreSQL 실행, 종료
```
docker start {컨테이너이름}
docker stop {컨테이너이름}
```
- 컨테이너 접속
```
docker exec -i -t {컨테이너이름} bash
```
- PostgreSQL 접속
```
psql -d postgres -U postgres
```
- 테이블 조회
```
\dt
```