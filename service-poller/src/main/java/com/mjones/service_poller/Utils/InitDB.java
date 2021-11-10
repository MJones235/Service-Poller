package com.mjones.service_poller.Utils;
import com.mjones.service_poller.Database;

import io.vertx.core.Vertx;

public class InitDB {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Database database = new Database(vertx);
        database.query("CREATE TABLE IF NOT EXISTS SERVICES (id TEXT PRIMARY KEY NOT NULL, url TEXT NOT NULL, name TEXT NOT NULL, created DATETIME NOT NULL, lastUpdated DATETIME NOT NULL, status TEXT NOT NULL);")
            .onComplete(res -> {
                if (res.succeeded()) {
                    System.out.println("DB has been initialised");
                } else {
                    System.out.println("db initialisation error: " + res.cause());
                }
                vertx.close(shutdown -> {
                    System.exit(0);
                });
            });
      }
}
