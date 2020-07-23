package dzq.sandbox.tools.reactor.client.interplay.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dzq.sandbox.tools.reactor.client.interplay.ResponseMutation;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;


public class MutTreatResponseAsErrorTest {

  @ParameterizedTest(name = "{index}: {3}")
  @MethodSource("samples")
  @DisplayName("constructor `MutTreatResponseAsError` should return a mutation converting response to error with a message transformed by the given function")
  public void t51e01724(
      String sampleLabel, String sampleResponseBody, Function<String, String> bodyTransform,
      String expectedErrorMessage
  ) {
    var sampleResponse = ClientResponse.create(HttpStatus.OK).body(sampleResponseBody).build();

    var mutation = new MutTreatResponseAsError(sampleLabel, bodyTransform);

    var exception = assertThrows(RuntimeException.class, () -> mutation.affect(sampleResponse).block());

    assertEquals(expectedErrorMessage, exception.getMessage());
  }

  @ParameterizedTest(name = "{index}: {3}")
  @MethodSource("samples")
  @DisplayName("builder `ResponseMutation.errorFilter(S).by(S)` should return a mutation converting response to error with a message transformed by the given function")
  public void t400c2a14(
      String sampleLabel, String sampleResponseBody, Function<String, String> bodyTransform,
      String expectedErrorMessage
  ) {
    var sampleResponse = ClientResponse.create(HttpStatus.OK).body(sampleResponseBody).build();

    var mutation = ResponseMutation.errorFilter(bodyTransform).by(sampleLabel);

    var exception = assertThrows(RuntimeException.class, () -> mutation.affect(sampleResponse).block());

    assertEquals(expectedErrorMessage, exception.getMessage());
  }

  public static Stream<Arguments> samples() {
    return Stream.of(
        Arguments.of("action", "this-is-body", f(x -> x), "Failed `action`: this-is-body"),
        Arguments.of("command", "{\"hello\":\"there\"}", f(x -> x.replace('"', '#')), "Failed `command`: {#hello#:#there#}")
    );
  }

  private static Function<String, String> f(Function<String, String> function) {
    return function;
  }
}
