package dzq.sandbox.tools.reactor.client.interplay;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("suite for testing ResponseMutation factory functions")
public class ResponseMutationTest {

  @Test
  @DisplayName("ResponseMutation.errorFilter should return instance of ResponseMutation.Source")
  public void t146b72e1() {
    var result = ResponseMutation.errorFilter(x -> x + "123");

    assertThat(result, instanceOf(ResponseMutation.Source.class));
  }
}
