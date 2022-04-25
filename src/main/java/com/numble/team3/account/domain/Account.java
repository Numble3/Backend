package com.numble.team3.account.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name = "tb_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id")
  private Long id;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String nickname;

  @Column
  private String password;

  @Column
  private String profile;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private RoleType roleType = RoleType.ROLE_USER;

  @Column(columnDefinition="tinyint(1)")
  private boolean deleted;

  public Account(String email, String nickname, String profile) {
    this.email = email;
    this.nickname = nickname;
    this.profile = profile;
  }
}
