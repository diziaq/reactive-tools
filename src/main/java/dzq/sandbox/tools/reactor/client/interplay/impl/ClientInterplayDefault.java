package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay;
import dzq.sandbox.tools.reactor.client.interplay.Respondent;
import dzq.sandbox.tools.reactor.client.interplay.ResponseMutation;
import dzq.sandbox.tools.reactor.client.interplay.impl.FacadeFactoryMethods.Respondents;
import java.util.Objects;
import java.util.function.UnaryOperator;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;


class ClientInterplayDefault implements ClientInterplay {

  private final Respondent<ClientRequest, ClientResponse> respondent;
  private final ResponseMutation.Source<String> deviationIntercept;

  ClientInterplayDefault(
      Respondent<ClientRequest, ClientResponse> respondent,
      ResponseMutation.Source<String> deviationIntercept
  ) {
    this.respondent = Objects.requireNonNull(respondent, "respondent");
    this.deviationIntercept = Objects.requireNonNull(deviationIntercept, "deviation intercept");
  }

  @Override
  public RequestHandleBuilder start(String actionMarker) {
    return new BuildRequestHandleBasic(respondent, deviationIntercept.by(actionMarker));
  }

  @Override
  public ClientInterplay inputConversion(UnaryOperator<ClientRequest> inputConversionOperator) {

    var newRespondent = Respondents.inputTransform(inputConversionOperator, respondent);

    return new ClientInterplayDefault(newRespondent, deviationIntercept);
  }

  @Override
  public ClientInterplay deviationIntercept(ResponseMutation.Source<String> newDeviationInterceptSource) {
    return new ClientInterplayDefault(respondent, newDeviationInterceptSource);
  }
}
