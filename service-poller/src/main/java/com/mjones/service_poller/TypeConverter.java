package com.mjones.service_poller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;

public class TypeConverter {

    public List<Service> resultSetToServiceList(ResultSet res) {
        List<Service> services = new ArrayList<>();
        res.getRows().forEach(row -> {
            services.add(Json.decodeValue(row.toString(), Service.class));
        });
        return services;
    }

    public Service messageToNewService(Message<Object> msg) {
        Service service = Json.decodeValue((String)msg.body(), Service.class);
        service.setId(UUID.randomUUID().toString());
        service.setCreated(new CurrentTimestamp().getDate());
        service.setLastUpdated(new CurrentTimestamp().getDate());
        service.setStatus(Status.PENDING);
        return service;
    }

    public JsonArray serviceToJsonArray(Service service) {
        return new JsonArray()
            .add(service.getId())
            .add(service.getName())
            .add(service.getUrl())
            .add(service.getCreated())
            .add(service.getLastUpdated())
            .add(service.getStatus().toString());
    }
}
