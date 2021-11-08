package com.mjones.service_poller;

import java.util.UUID;

public class Service {
    private UUID id;
    private String name;
    private String url;
    private Status status;

    public Service() {}

    public Service(String name, String url) {
        super();
        this.name = name;
        this.url = url;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Status getStatus() {
        return status;
    } 
    
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
