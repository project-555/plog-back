package com.plogcareers.backend.common.component;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.plogcareers.backend.common.exception.UnsupportedFileTypeException;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class PlogObjectStorageClient {
    private final ObjectStorage client;
    // TODO: yaml 설정 파일 이관 필요
    String bucketBaseURL = "https://objectstorage.ap-seoul-1.oraclecloud.com/p/Ygg8eb33Rpix8cffpOvCHnaSCCui_rp8Q42nHdWun4FW81PCpeheeh8HVGseIpBd/n/cnwjsjnbopx3/b/plog-bucket/o/";

    public PlogObjectStorageClient() throws IOException {
        ClassPathResource resource = new ClassPathResource("oracle/oracle_config");
        String configurationFilePath = resource.getFile().getPath();
        String profile = "DEFAULT";

        // Configuring the AuthenticationDetailsProvider. It's assuming there is a default OCI
        // config file
        // "~/.oci/config", and a profile in that config with the name "DEFAULT". Make changes to
        // the following
        // line if needed and use ConfigFileReader.parse(configurationFilePath, profile);

        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);

        final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
        this.client = ObjectStorageClient
                .builder()
                .region(Region.AP_SEOUL_1)
                .build(provider);
    }

    public String uploadFile(InputStream stream) throws IOException {
        UploadConfiguration uploadConfiguration = UploadConfiguration.builder()
                .allowMultipartUploads(true)
                .allowParallelUploads(true)
                .build();

        // Content Type 알아내기
        Tika tika = new Tika();
        TikaInputStream tikaInputStream = TikaInputStream.get(stream);
        String contentType = tika.detect(tikaInputStream);

        // 업로드 가능 파일 타입 체크 및 파일 확장자 결정
        String ext = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/bmp" -> ".bmp";
            case "image/webp" -> ".webp";
            default -> throw new UnsupportedFileTypeException();
        };

        // 파일 이름 랜덤 생성
        String fileName = UUID.randomUUID() + ext;

        // TODO: yaml 설정 파일 이관 필요
        // 파일 업로드
        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucketName("plog-bucket")
                .namespaceName("cnwjsjnbopx3")
                .body$(stream)
                .objectName(fileName)
                .contentType(contentType)
                .build();

        UploadRequest uploadDetails = UploadRequest.builder(stream, stream.available())
                .allowOverwrite(true)
                .build(request);
        uploadManager.upload(uploadDetails);

        return this.bucketBaseURL + fileName;
    }

}