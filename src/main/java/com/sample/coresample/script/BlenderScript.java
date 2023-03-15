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
                getSelectAll()+
                getDuplicatesMakeReal()+
                getMoveToCollection()+
                getSelectAll()+
                getHideObjectForLoop()+
                getSelectedObjectForLoop()+
                getJoinObject()+
                getSelectAll()+
                getExportScript(RenderPath.OUTPUT_PATH.getPath(), node.getFileName()+node.getExtension(), node.getExtension())+
                getQuitBlender();

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


    private String getExportScript(String outputPath, String fileName, String extension) {
        if (extension.contains("obj")) {
            return "bpy.ops.export_scene.obj(filepath = \"" + outputPath + fileName + "\", use_selection = True)\n \n";
        } else if (extension.contains("fbx")) {
            return "bpy.ops.export_scene.fbx(filepath = \"" + outputPath + fileName + "\", use_selection = True, use_visible = True)\n \n";
        } else {
            return null;
        }
    }


    private String getSaveMainFile(String blendFilePath) {
        return "bpy.ops.wm.save_mainfile(filepath=\"" + blendFilePath + "\") \n";
    }

    private String getNodeGroups(String collectionName){
        // 319_4-8_module_edited or 319_4-8_ver.2
        return "bpy.data.node_groups[\"Geometry Nodes\"].nodes[\"Collection Info\"].inputs[0].default_value = bpy.data.collections[\""+collectionName+"\"]\n \n";

    }

    private String getJoinObject() {
        return "bpy.ops.object.join() \n  \n";
    }

    private String getMoveToCollection() {
        return "bpy.ops.object.move_to_collection(collection_index=0, is_new=True, new_collection_name=\"Generate\") \n  \n";
    }

    private String getDuplicatesMakeReal() {
        return "bpy.ops.object.duplicates_make_real() \n  \n";
    }

    private String getHideObjectForLoop() {
        return """
                for o in bpy.context.selected_objects :
                    if o.name == 'BuildingGenerator' :
                        o.hide_set(True)
                    else :\s
                        o.hide_set(False)
                        
                        """;
    }

    private String getSelectedObjectForLoop() {
        return """
                Coll = bpy.data.collections['Generate']
                for obj in Coll.objects:
                    if obj.type == 'MESH':
                       obj.select_set(True)
                       bpy.context.view_layer.objects.active = obj
                       
                       """;
    }

    private String getQuitBlender() {
        return "bpy.ops.wm.quit_blender() \n  \n";
    }


}
