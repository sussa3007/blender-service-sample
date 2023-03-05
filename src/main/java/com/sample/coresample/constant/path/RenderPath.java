package com.sample.coresample.constant.path;

import com.sample.coresample.constant.blendfile.BlendFileName;

public enum RenderPath {
//    BLENDER_PATH("C:\\Program Files\\Blender Foundation\\Blender 3.4\\blender.exe"),
    BLENDER_PATH("D:\\Blender\\blender.exe"),
//    OUTPUT_PATH("C:\\\\blender\\\\");
    OUTPUT_PATH("D:\\\\BlenderOutput\\\\"),
//    SCRIPT_PATH("C:\\blender\\script.py");
    SCRIPT_PATH("D:\\BlenderOutput\\script.py"),
//    BLEND_PATH("C:\\blender\\Building_node\\KPB-BrickHouseElements-modified_v02.blend");
    BLEND_PATH("D:\\BlenderOutput\\Building_node\\"+ BlendFileName.BLEND_HOUSE.getPath());

    final String path;
    RenderPath(String path) {
        this.path = path;
    }

    public String  getPath() {
        return this.path;
    }
}
