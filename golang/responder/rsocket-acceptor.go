package responder

import (
	"context"
	"fmt"
	"log"
	"strconv"

	rsocket "github.com/rsocket/rsocket-go"

	"github.com/jjeffcaii/reactor-go/scheduler"
	"github.com/rsocket/rsocket-go/payload"
	"github.com/rsocket/rsocket-go/rx"
	"github.com/rsocket/rsocket-go/rx/flux"
	"github.com/rsocket/rsocket-go/rx/mono"
)

//Response ...
func Response() rsocket.RSocket {
	return rsocket.NewAbstractSocket(
		rsocket.MetadataPush(func(item payload.Payload) {
			log.Println("[Responder::MetadataPush] GOT METADATA_PUSH:", item)
		}),
		rsocket.FireAndForget(func(elem payload.Payload) {
			log.Println("[Responder::FireAndForget] GOT FNF:", elem)
		}),
		rsocket.RequestResponse(func(pl payload.Payload) mono.Mono {
			s := pl.DataUTF8()
			m, _ := pl.MetadataUTF8()
			log.Println("[Responder::RequestResponse] data:", s, "metadata:", m)
			return mono.Just(pl)

			// Graceful with context API.
			//return rx.NewMono(func(ctx context.Context, sink rx.MonoProducer) {
			//	time.Sleep(50 * time.Millisecond)
			//	select {
			//	case <-ctx.Done():
			//		break
			//	default:
			//		sink.Success(payload.Clone(pl))
			//	}
			//})
		}),
		rsocket.RequestStream(func(pl payload.Payload) flux.Flux {
			// for test: client metadata is totals as string

			// Here is my Java client code:
			//@Test
			//public void testRequestStream() {
			//	final int totals = 20;
			//	final CountDownLatch cdl = new CountDownLatch(totals);
			//	this.client.requestStream(
			//		DefaultPayload.create(RandomStringUtils.randomAlphanumeric(32), String.valueOf(totals)))
			//	.doOnNext(payload -> {
			//		log.info("income: data={}, metadata={}", payload.getDataUtf8(), payload.getMetadataUtf8());
			//		cdl.countDown();
			//	})
			//	.subscribe();
			//	try {
			//		cdl.await();
			//	} catch (InterruptedException e) {
			//		Thread.currentThread().interrupt();
			//		throw new Errorf(e);
			//	}
			//}
			s := pl.DataUTF8()
			m, _ := pl.MetadataUTF8()
			log.Println("[Responder::RequestStream] data:", s, "metadata:", m)
			totals := 5
			if n, err := strconv.Atoi(m); err == nil {
				totals = n
			}
			return flux.Create(func(ctx context.Context, emitter flux.Sink) {
				for i := 0; i < totals; i++ {
					// You can use context for graceful coroutine shutdown, stop produce.
					select {
					case <-ctx.Done():
						log.Println("[Responder::RequestStream] ctx done:", ctx.Err())
						return
					default:
						//time.Sleep(10 * time.Millisecond)
						emitter.Next(payload.NewString(fmt.Sprintf("%s_%d", s, i), m))
					}
				}
				emitter.Complete()
			})
		}),
		rsocket.RequestChannel(func(payloads rx.Publisher) flux.Flux {
			//return payloads.(flux.Flux)
			payloads.(flux.Flux).
				//LimitRate(1).
				SubscribeOn(scheduler.Elastic()).
				DoOnNext(func(elem payload.Payload) {
					log.Println("[Responder::RequestChannel] receiving:", elem)
				}).
				Subscribe(context.Background())
			return flux.Create(func(i context.Context, sink flux.Sink) {
				for i := 0; i < 3; i++ {
					sink.Next(payload.NewString("world", fmt.Sprintf("%d", i)))
				}
				sink.Complete()
			})

			//return payloads.(rx.Flux)
			// echo all incoming payloads
			//return rx.NewFlux(func(ctx context.Context, emitter rx.Producer) {
			//	payloads.(rx.Flux).
			//		DoFinally(func(ctx context.Context, st rx.SignalType) {
			//			emitter.Complete()
			//		}).
			//		DoOnNext(func(ctx context.Context, s rx.Subscription, elem payload.Payload) {
			//			metadata, _ := elem.Metadata()
			//			_ = emitter.Next(payload.New(elem.Data(), metadata))
			//		}).
			//		SubscribeOn(rx.ElasticScheduler()).
			//		Subscribe(context.Background())
			//})
		}),
	)
}
