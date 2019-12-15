use futures::prelude::*;
use rsocket_rust::prelude::*;
use crate::responder::ResponseCoon;
//use crate::responder::ResponseCoon;

pub struct RequestCoon {
    pub client:  Client
}

impl RequestCoon {
    pub async fn new() -> RequestCoon {
        RequestCoon {
            client: RSocketFactory::connect()
                //.acceptor(|| Box::new(responder::ResponseCoon))
                .acceptor(|| Box::new(ResponseCoon))
                .transport("tcp://127.0.0.1:7878")
                .setup(Payload::from("READY!"))
                .mime_type("text/plain", "text/plain")
                .start()
                .await
                .unwrap()
        }
    }

    pub fn destroy(self) {
        self.client.close();
    }

    pub async fn meta_push(&self) {
        let meta = Payload::builder().set_metadata_utf8("metadata only!").build();
        self.client.metadata_push(meta).await;
    }
    pub async fn fnf(&self) {
        println!("====ExecFireAndForget====");
        let fnf = Payload::from("Mock FNF");
        self.client.fire_and_forget(fnf).await;
    }

    pub async fn request_response(&self) {
        println!("====ExecRequestResponse====");
        let p = Payload::builder()
            .set_data_utf8("Hello World!")
            .set_metadata_utf8("Rust")
            .build();
        let resp = self.client.request_response(p).await.unwrap();
        println!("<<<<<<<< : {:?}", resp.data());
    }

    pub async fn request_stream(&self) {
        println!("====ExecRequestStream====");
        let sending = Payload::builder()
            .set_data_utf8("Hello Rust!")
            .set_metadata_utf8("foobar")
            .build();

        let mut results = self.client.request_stream(sending);
        loop {
            match results.next().await {
                Some(v) => println!("<<<<<<<< STREAM: {:?}", v),
                None => break,
            }
        }
    }

    pub async fn request_channel(&self) {
        println!("====ExecRequestChannel====");
        let mut sends = vec![];
        for i in 0..3 {
            let p = Payload::builder()
                .set_data_utf8(&format!("Hello#{}", i))
                .set_metadata_utf8("Rust")
                .build();
            sends.push(p);
        }

        let mut resps = self.client.request_channel(Box::pin(stream::iter(sends)));
        while let Some(v) = resps.next().await {
            println!("<<<<<<<< CHANNEL: {:?}", v);
        }
    }
}