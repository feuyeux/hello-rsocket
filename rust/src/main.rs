use hello_rsocket::requester;
use hello_rsocket::responder;
use std::thread;
use std::time::Duration;
use requester::Requester;

fn main() {
    let sleep_millis = Duration::from_millis(5);

    thread::spawn(|| {
        let _ = responder::start();
    });

    let requester = Requester::new();

    thread::sleep(sleep_millis);
    requester.fnf_push();

    thread::sleep(sleep_millis);
    requester.request_response();

    thread::sleep(sleep_millis);
    requester.request_stream();

    thread::sleep(sleep_millis);
    requester.request_channel();
}
