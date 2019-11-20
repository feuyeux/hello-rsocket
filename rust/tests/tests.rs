use hello_rsocket::requester;
use hello_rsocket::responder;
use std::thread;
use std::time::Duration;

#[test]
fn test_fnf() {
    setup();
    requester::fire_and_forget::exec_fire_and_forget();
}

#[test]
fn test_rr() {
    setup();
    requester::request_response::exec_request_response();
    sleep();
}

#[test]
fn test_rs() {
    setup();
    requester::request_stream::exec_request_stream();
    sleep();
}

fn setup() {
    thread::spawn(|| {
        let _ = responder::server::start();
    });
}

fn sleep() {
    let sleep_millis = Duration::from_millis(5);
    thread::sleep(sleep_millis);
}
