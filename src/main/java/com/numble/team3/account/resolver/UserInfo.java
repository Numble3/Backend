package com.numble.team3.account.resolver;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
  private Long accountId;
  private List<String> roleTypes;
}
