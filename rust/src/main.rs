use hello_rsocket::requester;
use hello_rsocket::responder;
use std::thread;
use std::time::Duration;
use requester::rsocket_requester::Requester;
fn main() {
    thread::spawn(|| {
        let _ = responder::start();
    });

    let sleep_millis = Duration::from_millis(5);

    thread::sleep(sleep_millis);
    Requester::fire_and_forget();

    thread::sleep(sleep_millis);
    Requester::request_channel();

    thread::sleep(sleep_millis);
    Requester::request_response();

    thread::sleep(sleep_millis);
    Requester::request_stream();
}
