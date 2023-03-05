package com.sample.coresample.dto;

import com.sample.coresample.entity.NodeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class ModelRequestDto {

    @NotNull
    Integer width;

    @NotNull
    Integer depth;

    @NotNull
    Integer height;

    @NotNull
    Integer upperFloor;

    @NotNull
    Integer ringFrequency;

    @NotNull
    Double gapFrequency;

    @NotNull
    String extension;

}
