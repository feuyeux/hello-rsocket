package requester

import (
	"log"

	"github.com/rsocket/rsocket-go/payload"
)

//ExecRequestStream ...
func ExecRequestStream() {
	log.Println("====ExecRequestStream====")
	cli, _ := BuildClient()
	defer cli.Close()
	f := cli.RequestStream(payload.NewString("你好", "世界"))
	PrintFlux(f, "[Request-Stream]")
}
