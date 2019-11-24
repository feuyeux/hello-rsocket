use hello_rsocket::responder;
use hello_rsocket::requester::rsocket_requester::Requester;
use std::thread;
use std::time::Duration;

#[test]
fn test_fnf() {
    setup();
    Requester::fire_and_forget();
}

#[test]
fn test_rr() {
    setup();
    Requester::request_response();
    sleep();
}

#[test]
fn test_rs() {
    setup();
    Requester::request_stream();
    sleep();
}

fn setup() {
    thread::spawn(|| {
        let _ = responder::start();
    });
}

fn sleep() {
    let sleep_millis = Duration::from_millis(5);
    thread::sleep(sleep_millis);
}
