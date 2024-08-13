package kr.bit.animalinc.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadRestController {

    private final AmazonS3Client amazonS3Client;

    @PostMapping("/upload/img")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("folderName") String folderName) throws IOException {

        if (!file.isEmpty()) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            String uploadName = folderName + "/" + splitName[0] + "_" + UUID.randomUUID().toString() + "." + splitName[1];

            amazonS3Client.putObject(new PutObjectRequest("aniinc", uploadName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String url = amazonS3Client.getUrl("aniinc", uploadName).toString();
            System.out.println(url);

            Map<String, String> map = new HashMap<>();
            map.put("url", url);
            map.put("upLoadFilename", uploadName);

            return ResponseEntity.ok(map);
        } else {
            System.out.println();
            return ResponseEntity.badRequest().body("File is empty");
        }
    }

    @PostMapping("/upload/imglist")
    public ResponseEntity<?> uploadImage(@RequestParam("files") List<MultipartFile> files,
                                         @RequestParam("folderName") String folderName) throws IOException {
        System.out.println(files.toString());
        List<Map<String, String>> result = new ArrayList<>();
        for (MultipartFile file : files) {
            System.out.println(file.getOriginalFilename());
            if (!file.isEmpty()) {
                String[] splitName = file.getOriginalFilename().split("\\.");
                String uploadName = folderName + "/" + splitName[0] + "_" + UUID.randomUUID().toString() + "." + splitName[1];

                amazonS3Client.putObject(new PutObjectRequest("aniinc", uploadName, file.getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                String url = amazonS3Client.getUrl("aniinc", uploadName).toString();
                System.out.println(url);

                Map<String, String> map = new HashMap<>();
                map.put("url", url);
                map.put("upLoadFilename", uploadName);

                result.add(map);
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("error","empty file");
                result.add(map);
            }

        }
        return ResponseEntity.ok(result);
    }
}
