package dzq.sandbox.tools.reactor.multipart;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@DisplayName("suite for testing DefaultMultipartFormData")
public class DefaultMultipartFormDataTest {

  @Test
  @DisplayName("file(<unknown-key>) should return MultipartFormData.File with null content and name")
  public void t90927904() {
    var sampleMap = Map.<String, Part>of();

    var formData = new DefaultMultipartFormData(sampleMap);

    var result = formData.file("something");

    assertNull(result.content());
    assertNull(result.name());
  }

  @Test
  @DisplayName("file(<known-key>) should return MultipartFormData.File with content and name from underlying map")
  public void t909a79f4() {
    var sampleName = "sample-name";
    var sampleContent = Flux.<DataBuffer>empty();
    var sampleMap = Map.<String, Part>of("abc", mockFilePart(sampleName, sampleContent));

    var formData = new DefaultMultipartFormData(sampleMap);

    var result = formData.file("abc");

    assertSame(sampleContent, result.content());
    assertEquals(sampleName, result.name());
  }

  @Test
  @DisplayName("any(<unknown-key>) should return null")
  public void t9b219a01() {
    var sampleMap = Map.<String, Part>of();

    var formData = new DefaultMultipartFormData(sampleMap);

    var result = formData.any("something");

    assertNull(result);
  }

  @Test
  @DisplayName("any(<known-key>) should return content from underlying map for FilePart")
  public void t459a12ac() {
    var sampleContent = Flux.<DataBuffer>empty();
    var sampleMap = Map.<String, Part>of("abc", mockFilePart("dummy", sampleContent));

    var formData = new DefaultMultipartFormData(sampleMap);

    var result = formData.any("abc");

    assertSame(sampleContent, result);
  }

  @Test
  @DisplayName("text(<known-key>) should throw when content from underlying map is FilePart")
  public void t42c74335() {
    var sampleContent = Flux.<DataBuffer>empty();
    var sampleMap = Map.<String, Part>of("abc", mockFilePart("dummy", sampleContent));

    var formData = new DefaultMultipartFormData(sampleMap);

    var exception = assertThrows(RuntimeException.class, () -> formData.text("abc"));

    assertEquals("Multipart data is expected to have text for `abc` field", exception.getMessage());
  }

  @Test
  @DisplayName("any(<known-key>) should return content from underlying map for FormFieldPart")
  public void t45832752() {
    var sampleContent = Flux.<DataBuffer>empty();
    var sampleMap = Map.<String, Part>of("abc", mockFormFieldPart("dummy", sampleContent, null));

    var formData = new DefaultMultipartFormData(sampleMap);

    var result = formData.any("abc");

    assertSame(sampleContent, result);
  }

  @Test
  @DisplayName("text(<known-key>) should return value from underlying map for FormFieldPart")
  public void t453d2752() {
    var sampleContent = "sample-content-value";
    var sampleMap = Map.<String, Part>of("abc", mockFormFieldPart("dummy", null, sampleContent));

    var formData = new DefaultMultipartFormData(sampleMap);

    var result = formData.text("abc");

    assertSame(sampleContent, result);
  }

  @Test
  @DisplayName("text(<unknown-key>) should return null")
  public void t5f26e821() {
    var sampleMap = Map.<String, Part>of();

    var formData = new DefaultMultipartFormData(sampleMap);

    var result = formData.text("something");

    assertNull(result);
  }

  @Test
  @DisplayName("file(<known-key>) should throw when content from underlying map is FormFieldPart")
  public void t11c74d3a() {
    var sampleContent = Flux.<DataBuffer>empty();
    var sampleMap = Map.<String, Part>of("abc", mockFormFieldPart("dummy", sampleContent, null));

    var formData = new DefaultMultipartFormData(sampleMap);

    var exception = assertThrows(RuntimeException.class, () -> formData.file("abc"));

    assertEquals("Multipart data is expected to have file for `abc` field", exception.getMessage());
  }
  // -----------------------------------

  static FilePart mockFilePart(String name, Flux<DataBuffer> content) {
    return new FilePart() {
      @Override
      public String filename() {
        return name;
      }

      @Override
      public Mono<Void> transferTo(Path dest) {
        throw new RuntimeException("should not be used");
      }

      @Override
      public String name() {
        return name;
      }

      @Override
      public HttpHeaders headers() {
        throw new RuntimeException("should not be used");
      }

      @Override
      public Flux<DataBuffer> content() {
        return content;
      }
    };
  }

  static FormFieldPart mockFormFieldPart(String name, Flux<DataBuffer> content, String value) {
    return new FormFieldPart() {
      @Override
      public String name() {
        return name;
      }

      @Override
      public HttpHeaders headers() {
        throw new RuntimeException("should not be used");
      }

      @Override
      public Flux<DataBuffer> content() {
        return content;
      }

      @Override
      public String value() {
        return value;
      }
    };
  }
}
