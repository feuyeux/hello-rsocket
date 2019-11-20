use futures::prelude::*;
use rsocket_rust::prelude::*;

use crate::requester::client;

pub fn exec_request_stream() {
    println!("====ExecRequestStream====");

    let cli = client::build_client();

    let sending = Payload::builder()
        .set_data_utf8("Hello Rust!")
        .set_metadata_utf8("foobar")
        .build();

    let task = cli.request_stream(sending).map_err(|_| ()).for_each(|it| {
        println!("<<<<<<<< STREAM: {:?}", it);
        Ok(())
    });
    task.wait().unwrap();

    let _ = cli.on_close();
}
