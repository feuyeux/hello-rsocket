package main

import (
	"time"

	"github.com/feuyeux/hello-rsocket/requester"
	"github.com/feuyeux/hello-rsocket/responder"
)

func main() {
	go responder.Start()

	requester.ExecRequestResponse()
	time.Sleep(100 * time.Millisecond)

	requester.ExecFireAndForget()
	time.Sleep(200 * time.Millisecond)

	requester.ExecRequestStream()
	time.Sleep(200 * time.Millisecond)

	go requester.ExecRequestChannel()
	time.Sleep(1000 * time.Millisecond)
}
