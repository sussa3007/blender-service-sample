package com.sample.coresample.entity;


import com.sample.coresample.dto.ModelRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class NodeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long nodeModelId;

    @Setter
    @Column(nullable = false)
    String fileName;

    @Setter
    @Column(nullable = false)
    String modelUrl;

    @Setter
    @Column(nullable = false)
    Integer width;

    @Setter
    @Column(nullable = false)
    Integer depth;

    @Setter
    @Column(nullable = false)
    Integer height;

    @Setter
    @Column(nullable = false)
    Integer upperFloor;

    @Setter
    @Column(nullable = false)
    Integer ringFrequency;

    @Setter
    @Column(nullable = false)
    Double vGapFrequency;

    @Setter
    @Column(nullable = false)
    String extension;


    NodeModel(ModelRequestDto requestDto) {
        this.width = requestDto.getWidth();
        this.depth = requestDto.getDepth();
        this.height = requestDto.getHeight();
        this.upperFloor = requestDto.getUpperFloor();
        this.ringFrequency = requestDto.getRingFrequency();
        this.vGapFrequency = requestDto.getGapFrequency();
        this.extension = requestDto.getExtension();
    }

    public static NodeModel of(ModelRequestDto requestDto){
        return new NodeModel(requestDto);
    }
}
