package dzq.sandbox.tools.reactor.client.request.body.receiver;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("suite for testing InsertionReceiver parts that will never be used, but exists for reading consistency")
public class InsertionReceiverUnusedCoverageTest {

  @Test
  @DisplayName("should call dummy values from unused methods on MinimalHttpOutputMessage")
  public void t412d3497() {
    var outputMessage = MinimalHttpOutputMessage.INSTANCE;

    outputMessage.beforeCommit(null);

    assertNull(outputMessage.bufferFactory());
    assertFalse(outputMessage.isCommitted());
    assertNull(outputMessage.getMethod());
    assertNull(outputMessage.getURI());
    assertNull(outputMessage.getCookies());
    assertNull(outputMessage.writeAndFlushWith(null));
    assertNull(outputMessage.writeWith(null));
  }

  @Test
  @DisplayName("should call dummy values from unused methods on WriteToConsumer")
  public void t11b67373() {
    var write = new WriteToConsumer<>(null);

    assertFalse(write.getWritableMediaTypes().isEmpty());
  }

  @Test
  @DisplayName("should call dummy values from unused methods on OneValueConsumption")
  public void t7954b362() {
    var oneValueCons = new OneValueConsumption<>(x -> {
    });
    oneValueCons.onNext(null);

    assertThrows(Throwable.class, () -> oneValueCons.onError(null));
    assertThrows(Throwable.class, () -> oneValueCons.onNext(null));
  }
}
