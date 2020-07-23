module dzq.sandbox.tools {

  exports dzq.sandbox.tools.reactor.client.interplay;
  exports dzq.sandbox.tools.reactor.client.request.body.receiver;
  exports dzq.sandbox.tools.reactor.multipart;

  requires spring.web;
  requires spring.core;
  requires spring.webflux;
  requires reactor.core;
  requires org.reactivestreams;
}
