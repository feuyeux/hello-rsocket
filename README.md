# Hello RSocket

### 1 Java version

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

#### curl
```bash
curl http://localhost:8080/hello/1
curl http://localhost:8080/hello-stream
curl http://localhost:8080/hello-channel
curl http://localhost:8080/hello-forget
```

### 2 Golang version

```bash
▶ cd golang

▶ go run main.go
2019/12/09 17:29:22 ====ExecRequestResponse====
2019/12/09 17:29:22 [Responder::RequestResponse] data: {1} , metadata: 2019-12-09 5:29:220 pm
2019/12/09 17:29:22 [Request-Response] response id: 1 ,name: Bonjour

2019/12/09 17:29:22 ====ExecFireAndForget====
2019/12/09 17:29:22 [Responder::FireAndForget] GOT FNF: 1

2019/12/09 17:29:22 ====ExecRequestStream====
2019/12/09 17:29:22 [Responder::RequestStream] data: {[1 2 2 4 1]} , metadata: 2019-12-09 5:29:220 pm
2019/12/09 17:29:22 [Request-Stream] response: {1 Bonjour}
2019/12/09 17:29:23 [Request-Stream] response: {2 Hola}
2019/12/09 17:29:23 [Request-Stream] response: {2 Hola}
2019/12/09 17:29:24 [Request-Stream] response: {4 Ciao}
2019/12/09 17:29:24 [Request-Stream] response: {1 Bonjour}

2019/12/09 17:29:25 ====ExecRequestChannel====
2019/12/09 17:29:25 [Responder::RequestChannel] data: {3} , metadata: 2019-12-09 5:29:250 pm
2019/12/09 17:29:25 [Request-Channel] response: {3 こんにちは}
2019/12/09 17:29:25 [Responder::RequestChannel] data: {0} , metadata: 2019-12-09 5:29:250 pm
2019/12/09 17:29:25 [Request-Channel] response: {0 Hello}
2019/12/09 17:29:25 [Responder::RequestChannel] data: {0} , metadata: 2019-12-09 5:29:250 pm
2019/12/09 17:29:25 [Request-Channel] response: {0 Hello}
2019/12/09 17:29:25 [Responder::RequestChannel] data: {1} , metadata: 2019-12-09 5:29:250 pm
2019/12/09 17:29:25 [Request-Channel] response: {1 Bonjour}
2019/12/09 17:29:25 [Responder::RequestChannel] data: {0} , metadata: 2019-12-09 5:29:250 pm
2019/12/09 17:29:25 [Request-Channel] response: {0 Hello}
```

### 3 Rust version

```bash
▶ cd rust 

▶ cargo run
    Finished dev [unoptimized + debuginfo] target(s) in 0.05s
     Running `target/debug/hello-rsocket`
====ExecFireAndForget====
>>>>>>>> fire_and_forget: Payload { m: None, d: Some(b"Mock FNF") }
>>>>>>>> metadata_push: Payload { m: Some(b"metadata only!"), d: None }
====ExecRequestResponse====
>>>>>>>> request_response: data=Some(b"Hello World!"),meta=Some(b"#0")
<<<<<<<< : Some(b"Hello World!")
>>>>>>>> request_response: data=Some(b"Hello World!"),meta=Some(b"#1")
<<<<<<<< : Some(b"Hello World!")
>>>>>>>> request_response: data=Some(b"Hello World!"),meta=Some(b"#2")
<<<<<<<< : Some(b"Hello World!")
====ExecRequestStream====
>>>>>>>> request_stream: Payload { m: Some(b"foobar"), d: Some(b"Hello Rust!") }
<<<<<<<< STREAM: Payload { m: Some(b"METADATA_0"), d: Some(b"DATA_0") }
<<<<<<<< STREAM: Payload { m: Some(b"METADATA_1"), d: Some(b"DATA_1") }
<<<<<<<< STREAM: Payload { m: Some(b"METADATA_2"), d: Some(b"DATA_2") }

```

### 4 NodeJs version

### 5 Java 0 version
> Fork from [https://github.com/b3rnoulli/rsocket-examples.git](https://github.com/b3rnoulli/rsocket-examples.git)

- Add, refactor, and fix some implementations for learning and breakpoints.
- Upgrade the blow dependencies and tools:

|   | from  | to   |
| ---- | ---- | ---- |
|  gradle    |   4.10   |  5.6.2    |
| `com.google.protobuf` | 0.8.8 | 0.8.10 |
| `com.google.protobuf:protoc` | 3.6.1 | 3.10.0 |
| `org.springframework.boot` | 2.2.0.M3 | 2.2.0.RC1 |

#### Introduction

This repository contains examples used in the series of articles about RSocket. The articles are available here: https://medium.com/@b3rnoulli/reactive-service-to-service-communication-with-rsocket-introduction-5d64e5b6909

It consist of following modules:
- interaction-model
- load-balancing
- resumability
- rpc
- spring-boot-requester
- spring-boot-responder

Each module address different aspect of the protocol, more detailed description is available in the module directories.

#### Build

The modules use ```gradle``` as a build tool. In order to crate executable jars please invoke
`./gradlew clean build` on the root directory. Each module can be built individually using the same command, 
but executed in the particular module directory.

Please notice that examples were designed to run inside your IDE.


#### Test

```shell
cd spring-boot-responder
gradle bootRun
```

```shell
cd spring-boot-requester
gradle bootRun
```

```shell
curl http://localhost:8080/customers/1
curl http://localhost:8080/customers
curl http://localhost:8080/customers-channel
```