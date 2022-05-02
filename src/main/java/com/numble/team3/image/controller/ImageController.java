package com.numble.team3.image.controller;

import com.numble.team3.exception.image.ImageResizeTypeUnSupportException;
import com.numble.team3.image.application.ImageService;
import com.numble.team3.image.application.request.CreateImageDto;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

  private final ImageService imageService;

  @PostMapping(value = "/images/resize", produces = "application/json")
  public ResponseEntity<Map> imageResize(@ModelAttribute CreateImageDto dto) throws IOException {
    if (!(dto.getType().equals("profile") || dto.getType().equals("thumbnail"))) {
      throw new ImageResizeTypeUnSupportException();
    }

    return new ResponseEntity(new HashMap<String, String>() {
      {
        put("url", imageService.uploadResizeImage(dto));
      }
    }, HttpStatus.CREATED);
  }
}
