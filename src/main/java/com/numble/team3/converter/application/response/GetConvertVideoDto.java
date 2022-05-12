package com.numble.team3.converter.application.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetConvertVideoDto {
  @ApiModelProperty(value = "업로드된 영상의 경로")
  private String url;

  @ApiModelProperty(value = "영상 시간")
  private long duration;
}
