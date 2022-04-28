package com.numble.team3.account.domain;

import com.numble.team3.sign.application.request.SignUpDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@Table(name = "tb_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

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
}
