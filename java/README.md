#### pojo
```bash
▶ cd pojo
▶ mvn clean install
```

#### responder
```bash
▶ cd responder
▶ mvn spring-boot:run
```

#### requester
```bash
▶ cd requester
▶ mvn spring-boot:run
```

#### cli
```bash
curl http://localhost:8080/hello/1
curl http://localhost:8080/hello-stream
curl http://localhost:8080/hello-channel
curl http://localhost:8080/hello-forget
```