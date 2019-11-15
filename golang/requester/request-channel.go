package requester

import (
	"context"
	"fmt"
	"log"

	"github.com/rsocket/rsocket-go/payload"
	"github.com/rsocket/rsocket-go/rx/flux"
)

// ExecRequestChannel ...
func ExecRequestChannel() {
	log.Println("====ExecRequestChannel====")

	cli, _ := BuildClient()
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
