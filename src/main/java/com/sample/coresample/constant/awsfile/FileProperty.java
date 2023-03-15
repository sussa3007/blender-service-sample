package com.sample.coresample.constant.awsfile;

import lombok.Getter;

public enum FileProperty {

    OBJ_DIR_NAME("obj/model"),
    FBX_DIR_NAME("fbx/model");

    @Getter
    private final String name;

    FileProperty(String name) {
        this.name = name;
    }
}
