package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.RequestMutation;
import java.net.URI;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.util.UriComponentsBuilder;


final class MutAppendSingleParameterToUrl implements RequestMutation {

  private final String uriParamName;
  private final String uriParamValue;

  MutAppendSingleParameterToUrl(String uriParamName, String uriParamValue) {
    this.uriParamName = uriParamName;
    this.uriParamValue = uriParamValue;
  }

  @Override
  public ClientRequest affect(ClientRequest request) {

    URI newUrl = UriComponentsBuilder
                     .fromUriString(request.url().toString())
                     .queryParam(uriParamName, uriParamValue)
                     .build().toUri();

    return ClientRequest.from(request).url(newUrl).build();
  }
}
