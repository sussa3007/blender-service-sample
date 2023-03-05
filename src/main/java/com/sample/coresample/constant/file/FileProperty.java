package com.sample.coresample.constant.file;

import lombok.Getter;

public enum FileProperty {

    BASIC_IMAGE_DIR_NAME("model");

    @Getter
    private final String name;

    FileProperty(String name) {
        this.name = name;
    }
}
