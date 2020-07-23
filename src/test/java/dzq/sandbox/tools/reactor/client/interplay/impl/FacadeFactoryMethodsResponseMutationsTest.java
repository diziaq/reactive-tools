package dzq.sandbox.tools.reactor.client.interplay.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dzq.sandbox.tools.reactor.client.interplay.impl.FacadeFactoryMethods.ResponseMutations;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;


@DisplayName("suite for testing FacadeFactoryMethods.ResponseMutations factory functions")
public class FacadeFactoryMethodsResponseMutationsTest {

  @Test
  @DisplayName("ResponseMutations.treatingAsError should return a mutation converting response to error " +
                   "with a message transformed by the given function")
  public void t1ad720e1() {
    String sampleLabel = "sample-label";
    Function<String, String> sampleFunction = x -> "<<<" + x + ">>>";
    ClientResponse sampleResponse = ClientResponse.create(HttpStatus.OK).body("hello").build();

    var mutation = ResponseMutations.treatingAsError(sampleLabel, sampleFunction);

    var mutatedErrorResponse = mutation.affect(sampleResponse);

    var exception = assertThrows(RuntimeException.class, () -> mutatedErrorResponse.block());

    assertEquals("Failed `sample-label`: <<<hello>>>", exception.getMessage());
  }
}
