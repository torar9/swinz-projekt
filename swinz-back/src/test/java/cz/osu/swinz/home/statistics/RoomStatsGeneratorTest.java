package cz.osu.swinz.home.statistics;

import cz.osu.swinz.database.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@TestPropertySource(properties = "app.scheduling.enable=false")
class RoomStatsGeneratorTest
{
    @Mock
    @PersistenceContext
    private EntityManager ent;
    @Mock
    @Autowired
    private RoomRepository roomRepo;

    @Mock
    private RoomStatsGenerator gen;

    @BeforeEach
    protected void setUp()
    {
        gen = new RoomStatsGenerator(ent);
    }

    @Test
    void getAverageLightTwoWeeks()
    {
        double expected = 33.2;
        double actual = gen.getAverageLightTwoWeeks(roomRepo.findById(1).get());

        assertEquals(expected, actual, 0.1);
    }

    @Test
    void getHeatDaysInYear()
    {
        int expected = 16;
        int actual = gen.getHeatDaysInYear();

        assertEquals(expected, actual);
    }

    @Test
    void getMonthStats()
    {
        RoomStats stats = gen.getMonthStats(roomRepo.findAll()).get(0);
        System.out.println(stats);

        if(stats.getList().size() != 4)
            fail();

        if(!stats.getMonthName().equals("březen"))
            fail();

        RoomMonthStatistics r1 = stats.getList().get(0);
        RoomMonthStatistics r2 = stats.getList().get(1);
        RoomMonthStatistics r3 = stats.getList().get(2);

        if(!r1.getRoomName().equals("Obývák"))
            fail();
        if(r1.getAverageDayLight().doubleValue() != 34.67)
            fail();
        if(r1.getDaysHeaterOn().intValue() != 1)
            fail();
        if(r1.getPowerConsumption().doubleValue() != 2.06)
            fail();

        if(!r2.getRoomName().equals("Ložnice"))
            fail();
        if(r2.getAverageDayLight().doubleValue() != 2.0)
            fail();
        if(r2.getDaysHeaterOn().intValue() != 0)
            fail();
        if(r2.getPowerConsumption().doubleValue() != 0.69)
            fail();

        if(!r3.getRoomName().equals("Dětský pokoj"))
            fail();
        if(r3.getAverageDayLight().doubleValue() != 1.0)
            fail();
        if(r3.getDaysHeaterOn().intValue() != 0)
            fail();
        if(r3.getPowerConsumption().doubleValue() != 0.81)
            fail();
    }
}