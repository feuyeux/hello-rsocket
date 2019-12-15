extern crate rsocket_rust;

use self::rsocket_rust::prelude::{RSocketFactory, EchoRSocket};
use self::rsocket_rust::frame::Payload;

#[tokio::main]
#[test]
async fn test() {
    let cli = RSocketFactory::connect()
        .acceptor(|| Box::new(EchoRSocket))
        .transport("tcp://127.0.0.1:7878")
        .setup(Payload::from("READY!"))
        .mime_type("text/plain", "text/plain")
        .start()
        .await
        .unwrap();
    let req = Payload::builder()
        .set_data_utf8("Hello World!")
        .set_metadata_utf8("Rust")
        .build();
    let res = cli.request_response(req).await.unwrap();
    println!("got: {:?}", res);
    cli.close();
}
