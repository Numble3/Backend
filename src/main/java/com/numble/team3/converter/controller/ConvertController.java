package com.numble.team3.converter.controller;

import com.numble.team3.converter.annotation.VideoConvertSwagger;
import com.numble.team3.converter.application.VideoConvertService;
import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import com.numble.team3.exception.convert.ImageResizeTypeUnSupportException;
import com.numble.team3.converter.annotation.ImageResizeSwagger;
import com.numble.team3.converter.application.ImageConvertService;
import com.numble.team3.converter.application.request.CreateImageDto;
import com.numble.team3.exception.image.ImageWrongRatioException;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"파일 변환 (이미지 리사이징, 동영상 변환)"})
public class ConvertController {

  private final ImageConvertService imageConvertService;
  private final VideoConvertService videoConvertService;

  @ImageResizeSwagger
  @PostMapping(value = "/images/resize", produces = "application/json")
  public ResponseEntity<Map> imageResize(@ModelAttribute CreateImageDto dto) throws IOException {
    checkResizeType(dto.getType());
    checkResizeRatio(
        dto.getType(), Integer.parseInt(dto.getWidth()), Integer.parseInt(dto.getHeight()));

    return new ResponseEntity(
        new HashMap<String, String>() {
          {
            put("url", imageConvertService.uploadResizeImage(dto));
          }
        },
        HttpStatus.CREATED);
  }

  @VideoConvertSwagger
  @PostMapping(
      value = "/videos/storage",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetConvertVideoDto> videoConvert(@ModelAttribute CreateVideoDto dto)
      throws IOException {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(videoConvertService.uploadConvertVideo(dto));
  }

  private void checkResizeType(String type) {
    if (!(type.equals("profile") || type.equals("thumbnail"))) {
      throw new ImageResizeTypeUnSupportException();
    }
  }

  private void checkResizeRatio(String type, int width, int height) {
    if (type.equals("profile")) {
      if (width != height) {
        throw new ImageWrongRatioException();
      }
    } else {
      if ((height * 16) != (width * 9)) {
        throw new ImageWrongRatioException();
      }
    }
  }
}
