package com.mjones.service_poller;

import org.apache.commons.validator.routines.UrlValidator;

import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  private final Logger LOGGER = LoggerFactory.getLogger( MainVerticle.class );

  @Override
  public void start() throws Exception {

    vertx.deployVerticle(new ServiceRepository());

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    router.get("/api/services").handler(ctx -> getServices(ctx, vertx));

    router.post("/api/services").handler(ctx -> postService(ctx, vertx));

    server.requestHandler(router).listen(8080, http -> {
      if (http.succeeded()) {
        LOGGER.info("Service poller HTTP server started on port 8080");
      } else {
        LOGGER.info("Service poller HTTP server failed to start");
      }
    });
  }

  private void getServices(RoutingContext ctx, Vertx vertx) {
    vertx.eventBus().request("service.services-get", "",
      res -> {
        if (res.succeeded()) {
          ctx.response()
            .putHeader("content-type", "application/json")
            .end(res.result().body().toString());
        } else {
          ctx.fail(res.cause());
        }
      }
    );
  }

  private void postService(RoutingContext ctx, Vertx vertx) {
    final String bodyJson = ctx.getBodyAsString();
    final Service service = Json.decodeValue(bodyJson, Service.class);
    if (StringUtil.isNullOrEmpty(service.getName()) || StringUtil.isNullOrEmpty(service.getUrl())) {
      ctx.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end("{ 'error' : 'Please provide a service name and url' }");
    } else if (!urlValidator(service.getUrl())) {
      ctx.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end("{ 'error' : 'Invalid url' }");
    } else {
      vertx.eventBus().request("service.service-add", Json.encode(service), 
        res -> {
          if (res.succeeded()) {
            LOGGER.info(res.result().body().toString());
            ctx.response()
              .putHeader("content-type", "application/json")
              .end(res.result().body().toString());
          } else {
            ctx.fail(res.cause());
          }
        }
      );
    }
  }

  private static boolean urlValidator(String url) {
    UrlValidator validator = new UrlValidator();
    return validator.isValid(url);
  }
}
