package com.sender.dto;

import java.util.Objects;

public class Hit {
    private final String largeImageURL;
    private final String imageURL;
    private int imageWidth;
    private int imageHeight;

    public Hit(String largeImageURL, int imageWidth, int imageHeight, String imageURL) {
        this.imageURL = imageURL;
        this.largeImageURL = largeImageURL;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hit hit = (Hit) o;
        return imageWidth == hit.imageWidth && imageHeight == hit.imageHeight && Objects.equals(largeImageURL, hit.largeImageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(largeImageURL, imageWidth, imageHeight);
    }
}
