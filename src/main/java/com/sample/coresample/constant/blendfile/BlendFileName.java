package com.sample.coresample.constant.blendfile;

public enum BlendFileName {

    BLEND_HOUSE("KPB-BrickHouseElements-modified_v02.blend");

    final String name;

    BlendFileName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
