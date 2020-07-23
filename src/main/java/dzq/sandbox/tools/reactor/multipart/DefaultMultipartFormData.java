package dzq.sandbox.tools.reactor.multipart;


import java.util.Map;
import java.util.Objects;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;


final class DefaultMultipartFormData implements MultipartFormData {

  private final Map<String, Part> map;

  public DefaultMultipartFormData(Map<String, Part> map) {
    this.map = Objects.requireNonNull(map);
  }

  @Override
  public AsyncFile file(String key) {
    Part part = map.get(key);

    if (part == null) {

      return new AsyncFile(null, null);

    } else if (part instanceof FilePart) {

      FilePart filePart = (FilePart) part;

      String filename = Objects.requireNonNull(filePart.filename(), "unexpected null in filename at `" + key + "`");
      Publisher<DataBuffer> content = Objects.requireNonNull(filePart.content(), "unexpected null instead of file content at `" + key + "`");

      return new AsyncFile(filename, content);

    } else {
      throw new RuntimeException("Multipart data is expected to have file for `" + key + "` field");
    }
  }

  @Override
  public String text(String key) {
    Part part = map.get(key);

    if (part == null) {

      return null;

    } else if (part instanceof FormFieldPart) {

      FormFieldPart fieldPart = (FormFieldPart) part;
      String value = Objects.requireNonNull(fieldPart.value(), "unexpected null value in field `" + key + "`");

      return value;

    } else {
      throw new RuntimeException("Multipart data is expected to have text for `" + key + "` field");
    }
  }

  @Override
  public Publisher<DataBuffer> any(String key) {
    Part part = map.get(key);
    return (part == null) ? null : part.content();
  }
}
