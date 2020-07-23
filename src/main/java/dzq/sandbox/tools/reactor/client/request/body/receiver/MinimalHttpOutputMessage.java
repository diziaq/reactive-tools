package dzq.sandbox.tools.reactor.client.request.body.receiver;


import java.net.URI;
import java.util.function.Supplier;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;


class MinimalHttpOutputMessage implements ClientHttpRequest {

  public static MinimalHttpOutputMessage INSTANCE = new MinimalHttpOutputMessage();

  private MinimalHttpOutputMessage() {
  }

  @Override
  public HttpHeaders getHeaders() {
    return HttpHeaders.EMPTY;
  }

  @Override
  public DataBufferFactory bufferFactory() {
    return null;
  }

  @Override
  public void beforeCommit(Supplier<? extends Mono<Void>> action) {
    // nothing
  }

  @Override
  public boolean isCommitted() {
    return false;
  }

  @Override
  public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
    return null;
  }

  @Override
  public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
    return null;
  }

  @Override
  public Mono<Void> setComplete() {
    return null;
  }

  @Override
  public HttpMethod getMethod() {
    return null;
  }

  @Override
  public URI getURI() {
    return null;
  }

  @Override
  public MultiValueMap<String, HttpCookie> getCookies() {
    return null;
  }
}
