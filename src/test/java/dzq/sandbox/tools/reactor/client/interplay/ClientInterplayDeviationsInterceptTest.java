package dzq.sandbox.tools.reactor.client.interplay;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


@DisplayName("suite for testing handling of response deviations ")
public class ClientInterplayDeviationsInterceptTest {

  @Test
  @DisplayName("should have respondent's default failure replacement to be an exception thrown")
  public void t572a6565() {
    ClientResponse sampleResponse = randomResponse();
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(sampleResponse);
    Predicate<ClientResponse> allowNonePredicate = x -> false;

    var responseMono = ClientInterplay
                           .create(sampleRespondent)
                           .start("dummy")
                           .send(null)
                           .expect(allowNonePredicate)
                           .release();

    var exception = assertThrows(RuntimeException.class, () -> responseMono.block());

    assertEquals("deviation intercept is not defined", exception.getMessage());
  }

  @Test
  @DisplayName("when expected predicate is failed `release()` should return respondent's deviation intercept result")
  public void t42b66d05() {
    ClientResponse sampleResponse = randomResponse();
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(createValidResponse(HttpStatus.OK, "unreachable"));
    ResponseMutation.Source<String> deviationIntercept = x -> y -> Mono.just(sampleResponse);
    Predicate<ClientResponse> allowNonePredicate = x -> false;

    var responseMono = ClientInterplay
                           .create(sampleRespondent, deviationIntercept)
                           .start("dummy")
                           .send(null)
                           .expect(allowNonePredicate)
                           .release();

    var responseResult = responseMono.block();

    assertSame(sampleResponse, responseResult);
  }

  @Test
  @DisplayName("method `deviationIntercept(R)` should bind new deviation intercept to an existing respondent")
  public void t68e38c27() {
    ClientResponse sampleResponse = randomResponse();
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(createValidResponse(HttpStatus.OK, "unreachable"));
    ResponseMutation.Source<String> deviationIntercept = x -> y -> Mono.just(sampleResponse);
    Predicate<ClientResponse> allowNonePredicate = x -> false;

    var responseMono = ClientInterplay
                           .create(sampleRespondent, x -> y -> Mono.error(new RuntimeException("skipped intercept")))
                           .deviationIntercept(deviationIntercept)
                           .start("dummy")
                           .send(null)
                           .expect(allowNonePredicate)
                           .release();

    var responseResult = responseMono.block();

    assertSame(sampleResponse, responseResult);
  }

  // -----------------------------------------------------

  private static ClientResponse randomResponse() {
    return createValidResponse(HttpStatus.SWITCHING_PROTOCOLS, "something");
  }

  private static ClientResponse createValidResponse(HttpStatus status, String body) {
    return ClientResponse.create(status).body(body).build();
  }
}
