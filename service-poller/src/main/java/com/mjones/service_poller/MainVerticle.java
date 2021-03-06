package com.mjones.service_poller;

import org.apache.commons.validator.routines.UrlValidator;

import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  private final Logger LOGGER = LoggerFactory.getLogger( MainVerticle.class );
  private final Integer MILLIS = 1000;
  private final Integer INTERVAl = 60;
  private Poller poller;
  private Database database;
  private ServiceRepository serviceRepository;

  @Override
  public void start() throws Exception {

    poller = new Poller();
    database = new Database(vertx);
    serviceRepository = new ServiceRepository(database);
  
    vertx.deployVerticle(serviceRepository);

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    //
    // Poll services every 60 seconds
    //
    vertx.setPeriodic(INTERVAl * MILLIS, handler -> {
      poller.pollServices(serviceRepository.getServices(), vertx, database);
    });


    //
    // Handle api requests
    //
    router.get("/services/get").handler(ctx -> getServices(ctx, vertx));
    router.post("/services/create").handler(ctx -> postService(ctx, vertx));
    router.post("/services/delete").handler(ctx -> deleteService(ctx, vertx));


    //
    // Start http server
    //
    server.requestHandler(router).listen(8080, http -> {
      if (http.succeeded()) {
        LOGGER.info("Service poller HTTP server started on port 8080");
      } else {
        LOGGER.info("Service poller HTTP server failed to start");
        LOGGER.info("Error: " + http.cause());
      }
    });
  }

  //
  // Get service data
  //
  private void getServices(RoutingContext ctx, Vertx vertx) {
    vertx.eventBus().request("service.services-get", "",
      res -> {
        if (res.succeeded()) {
          returnResponse(ctx, res);
        } else {
          ctx.fail(res.cause());
        }
      }
    );
  }

  //
  // Create new service
  //
  private void postService(RoutingContext ctx, Vertx vertx) {
    final String bodyJson = ctx.getBodyAsString();
    final Service service = Json.decodeValue(bodyJson, Service.class);
    String errorMessage = getInputError(service);
    if(errorMessage != null) {
      returnError(ctx, errorMessage);
    } else {
      vertx.eventBus().request("service.service-add", Json.encode(service), 
        res -> {
          if (res.succeeded()) {
              returnResponse(ctx, res);
              poller.pollServices(serviceRepository.getServices(), vertx, database);
          } else {
            ctx.fail(res.cause());
          }
        }
      );
    }
  }

  //
  // Delete service
  //
  private void deleteService(RoutingContext ctx, Vertx vertx) {
    final String bodyJson = ctx.getBodyAsString();
    final Service service = Json.decodeValue(bodyJson, Service.class);
    vertx.eventBus().request("services.services-delete", Json.encode(service),
      res -> {
        if (res.succeeded()) {
          returnResponse(ctx, res);
        } else {
          ctx.fail(res.cause());
        }
      });
  }
  

  private static void returnResponse(RoutingContext ctx, AsyncResult<Message<Object>> res) {
    ctx.response()
      .putHeader("content-type", "application/json")
      .end(res.result().body().toString());
  }

  private static void returnError(RoutingContext ctx, String message) {
    ctx.response()
        .setStatusCode(400)
        .putHeader("content-type", "application/json")
        .end(message);
  }

  private static String getInputError(Service service) {
    if (StringUtil.isNullOrEmpty(service.getName())) {
      return "{ 'error' : 'Please provide a name for the service' }";
    } else if (StringUtil.isNullOrEmpty(service.getUrl())) {
      return "{ 'error' : 'Please provide a url for the service' }";
    } else if (!(new UrlValidator()).isValid(service.getUrl())) {
      return "{ 'error' : 'Invalid url' }";
    } else {
      return null;
    }
  }
}
