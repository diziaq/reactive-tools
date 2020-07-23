package dzq.sandbox.tools.reactor.client.request.body.receiver;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;


@DisplayName("suite for testing InsertionReceiver")
public class InsertionReceiverTest {

  @Test
  @DisplayName("should extract body from request with not empty body")
  public void t412d3497() {
    var sampleBody = new Object();
    var sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create(""))
                            .body(BodyInserters.fromValue(sampleBody)).build();

    var receiver = InsertionReceiver.forClass(Object.class);

    Object result = receiver.receiveValue(sampleRequest.body());

    assertSame(sampleBody, result);
  }

  @Test
  @DisplayName("should throw when trying to extract body with incompatible class")
  public void t4ce36787() {
    var sampleBody = new Object();
    var sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create(""))
                            .body(BodyInserters.fromValue(sampleBody)).build();

    var receiver = InsertionReceiver.forClass(String.class);

    var exception = assertThrows(RuntimeException.class, () -> receiver.receiveValue(sampleRequest.body()));

    assertEquals("Value has unexpected type (java.lang.Object) instead of (java.lang.String)", exception.getMessage());
  }

  @Test
  @DisplayName("should throw when trying to extract body from request with empty body")
  public void t3a252597() {
    var sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create("")).build();

    var receiver = InsertionReceiver.forClass(Object.class);

    var exception = assertThrows(RuntimeException.class, () -> receiver.receiveValue(sampleRequest.body()));

    assertEquals("Value was not received, check your inserter worked properly", exception.getMessage());
  }

  @Test
  @DisplayName("should throw when trying to extract empty body from request with non-empty body")
  public void t2385b632() {
    var sampleBody = new Object();
    var sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create(""))
                            .body(BodyInserters.fromValue(sampleBody)).build();

    var receiver = InsertionReceiver.forEmptyBody();

    var exception = assertThrows(RuntimeException.class, () -> receiver.receiveValue(sampleRequest.body()));

    assertThat(exception.getMessage(), containsString("Value is received unexpectedly"));
  }

  @Test
  @DisplayName("should extract null from request with empty body")
  public void t45727321() {
    var sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create("")).build();

    var receiver = InsertionReceiver.forEmptyBody();

    Void result = receiver.receiveValue(sampleRequest.body());

    assertNull(result);
  }
}
