package com.numble.team3;

import com.numble.team3.converter.application.VideoConvertService;
import com.numble.team3.converter.domain.ConvertVideoUtils;
import com.numble.team3.converter.infra.VideoFfmpegUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class Team3ApplicationTests {

  @MockBean protected ConvertVideoUtils convertVideoUtils;

  @MockBean protected VideoFfmpegUtils videoFfmpegUtils;

  @MockBean protected VideoConvertService videoConvertService;
}
