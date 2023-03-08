package com.sample.coresample.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sample.coresample.constant.command.BlenderCommand;
import com.sample.coresample.constant.awsfile.FileProperty;
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

    public ModelResponseDto generateModel(
            ModelRequestDto requestDto
    ) {
        log.info("RequestDto = {} ", requestDto);
        NodeModel model = NodeModel.of(requestDto);
        UUID uuid = UUID.randomUUID();

        model.setFileName(uuid.toString());
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
            log.info("Command = {} ", command);
            exec.waitFor();
            int exitCode = exec.exitValue();
            log.info("작업 완료! Code = {} ", exitCode);
            if (exitCode != 0) {
                log.error("Blender:: Error Occurred!!");
            }

            return ModelResponseDto.of(uploadAndClearOutputFile(model, scriptPath));
        } catch (IOException e) {
            log.error("Blender:: IOException starting process!");
            throw new RuntimeException("IOException starting process!");
        } catch (Exception e) {
            throw new RuntimeException("Exception");
        }
    }

    public String uploadPathFile(String path, String dir) {
        File file = new File(path);
        String s3FileName = file.getName();
        String fileName = dir +"/" + s3FileName;
        return putS3(file, fileName);
    }

    private NodeModel uploadAndClearOutputFile(
            NodeModel model,
            String scriptPath
    ) throws IOException {
        Files.delete(Paths.get(scriptPath));
        NodeModel upload = null;
        if (model.getExtension().contains("obj")) {
            File obj = new File(RenderPath.OUTPUT_PATH.getPath() + model.getFileName() + model.getExtension());
            File mtl = new File(RenderPath.OUTPUT_PATH.getPath() + model.getFileName() + ".mtl");

            upload = uploadModel(obj, FileProperty.OBJ_DIR_NAME.getName(), model);
            uploadModelSubFile(mtl, FileProperty.OBJ_DIR_NAME.getName());
            Files.delete(Paths.get(RenderPath.OUTPUT_PATH.getPath() + model.getFileName() + model.getExtension()));
            Files.delete(Paths.get(RenderPath.OUTPUT_PATH.getPath() + model.getFileName() + ".mtl"));
            log.info("Local Output File(.obj & .mtl) 삭제 완료!");
        }
        return upload;
    }

    private NodeModel uploadModel(
            File uploadFile,
            String dirName,
            NodeModel model
    ) {
        String s3FileName = uploadFile.getName();
        String fileName = dirName + "/" + s3FileName;
        String uploadImageUrl = putS3(uploadFile, fileName);
        model.setModelUrl(uploadImageUrl);
        return model;
    }

    private void uploadModelSubFile(
            File uploadFile,
            String dirName
    ) {
        String s3FileName = uploadFile.getName();
        String fileName = dirName + "/" + s3FileName;
        putS3(uploadFile, fileName);
    }

    private String putS3(
            File uploadFile,
            String fileName
    ) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        log.info("S3 Bucket Upload 완료!");
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
