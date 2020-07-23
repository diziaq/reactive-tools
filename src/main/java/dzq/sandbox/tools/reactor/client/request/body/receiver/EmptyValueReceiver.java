package dzq.sandbox.tools.reactor.client.request.body.receiver;


import java.util.concurrent.atomic.AtomicReference;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;


class EmptyValueReceiver<T extends Void> implements InsertionReceiver<T> {

  private final AtomicReference<Object> reference;

  private static final Object EMPTY_MARKER = new Object();

  EmptyValueReceiver() {
    this.reference = new AtomicReference<>(EMPTY_MARKER);
  }

  @Override
  public T receiveValue(BodyInserter<?, ? extends ReactiveHttpOutputMessage> bodyInserter) {
    demandValueFrom(bodyInserter);

    return receivedValue();
  }

  private void demandValueFrom(BodyInserter<?, ? extends ReactiveHttpOutputMessage> bodyInserter) {
    var inserter = (BodyInserter<?, ReactiveHttpOutputMessage>) bodyInserter;

    inserter.insert(
        MinimalHttpOutputMessage.INSTANCE,
        new SingleWriterContext(new WriteToConsumer<>(reference::set))
    );
  }

  private T receivedValue() {
    Object value = reference.get();
    reference.set(EMPTY_MARKER);

    if (value != EMPTY_MARKER) {
      throw new RuntimeException("Value is received unexpectedly: " + value);
    }

    return null;
  }
}
