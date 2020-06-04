package cz.osu.swinz.Controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@TestPropertySource(properties = "app.scheduling.enable=false")
public class StatisticsControllerUnitTest
{
    @Resource
    private StatisticsController stat;

    @Test
    public void testInvalidParamGetMonthStats()
    {
        ResponseEntity<Double> resp = stat.getMonthLightStats(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }
}
