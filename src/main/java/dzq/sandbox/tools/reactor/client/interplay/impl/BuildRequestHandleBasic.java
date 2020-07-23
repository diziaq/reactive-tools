package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay.RequestHandleBuilder;
import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay.ResponseHandleBuilder;
import dzq.sandbox.tools.reactor.client.interplay.Respondent;
import dzq.sandbox.tools.reactor.client.interplay.ResponseMutation;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;


final class BuildRequestHandleBasic implements RequestHandleBuilder {

  final ResponseMutation deviationIntercept;
  final Respondent<ClientRequest, ClientResponse> respondent;

  BuildRequestHandleBasic(
      Respondent<ClientRequest, ClientResponse> respondent,
      ResponseMutation deviationIntercept
  ) {
    this.deviationIntercept = deviationIntercept;
    this.respondent = respondent;
  }

  @Override
  public ResponseHandleBuilder send(ClientRequest request) {
    return new BuildResponseHandleBasic(this, request);
  }
}
