package com.sample.coresample.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sample.coresample.constant.command.BlenderCommand;
import com.sample.coresample.constant.file.FileProperty;
import com.sample.coresample.constant.path.RenderPath;
import com.sample.coresample.dto.ModelRequestDto;
import com.sample.coresample.dto.ModelResponseDto;
import com.sample.coresample.entity.NodeModel;
import com.sample.coresample.repository.NodeModelRepository;
import com.sample.coresample.script.BlenderScript;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BlenderService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final NodeModelRepository nodeModelRepository;

    private final AmazonS3 amazonS3;

    public ModelResponseDto uploadModel(
            ModelRequestDto requestDto
    ) {
        System.out.println(requestDto);
        NodeModel model = NodeModel.of(requestDto);
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + requestDto.getExtension();

        model.setFileName(fileName);
        String script = BlenderScript.of(model).getScript();
        String scriptPath = RenderPath.SCRIPT_PATH.getPath();
        String command = BlenderCommand.TEST_BLENDER.getCommand();

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(scriptPath, true));

            fw.write(script);
            log.info("스크립트 작성 완료!");
            fw.flush();
            fw.close();

            Process exec = Runtime.getRuntime().exec(command);
            log.info("Command = {} ",command);
            exec.waitFor();
            int exitCode = exec.exitValue();
            log.info("작업 완료! Code = {} ",exitCode);
            if(exitCode != 0) {
                log.error("Blender:: Error Occurred!!");
            }

            Files.delete(Paths.get(scriptPath));
            File file = new File(RenderPath.OUTPUT_PATH.getPath() + model.getFileName());
            ModelResponseDto upload = upload(file, FileProperty.BASIC_IMAGE_DIR_NAME.getName(), ModelResponseDto.of(model));
            Files.delete(Paths.get(RenderPath.OUTPUT_PATH.getPath() + model.getFileName()));
            Files.delete(Paths.get(RenderPath.OUTPUT_PATH.getPath() + uuid+".mlt"));
            return upload;
        } catch (IOException e) {
            log.error("Blender:: IOException starting process!");
            throw new RuntimeException("IOException starting process!");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }


    private ModelResponseDto upload(File uploadFile, String dirName, ModelResponseDto response) {
        String s3FileName = uploadFile.getName();
        String fileName = dirName + "/" + s3FileName;
        String uploadImageUrl = putS3(uploadFile, fileName);
        response.setModelUrl(uploadImageUrl);
        return response;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        // publicRead 권한으로 업로드
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
