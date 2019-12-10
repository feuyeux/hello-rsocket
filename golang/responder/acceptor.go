package responder

import (
	"context"
	"github.com/feuyeux/hello-rsocket/common"
	"github.com/jjeffcaii/reactor-go/scheduler"
	"log"
	"strconv"
	"time"

	rsocket "github.com/rsocket/rsocket-go"

	"github.com/rsocket/rsocket-go/payload"
	"github.com/rsocket/rsocket-go/rx"
	"github.com/rsocket/rsocket-go/rx/flux"
	"github.com/rsocket/rsocket-go/rx/mono"
)

func RSocketAcceptor() rsocket.RSocket {
	helloList := []string{"Hello", "Bonjour", "Hola", "こんにちは", "Ciao", "안녕하세요"}

	return rsocket.NewAbstractSocket(
		rsocket.MetadataPush(func(item payload.Payload) {
			log.Println("[Responder::MetadataPush] GOT METADATA_PUSH:", item)
		}),
		rsocket.FireAndForget(func(p payload.Payload) {
			data := p.Data()
			request := common.JsonToHelloRequest(data)
			log.Println("[Responder::FireAndForget] GOT FNF:", request.Id)
		}),
		rsocket.RequestResponse(func(p payload.Payload) mono.Mono {
			data := p.Data()
			request := common.JsonToHelloRequest(data)
			metadata, _ := p.MetadataUTF8()
			log.Println("[Responder::RequestResponse] data:", request, ", metadata:", metadata)
			id := request.Id
			index, _ := strconv.Atoi(id)
			response := common.HelloResponse{Id: id, Name: helloList[index]}
			json, _ := response.ToJson()
			meta, _ := p.Metadata()
			rp := payload.New(json, meta)
			return mono.Just(rp)
		}),
		rsocket.RequestStream(func(p payload.Payload) flux.Flux {
			data := p.Data()
			request := common.JsonToHelloRequests(data)
			metadata, _ := p.MetadataUTF8()
			log.Println("[Responder::RequestStream] data:", request, ", metadata:", metadata)

			return flux.Create(func(ctx context.Context, emitter flux.Sink) {
				for i := range request.Ids {
					// You can use context for graceful coroutine shutdown, stop produce.
					select {
					case <-ctx.Done():
						log.Println("[Responder::RequestStream] ctx done:", ctx.Err())
						return
					default:
						id := request.Ids[i]
						index, _ := strconv.Atoi(id)
						response := common.HelloResponse{Id: id, Name: helloList[index]}
						json, _ := response.ToJson()
						meta, _ := p.Metadata()
						rp := payload.New(json, meta)
						emitter.Next(rp)
						time.Sleep(500 * time.Millisecond)
					}
				}
				emitter.Complete()
			})
		}),
		rsocket.RequestChannel(func(payloads rx.Publisher) flux.Flux {
			return flux.Create(func(i context.Context, sink flux.Sink) {
				payloads.(flux.Flux).
					SubscribeOn(scheduler.Elastic()).
					DoOnNext(func(p payload.Payload) {
						data := p.Data()
						request := common.JsonToHelloRequest(data)
						metadata, _ := p.MetadataUTF8()
						log.Println("[Responder::RequestChannel] data:", request, ", metadata:", metadata)
						id := request.Id
						index, _ := strconv.Atoi(id)
						response := common.HelloResponse{Id: request.Id, Name: helloList[index]}
						json, _ := response.ToJson()
						sink.Next(payload.New(json, nil))
					}).
					Subscribe(context.Background())
				//sink.Complete()
			})
		}),
	)
}
