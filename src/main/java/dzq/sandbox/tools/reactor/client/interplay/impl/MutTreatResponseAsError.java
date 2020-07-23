package dzq.sandbox.tools.reactor.client.interplay.impl;


import dzq.sandbox.tools.reactor.client.interplay.ResponseMutation;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;


final class MutTreatResponseAsError implements ResponseMutation {

  private final String label;
  private final Function<String, String> errorMessageTransform;
  private final Consumer<String> eventConsumer;

  MutTreatResponseAsError(String label, Function<String, String> errorMessageTransform) {
    this(label, errorMessageTransform, s -> {
    });
  }

  MutTreatResponseAsError(String label, Function<String, String> errorMessageTransform, Consumer<String> eventConsumer) {
    this.label = label;
    this.errorMessageTransform = errorMessageTransform;
    this.eventConsumer = eventConsumer;
  }

  @Override
  public Mono<ClientResponse> affect(ClientResponse response) {

    return response.bodyToMono(String.class)
                   .map(origMessage -> {
                     String newMessage = errorMessageTransform.apply(origMessage);

                     logMessageSwap(response.statusCode(), origMessage, newMessage);

                     return newMessage;
                   })
                   .flatMap(msg -> Mono.error(() -> new RuntimeException("Failed `" + label + "`: " + msg)));
  }

  private void logMessageSwap(HttpStatus status, String origMessage, String newMessage) {

    if (!Objects.equals(origMessage, newMessage)) {
      eventConsumer.accept(
          MessageFormat.format("Error message was replaced with an error for response with status {0} : ({1}) => ({2})",
              status, origMessage, newMessage)
      );
    } else {
      eventConsumer.accept(
          MessageFormat.format("Error message was exposed to externals for response with status {0}: ({1})",
              status, origMessage)
      );
    }
  }
}
