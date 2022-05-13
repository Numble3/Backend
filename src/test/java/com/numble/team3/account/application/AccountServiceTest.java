package com.numble.team3.account.application;

import static com.numble.team3.factory.dto.AccountDtoFactory.createUpdateMyAccountDto;
import static com.numble.team3.factory.entity.AccountEntityFactory.createAccount;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.application.response.GetMyAccountDetailDto;
import com.numble.team3.account.application.response.GetMyAccountDto;
import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.AccountUtils;
import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.admin.application.response.GetAccountDetailDto;
import com.numble.team3.admin.application.response.GetAccountListDto;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import com.numble.team3.factory.UserInfoFactory;
import com.numble.team3.video.infra.JpaVideoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("AccountService 테스트")
class AccountServiceTest {

  @Mock
  JpaAccountRepository accountRepository;

  @Mock
  JpaVideoRepository videoRepository;

  @Mock
  AccountUtils accountUtils;

  AccountService accountService;

  @BeforeEach
  void beforeEach() {
    accountService = new AccountService(accountRepository, videoRepository, accountUtils);
  }

  @Test
  void changeAccountLastLoginByScheduler_성공_테스트() throws Exception {
    // given
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);

    given(accountRepository.findAll()).willReturn(List.of(account));
    given(accountUtils.getAllLastLoginAccountId()).willReturn(List.of(1L));
    given(accountUtils.getAccountLastLoginTime(anyLong())).willReturn("2022-02-02");

    // when
    accountService.changeAccountLastLoginByScheduler();

    // then
    verify(accountUtils).deleteAllLastLoginTime(anyLong());
  }

  @Test
  void getAccounts_성공_테스트() throws Exception {
    // given
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);
    Page<Account> accounts = new PageImpl<>(List.of(account));
    PageRequest pageRequest = PageRequest.of(0, 1);

    given(accountRepository.findAllWithAdmin(any(RoleType.class), anyBoolean(), any(PageRequest.class))).willReturn(accounts);

    // when
    GetAccountListDto result = accountService.getAccounts(pageRequest);

    // then
    assertEquals(1, result.getAccountDtos().size());
    assertEquals(1L, result.getTotalCount());
    assertEquals(1, result.getNowPage());
    assertEquals(1, result.getTotalPage());
    assertEquals(1, result.getSize());
  }

  @Test
  void getAccountByAdmin_성공_테스트() throws Exception {
    // given
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when
    GetAccountDetailDto result = accountService.getAccountByAccountId(1L);

    // then
    assertEquals(1L, result.getId());
    assertEquals("test@email.com", result.getEmail());
    assertEquals("nickname", result.getNickname());
  }

  @Test
  void getAccountByAdmin_실패_테스트() {
    // given
    given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

    // when, then
    assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByAccountId(1L));
  }

  @Test
  void withdrawalAccount_성공_테스트() throws Exception {
    // given
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when
    accountService.withdrawalAccount(1L);

    // then
    assertTrue(account.isDeleted());
  }

  @Test
  void withdrawalAccount_실패_테스트() {
    // given
    given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

    // when, then
    assertThrows(AccountNotFoundException.class, () -> accountService.withdrawalAccount(1L));
  }

  @Test
  void getMyAccount_성공_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when
    GetMyAccountDto result = accountService.getAccountByUserInfo(userInfo);

    // then
    assertEquals("test@email.com", result.getEmail());
    assertEquals("nickname", result.getNickname());
  }

  @Test
  void getMyAccount_없는_회원_실패_테스트() {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);

    given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

    // when, then
    assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByUserInfo(userInfo));
  }

  @Test
  void getMyAccount_탈퇴_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);
    setField(account, "deleted", true);

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when, then
    assertThrows(AccountWithdrawalException.class, () -> accountService.getAccountByUserInfo(userInfo));
  }

  @Test
  void updateMyAccount_성공_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);
    UpdateMyAccountDto dto =
      createUpdateMyAccountDto(null, "change nickname");

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when
    accountService.updateAccount(userInfo, dto);

    // then
    assertEquals("change nickname", account.getNickname());
  }

  @Test
  void updateMyAccount_없는_회원_실패_테스트() {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    UpdateMyAccountDto dto =
      createUpdateMyAccountDto(null, "change nickname");

    given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

    // when, then
    assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(userInfo, dto));
  }

  @Test
  void updateMyAccount_탈퇴_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);
    setField(account, "deleted", true);
    UpdateMyAccountDto dto =
      createUpdateMyAccountDto(null, "change nickname");

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when, then
    assertThrows(AccountWithdrawalException.class, () -> accountService.updateAccount(userInfo, dto));

  }

  @Test
  void getDetailAccountByUserInfo_성공_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));
    given(videoRepository.findAllByAccountIdAndLimit(anyLong(), anyInt())).willReturn(List.of());

    // when
    GetMyAccountDetailDto result =
      accountService.getDetailAccountByUserInfo(userInfo);

    // then
    assertEquals("test@email.com", result.getAccountDto().getEmail());
    assertEquals("nickname", result.getAccountDto().getNickname());
    assertEquals(0, result.getVideoDtos().size());
  }

  @Test
  void getDetailAccountByUserInfo_없는_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);

    given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

    // when, then
    assertThrows(AccountNotFoundException.class, () -> accountService.getDetailAccountByUserInfo(userInfo));
  }

  @Test
  void getDetailAccountByUserInfo_탈퇴_회원_실패_테스트() throws Exception {
    // given
    UserInfo userInfo = UserInfoFactory.createUserInfo(1L, RoleType.ROLE_USER);
    Account account =
      createAccount(1L, "test@email.com", "password", "nickname", RoleType.ROLE_USER);
    setField(account, "deleted", true);

    given(accountRepository.findById(anyLong())).willReturn(Optional.ofNullable(account));

    // when, then
    assertThrows(AccountWithdrawalException.class, () -> accountService.getDetailAccountByUserInfo(userInfo));
  }
}