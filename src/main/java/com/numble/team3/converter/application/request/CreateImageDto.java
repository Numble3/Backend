package com.numble.team3.converter.application.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateImageDto {

  @ApiModelProperty(value = "이미지 파일", required = true)
  @NotBlank(message = "업로드할 이미지 파일을 선택해주세요.")
  private MultipartFile file;

  @ApiModelProperty(value = "리사이즈 가로 길이", required = true)
  @NotBlank(message = "리사이즈할 가로 길이를 입력해주세요.")
  private String width;

  @ApiModelProperty(value = "리사이즈 세로 길이", required = true)
  @NotBlank(message = "라사이즈할 세로 길이를 입력해주세요.")
  private String height;

  @ApiModelProperty(value = "리사이즈 타입(thumbnail / profile)", required = true)
  @NotBlank(message = "리사이즈할 타입을 입력해주세요.")
  private String type;
}