package dzq.sandbox.tools.reactor.client.interplay;


import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


@DisplayName("suite for testing handling of input request conversion")
public class ClientInterplayInputConversionTest {

  @Test
  @DisplayName("should pass a converted request to respondent's")
  public void t572a6565() {
    ClientRequest sampleRequest = ClientRequest.create(HttpMethod.GET, URI.create("")).build();

    var respondent = mockRespondent();
    UnaryOperator<ClientRequest> inputConversion = x -> sampleRequest;
    Predicate<ClientResponse> allowNonePredicate = x -> true;

    ClientInterplay
        .create(respondent)
        .inputConversion(inputConversion)
        .start("dummy")
        .send(null)
        .expect(allowNonePredicate)
        .release();

    var requestCaptor = ArgumentCaptor.forClass(ClientRequest.class);

    verify(respondent, only()).act(requestCaptor.capture());

    ClientRequest receivedRequest = requestCaptor.getValue();

    assertSame(sampleRequest, receivedRequest);
  }

  @Test
  @DisplayName("should send initial request to input conversion operator")
  public void t498e29f4() {
    var sampleRequest = ClientRequest.create(HttpMethod.OPTIONS, URI.create("sample/path/to/resource")).build();

    var respondent = mockRespondent();
    var inputConversion = mockInputConversion();

    ClientInterplay
        .create(respondent)
        .inputConversion(inputConversion)
        .start("dummy")
        .send(sampleRequest)
        .expect(x -> true)
        .release();

    var requestCaptor = ArgumentCaptor.forClass(ClientRequest.class);

    verify(inputConversion, only()).apply(requestCaptor.capture());

    ClientRequest incomingRequest = requestCaptor.getValue();

    assertSame(sampleRequest, incomingRequest);
  }

  public static Respondent<ClientRequest, ClientResponse> mockRespondent() {

    Respondent<ClientRequest, ClientResponse> respondent = mock(Respondent.class);
    when(respondent.act(any())).thenReturn(Mono.empty());

    return respondent;
  }

  public static UnaryOperator<ClientRequest> mockInputConversion() {

    UnaryOperator<ClientRequest> operator = mock(UnaryOperator.class);
    when(operator.apply(any())).thenReturn(any());

    return operator;
  }

  // -----------------------------------------------------

  private static ClientResponse randomResponse() {
    return createValidResponse(HttpStatus.SWITCHING_PROTOCOLS, "something");
  }

  private static ClientResponse createValidResponse(HttpStatus status, String body) {
    return ClientResponse.create(status).body(body).build();
  }
}
