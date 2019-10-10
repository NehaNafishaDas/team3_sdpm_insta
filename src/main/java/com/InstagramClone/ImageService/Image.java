package com.InstagramClone.ImageService;

import java.util.UUID;

public class Image {

    private final UUID id;
    private final String name;

    public Image(UUID uniqueID, String name) {
        this.id = uniqueID;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return name;
    }
}