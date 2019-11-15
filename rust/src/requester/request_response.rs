use futures::prelude::*;
use rsocket_rust::prelude::*;

use crate::requester::client;

pub fn exec_request_response() {
    println!("====ExecRequestResponse====");
    let cli = client::build_client();

    for n in 0..3 {
        let pa = Payload::builder()
            .set_data_utf8("Hello World!")
            .set_metadata_utf8(&format!("#{}", n))
            .build();
        let resp = cli.request_response(pa).wait().unwrap();
        println!("<<<<<<<< : {:?}", resp.data());
    }

    let _ = cli.on_close();
}

