package dzq.sandbox.tools.reactor.client.interplay;


import dzq.sandbox.tools.reactor.client.interplay.impl.ClientInterplays;
import java.net.URI;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


public interface ClientInterplay {

  RequestHandleBuilder start(String actionMarker);

  ClientInterplay inputConversion(UnaryOperator<ClientRequest> inputConversionOperator);

  ClientInterplay deviationIntercept(ResponseMutation.Source<String> deviationIntercept);

  /**
   *
   */
  interface ResponseHandleBuilder {

    ResponseResultBuilder expect(Predicate<ClientResponse> responsePredicate);

    default ResponseResultBuilder expect(HttpStatus status) {
      return expect(cr -> cr.statusCode() == status);
    }
  }

  /**
   *
   */
  interface ResponseResultBuilder {

    Mono<ClientResponse> release();

    default <T> Mono<T> releaseAs(Function<ClientResponse, T> mapper) {
      return release().map(mapper);
    }

    default <T> Mono<T> releaseBody(Class<T> bodyClass) {
      return release().flatMap(x -> x.bodyToMono(bodyClass));
    }

    default Mono<String> releaseHeader(String headerName) {
      return release().map(x -> {
        var values = x.headers().header(headerName);
        int size = values.size();

        return size == 1 ? values.get(0) : (size > 1 ? "<many>" : "<none>");
      });
    }
  }

  /**
   *
   */
  interface RequestHandleBuilder {

    ResponseHandleBuilder send(ClientRequest request);

    default ResponseHandleBuilder send(HttpMethod method, String uriTail, MultiValueMap<String, ?> multipartBody) {
      return send(ClientRequest
                      .create(method, URI.create(uriTail))
                      .body(BodyInserters.fromMultipartData(multipartBody))
                      .build()
      );
    }

    default ResponseHandleBuilder send(HttpMethod method, String uriTail, Object body) {
      return send(ClientRequest
                      .create(method, URI.create(uriTail))
                      .body(BodyInserters.fromValue(body))
                      .build()
      );
    }

    default ResponseHandleBuilder send(HttpMethod method, String uriTail) {
      return send(ClientRequest
                      .create(method, URI.create(uriTail))
                      .build()
      );
    }
  }

  /**
   *
   */
  static ClientInterplay create(Respondent<ClientRequest, ClientResponse> respondent) {
    return ClientInterplays.create(
        respondent,
        x -> y -> {
          throw new RuntimeException("deviation intercept is not defined");
        }
    );
  }

  /**
   *
   */
  static ClientInterplay create(
      Respondent<ClientRequest, ClientResponse> respondent,
      ResponseMutation.Source<String> deviationIntercept
  ) {
    return ClientInterplays.create(respondent, deviationIntercept);
  }
}
