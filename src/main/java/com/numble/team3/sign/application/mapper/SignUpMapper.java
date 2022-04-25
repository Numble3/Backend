package com.numble.team3.sign.application.mapper;

import com.numble.team3.account.domain.Account;
import com.numble.team3.sign.application.request.SignUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SignUpMapper {

  SignUpMapper INSTANCE = Mappers.getMapper(SignUpMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", expression = "java(org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(dto.getPassword()))")
  Account toEntity(SignUpDto dto);
}
