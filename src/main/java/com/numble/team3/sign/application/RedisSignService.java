package com.numble.team3.sign.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.exception.account.AccountEmailAlreadyExistsException;
import com.numble.team3.exception.account.AccountNicknameAlreadyExistsException;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.account.AccountWithdrawalException;
import com.numble.team3.exception.sign.TokenFailureException;
import com.numble.team3.jwt.PrivateClaims;
import com.numble.team3.jwt.TokenHelper;
import com.numble.team3.sign.application.request.SignInDto;
import com.numble.team3.sign.application.request.SignUpDto;
import com.numble.team3.sign.application.response.TokenDto;
import com.numble.team3.exception.sign.SignInFailureException;
import com.numble.team3.sign.infra.SignRedisUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedisSignService implements SignService {

  private final JpaAccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenHelper accessTokenHelper;
  private final TokenHelper refreshTokenHelper;
  private final SignRedisUtils signRedisUtils;

  @Transactional
  @Override
  public void signUp(SignUpDto dto) {
    validateSignUpInfo(dto);

    accountRepository.save(SignUpDto.toEntity(dto, passwordEncoder));
  }

  @Transactional(readOnly = true)
  @Override
  public TokenDto signIn(SignInDto dto) {
    Account account =
      accountRepository.findByEmail(dto.getEmail()).orElseThrow(SignInFailureException::new);

    if (account.isDeleted()) {
      throw new AccountWithdrawalException();
    }

    validatePassword(dto, account);

    PrivateClaims privateClaims = createPrivateClaims(account);
    String accessToken = accessTokenHelper.createToken(privateClaims);
    String refreshToken = refreshTokenHelper.createToken(privateClaims);

    signRedisUtils.saveAccessToken(account.getId(), accessToken);
    signRedisUtils.saveRefreshToken(account.getId(), refreshToken);

    return new TokenDto(accessToken, refreshToken);
  }

  @Override
  public TokenDto createAccessTokenByRefreshToken(String refreshToken) {
    PrivateClaims privateClaims =
        refreshTokenHelper.parse(refreshToken).orElseThrow(TokenFailureException::new);

    if (!signRedisUtils.validToken(
        refreshToken, "refreshToken", Optional.ofNullable(privateClaims.getAccountId()))) {
      throw new TokenFailureException();
    }

    String accessToken = accessTokenHelper.createToken(privateClaims);
    Long accountId = Long.valueOf(privateClaims.getAccountId());

    signRedisUtils.deleteToken("accessToken", accountId);
    signRedisUtils.saveAccessToken(accountId, accessToken);

    return new TokenDto(accessToken, refreshToken);
  }

  @Override
  public void logout(String accessToken) {
    Long accountId =
        accessTokenHelper
            .parse(accessToken)
            .map(claims -> Long.valueOf(claims.getAccountId()))
            .orElseThrow(TokenFailureException::new);

    signRedisUtils.deleteToken("refreshToken", accountId);
    signRedisUtils.deleteToken("accessToken", accountId);
  }

  @Transactional
  @Override
  public void withdrawal(String accessToken) {
    Account account =
      accountRepository.findById(accessTokenHelper.parse(accessToken)
      .map(claims -> Long.valueOf(claims.getAccountId()))
      .orElseThrow(TokenFailureException::new)).orElseThrow(AccountNotFoundException::new);

    account.changeDeleted(true);

    signRedisUtils.deleteToken("refreshToken", account.getId());
    signRedisUtils.deleteToken("accessToken", account.getId());
  }

  private void validateSignUpInfo(SignUpDto dto) {
    if (accountRepository.existsByEmail(dto.getEmail())) {
      throw new AccountEmailAlreadyExistsException();
    }
    if (accountRepository.existsByNickname(dto.getNickname())) {
      throw new AccountNicknameAlreadyExistsException();
    }
  }

  private void validatePassword(SignInDto dto, Account account) {
    if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
      throw new SignInFailureException();
    }
  }

  private PrivateClaims createPrivateClaims(Account account) {

    return new PrivateClaims(
        String.valueOf(account.getId()), List.of(account.getRoleType().toString()));
  }
}
