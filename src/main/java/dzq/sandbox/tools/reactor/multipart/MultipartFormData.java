package dzq.sandbox.tools.reactor.multipart;


import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;


public interface MultipartFormData {

  AsyncFile file(String key);

  String text(String key);

  Publisher<DataBuffer> any(String key);

  static MultipartFormData of(MultiValueMap<String, Part> map) {
    return new DefaultMultipartFormData(map.toSingleValueMap());
  }
}
