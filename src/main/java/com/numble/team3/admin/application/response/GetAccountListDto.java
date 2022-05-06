package com.numble.team3.admin.application.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAccountListDto {

  private List<GetAccountSimpleDto> accountDtos;
  private Long totalCount;
  private int nowPage;
  private int totalPage;
  private int size;
}
