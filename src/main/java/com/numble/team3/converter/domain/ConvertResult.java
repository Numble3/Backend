package com.numble.team3.converter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConvertResult {
  private long videoDuration;
  private String uploadDir;
  private String uploadFilePath;
}
