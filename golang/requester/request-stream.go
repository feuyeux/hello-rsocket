package requester

import (
	"context"
	"log"

	rsocket "github.com/rsocket/rsocket-go"
	"github.com/rsocket/rsocket-go/payload"
)

//ExecRequestStream ...
func ExecRequestStream() {
	log.Println("====ExecRequestStream====")

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

	f := cli.RequestStream(payload.NewString("你好", "世界"))
	PrintFlux(f)
}
