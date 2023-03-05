package com.sample.coresample.constant.blendfile;

public enum BlendFileName {

    BLEND_HOUSE("KPB-BrickHouseElements-modified_v02.blend");

    final String path;

    BlendFileName(String path) {
        this.path = path;
    }

    public String  getPath() {
        return this.path;
    }
}
