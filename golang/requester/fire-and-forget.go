package requester

import (
	"context"
	"log"

	rsocket "github.com/rsocket/rsocket-go"
	"github.com/rsocket/rsocket-go/payload"
)

//ExecFireAndForget ...
func ExecFireAndForget() {
	log.Println("====ExecFireAndForget====")

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

	cli.FireAndForget(payload.NewString("hello", "bonjour"))
}
