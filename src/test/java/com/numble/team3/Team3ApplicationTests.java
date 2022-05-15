package com.numble.team3;

import com.numble.team3.converter.application.VideoConvertService;
import com.numble.team3.converter.domain.ConvertVideoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class Team3ApplicationTests {

  @MockBean
  protected ConvertVideoUtils convertVideoUtils;

  @MockBean
  protected VideoConvertService videoConvertService;

  @Test
  void contextLoads() {}
}
