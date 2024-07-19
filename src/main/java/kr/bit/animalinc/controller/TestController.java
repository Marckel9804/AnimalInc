package kr.bit.animalinc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/testapi")
public class TestController {

    @GetMapping("/test1")
    public Map<String, String > test1() {
        return Map.of("test", "success");
    }
}
