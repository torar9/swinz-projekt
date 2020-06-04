package cz.osu.swinz.home.statistics;

import cz.osu.swinz.database.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional
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
        double expected = 34.6;
        double actual = gen.getAverageLightTwoWeeks(roomRepo.findById(1).get());

        assertEquals(expected, actual);
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
        List<RoomStats> stats = gen.getMonthStats(roomRepo.findAll());

        for(RoomStats e : stats)
        {
            System.out.println(e);
        }
        fail();
    }
}