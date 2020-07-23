package dzq.sandbox.tools.reactor.client.interplay;


import reactor.core.publisher.Mono;


public interface Respondent<Q, S> {

  Mono<S> act(Q request);
}
