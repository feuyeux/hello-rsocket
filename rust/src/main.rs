//use crate::{responder, requester};
use std::thread;
use std::time::Duration;
use hello_rsocket::{responder, requester};
use std::error::Error;

#[tokio::main]
async fn main() -> Result<(), Box<dyn Error>> {
    let sleep_millis = Duration::from_millis(5);

    thread::sleep(sleep_millis);
    let request_coon = requester::RequestCoon::new().await;

    request_coon.fnf().await;
    request_coon.meta_push().await;
    thread::sleep(sleep_millis);

    request_coon.request_response().await;
    thread::sleep(sleep_millis);

    request_coon.request_stream().await;
    thread::sleep(sleep_millis);

    //futures do nothing unless you `.await` or poll them
    request_coon.request_channel().await;

    responder::start().await
}
