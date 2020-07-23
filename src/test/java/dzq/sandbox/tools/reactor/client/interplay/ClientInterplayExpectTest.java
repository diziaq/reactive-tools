package dzq.sandbox.tools.reactor.client.interplay;


import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


@DisplayName("suite for testing `expect()` method behaviour on different inputs")
public class ClientInterplayExpectTest {

  @Test
  @DisplayName("method `expect(HS)` should expect request with given status")
  public void t5a570272() {
    ClientResponse sampleResponse = createValidResponse(HttpStatus.MULTI_STATUS, "dummy");
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(sampleResponse);

    var responseMono = ClientInterplay
                           .create(sampleRespondent)
                           .start("dummy")
                           .send(null)
                           .expect(HttpStatus.MULTI_STATUS)
                           .release();

    ClientResponse resultResponse = responseMono.block();

    assertSame(sampleResponse, resultResponse);
  }

  @Test
  @DisplayName("method `expect(HS)` should redirect to deviation any request with unexpected status")
  public void t4b48e628() {
    ClientResponse originalResponse = createValidResponse(HttpStatus.OK, "dummy");
    ClientResponse replacementResponse = createValidResponse(HttpStatus.LOCKED, "replacement");
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(originalResponse);
    ResponseMutation.Source<String> deviationIntercept = x -> y -> Mono.just(replacementResponse);

    var responseMono = ClientInterplay
                           .create(sampleRespondent, deviationIntercept)
                           .start("dummy")
                           .send(null)
                           .expect(HttpStatus.MULTI_STATUS)
                           .release();

    ClientResponse resultResponse = responseMono.block();

    assertSame(replacementResponse, resultResponse);
  }

  // -----------------------------------------------------

  private static ClientResponse createValidResponse(HttpStatus status, String body) {
    return ClientResponse.create(status).body(body).build();
  }
}
