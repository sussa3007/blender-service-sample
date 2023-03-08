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

    @GetMapping("/viewer/gltf")
    public ModelAndView gltf() {
        return new ModelAndView("blender/gltf");
    }

    @GetMapping("/viewer/obj")
    public ModelAndView obj() {
        return new ModelAndView("blender/obj");
    }

    @GetMapping("/viewer/fbx")
    public ModelAndView fbx() {
        return new ModelAndView("blender/fbx");
    }

    @GetMapping
    public ModelAndView home() {

        return new ModelAndView(
                "blender/index"
                , Map.of(
                "fileName", "ToyCar"
//                "fileName", "693ab010-40e3-4b95-b29e-6294337984bf"
        )
        );
    }

    @PostMapping
    public ModelAndView callBlender(
            @ModelAttribute ModelRequestDto requestDto
    ) {
        ModelResponseDto modelResponseDto = blenderService.generateModel(requestDto);

        return new ModelAndView(
                "blender/response"
                , Map.of(
                "response", modelResponseDto)
        );
    }

    @PostMapping("/api")
    public ResponseEntity callBlenderAPI(
            @RequestBody ModelRequestDto requestDto
    ) {
        ModelResponseDto modelResponseDto = blenderService.generateModel(requestDto);
        return ResponseEntity.ok().body(modelResponseDto);
    }

    @PostMapping("/api/upload")
    public ResponseEntity uploadPathFile(
            @RequestBody Map<String, String> filePath
    ) {
        System.out.println("filePath :: " + filePath.get("filePath"));
        String fileUrl = blenderService.uploadPathFile(filePath.get("filePath"), filePath.get("dir"));
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }

}
