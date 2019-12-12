use tokio::sync::mpsc;
use std::error::Error;
use futures::prelude::*;
use rsocket_rust::prelude::*;

pub async fn start() -> Result<(), Box<dyn Error>> {
    RSocketFactory::receive()
        .transport(URI::Tcp("127.0.0.1:7878".to_string()))
        .acceptor(|_setup, _sending_socket| Box::new(ResponseCoon))
        .serve()
        .await
}

pub struct ResponseCoon;

impl RSocket for ResponseCoon {
    fn metadata_push(&self, req: Payload) -> Mono<()> {
        println!(">>>>>>>> metadata_push: {:?}", req);
        Box::pin(future::ok::<(), RSocketError>(()))
    }

    fn fire_and_forget(&self, req: Payload) -> Mono<()> {
        println!(">>>>>>>> fire_and_forget: {:?}", req);
        Box::pin(future::ok::<(), RSocketError>(()))
    }

    fn request_response(&self, req: Payload) -> Mono<Payload> {
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

        Box::pin(futures::stream::iter(vec![
            Ok(req.clone()),
            Ok(req.clone()),
            Ok(req),
        ]))
    }

    fn request_channel(&self, mut reqs: Flux<Payload>) -> Flux<Payload> {
        let (sender, receiver) = mpsc::unbounded_channel::<RSocketResult<Payload>>();
        tokio::spawn(async move {
            while let Some(it) = reqs.next().await {
                let pa = it.unwrap();
                println!("{:?}", pa);
                sender.send(Ok(pa)).unwrap();
            }
        });
        Box::pin(receiver)
    }
}
