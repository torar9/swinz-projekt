package cz.osu.swinz;

import cz.osu.swinz.Controllers.StatisticsController;
import cz.osu.swinz.database.HouseRepository;
import cz.osu.swinz.database.RoomRepository;
import cz.osu.swinz.home.RoomStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class StatisticsControllerUnitTest
{
    @Resource
    private StatisticsController ste;

    @Mock
    @Autowired
    private HouseRepository houseRepo;

    @Mock
    @Autowired
    private RoomRepository roomRepo;
}
