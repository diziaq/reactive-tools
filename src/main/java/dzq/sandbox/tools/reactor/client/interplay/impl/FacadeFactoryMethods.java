package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.RequestMutation;
import dzq.sandbox.tools.reactor.client.interplay.Respondent;
import dzq.sandbox.tools.reactor.client.interplay.ResponseMutation;
import java.util.function.Function;
import reactor.core.publisher.Mono;


public final class FacadeFactoryMethods {

  private FacadeFactoryMethods() {
    // no instances required
  }

  public static final class Respondents {

    private Respondents() {
      // no instances required
    }

    public static <Q, S> Respondent<Q, S> using(Function<Q, Mono<S>> respondingFunction) {
      return respondingFunction::apply;
    }

    public static <A, B, C> Respondent<A, C> inputTransform(
        Function<A, B> inputTransform,
        Respondent<B, C> respondent
    ) {
      return request -> respondent.act(inputTransform.apply(request));
    }

    public static <A, B, C> Respondent<A, C> outputTransform(
        Respondent<A, B> respondent,
        Function<B, C> outputTransform
    ) {
      return request -> respondent.act(request).map(outputTransform);
    }
  }

  public static final class ResponseMutations {

    private ResponseMutations() {
      // no instances required
    }

    public static ResponseMutation treatingAsError(String label, Function<String, String> errorMessageTransform) {
      return new MutTreatResponseAsError(label, errorMessageTransform);
    }
  }

  public static final class RequestMutations {

    private RequestMutations() {
      // no instances required
    }

    public static RequestMutation appendingParameter(String paramName, String paramValue) {
      return new MutAppendSingleParameterToUrl(paramName, paramValue);
    }
  }
}
