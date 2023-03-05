package com.sample.coresample.controller;


import com.sample.coresample.dto.ModelRequestDto;
import com.sample.coresample.dto.ModelResponseDto;
import com.sample.coresample.service.BlenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/blender")
@Validated
public class BlenderController {

    final BlenderService blenderService;

    @GetMapping
    public ModelAndView home() {

        return new ModelAndView(
                "blender/home"
                ,Map.of(
                        "blenderUrl", "https://image-test-suyoung.s3.ap-northeast-2.amazonaws.com/image/test.obj"
        )
        );
    }

    @PostMapping
    public ModelAndView callBlender(
            @ModelAttribute ModelRequestDto requestDto
    ) {
        System.out.println(requestDto);
        ModelResponseDto modelResponseDto = blenderService.uploadModel(requestDto);
        System.out.println(modelResponseDto);

        if (modelResponseDto == null) {
            return new ModelAndView(
                    "blender/home"
                    ,Map.of(
                    "blenderUrl", "https://image-test-suyoung.s3.ap-northeast-2.amazonaws.com/image/test.obj")
            );
        } else {
            return new ModelAndView(
                    "blender/response"
                    ,Map.of(
                    "response", modelResponseDto)
            );
        }
    }

    @PostMapping("/api")
    public ResponseEntity callBlenderAPI(
            @RequestBody ModelRequestDto requestDto
    ) {
        ModelResponseDto modelResponseDto = blenderService.uploadModel(requestDto);
        return ResponseEntity.ok().body(modelResponseDto);
    }
}
