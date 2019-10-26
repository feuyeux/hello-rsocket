package requester

import (
	"context"
	"log"

	rsocket "github.com/rsocket/rsocket-go"
	"github.com/rsocket/rsocket-go/payload"
)

func ExecRequestResponse() {
	log.Println("====ExecRequestResponse====")

	cli, err := rsocket.Connect().
		Resume().
		Fragment(1024).
		SetupPayload(payload.NewString("Hello", "World")).
		Transport("tcp://127.0.0.1:7878").
		Start(context.Background())
	if err != nil {
		panic(err)
	}
	defer cli.Close()

	// Send request
	result, err := cli.RequestResponse(payload.NewString("你好", "世界")).Block(context.Background())
	if err != nil {
		panic(err)
	}
	log.Println("[Request-Response] response:", result)
	log.Println("[Request-Response] data:", result.DataUTF8())
}
