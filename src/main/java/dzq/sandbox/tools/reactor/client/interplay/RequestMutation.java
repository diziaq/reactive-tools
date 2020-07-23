package dzq.sandbox.tools.reactor.client.interplay;


import dzq.sandbox.tools.reactor.client.interplay.impl.FacadeFactoryMethods.RequestMutations;
import org.springframework.web.reactive.function.client.ClientRequest;


public interface RequestMutation {

  ClientRequest affect(ClientRequest request);

  interface Source<T> {

    RequestMutation by(T seed);
  }

  static Source<String> appendUrlParam(String paramName) {
    return paramValue -> RequestMutations.appendingParameter(paramName, paramValue);
  }
}
