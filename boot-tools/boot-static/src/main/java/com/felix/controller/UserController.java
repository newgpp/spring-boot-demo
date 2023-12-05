package com.felix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author felix
 * @desc some desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get")
    public ObjectNode getUser(@RequestParam("name") String name, @RequestParam("age") String age) {
        log.debug("getUser, name={}, age={}", name, age);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        return objectNode.put("name", name).put("age", age);
    }

    @PostMapping("/save")
    public ObjectNode saveUser(@RequestParam("name") String name, @RequestParam("age") String age) {
        log.debug("saveUser, name={}, age={}", name, age);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        return objectNode.put("name", name).put("age", age);
    }

    @PostMapping("/detail/save")
    public ObjectNode saveUserDetail(@RequestBody ObjectNode detail) {
        log.debug("saveUserDetail, detail={}", detail);
        return detail;
    }

    @PostMapping("/image/save")
    public ObjectNode saveUserImage(@RequestParam("name") String name, @RequestParam("age") String age
            , @RequestPart("file") MultipartFile file) {
        log.debug("saveUserImage, name={}, age={}, file={}", name, age, file);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        return objectNode.put("name", name).put("age", age)
                .put("file", file.getSize());
    }


}
