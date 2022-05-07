package com.numble.team3.account.application;

import com.numble.team3.account.application.request.UpdateMyAccountDto;
import com.numble.team3.account.application.response.GetMyAccountDto;
import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.AccountUtils;
import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.admin.application.response.GetAccountDetailDto;
import com.numble.team3.admin.application.response.GetAccountListDto;
import com.numble.team3.admin.application.response.GetAccountSimpleDto;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

  private final JpaAccountRepository accountRepository;
  private final AccountUtils accountUtils;

  public void changeAccountLastLoginByScheduler() {
    List<Account> accounts = accountRepository.findAll();

    List<Long> accountIds = accountUtils.getAllLastLoginAccountId();

    accounts
      .stream().filter(account -> accountIds.contains(account.getId()))
      .forEach(account -> {
        String lastLoginTime =
          accountUtils.getAccountLastLoginTime(account.getId());
        if (!(account.getLastLogin() != null && account.getLastLogin().equals(lastLoginTime))) {
          account.changeLastLogin();
        }});

    accountIds.stream().forEach(accountId -> accountUtils.deleteAllLastLoginTime(accountId));
  }

  @Transactional(readOnly = true)
  public GetAccountListDto getAccounts(PageRequest pageRequest) {
    Page<Account> accounts = accountRepository.findAllWithAdmin(RoleType.ROLE_USER, false, pageRequest);

    return GetAccountListDto.builder()
      .accountDtos(
        accounts.stream().map(
          account -> {
            GetAccountSimpleDto dto = GetAccountSimpleDto.fromEntity(account);
            accountUtils.optionalGetAccountLastLoginTime(account.getId()).ifPresent(lastLogin -> dto.changeLastLogin(lastLogin));
            return dto;
          }).collect(Collectors.toList()))
      .totalCount(accounts.getTotalElements())
      .nowPage(accounts.getNumber() + 1)
      .totalPage(accounts.getTotalPages())
      .size(accounts.getSize())
      .build();
  }

  @Transactional(readOnly = true)
  public GetAccountDetailDto getAccountByAdmin(Long accountId) {
    GetAccountDetailDto dto =
      GetAccountDetailDto.fromEntity(accountRepository.findById(accountId)
        .orElseThrow(AccountNotFoundException::new));

    accountUtils.optionalGetAccountLastLoginTime(dto.getId()).ifPresent(lastLogin -> dto.changeLastLogin(lastLogin));

    return dto;
  }

  public void withdrawalAccount(Long accountId) {
    Account account =
      accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);

    account.changeDeleted(true);
  }

  @Transactional(readOnly = true)
  public GetMyAccountDto getMyAccount(UserInfo userInfo) {
    Account account =
      accountRepository.findById(userInfo.getAccountId()).orElseThrow(AccountNotFoundException::new);

    if (account.isDeleted()) {
      throw new AccountWithdrawalException();
    }

    return new GetMyAccountDto(account.getEmail(), account.getProfile(), account.getNickname());
  }

  public void updateMyAccount(UserInfo userInfo, UpdateMyAccountDto dto) {
    Account account =
      accountRepository.findById(userInfo.getAccountId()).orElseThrow(AccountNotFoundException::new);

    if (account.isDeleted()) {
      throw new AccountWithdrawalException();
    }

    account.changeMyAccount(dto.getNickname(), dto.getProfile());
  }
}
