package com.numble.team3.converter.application.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateVideoDto {
  @ApiModelProperty(value = "비디오 파일", required = true)
  @NotBlank(message = "업로드할 비디오 파일을 선택해주세요.")
  private MultipartFile videoFile;
}
