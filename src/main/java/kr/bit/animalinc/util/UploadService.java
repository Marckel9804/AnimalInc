package kr.bit.animalinc.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final AmazonS3Client amazonS3Client;

    public String uploadImage(MultipartFile file, String folderName) throws IOException {
        if (!file.isEmpty()) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            String uploadName = folderName + "/" + splitName[0] + "_" + UUID.randomUUID().toString() + "." + splitName[1];

            amazonS3Client.putObject(new PutObjectRequest("aniinc", uploadName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String url = amazonS3Client.getUrl("aniinc", file.getOriginalFilename()).toString();
            System.out.println(url);

            return uploadName;
        } else {
            return "file is empty";
        }
    }
}
