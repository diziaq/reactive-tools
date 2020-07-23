package dzq.sandbox.tools.reactor.client.interplay.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;

import dzq.sandbox.tools.reactor.client.interplay.impl.FacadeFactoryMethods.RequestMutations;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;


@DisplayName("suite for testing FacadeFactoryMethods.RequestMutations factory functions")
public class FacadeFactoryMethodsRequestMutationsTest {

  @Test
  @DisplayName("ResponseMutations.treatingAsError should return a mutation converting response to error " +
                   "with a message transformed by the given function")
  public void t107506b1() {
    ClientRequest sampleResponse = ClientRequest.create(HttpMethod.GET, URI.create("my/path")).build();
    var mutation = RequestMutations.appendingParameter("something", "x1x2x3");

    var mutatedRequest = mutation.affect(sampleResponse);

    var url = mutatedRequest.url();

    assertEquals("my/path?something=x1x2x3", url.toString());
  }
}
