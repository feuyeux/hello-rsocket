package requester

import (
	"context"
	"github.com/rsocket/rsocket-go"
	"github.com/rsocket/rsocket-go/payload"
)

func BuildClient() (rsocket.Client, error) {
	cli, err := rsocket.Connect().
		Resume().
		Fragment(1024).
		SetupPayload(payload.NewString("Hello", "World")).
		Transport("tcp://127.0.0.1:7878").
		Start(context.Background())
	if err != nil {
		panic(err)
	}
	return cli, err
}
