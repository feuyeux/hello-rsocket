package responder

import (
	"context"
	"errors"
	"log"
	"strings"

	rsocket "github.com/rsocket/rsocket-go"
	"github.com/rsocket/rsocket-go/payload"
)

func Start() {
	err := rsocket.Receive().
		Resume().
		Fragment(1024).
		Acceptor(
			func(setup payload.SetupPayload, sendingSocket rsocket.CloseableRSocket) (rsocket.RSocket, error) {
				sendingSocket.OnClose(func(err error) {
					log.Println("***** socket disconnected *****")
				})
				// For SETUP_REJECT testing.
				if strings.EqualFold(setup.DataUTF8(), "REJECT_ME") {
					return nil, errors.New("bye bye bye")
				}
				return RSocketAcceptor(), nil
			}).
		Transport("tcp://127.0.0.1:7878").
		Serve(context.Background())
	panic(err)
}
