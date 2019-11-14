use futures::prelude::*;
use rsocket_rust::prelude::*;

pub fn exec_request_response(){
    println!("====ExecRequestResponse====");
    let cli = RSocketFactory::connect()
        .acceptor(|| Box::new(MockResponder))
        .transport(URI::Tcp("127.0.0.1:7979"))
        .setup(Payload::from("READY!"))
        .mime_type("text/plain", "text/plain")
        .start()
        .unwrap();

    for n in 0..11 {
        let pa = Payload::builder()
            .set_data_utf8("Hello World!")
            .set_metadata_utf8(&format!("#{}", n))
            .build();
        let resp = cli.request_response(pa).wait().unwrap();
        println!("******* response: {:?}", resp.data());
    }
    cli.on_close().wait().unwrap();
}