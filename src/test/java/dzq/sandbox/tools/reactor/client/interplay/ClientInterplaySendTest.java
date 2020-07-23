package dzq.sandbox.tools.reactor.client.interplay;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


@DisplayName("suite for testing `send()` method behaviour on different inputs")
public class ClientInterplaySendTest {

  @Test
  @DisplayName("should send request as is")
  public void t125b46c2() {
    var sampleRequest = ClientRequest.create(HttpMethod.OPTIONS, URI.create("sample/path/to/resource")).build();

    var respondent = mockRespondent();

    ClientInterplay
        .create(respondent)
        .start("dummy")
        .send(sampleRequest)
        .expect(x -> true)
        .release();

    var requestCaptor = ArgumentCaptor.forClass(ClientRequest.class);

    verify(respondent, only()).act(requestCaptor.capture());

    ClientRequest result = requestCaptor.getValue();

    assertSame(sampleRequest, result);
  }

  @Test
  @DisplayName("should send request with given method, url and empty body")
  public void t440162a2() {
    var sampleMethod = HttpMethod.POST;
    var sampleUrl = "sample/path/to/resource";

    var respondent = mockRespondent();

    ClientInterplay
        .create(respondent)
        .start("dummy")
        .send(sampleMethod, sampleUrl)
        .expect(x -> true)
        .release();

    var requestCaptor = ArgumentCaptor.forClass(ClientRequest.class);

    verify(respondent, only()).act(requestCaptor.capture());

    ClientRequest result = requestCaptor.getValue();

    assertEquals(sampleMethod, result.method());
    assertEquals(sampleUrl, result.url().toString());
  }

  @Test
  @DisplayName("should send request with given method, url and body")
  public void t31067ab1() {
    var sampleMethod = HttpMethod.POST;
    var sampleUrl = "sample/path/to/resource";
    var sampleBody = new Object();

    var respondent = mockRespondent();

    ClientInterplay
        .create(respondent)
        .start("dummy")
        .send(sampleMethod, sampleUrl, sampleBody)
        .expect(x -> true)
        .release();

    var requestCaptor = ArgumentCaptor.forClass(ClientRequest.class);

    verify(respondent, only()).act(requestCaptor.capture());

    ClientRequest result = requestCaptor.getValue();

    assertEquals(sampleMethod, result.method());
    assertEquals(sampleUrl, result.url().toString());
  }

  @Test
  @DisplayName("should send request with given method, url and multipart body")
  public void t246262b1() {
    var sampleMethod = HttpMethod.POST;
    var sampleUrl = "sample/path/to/resource";
    var sampleMultipartBody = new LinkedMultiValueMap<>(Map.of("A", List.of(1, 2, 3), "B", List.of(4, 5, 6)));

    var respondent = mockRespondent();

    ClientInterplay
        .create(respondent)
        .start("dummy")
        .send(sampleMethod, sampleUrl, sampleMultipartBody)
        .expect(x -> true)
        .release();

    var requestCaptor = ArgumentCaptor.forClass(ClientRequest.class);

    verify(respondent, only()).act(requestCaptor.capture());

    ClientRequest result = requestCaptor.getValue();

    assertEquals(sampleMethod, result.method());
    assertEquals(sampleUrl, result.url().toString());
  }

  public static Respondent<ClientRequest, ClientResponse> mockRespondent() {

    Respondent<ClientRequest, ClientResponse> respondent = mock(Respondent.class);
    when(respondent.act(any())).thenReturn(Mono.empty());

    return respondent;
  }
}
