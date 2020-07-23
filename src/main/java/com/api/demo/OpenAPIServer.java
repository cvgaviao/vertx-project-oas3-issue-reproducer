package com.api.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterFactory;

public class OpenAPIServer extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(OpenAPIServer.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new OpenAPIServer());
  }

  @Override
  public void start(final Promise<Void> bootstrapFuture) throws Exception {
    RouterFactory.create(vertx,
                         "oas3/petstore.yaml",
                         ar -> {
                           if (ar.succeeded()) {
                             RouterFactory factory = ar.result();

                             Router router = factory.createRouter();

                             HttpServer server =
                                 vertx.createHttpServer(new HttpServerOptions()
                                   .setPort(8082).setHost("localhost"));
                             server.requestHandler(router).listen();

                             bootstrapFuture.complete();
                           } else {
                             logger.error("bootstrap failed",
                                          ar.cause());
                             bootstrapFuture.fail(ar.cause());
                           }
                         });
  }

}
