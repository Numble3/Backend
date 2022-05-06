package com.numble.team3.account.domain;

import com.numble.team3.common.entity.BaseTimeEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id")
  private Long id;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String nickname;

  @Column private String password;

  @Column private String profile;

  @Enumerated(EnumType.STRING)
  private RoleType roleType = RoleType.ROLE_USER;

  @Column(columnDefinition = "tinyint(1)")
  private boolean deleted;

  @Column
  private String lastLogin;

  public static Account createSignUpOauth2Account(String email, String nickname, String profile) {
    Account account = new Account();
    account.initSignUpOauth2AccountField(email, nickname, profile);
    return account;
  }

  public static Account createSignUpAccount(String email, String nickname, String password) {
    Account account = new Account();
    account.initSignUpAccountField(email, nickname, password);
    return account;
  }

  private void initSignUpOauth2AccountField(String email, String nickname, String profile) {
    this.email = email;
    this.nickname = nickname;
    this.profile = profile;
  }

  private void initSignUpAccountField(String email, String nickname, String password) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
  }

  public void changeDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public void changeLastLogin() {
    this.lastLogin = DateTimeFormatter.ofPattern("yyyy.MM.dd").format(LocalDateTime.now().minusDays(1));
  }
}
