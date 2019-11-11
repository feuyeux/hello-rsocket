mod requester;
mod responder;

fn main() {
    //responder::server::rsocket_rust;
    requester::fire_and_forget::exec_fire_and_forget();
    requester::request_channel::exec_request_channel();
    requester::request_response::exec_request_response();
    requester::request_stream::exec_request_stream();
}
