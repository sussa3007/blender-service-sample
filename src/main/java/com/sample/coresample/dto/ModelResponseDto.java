package com.sample.coresample.dto;

import com.sample.coresample.entity.NodeModel;
import jakarta.persistence.Column;
import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class ModelResponseDto {
    Long nodeModelId;

    String fileName;

    String modelUrl;

    Integer width;

    Integer depth;

    Integer height;

    Integer upperFloor;

    Integer ringFrequency;

    Double vGapFrequency;

    ModelResponseDto(NodeModel node) {
        this.nodeModelId = node.getNodeModelId();
        this.fileName = node.getFileName();
        this.modelUrl = node.getModelUrl();
        this.width = node.getWidth();
        this.depth = node.getWidth();
        this.height = node.getHeight();
        this.upperFloor = node.getUpperFloor();
        this.ringFrequency = node.getRingFrequency();
        this.vGapFrequency = node.getVGapFrequency();
    }

    public static ModelResponseDto of(NodeModel node) {
        return new ModelResponseDto(node);
    }
}
