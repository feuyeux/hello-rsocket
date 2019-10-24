package main

import (
	"time"

	"github.com/feuyeux/hello-rsocket/requester"
	"github.com/feuyeux/hello-rsocket/responder"
)

func main() {
	go responder.Start()
	requester.ExecRequestResponse()
	requester.ExecFireAndForget()

	time.Sleep(500 * time.Millisecond)
}
