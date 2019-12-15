extern crate rsocket_rust;
extern crate tokio;
use std::env;
use std::error::Error;
use std::string::ToString;
use self::rsocket_rust::prelude::RSocketFactory;

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {
    let addr = env::args().nth(1).unwrap_or("tcp://127.0.0.1:7878".to_string());
    RSocketFactory::receive()
        .transport(&addr)
        .acceptor(|setup, _socket| {
            println!("accept setup: {:?}", setup);
            Ok(Box::new(EchoRSocket))
            // Or you can reject setup
            // Err(From::from("SETUP_NOT_ALLOW"))
        })
        .serve()
        .await
}