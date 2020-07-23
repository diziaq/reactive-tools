package dzq.sandbox.tools.reactor.client.interplay;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


@DisplayName("suite for testing basic successful cases of receiving response")
public class ClientInterplayBasicSuccessfulCasesTest {

  @Test
  @DisplayName("when expected predicate is passed `release()` should return respondent's response unchanged")
  public void t3b46c295() {
    ClientResponse sampleResponse = randomResponse();
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(sampleResponse);
    Predicate<ClientResponse> allowAllPredicate = x -> true;

    var responseMono = ClientInterplay
                           .create(sampleRespondent)
                           .start("dummy")
                           .send(null)
                           .expect(allowAllPredicate)
                           .release();

    ClientResponse resultResponse = responseMono.block();

    assertSame(sampleResponse, resultResponse);
  }

  @Test
  @DisplayName("when expected predicate is passed `releaseBody(C)` should return respondent's response body unchanged")
  public void t35e2210a() {
    String sampleBody = "hello-body";
    ClientResponse sampleResponse = createValidResponse(HttpStatus.OK, sampleBody);
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(sampleResponse);
    Predicate<ClientResponse> allowAllPredicate = x -> true;

    var responseMono = ClientInterplay
                           .create(sampleRespondent)
                           .start("dummy")
                           .send(null)
                           .expect(allowAllPredicate)
                           .releaseBody(String.class);

    String resultBody = responseMono.block();

    assertEquals(sampleBody, resultBody);
  }

  @Test
  @DisplayName("when expected predicate is passed `releaseAs(F)` should return respondent's response mapped by the given func")
  public void t3214110a() {
    String sampleBody = "hello-body";
    ClientResponse sampleResponse = createValidResponse(HttpStatus.valueOf(204), sampleBody);
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(sampleResponse);
    Function<ClientResponse, Integer> map = cr -> cr.rawStatusCode() * 1000;

    var responseMono = ClientInterplay
                           .create(sampleRespondent)
                           .start("dummy")
                           .send(null)
                           .expect(allowAllPredicate())
                           .releaseAs(map);

    Integer resultValue = responseMono.block();

    assertEquals(204000, resultValue);
  }

  @ParameterizedTest(name = "{index} : {0}")
  @MethodSource("sampleResponseHeaderExpectations")
  @DisplayName("when expected predicate is passed `releaseHeader(S)` should return header value according to when respondent's response headers")
  public void t52a075d7(String expectedValue, String headerName, List<String> headerValues) {
    ClientResponse sampleResponse = createValidResponse(HttpStatus.MULTI_STATUS, headerName, headerValues);
    Respondent<ClientRequest, ClientResponse> sampleRespondent = cr -> Mono.just(sampleResponse);
    Predicate<ClientResponse> allowAllPredicate = x -> true;

    var responseMono = ClientInterplay
                           .create(sampleRespondent)
                           .start("dummy")
                           .send(null)
                           .expect(allowAllPredicate)
                           .releaseHeader(headerName);

    String resultHeaderValue = responseMono.block();

    assertEquals(expectedValue, resultHeaderValue);
  }

  public static Stream<Arguments> sampleResponseHeaderExpectations() {
    return Stream.of(
        Arguments.of("<none>", HttpHeaders.FROM, List.of()),
        Arguments.of("single-value", HttpHeaders.FROM, List.of("single-value")),
        Arguments.of("<many>", HttpHeaders.FROM, List.of("value-1", "value-2"))
    );
  }

  // -----------------------------------------------------

  private static Predicate<ClientResponse> allowAllPredicate() {
    return x -> true;
  }

  private static ClientResponse randomResponse() {
    return createValidResponse(HttpStatus.ALREADY_REPORTED, "something");
  }

  private static ClientResponse createValidResponse(HttpStatus status, String body) {
    return ClientResponse.create(status).body(body).build();
  }

  private static ClientResponse createValidResponse(HttpStatus status, String headerName, List<String> headerValues) {
    return ClientResponse.create(status).header(headerName, headerValues.toArray(new String[0])).build();
  }

}
