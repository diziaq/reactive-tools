package dzq.sandbox.tools.reactor.shortcuts;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ZipPublishersTest {

  @Test
  @DisplayName("should return mono result of async function of two async arguments")
  public void t93652235() {
    var result = ZipPublishers.flat(
        Mono.just("tick-"),
        Mono.just(3),
        (a, b) -> Mono.just(a.repeat(b))
    );

    assertEquals("tick-tick-tick-", result.block());
  }

  @Test
  @DisplayName("should return mono result of async function of three async arguments")
  public void t315a31b2() {
    var result = ZipPublishers.flat(
        Mono.just(50),
        Mono.just("tick-"),
        Mono.just(47),
        (a, b, c) -> Mono.just(b.repeat(a - c))
    );

    assertEquals("tick-tick-tick-", result.block());
  }

  @Test
  @DisplayName("should return flux result of async function of two async arguments")
  public void t13b526f6() {
    var result = ZipPublishers.flatMany(
        Mono.just("tick-"),
        Mono.just(3),
        (a, b) -> Flux.just(a.repeat(b), a.repeat(b - 1), a.repeat(b - 2))
    );
    List<String> block = result.collectList().block();

    assertThat(block, contains("tick-tick-tick-", "tick-tick-", "tick-"));
  }

  @Test
  @DisplayName("should return flux result of async function of two async arguments")
  public void t1a2140fc() {
    var result = ZipPublishers.flatMany(
        Mono.just(42),
        Mono.just("tick-"),
        Mono.just(45),
        (a, b, c) -> Flux.just(b.repeat(c - a), b.repeat(c - a - 1), b.repeat(c - a - 2))
    );
    List<String> block = result.collectList().block();

    assertThat(block, contains("tick-tick-tick-", "tick-tick-", "tick-"));
  }

  @Test
  @DisplayName("should return flux result of bi-function of two plain arguments")
  public void t241d35e1() {
    var result = ZipPublishers.alignMany(
        Mono.just(3),
        "tick-",
        (a, b) -> Flux.just(b.repeat(a), b.repeat(a - 1), b.repeat(a - 2))
    );
    List<String> block = result.collectList().block();

    assertThat(block, contains("tick-tick-tick-", "tick-tick-", "tick-"));
  }

  @Test
  @DisplayName("should return mono result of bi-function of two plain arguments")
  public void t2e09f530() {
    var result = ZipPublishers.align(
        Mono.just(3),
        "tick-",
        (a, b) -> Mono.just(b.repeat(a))
    );

    String block = result.block();

    assertEquals("tick-tick-tick-", block);
  }

  @Test
  @DisplayName("should return flux result of function of one plain argument")
  public void t801868a1() {
    var result = ZipPublishers.alignMany(
        Mono.just(40),
        (a) -> Flux.just(a - 1, a - 2, a - 3)
    );
    List<Integer> block = result.collectList().block();

    assertThat(block, contains(39, 38, 37));
  }

  @Test
  @DisplayName("should return flux result of function of one plain argument")
  public void t1a53509f() {
    var result = ZipPublishers.align(
        Mono.just(3),
        (a) -> Mono.just(a)
    );

    Integer block = result.block();

    assertEquals(3, block);
  }
}
