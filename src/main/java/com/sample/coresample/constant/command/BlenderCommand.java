package com.sample.coresample.constant.command;


import com.sample.coresample.constant.path.RenderPath;

public enum BlenderCommand {

    TEST_BLENDER(RenderPath.BLENDER_PATH.getPath()+" --background "+RenderPath.BLEND_PATH.getPath()+" --python "+RenderPath.SCRIPT_PATH.getPath());

    final String command;
    BlenderCommand(String command) {
        this.command = command;
    }

    public String  getCommand() {
        return this.command;
    }
}
