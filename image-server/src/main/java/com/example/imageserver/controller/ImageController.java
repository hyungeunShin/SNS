package com.example.imageserver.controller;

import com.example.imageserver.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService service;

    //미리보기 기능용도
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String imageId = service.store(file);
            return ResponseEntity.ok(imageId);
        } catch(IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image: "+ e.getMessage());
        }
    }

    //미리보기 기능용도
    @GetMapping("/view/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable("imageId") String imageId, @RequestParam(value = "thumbnail", defaultValue = "false") Boolean isThumbnail) {
        Resource image = service.get(imageId, isThumbnail);
        return (image != null) ? ResponseEntity.ok().contentType(MediaType.parseMediaType("image/jpeg")).body(image) : ResponseEntity.notFound().build();
    }
}
