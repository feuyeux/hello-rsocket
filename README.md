# Hello RSocket

> Fork from [https://github.com/b3rnoulli/rsocket-examples.git](https://github.com/b3rnoulli/rsocket-examples.git)

- Add, refactor, and fix some implementations for learning and breakpoints.
- Upgrade the blow dependencies and tools:

|   | from  | to   |
| ---- | ---- | ---- |
|  gradle    |   4.10   |  5.6.2    |
| `com.google.protobuf` | 0.8.8 | 0.8.10 |
| `com.google.protobuf:protoc` | 3.6.1 | 3.10.0 |
| `org.springframework.boot` | 2.2.0.M3 | 2.2.1.RELEASE |

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
cd java0/spring-boot-responder
gradle bootRun
```

```shell
cd java0/spring-boot-requester
gradle bootRun
```

```shell
curl http://localhost:8080/customers/1
curl http://localhost:8080/customers
curl http://localhost:8080/customers-channel
```

## hello-rsocket series
- hello-rsocket-java https://github.com/feuyeux/hello-rsocket-java
- hello-rsocket-golang https://github.com/feuyeux/hello-rsocket-golang
- hello-rsocket-rust https://github.com/feuyeux/hello-rsocket-rust