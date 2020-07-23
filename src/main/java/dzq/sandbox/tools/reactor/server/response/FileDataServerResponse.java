package dzq.sandbox.tools.reactor.server.response;


import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public final class FileDataServerResponse {

  private FileDataServerResponse() {
    // no instances
  }

  public static Mono<ServerResponse> buildFromDataBuffer(
      HttpStatus status,
      String fileName,
      Publisher<DataBuffer> bodyPublisher
  ) {
    return ServerResponse
               .status(status)
               .contentType(MediaType.APPLICATION_OCTET_STREAM)
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
               .body(bodyPublisher, DataBuffer.class);
  }

  public static Mono<ServerResponse> buildFromBytes(
      HttpStatus status,
      String fileName,
      Flux<byte[]> bodyPublisher
  ) {
    var factory = new DefaultDataBufferFactory();
    return buildFromDataBuffer(status, fileName, bodyPublisher.map(factory::wrap));
  }
}
