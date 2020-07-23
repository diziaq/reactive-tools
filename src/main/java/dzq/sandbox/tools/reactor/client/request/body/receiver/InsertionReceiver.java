package dzq.sandbox.tools.reactor.client.request.body.receiver;


import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;


// This interface is for cooperation with BodyInserter.insert method
//
// Its main intention is extracting a ClientRequest's body as a value of particular type
// for testing purposes
//
// Examples:
// --------------------
//    1. ClientRequest request = getRequestWithASpecificBody();
//       SampleBodyJson jsonBody = InsertionReceiver.forClass(SampleBodyJson.class).receiveValue(request.body());
//       [
//         verification of contents of jsonBody
//       ]
// --------------------
//    2. ClientRequest request = getRequestWithEmptyBody();
//       Void emptyBody = InsertionReceiver.forEmptyBody().receiveValue(request.body());
//       [
//         We use null (only value for Void) for representation of empty body.
//         At this point either emptyBody==null or an exception is already thrown from InsertionReceiver.
//         Nevertheless, it is recommended using assertNull(emptyBody) to explicitly express intention of a test case.
//       ]
public interface InsertionReceiver<T> {

  T receiveValue(BodyInserter<?, ? extends ReactiveHttpOutputMessage> bodyInserter);

  static <T> InsertionReceiver<T> forClass(Class<T> clazz) {
    return new AccurateValueReceiver<>(clazz);
  }

  static InsertionReceiver<Void> forEmptyBody() {
    return new EmptyValueReceiver<>();
  }
}
