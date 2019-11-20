use rsocket_rust::prelude::*;

pub fn build_client() -> Client {
    let cli = RSocketFactory::connect()
        .acceptor(|| Box::new(MockResponder))
        .transport(URI::Tcp("127.0.0.1:7979"))
        .setup(Payload::from("READY!"))
        .mime_type("text/plain", "text/plain")
        .start()
        .unwrap();
    cli
}
