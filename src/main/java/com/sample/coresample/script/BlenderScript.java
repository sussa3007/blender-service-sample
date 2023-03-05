package com.sample.coresample.script;

import com.sample.coresample.entity.NodeModel;
import com.sample.coresample.constant.path.RenderPath;
import lombok.Getter;

public class BlenderScript {

    @Getter
    String script;

    BlenderScript(NodeModel node) {
        this.script = "import bpy\n" +
                " \n" +
                //width
                "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_2\"] = " + node.getWidth() + "\n" +
                " \n" +
                //depth
                "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_3\" ]= " + node.getDepth() + "\n" +
                " \n" +
                //height
                "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_4\"] = " + node.getHeight() + "\n" +
                " \n" +
                //Upper_Floor
                "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_5\"] = " + node.getUpperFloor() + "\n" +
                " \n" +
                //Ring_Frequency
                "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_6\"] = " + node.getRingFrequency() + "\n" +
                " \n" +
                //V_Gap_Frequency
                "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_7\"] = " + node.getVGapFrequency() + "\n" +
                " \n" +
                "bpy.ops.object.modifier_add(type='NODES')\n" +
                " \n" +
                "bpy.ops.object.select_all(action=\"SELECT\")\n" +
                " \n" +

//                "bpy.data.node_groups[\"Geometry Nodes\"].nodes[\"Collection Info\"].inputs[0].default_value = bpy.data.collections[\"319_4-8_module_edited\"]\n" +
                "bpy.data.node_groups[\"Geometry Nodes\"].nodes[\"Collection Info\"].inputs[0].default_value = bpy.data.collections[\"319_4-8_ver.2\"]\n" +
                " \n" +

                "bpy.ops.wm.obj_export(\n" +
                "filepath=\"" + RenderPath.OUTPUT_PATH.getPath() + node.getFileName() + "\",\n" +
                "export_selected_objects=True,\n" +
                ")\n" +
                " \n" +
                "bpy.ops.wm.save_mainfile(filepath=\"" + RenderPath.BLEND_PATH.getPath() + "\")";

    }


    public static BlenderScript of(NodeModel node) {
        return new BlenderScript(node);
    }

}
