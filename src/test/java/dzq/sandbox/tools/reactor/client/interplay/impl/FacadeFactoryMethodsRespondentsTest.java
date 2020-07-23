package dzq.sandbox.tools.reactor.client.interplay.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;

import dzq.sandbox.tools.reactor.client.interplay.impl.FacadeFactoryMethods.Respondents;
import dzq.sandbox.tools.reactor.client.interplay.Respondent;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.core.publisher.Mono;


@DisplayName("suite for testing FacadeFactoryMethods.Respondents factory functions")
public class FacadeFactoryMethodsRespondentsTest {

  @ParameterizedTest(name = "{index} : {0} times {1}")
  @MethodSource("samplesForStringRepeatTimes")
  @DisplayName("Respondents.using(F) should return a respondent relied on the given function")
  public void t1ad720e1(Integer times, String seedString, String expectedResult) {

    Function<Integer, Mono<String>> sampleFunction = (Integer n) -> Mono.just(seedString.repeat(n));

    var respondent = Respondents.using(sampleFunction);
    var result = respondent.act(times).block();

    assertEquals(expectedResult, result);
  }

  @ParameterizedTest(name = "{index} : {0} times {1}")
  @MethodSource("samplesForStringRepeatTimes")
  @DisplayName("Respondents.inputTransform(F,R) should return a respondent relied on the given function and "
                   + "delegating processing to the given respondent")
  public void t59a321d5(Integer times, String seedString, String rawExpectedResult) {

    Respondent<String, String> sampleDelegateRespondent = x -> Mono.just("<" + x + ">");
    Function<Integer, String> sampleTransform = n -> seedString.repeat(n);

    var respondent = Respondents.inputTransform(sampleTransform, sampleDelegateRespondent);
    var result = respondent.act(times).block();

    var expectedResult = "<" + rawExpectedResult + ">";

    assertEquals(expectedResult, result);
  }

  @ParameterizedTest(name = "{index} : {0} times {1}")
  @MethodSource("samplesForStringRepeatTimesMultiplied")
  @DisplayName("Respondents.outputTransform(R,F) should return a respondent delegating processing to the given respondent "
                   + "and passing result to the given function")
  public void t572708d5(Integer times, Integer seedNumber, String seedString, String expectedResult) {

    Respondent<Integer, Integer> sampleDelegateRespondent = x -> Mono.just(x * times);
    Function<Integer, String> sampleTransform = n -> seedString.repeat(n);

    var respondent = Respondents.outputTransform(sampleDelegateRespondent, sampleTransform);
    var result = respondent.act(seedNumber).block();

    assertEquals(expectedResult, result);
  }

  public static Stream<Arguments> samplesForStringRepeatTimes() {
    return Stream.of(
        Arguments.of(5, "a", "aaaaa"),
        Arguments.of(2, "abc", "abcabc")
    );
  }

  public static Stream<Arguments> samplesForStringRepeatTimesMultiplied() {
    return Stream.of(
        Arguments.of(5, 2, "ABC", "ABCABCABCABCABCABCABCABCABCABC"),
        Arguments.of(3, 4, "B", "BBBBBBBBBBBB")
    );
  }
}
