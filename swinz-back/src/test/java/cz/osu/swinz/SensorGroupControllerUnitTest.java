package cz.osu.swinz;

import cz.osu.swinz.database.Room;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SensorGroupControllerUnitTest
{
    @InjectMocks
    @Resource
    SensorGroupController sens;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void  testGetInvalidRoom()
    {
        try
        {
            Room room = sens.getRoom(-1);
            fail();
        }
        catch(Exception e)
        {
        }
    }

    @Test
    public void testGetRoom()
    {
        try
        {
            Room room = sens.getRoom(1);
            if(room.getId() != 1)
                fail();
        }
        catch (Exception e)
        {
            fail();
        }
    }
}
