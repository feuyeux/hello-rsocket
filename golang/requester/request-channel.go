package requester

import (
	"context"
	"fmt"
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

	send := flux.Create(func(i context.Context, sink flux.Sink) {
		for i := 1; i <= 10; i++ {
			sink.Next(payload.NewString(fmt.Sprintf("foo_%04d", i), fmt.Sprintf("bar_%04d", i)))
		}
		sink.Complete()
	})

	f := cli.RequestChannel(send)
	PrintFlux(f, "[Request-Channel]")
}

// PrintFlux ...
func PrintFlux(f flux.Flux, s string) (err error) {
	_, err = f.
		DoOnNext(func(result payload.Payload) {
			log.Println(s, "response:", result)
			log.Println(s, "data:", result.DataUTF8())
		}).
		BlockLast(context.Background())
	return
}
