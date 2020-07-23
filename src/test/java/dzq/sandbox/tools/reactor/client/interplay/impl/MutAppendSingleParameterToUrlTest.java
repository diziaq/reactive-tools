package dzq.sandbox.tools.reactor.client.interplay.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;

import dzq.sandbox.tools.reactor.client.interplay.RequestMutation;
import java.net.URI;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;


public class MutAppendSingleParameterToUrlTest {

  @ParameterizedTest(name = "{index}: {3}")
  @MethodSource("samples")
  @DisplayName("constructor `MutAppendSingleParameterToUrl` should return a mutation converting request path appending a single parameter")
  public void t238002a3(String sampleUrl, String paramName, String paramValue, String expectedUrl) {
    var sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create(sampleUrl)).build();
    var mutation = new MutAppendSingleParameterToUrl(paramName, paramValue);

    var mutatedRequest = mutation.affect(sampleRequest);

    var url = mutatedRequest.url();

    assertEquals(expectedUrl, url.toString());
  }

  @ParameterizedTest(name = "{index}: {3}")
  @MethodSource("samples")
  @DisplayName("builder `RequestMutation.appendUrlParam(S).by(S)` should return a mutation converting request path appending a single parameter")
  public void t426e2360(String sampleUrl, String paramName, String paramValue, String expectedUrl) {
    var sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create(sampleUrl)).build();
    var mutationSource = RequestMutation.appendUrlParam(paramName).by(paramValue);

    var mutatedRequest = mutationSource.affect(sampleRequest);

    var url = mutatedRequest.url();

    assertEquals(expectedUrl, url.toString());
  }

  public static Stream<Arguments> samples() {
    return Stream.of(
        Arguments.of("simple/path", "abc", "123", "simple/path?abc=123"),
        Arguments.of("path/with?a=qwerty", "def", "63832", "path/with?a=qwerty&def=63832")
    );
  }
}
