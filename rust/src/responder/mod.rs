use tokio::sync::mpsc;
use std::error::Error;
use futures::prelude::*;
use rsocket_rust::prelude::*;

pub async fn start() -> Result<(), Box<dyn Error>> {
    RSocketFactory::receive()
        .transport("tcp://127.0.0.1:7878")
        .acceptor(|_setup, _socket| {
            Ok(Box::new(ResponseCoon))
        })
        .serve()
        .await
}

pub struct ResponseCoon;

impl RSocket for ResponseCoon {
    fn metadata_push(&self, req: Payload) -> Mono<()> {
        println!(">>>>>>>> metadata_push: {:?}", req);
        Box::pin(async {})
    }

    fn fire_and_forget(&self, req: Payload) -> Mono<()> {
        println!(">>>>>>>> fire_and_forget: {:?}", req);
        Box::pin(async {})
    }

    fn request_response(&self, req: Payload) -> Mono<Result<Payload, RSocketError>>  {
        println!(
            ">>>>>>>> request_response: data={:?},meta={:?}",
            req.data(),
            req.metadata()
        );
        Box::pin(future::ok::<Payload, RSocketError>(req))
    }

    fn request_stream(&self, req: Payload) -> Flux<Payload> {
        println!(">>>>>>>> request_stream: {:?}", req);
//        let mut results = vec![];
//        for n in 0..3 {
//            let p = Payload::builder()
//                .set_data(Bytes::from(format!("DATA_{}", n)))
//                .set_metadata(Bytes::from(format!("METADATA_{}", n)))
//                .build();
//            results.push(p);
//        }
//        Box::pin(futures::stream::iter(results))
        Box::pin(futures::stream::iter(vec![req.clone(), req.clone(), req]))
    }

    fn request_channel(&self, mut reqs: Flux<Payload>) -> Flux<Payload> {
        let (sender, receiver) = mpsc::unbounded_channel::<Payload>();
        tokio::spawn(async move {
            while let Some(p) = reqs.next().await {
                println!("{:?}", p);
                sender.send(p).unwrap();
            }
        });
        Box::pin(receiver)
    }
}
