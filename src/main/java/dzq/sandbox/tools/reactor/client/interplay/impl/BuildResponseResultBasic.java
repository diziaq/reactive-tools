package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay.ResponseResultBuilder;
import java.util.function.Predicate;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


final class BuildResponseResultBasic implements ResponseResultBuilder {

  final BuildResponseHandleBasic responseBuilder;
  final Predicate<ClientResponse> responsePredicate;

  BuildResponseResultBasic(
      BuildResponseHandleBasic responseBuilder,
      Predicate<ClientResponse> responsePredicate
  ) {
    this.responseBuilder = responseBuilder;
    this.responsePredicate = responsePredicate;
  }

  @Override
  public Mono<ClientResponse> release() {

    var request = responseBuilder.request;
    var respondent = responseBuilder.requestBuilder.respondent;
    var deviationIntercept = responseBuilder.requestBuilder.deviationIntercept;

    return respondent
               .act(request)
               .flatMap(cr ->
                            responsePredicate.test(cr)
                                ? Mono.just(cr)
                                : deviationIntercept.affect(cr)
               );
  }
}
