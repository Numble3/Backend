package com.numble.team3.account.application.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyAccountDetailDto {

  private GetMyAccountDto accountDto;
  private List<GetMySimpleVideoDto> videoDtos;
}