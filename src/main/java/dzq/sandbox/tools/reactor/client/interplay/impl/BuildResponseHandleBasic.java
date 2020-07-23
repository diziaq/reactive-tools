package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay.ResponseHandleBuilder;
import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay.ResponseResultBuilder;
import java.util.function.Predicate;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;


final class BuildResponseHandleBasic implements ResponseHandleBuilder {

  final ClientRequest request;
  final BuildRequestHandleBasic requestBuilder;

  BuildResponseHandleBasic(
      BuildRequestHandleBasic requestBuilder,
      ClientRequest request
  ) {
    this.requestBuilder = requestBuilder;
    this.request = request;
  }

  @Override
  public ResponseResultBuilder expect(Predicate<ClientResponse> responsePredicate) {
    return new BuildResponseResultBasic(this, responsePredicate);
  }
}
