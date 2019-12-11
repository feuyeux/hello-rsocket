package requester

import (
	"context"
	"fmt"
	"github.com/feuyeux/hello-rsocket/common"
	"github.com/rsocket/rsocket-go/payload"
	"github.com/rsocket/rsocket-go/rx/flux"
	"log"
	"math/rand"
	"strconv"
	"time"
)

//ExecFireAndForget ...
func ExecFireAndForget() {
	fmt.Println()
	log.Println("====ExecFireAndForget====")
	client, _ := BuildClient()
	defer client.Close()
	request := &common.HelloRequest{Id: "1"}
	json, _ := request.ToJson()
	client.FireAndForget(payload.New(json, nil))
}
func ExecRequestResponse() {
	log.Println("====ExecRequestResponse====")
	client, _ := BuildClient()
	defer client.Close()
	// Send request
	request := &common.HelloRequest{Id: "1"}
	json, _ := request.ToJson()
	p := payload.New(json, []byte(Now()))
	result, _ := client.RequestResponse(p).Block(context.Background())
	data := result.Data()
	response := common.JsonToHelloResponse(data)
	//redisClient := common.RedisClient{}
	//redisClient.Open("","6379","")
	//redisClient.Set("2019-12-09-RSOCKET", string(data))
	//redisData := redisClient.Get("2019-12-09-RSOCKET")
	//log.Println("[Request-Response] redisData:", redisData)
	log.Println("[Request-Response] response id:", response.Id, ",name:", response.Name)
}
func ExecRequestChannel() {
	fmt.Println()
	log.Println("====ExecRequestChannel====")
	cli, _ := BuildClient()
	defer cli.Close()

	send := flux.Create(func(i context.Context, sink flux.Sink) {
		for i := 1; i <= 5; i++ {
			request := &common.HelloRequest{}
			request.Id = RandomId(5)
			json, _ := request.ToJson()
			p := payload.New(json, []byte(Now()))
			sink.Next(p)
			time.Sleep(100 * time.Millisecond)
		}
		time.Sleep(1000 * time.Millisecond)
		sink.Complete()
	})

	f := cli.RequestChannel(send)
	PrintFlux(f, "[Request-Channel]")
}
func ExecRequestStream() {
	fmt.Println()
	log.Println("====ExecRequestStream====")
	cli, _ := BuildClient()
	defer cli.Close()
	ids := RandomIds(5)

	request := &common.HelloRequests{}
	request.Ids = ids
	json, _ := request.ToJson()
	p := payload.New(json, []byte(Now()))
	f := cli.RequestStream(p)
	PrintFlux(f, "[Request-Stream]")
}

func RandomIds(max int) []string {
	ids := make([]string, max, max)
	for i := range ids {
		ids[i] = RandomId(max)
	}
	return ids
}

func RandomId(max int) string {
	return strconv.Itoa(rand.Intn(max))
}

func Now() string {
	return time.Now().Format("2019-12-11 01:10:50 pm")
}

// PrintFlux ...
func PrintFlux(f flux.Flux, s string) (err error) {
	_, err = f.
		DoOnNext(func(p payload.Payload) {
			data := p.Data()
			response := common.JsonToHelloResponse(data)
			log.Println(s, "response:", response)
		}).
		BlockLast(context.Background())
	return
}
