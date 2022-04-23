package com.numble.team3.jwt;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PrivateClaims {
  private String accountId;
  private List<String> roleTypes;
}
