package requester

import (
	"context"
	"log"

	rsocket "github.com/rsocket/rsocket-go"
	"github.com/rsocket/rsocket-go/payload"
	"github.com/rsocket/rsocket-go/rx/flux"
)

// ExecRequestChannel ...
func ExecRequestChannel() {
	log.Println("====ExecRequestChannel====")

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

	var send flux.Flux
	f := cli.RequestChannel(send)
	PrintFlux(f)

}

// PrintFlux ...
func PrintFlux(f flux.Flux) (err error) {
	_, err = f.
		DoOnNext(func(result payload.Payload) {
			log.Println("response:", result)
			log.Println("%s\n", result.DataUTF8())
		}).
		BlockLast(context.Background())
	return
}
