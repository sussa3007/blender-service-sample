package com.sample.coresample.script;

import com.sample.coresample.entity.NodeModel;
import com.sample.coresample.constant.path.RenderPath;
import lombok.Data;

@Data
public class BlenderScript {

    String script;

    final int WIDTH = 2;
    final int DEPTH = 3;
    final int HEIGHT = 4;
    final int UPPER_FLOOR = 5;
    final int RING_FREQUENCY = 6;
    final int V_GAP_FREQUENCY = 7;

    BlenderScript(NodeModel node) {
        this.script = getImportBpy()+
                getModifiers(WIDTH, node.getWidth())+
                getModifiers(DEPTH, node.getDepth())+
                getModifiers(HEIGHT, node.getHeight())+
                getModifiers(UPPER_FLOOR, node.getUpperFloor())+
                getModifiers(RING_FREQUENCY, node.getRingFrequency())+
                getModifiersVGapFrequency(V_GAP_FREQUENCY, node.getVGapFrequency())+
                getModifiersAdd("NODES")+
                getSelectAll()+
                getNodeGroups("319_4-8_ver.2")+
                getExportObj(RenderPath.OUTPUT_PATH.getPath(), node.getFileName())+
                getSaveMainFile(RenderPath.BLEND_PATH.getPath());

    }


    public static BlenderScript of(NodeModel node) {
        return new BlenderScript(node);
    }

    private String getImportBpy() {
        return "import bpy\n \n";
    }

    private String getModifiers(int inputIndex, int parameter) {
        return "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_"+inputIndex+"\"] = " + parameter + "\n \n" ;
    }

    private String getModifiersVGapFrequency(int inputIndex, double parameter) {
        return "bpy.context.object.modifiers[\"GeometryNodes\"][\"Input_"+inputIndex+"\"] = " + parameter + "\n \n" ;
    }

    private String getModifiersAdd(String type) {
        return "bpy.ops.object.modifier_add(type='"+type+"')\n \n";
    }

    private String getSelectAll() {
        return "bpy.ops.object.select_all(action=\"SELECT\")\n \n";
    }

    private String getExportObj(String outputPath, String fileName) {
        return "bpy.ops.wm.obj_export(\n" +
                "filepath=\"" + outputPath + fileName + "\",\n" +
                "export_selected_objects=True,\n" +
                ")\n \n";
    }

    private String getSaveMainFile(String blendFilePath) {
        return "bpy.ops.wm.save_mainfile(filepath=\"" + blendFilePath + "\") \n";
    }

    private String getNodeGroups(String collectionName){
        // 319_4-8_module_edited or 319_4-8_ver.2
        return "bpy.data.node_groups[\"Geometry Nodes\"].nodes[\"Collection Info\"].inputs[0].default_value = bpy.data.collections[\""+collectionName+"\"]\n \n";

    }



}
