package com.numble.team3.converter.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetConvertVideoDto {
  private String url;
  private long duration;
}
