package com.sender.dto;

public class Json {
    private final Hit[] hits;

    public Json(Hit[] hits) {
        this.hits = hits;
    }

    public Hit[] getHits() {
        return hits;
    }

}
