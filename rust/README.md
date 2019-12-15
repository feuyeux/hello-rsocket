```rust
▶ cargo run
    Finished dev [unoptimized + debuginfo] target(s) in 0.05s
     Running `target/debug/hello-rsocket`
====ExecFireAndForget====
>>>>>>>> fire_and_forget: Payload { m: None, d: Some(b"Mock FNF") }
>>>>>>>> metadata_push: Payload { m: Some(b"metadata only!"), d: None }
====ExecRequestResponse====
>>>>>>>> request_response: data=Some(b"Hello World!"),meta=Some(b"#0")
<<<<<<<< : Some(b"Hello World!")
>>>>>>>> request_response: data=Some(b"Hello World!"),meta=Some(b"#1")
<<<<<<<< : Some(b"Hello World!")
>>>>>>>> request_response: data=Some(b"Hello World!"),meta=Some(b"#2")
<<<<<<<< : Some(b"Hello World!")
====ExecRequestStream====
>>>>>>>> request_stream: Payload { m: Some(b"foobar"), d: Some(b"Hello Rust!") }
<<<<<<<< STREAM: Payload { m: Some(b"METADATA_0"), d: Some(b"DATA_0") }
<<<<<<<< STREAM: Payload { m: Some(b"METADATA_1"), d: Some(b"DATA_1") }
<<<<<<<< STREAM: Payload { m: Some(b"METADATA_2"), d: Some(b"DATA_2") }
```