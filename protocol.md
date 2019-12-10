### Protocol
https://github.com/rsocket/rsocket/blob/master/Protocol.md
NOTE: Byte ordering is big endian.

#### Framing Format
Frame Length: (24 bits = max value 16,777,215)
RSocket Frame

#### Frame Header Format
Stream ID: (31 bits = max value 2^31-1 = 2,147,483,647) 
Frame Type: (6 bits = max value 63) 
Flags: (10 bits) 

Metadata Length: (24 bits = max value 16,777,215) 
Metadata Payload
Payload of Frame

#### Frame Types

| Type                                                         | Value | Description                                                  |
| :----------------------------------------------------------- | :---- | :----------------------------------------------------------- |
| **RESERVED**                                                 | 0x00  | **Reserved**                                                 |
| [**SETUP**](http://rsocket.io/docs/Protocol#frame-setup)     | 0x01  | **Setup**: Sent by client to initiate protocol processing.   |
| [**LEASE**](http://rsocket.io/docs/Protocol#frame-lease)     | 0x02  | **Lease**: Sent by Responder to grant the ability to send requests. |
| [**KEEPALIVE**](http://rsocket.io/docs/Protocol#frame-keepalive) | 0x03  | **Keepalive**: Connection keepalive.                         |
| [**REQUEST_RESPONSE**](http://rsocket.io/docs/Protocol#frame-request-response) | 0x04  | **Request Response**: Request single response.               |
| [**REQUEST_FNF**](http://rsocket.io/docs/Protocol#frame-fnf) | 0x05  | **Fire And Forget**: A single one-way message.               |
| [**REQUEST_STREAM**](http://rsocket.io/docs/Protocol#frame-request-stream) | 0x06  | **Request Stream**: Request a completable stream.            |
| [**REQUEST_CHANNEL**](http://rsocket.io/docs/Protocol#frame-request-channel) | 0x07  | **Request Channel**: Request a completable stream in both directions. |
| [**REQUEST_N**](http://rsocket.io/docs/Protocol#frame-request-n) | 0x08  | **Request N**: Request N more items with [Reactive Streams](http://www.reactive-streams.org/) semantics. |
| [**CANCEL**](http://rsocket.io/docs/Protocol#frame-cancel)   | 0x09  | **Cancel Request**: Cancel outstanding request.              |
| [**PAYLOAD**](http://rsocket.io/docs/Protocol#frame-payload) | 0x0A  | **Payload**: Payload on a stream. For example, response to a request, or message on a channel. |
| [**ERROR**](http://rsocket.io/docs/Protocol#frame-error)     | 0x0B  | **Error**: Error at connection or application level.         |
| [**METADATA_PUSH**](http://rsocket.io/docs/Protocol#frame-metadata-push) | 0x0C  | **Metadata**: Asynchronous Metadata frame                    |
| [**RESUME**](http://rsocket.io/docs/Protocol#frame-resume)   | 0x0D  | **Resume**: Replaces SETUP for Resuming Operation (optional) |
| [**RESUME_OK**](http://rsocket.io/docs/Protocol#frame-resume-ok) | 0x0E  | **Resume OK** : Sent in response to a RESUME if resuming operation possible (optional) |
| [**EXT**](http://rsocket.io/docs/Protocol#frame-ext)         | 0x3F  | **Extension Header**: Used To Extend more frame types as well as extensions. |

#### Error Codes

| Type                  | Value      | Description                                                  |
| :-------------------- | :--------- | :----------------------------------------------------------- |
| **RESERVED**          | 0x00000000 | **Reserved**                                                 |
| **INVALID_SETUP**     | 0x00000001 | The Setup frame is invalid for the server (it could be that the client is too recent for the old server). Stream ID MUST be 0. |
| **UNSUPPORTED_SETUP** | 0x00000002 | Some (or all) of the parameters specified by the client are unsupported by the server. Stream ID MUST be 0. |
| **REJECTED_SETUP**    | 0x00000003 | The server rejected the setup, it can specify the reason in the payload. Stream ID MUST be 0. |
| **REJECTED_RESUME**   | 0x00000004 | The server rejected the resume, it can specify the reason in the payload. Stream ID MUST be 0. |
| **CONNECTION_ERROR**  | 0x00000101 | The connection is being terminated. Stream ID MUST be 0. Sender or Receiver of this frame MAY close the connection immediately without waiting for outstanding streams to terminate. |
| **CONNECTION_CLOSE**  | 0x00000102 | The connection is being terminated. Stream ID MUST be 0. Sender or Receiver of this frame MUST wait for outstanding streams to terminate before closing the connection. New requests MAY not be accepted. |
| **APPLICATION_ERROR** | 0x00000201 | Application layer logic generating a [Reactive Streams](http://www.reactive-streams.org/) *onError*event. Stream ID MUST be > 0. |
| **REJECTED**          | 0x00000202 | Despite being a valid request, the Responder decided to reject it. The Responder guarantees that it didn't process the request. The reason for the rejection is explained in the Error Data section. Stream ID MUST be > 0. |
| **CANCELED**          | 0x00000203 | The Responder canceled the request but may have started processing it (similar to REJECTED but doesn't guarantee lack of side-effects). Stream ID MUST be > 0. |
| **INVALID**           | 0x00000204 | The request is invalid. Stream ID MUST be > 0.               |
| **RESERVED**          | 0xFFFFFFFF | **Reserved for Extension Use**                               |

### Impl
- https://github.com/rsocket/rsocket-java
- https://github.com/rsocket/rsocket-go
- https://github.com/rsocket/rsocket-js