extern crate rsocket_rust;
extern crate tokio;

use crate::responder::rsocket_acceptor::Response;
use rsocket_rust::prelude::*;
use std::boxed::*;
use bytes::Bytes;

pub fn start() {
  let server = RSocketFactory::receive()
    .acceptor(|setup, sending_socket| {
        let resp = sending_socket
          .request_response(
            Payload::builder()
              .set_data(Bytes::from("Hello Client!"))
              .build(),
          );
          print!("{:?}",resp);
          Box::new(Response);
    })
    .transport(URI::Tcp("127.0.0.1:7979"))
    .serve();
  tokio::run(server);
}
