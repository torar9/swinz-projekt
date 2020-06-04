package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.House;
import cz.osu.swinz.database.HouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@TestPropertySource(properties = "app.scheduling.enable=false")
public class HomeControllerUnitTest
{
    @Resource
    private HomeController gce;

    @Mock
    @Autowired
    private HouseRepository houseRepo;

    @Test
    public void testSetHeaterTrue()
    {
        try
        {
            gce.setGlovalHeaterState(true);
            for(House h : houseRepo.findAll())
            {
                if(!h.isHeaterOn())
                    fail();
            }

            gce.setGlovalHeaterState(false);
            for(House h : houseRepo.findAll())
            {
                if(h.isHeaterOn())
                    fail();
            }
        }
        catch (Exception e)
        {
            fail();
        }
    }

    @Test
    public void testSetHeaterFalse()
    {
        try
        {
            gce.setGlovalHeaterState(false);
            for(House h : houseRepo.findAll())
            {
                if(h.isHeaterOn())
                    fail();
            }
        }
        catch (Exception e)
        {
            fail();
        }
    }

    @Test
    public void testSetGlobalTemp()
    {
        gce.setGlobalTemp(50.0);
        for(House h : houseRepo.findAll())
        {
            if(h.getTargetTemperature() != 50.0)
                fail();
        }
    }
}
