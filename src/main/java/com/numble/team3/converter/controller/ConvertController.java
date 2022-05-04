package com.numble.team3.converter.controller;

import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertUrlDto;
import com.numble.team3.exception.convert.ImageResizeTypeUnSupportException;
import com.numble.team3.converter.annotation.ImageResizeSwagger;
import com.numble.team3.converter.application.ConvertService;
import com.numble.team3.converter.application.request.CreateImageDto;
import io.swagger.annotations.Api;
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
@Api(tags = {"파일 변환 (이미지 리사이징, 동영상 변환)"})
public class ConvertController {

  private final ConvertService convertService;

  @ImageResizeSwagger
  @PostMapping(value = "/images/resize", produces = "application/json")
  public ResponseEntity<Map> imageResize(@ModelAttribute CreateImageDto dto) throws IOException {
    if (!(dto.getType().equals("profile") || dto.getType().equals("thumbnail"))) {
      throw new ImageResizeTypeUnSupportException();
    }

    return new ResponseEntity(new HashMap<String, String>() {
      {
        put("url", convertService.uploadResizeImage(dto));
      }
    }, HttpStatus.CREATED);
  }

  @PostMapping(value = "/videos/storage")
  public ResponseEntity<GetConvertUrlDto> videoConvert(@ModelAttribute CreateVideoDto dto) throws IOException{
    return ResponseEntity.status(HttpStatus.CREATED).body(convertService.uploadConvertVideo(dto));
  }
}