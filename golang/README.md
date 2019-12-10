main.go

```bash
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