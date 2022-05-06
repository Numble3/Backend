package com.numble.team3.converter.application;

import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import com.numble.team3.converter.domain.ConvertImageUtils;
import com.numble.team3.converter.domain.ConvertVideoUtils;
import com.numble.team3.exception.convert.ImageConvertFailureException;
import com.numble.team3.exception.convert.ImageTypeUnSupportException;
import com.numble.team3.converter.application.request.CreateImageDto;
import com.numble.team3.exception.convert.VideoConvertFailureException;
import com.numble.team3.exception.convert.VideoTypeUnSupportException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiGatewayConvertService implements ConvertService {

  private static final List<String> IMAGE_TYPE = List.of("jpeg", "png", "jpg");
  private static final List<String> VIDEO_TYPE =
      List.of("mp4", ".avi", ".wmv", ".mpg", ".mpeg", "webm");

  private final ConvertImageUtils imageUtils;
  private final ConvertVideoUtils videoUtils;

  @Override
  public String uploadResizeImage(CreateImageDto dto) {
    String filename = createImageFilename(dto.getFile().getOriginalFilename());

    try {
      imageUtils.saveTempImageFileForMetadata(filename, dto);
    } catch (IOException e) {
      throw new ImageConvertFailureException();
    }

    return imageUtils.processImageResize(filename, dto);
  }

  @Override
  public GetConvertVideoDto uploadConvertVideo(CreateVideoDto dto) throws IOException {
    String filename = createVideoFilename(dto.getFile().getOriginalFilename());

    try {
      videoUtils.saveTempVideoForConvert(filename, dto);
    } catch (IOException e) {
      throw new VideoConvertFailureException();
    }
    return videoUtils.processConvertVideo(filename);
  }

  private String createImageFilename(String originalFilename) {
    String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

    if (!(IMAGE_TYPE.contains(ext))) {
      throw new ImageTypeUnSupportException();
    }

    return UUID.randomUUID().toString().substring(0, 10) + "." + ext;
  }

  private String createVideoFilename(String originalFilename) {
    String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

    if (!(VIDEO_TYPE.contains(ext))) {
      throw new VideoTypeUnSupportException();
    }

    return UUID.randomUUID().toString().substring(0, 10) + "." + ext;
  }
}
