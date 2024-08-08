package kr.bit.animalinc.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor
public class UploadOSTests {

    private final UploadService uploadService;

    @Test
    public void testUploadService() {

    }
}
