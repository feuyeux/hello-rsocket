package requester

import (
	"log"
	"github.com/rsocket/rsocket-go/payload"
)

//ExecFireAndForget ...
func ExecFireAndForget() {
	log.Println("====ExecFireAndForget====")
	cli, _ := BuildClient()
	defer cli.Close()
	cli.FireAndForget(payload.NewString("hello", "bonjour"))
}
