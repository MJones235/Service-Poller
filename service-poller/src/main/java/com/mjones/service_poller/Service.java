package com.mjones.service_poller;

import com.mjones.service_poller.Utils.Status;

public class Service {

    private String id;
    private String name;
    private String url;
    private Status status;
    private String created;
    private String lastUpdated;

    public Service() {}

    public Service(String name, String url) {
        super();
        this.name = name;
        this.url = url;
    }

    public String getId() {
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

    public String getCreated() {
        return created;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setId(String id) {
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

    public void setCreated(String created) {
        this.created = created;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
