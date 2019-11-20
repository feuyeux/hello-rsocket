use futures::prelude::*;
use rsocket_rust::prelude::*;

use crate::requester::client;
pub fn exec_fire_and_forget() {
    println!("====ExecFireAndForget====");

    let cli = client::build_client();

    let fnf = Payload::from("Mock FNF");
    cli.fire_and_forget(fnf).wait().unwrap();

    let pa = Payload::builder()
        .set_metadata_utf8("metadata only!")
        .build();
    // metadata push
    cli.metadata_push(pa).wait().unwrap();

    let _ = cli.on_close();
}
