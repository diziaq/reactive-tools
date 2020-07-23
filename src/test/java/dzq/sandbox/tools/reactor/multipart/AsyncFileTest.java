package dzq.sandbox.tools.reactor.multipart;


import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;


@DisplayName("suite for testing AsyncFile consistence")
public class AsyncFileTest {

  @Test
  @DisplayName("should have properties passed to constructor")
  public void t29386592() {
    var sampleName = "iuadyvosau98ad8";
    Flux<DataBuffer> sampleContent = Flux.empty();

    var result = new AsyncFile(sampleName, sampleContent);

    assertSame(sampleName, result.name());
    assertSame(sampleContent, result.content());
  }

  @Test
  @DisplayName("should allow null properties as is")
  public void t3982c36a() {
    var result = new AsyncFile(null, null);

    assertNull(result.name());
    assertNull(result.content());
  }
}
