package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay;
import dzq.sandbox.tools.reactor.client.interplay.Respondent;
import dzq.sandbox.tools.reactor.client.interplay.ResponseMutation.Source;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;


public final class ClientInterplays {

  private ClientInterplays() {
    // no instances required
  }

  /**
   *
   */
  public static ClientInterplay create(
      Respondent<ClientRequest, ClientResponse> respondent,
      Source<String> deviationIntercept
  ) {
    return new ClientInterplayDefault(respondent, deviationIntercept);
  }
}
