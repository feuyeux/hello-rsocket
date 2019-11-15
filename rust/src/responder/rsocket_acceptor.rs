use bytes::Bytes;
use futures::{Future, Stream};
use futures::future::ok;
use rsocket_rust::prelude::{Payload, RSocket, RSocketError};
use tokio::prelude::stream::*;

pub struct Response;

impl RSocket for Response {
    fn metadata_push(&self, req: Payload) -> Box<dyn  Future<Item=(), Error=RSocketError>> {
        println!(">>>>>>>> metadata_push: {:?}", req);
        Box::new(ok(()).map_err(|()| RSocketError::from("foobar")))
    }

    fn fire_and_forget(&self, req: Payload) -> Box<dyn Future<Item=(), Error=RSocketError>> {
        println!(">>>>>>>> fire_and_forget: {:?}", req);
        Box::new(ok(()).map_err(|()| RSocketError::from("foobar")))
    }

    fn request_response(&self, req: Payload) -> Box<dyn Future<Item=Payload, Error=RSocketError>> {
        println!(">>>>>>>> request_response: data={:?},meta={:?}", req.data(), req.metadata());
        Box::new(ok(req))
    }

    fn request_stream(&self, req: Payload) -> Box<dyn Stream<Item=Payload, Error=RSocketError>> {
        println!(">>>>>>>> request_stream: {:?}", req);
        let mut results = vec![];
        for n in 0..3 {
            let pa = Payload::builder()
                .set_data(Bytes::from(format!("DATA_{}", n)))
                .set_metadata(Bytes::from(format!("METADATA_{}", n)))
                .build();
            results.push(pa);
        }
        Box::new(iter_ok(results))
    }
}