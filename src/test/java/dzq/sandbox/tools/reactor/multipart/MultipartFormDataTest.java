package dzq.sandbox.tools.reactor.multipart;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;


@DisplayName("suite for testing MultipartFormData factory functions")
public class MultipartFormDataTest {

  @Test
  @DisplayName("`of` should return instance of DefaultMultipartFormData")
  public void t5d67e657() {
    var result = MultipartFormData.of(new LinkedMultiValueMap<>());

    assertThat(result, instanceOf(DefaultMultipartFormData.class));
  }
}
