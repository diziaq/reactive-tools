package dzq.sandbox.tools.reactor.shortcuts;


import java.util.function.BiFunction;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


// shortcuts for zipping publishers
public final class ZipPublishers {

  private ZipPublishers() {
    // no instances required
  }

  @FunctionalInterface
  public interface MonoFunc2Arg<A, B, R> {

    Mono<R> apply(A a, B b);
  }

  @FunctionalInterface
  public interface MonoFunc3Arg<A, B, C, R> {

    Mono<R> apply(A a, B b, C c);
  }

  @FunctionalInterface
  public interface FluxFunc2Arg<A, B, R> {

    Flux<R> apply(A a, B b);
  }

  @FunctionalInterface
  public interface FluxFunc3Arg<A, B, C, R> {

    Flux<R> apply(A a, B b, C c);
  }

  public static <A, B, R> Mono<R> flat(Mono<A> a, Mono<B> b, MonoFunc2Arg<A, B, R> function) {
    return Mono.zip(a, b).flatMap(t -> function.apply(t.getT1(), t.getT2()));
  }

  public static <A, B, C, R> Mono<R> flat(Mono<A> a, Mono<B> b, Mono<C> c, MonoFunc3Arg<A, B, C, R> function) {
    return Mono.zip(a, b, c).flatMap(t -> function.apply(t.getT1(), t.getT2(), t.getT3()));
  }

  public static <A, B, R> Flux<R> flatMany(Mono<A> a, Mono<B> b, FluxFunc2Arg<A, B, R> function) {
    return Mono.zip(a, b).flatMapMany(t -> function.apply(t.getT1(), t.getT2()));
  }

  public static <A, B, C, R> Flux<R> flatMany(Mono<A> a, Mono<B> b, Mono<C> c, FluxFunc3Arg<A, B, C, R> function) {
    return Mono.zip(a, b, c).flatMapMany(t -> function.apply(t.getT1(), t.getT2(), t.getT3()));
  }

  public static <A, B, R> Flux<R> alignMany(Mono<A> monoA, B b, BiFunction<A, B, Flux<R>> function) {
    return monoA.flatMapMany(a -> function.apply(a, b));
  }

  public static <A, R> Flux<R> alignMany(Mono<A> monoA, Function<A, Flux<R>> function) {
    return monoA.flatMapMany(function::apply);
  }

  public static <A, B, R> Mono<R> align(Mono<A> monoA, B b, BiFunction<A, B, Mono<R>> function) {
    return monoA.flatMap(a -> function.apply(a, b));
  }

  public static <A, R> Mono<R> align(Mono<A> monoA, Function<A, Mono<R>> function) {
    return monoA.flatMap(function::apply);
  }
}
