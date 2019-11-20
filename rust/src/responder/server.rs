use crate::responder::rsocket_acceptor::Response;
use rsocket_rust::prelude::*;

pub fn start() {
    let server = RSocketFactory::receive()
        .acceptor(|_setup, _sending_socket| Box::new(Response))
        .transport(URI::Tcp("127.0.0.1:7979"))
        .serve();
    tokio::run(server);
}
