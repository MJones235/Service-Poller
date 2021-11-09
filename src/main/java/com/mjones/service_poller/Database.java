package com.mjones.service_poller;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;

public class Database {

  private final SQLClient client;

  public Database(Vertx vertx){
    JsonObject config = new JsonObject()
        .put("url", "jdbc:sqlite:services.db")
        .put("driver_class", "org.sqlite.JDBC")
        .put("max_pool_size", 30);

    client = JDBCClient.createShared(vertx, config);
  }

  public Future<ResultSet> query(String query) {
    return query(query, new JsonArray());
  }

  public Future<ResultSet> query(String query, JsonArray params) {
    
    Promise<ResultSet> queryResult = Promise.promise();

    if (query == null || query.isEmpty()) {
      queryResult.fail("Empty query");
    }

    if(!query.endsWith(";")) {
      query = query + ";";
    }

    client.queryWithParams(query, params, result -> {
      if(result.failed()){
        queryResult.fail(result.cause());
      } else {
        queryResult.complete(result.result());
      }
    });

    return queryResult.future();
  }
}