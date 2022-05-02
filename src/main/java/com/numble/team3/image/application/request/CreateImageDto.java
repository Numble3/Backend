package com.numble.team3.image.application.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateImageDto {

  @NotBlank(message = "업로드할 이미지 파일을 선택해주세요.")
  private MultipartFile file;

  @NotBlank(message = "리사이즈할 가로 길이를 입력해주세요.")
  private String width;

  @NotBlank(message = "라사이즈할 세로 길이를 입력해주세요.")
  private String height;

  @NotBlank(message = "리사이즈할 타입을 입력해주세요.")
  private String type;
}