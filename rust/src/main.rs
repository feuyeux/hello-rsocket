use hello_rsocket::requester;
use hello_rsocket::responder;
use std::thread;
use std::time::Duration;

fn main() {
    thread::spawn(|| {
        let _ = responder::server::start();
    });

    let sleep_millis = Duration::from_millis(5);

    thread::sleep(sleep_millis);
    requester::fire_and_forget::exec_fire_and_forget();

    thread::sleep(sleep_millis);
    requester::request_channel::exec_request_channel();

    thread::sleep(sleep_millis);
    requester::request_response::exec_request_response();

    thread::sleep(sleep_millis);
    requester::request_stream::exec_request_stream();
}
