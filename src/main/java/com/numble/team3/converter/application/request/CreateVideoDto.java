package com.numble.team3.converter.application.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateVideoDto {
  @ApiModelProperty(value = "비디오 파일", required = true)
  private MultipartFile videoFile;
}
