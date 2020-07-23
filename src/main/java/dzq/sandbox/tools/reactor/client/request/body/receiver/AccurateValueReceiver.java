package dzq.sandbox.tools.reactor.client.request.body.receiver;


import java.util.concurrent.atomic.AtomicReference;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;


class AccurateValueReceiver<T> implements InsertionReceiver<T> {

  private final Class<T> clazz;
  private final AtomicReference<Object> reference;

  private static final Object EMPTY_MARKER = new Object();

  AccurateValueReceiver(Class<T> clazz) {
    this.clazz = clazz;
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

    T validatedValue;

    if (value == EMPTY_MARKER) {
      throw new RuntimeException("Value was not received, check your inserter worked properly");
    } else if (!clazz.isAssignableFrom(value.getClass())) {
      throw new RuntimeException(
          "Value has unexpected type ("
              + value.getClass().getTypeName()
              + ") instead of (" + clazz.getTypeName() + ")");
    } else {
      validatedValue = clazz.cast(value);
    }

    return validatedValue;
  }
}
