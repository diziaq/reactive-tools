package dzq.sandbox.tools.reactor.client.interplay.impl;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import dzq.sandbox.tools.reactor.client.interplay.ClientInterplay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("suite for testing ClientInterplays factory functions")
public class ClientInterplaysTest {

  @Test
  @DisplayName("ClientInterplays.create should return instance of ClientInterplayDefault")
  public void t02f750a2() {
    ClientInterplay clientInterplay = ClientInterplays.create(r -> null, x -> y -> null);

    assertThat(clientInterplay, instanceOf(ClientInterplayDefault.class));
  }
}
