extern crate futures as f;
extern crate rsocket_rust;
extern crate tokio;

use core::marker;
use std::io::Bytes;

use rsocket_rust::prelude::{Payload, RSocket, RSocketError};
use tokio::prelude::stream::IterOk;
use self::f::future::ok;
use self::f::{TryFutureExt, Future, Stream};

pub struct Response;

impl RSocket for Response {
    fn metadata_push(&self, req: Payload) -> Box<dyn  Future<Item=(), Error=RSocketError>> {
        println!("receive metadata_push: {:?}", req);
        Box::new(ok(()).map_err(|()| RSocketError::from("foobar")))
    }

    fn fire_and_forget(&self, req: Payload) -> Box<dyn Future<Item=(), Error=RSocketError>> {
        println!("receive request_fnf: {:?}", req);
        Box::new(ok(()).map_err(|()| RSocketError::from("foobar")))
    }

    fn request_response(
        &self,
        req: Payload,
    ) -> Box<dyn Future<Item=Payload, Error=RSocketError>> {
        println!(">>>>>>>> mock responder: {:?}", req);
        Box::new(ok(req))
    }

    fn request_stream(&self, req: Payload) -> Box<dyn Stream<Item=Payload, Error=RSocketError>> {
        println!(">>>>>>>> accept stream: {:?}", req);
        let mut results = vec![];
        for n in 0..10 {
            let pa = Payload::builder()
                .set_data(Bytes::from(format!("DATA_{}", n)))
                .set_metadata(Bytes::from(format!("METADATA_{}", n)))
                .build();
            results.push(pa);
        }
        Box::new(iter_ok(results))
    }
}

fn iter_ok<I, E>(i: I) -> IterOk<I::IntoIter, E> where I: IntoIterator {
    IterOk {
        iter: i.into_iter(),
        _marker: marker::PhantomData,
    }
}