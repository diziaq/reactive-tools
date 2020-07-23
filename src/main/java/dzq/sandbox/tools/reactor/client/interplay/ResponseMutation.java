package dzq.sandbox.tools.reactor.client.interplay;


import dzq.sandbox.tools.reactor.client.interplay.impl.FacadeFactoryMethods.ResponseMutations;
import java.util.function.Function;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


public interface ResponseMutation {

  Mono<ClientResponse> affect(ClientResponse request);

  interface Source<T> {

    ResponseMutation by(T seed);
  }

  static Source<String> errorFilter(Function<String, String> errorMessageTransform) {
    return label -> ResponseMutations.treatingAsError(label, errorMessageTransform);
  }
}
