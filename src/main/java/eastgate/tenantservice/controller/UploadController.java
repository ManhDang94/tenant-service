package eastgate.tenantservice.controller;

import eastgate.tenantservice.response.BaseResponse;
import eastgate.tenantservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Service
@Slf4j
@RequiredArgsConstructor
public class UploadController {

    private final JobLauncher jobLauncher;
    private final Job importEventsJob;
    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public BaseResponse<?> upload(@RequestParam("files") MultipartFile[] files) {
        log.info("start call api to upload file");
        String response = "OK";
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {

                try {
                    String path = fileStorageService.saveFile(file);
                    log.info("save file " + path);
                    JobParameters jobParameters = new JobParametersBuilder()
                            .addString("fileName", file.getOriginalFilename())
                            .addLong("time", System.currentTimeMillis())
                            .addString("inputFilePath", path)
                            .toJobParameters();
                    JobExecution jobExecution = jobLauncher.run(importEventsJob, jobParameters);

                } catch (Exception e) {
                    log.error("error: ", e);
                }
            }
        }
        log.info("response = {}", response);
        return BaseResponse.success(response);
    }
}
