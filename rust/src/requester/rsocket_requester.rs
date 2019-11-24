use futures::prelude::*;
use rsocket_rust::prelude::*;

use crate::requester;

pub struct Requester;

impl Requester {
    pub fn fire_and_forget() -> () {
        println!("====ExecFireAndForget====");

        let cli = requester::build_client();

        cli.fire_and_forget(Payload::from("Mock FNF")).wait().unwrap();

        // metadata push
        cli.metadata_push(Payload::builder().set_metadata_utf8("metadata only!").build()).wait().unwrap();

        let _ = cli.on_close();
    }

    pub fn request_response() -> () {
        println!("====ExecRequestResponse====");
        let cli = requester::build_client();

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

    pub fn request_stream() -> () {
        println!("====ExecRequestStream====");

        let cli = requester::build_client();

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

    pub fn request_channel() -> () {}
}

