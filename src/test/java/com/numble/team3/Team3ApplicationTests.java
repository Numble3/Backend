package com.numble.team3;

import com.numble.team3.video.domain.VideoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class Team3ApplicationTests {

	@MockBean
	protected VideoUtils videoUtils;

	@Test
	void contextLoads() {
	}

}
