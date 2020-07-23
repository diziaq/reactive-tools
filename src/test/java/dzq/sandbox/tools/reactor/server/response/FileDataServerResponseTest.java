package dzq.sandbox.tools.reactor.server.response;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;


@DisplayName("suite for testing PagedServerResponse build functions")
public class FileDataServerResponseTest {

  @Test
  @DisplayName("should create response with given attributes and header `content-disposition`")
  public void t92365932() {
    var sampleHttpStatus = HttpStatus.ACCEPTED;
    var sampleName = "sample-name";
    var sampleContent = byteArraysOf("first", "+", "second");

    var resultMono = FileDataServerResponse
                         .buildFromBytes(
                             sampleHttpStatus,
                             sampleName,
                             sampleContent
                         );

    var result = resultMono.block();

    assertEquals(sampleHttpStatus, result.statusCode());
    assertThat(result.headers().get("content-disposition"), contains("attachment; filename=\"sample-name\""));

    // TODO: check body via result.writeTo()
    // assertEquals("first+second", resultBody.toString())
  }

  @Test
  @DisplayName("should create response with given attributes and header `content-disposition`")
  public void t124a93c2() {
    var factory = new DefaultDataBufferFactory();

    var sampleHttpStatus = HttpStatus.OK;
    var sampleName = "sample-name";
    var sampleContent = dataBuffersOf("first", "+", "second");

    var resultMono = FileDataServerResponse
                         .buildFromDataBuffer(
                             sampleHttpStatus,
                             sampleName,
                             sampleContent
                         );

    var result = resultMono.block();

    assertEquals(sampleHttpStatus, result.statusCode());
    assertThat(result.headers().get("content-disposition"), contains("attachment; filename=\"sample-name\""));

    // TODO: check body via result.writeTo()
    // assertEquals("first+second", resultBody.toString())
  }

  private static Flux<DataBuffer> dataBuffersOf(String... strings) {
    var factory = new DefaultDataBufferFactory();

    return byteArraysOf(strings).map(factory::wrap);
  }

  private static Flux<byte[]> byteArraysOf(String... strings) {
    var stream = Arrays.stream(strings).map(s -> s.getBytes(StandardCharsets.UTF_8));

    return Flux.fromStream(stream);
  }
}
