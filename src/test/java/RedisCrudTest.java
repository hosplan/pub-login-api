import com.iuni.login.helper.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;


@Slf4j
class RedisCrudTest {
    final String KEY = "key";
    final String VALUE = "value";
    final Duration DURATION = Duration.ofMillis(5000);

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void shutDown() {
        redisService.setValues(KEY, VALUE, DURATION);
    }

    @AfterEach
    void tearDown() {
        redisService.deleteValues(KEY);
    }

    @Test
    @DisplayName("Redis에 데이터를 저장하면 정상적으로 조회된다.")
    void saveAndFindTest() throws Exception {
        // when
        String findValue = redisService.getValues(KEY);

        // then
        Assertions.assertThat(VALUE).isEqualTo(findValue);
    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 수정할 수 있다.")
    void updateTest() throws Exception {
        // given
        String updateValue = "updateValue";
        redisService.setValues(KEY, updateValue, DURATION);

        // when
        String findValue = redisService.getValues(KEY);

        // then
        Assertions.assertThat(updateValue).isEqualTo(findValue);
        Assertions.assertThat(VALUE).isNotEqualTo(findValue);
    }

    @Test
    @DisplayName("Redis에 저장된 데이터를 삭제할 수 있다.")
    void deleteTest() throws Exception {
        // when
        redisService.deleteValues(KEY);
        String findValue = redisService.getValues(KEY);

        // then
        Assertions.assertThat(findValue).isEqualTo("false");
    }

//    @Test
//    @DisplayName("Redis에 저장된 데이터는 만료시간이 지나면 삭제된다.")
//    void expiredTest() throws Exception {
//        // when
//        String findValue = redisService.getValues(KEY);
//        await().pollDelay(Duration.ofMillis(6000)).untilAsserted(
//                () -> {
//                    String expiredValue = redisService.getValues(KEY);
//                    Assertions.assertThat(expiredValue).isNotEqualTo(findValue);
//                    Assertions.assertThat(expiredValue).isEqualTo("false");
//                }
//        );
//    }
}
