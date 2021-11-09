package com.mjones.service_poller;

import java.util.List;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;

public class Poller {

    public void pollServices(List<Service> services, Vertx vertx, Database database) {
        WebClient client = WebClient.create(vertx);
        services.forEach(service -> {
            client.getAbs(service.getUrl())
                .send()
                .onSuccess(res -> updateResult(database, Status.OK, service))
                .onFailure(res -> updateResult(database, Status.FAIL, service));
        });
    }

    private void updateResult(Database database, Status status, Service service) {
        service.setStatus(status);
        database.query("UPDATE SERVICES SET status = ?, lastUpdated = ? WHERE id = ?", 
            new JsonArray()
                .add(status.toString())
                .add(new CurrentTimestamp().getDate())
                .add(service.getId()));
    }
}
