use std::thread;
use std::time::Duration;

mod requester;
mod responder;

fn main() {
    thread::spawn(|| {
        responder::server::start();
        thread::sleep(Duration::from_millis(1));
    });

    let sleep_millis = Duration::from_millis(5);

    thread::sleep(sleep_millis);
    requester::fire_and_forget::exec_fire_and_forget();
    requester::request_channel::exec_request_channel();
    requester::request_response::exec_request_response();
    requester::request_stream::exec_request_stream();
}
