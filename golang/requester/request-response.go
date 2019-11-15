package requester

import (
	"context"
	"log"

	"github.com/rsocket/rsocket-go/payload"
)

func ExecRequestResponse() {
	log.Println("====ExecRequestResponse====")

	cli, err := BuildClient()
	defer cli.Close()
	// Send request
	result, err := cli.RequestResponse(payload.NewString("你好", "世界")).Block(context.Background())
	if err != nil {
		panic(err)
	}
	log.Println("[Request-Response] response:", result)
	log.Println("[Request-Response] data:", result.DataUTF8())
}

