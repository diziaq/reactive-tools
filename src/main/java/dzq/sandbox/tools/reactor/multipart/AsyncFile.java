package dzq.sandbox.tools.reactor.multipart;


import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;


// Tuple for coupling file name and file contents publisher
public final class AsyncFile {

  private final String name;
  private final Publisher<DataBuffer> content;

  public AsyncFile(String name, Publisher<DataBuffer> content) {
    this.name = name;
    this.content = content;
  }

  public String name() {
    return name;
  }

  public Publisher<DataBuffer> content() {
    return content;
  }
}
