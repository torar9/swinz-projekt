package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.Room;
import cz.osu.swinz.database.RoomRepository;
import cz.osu.swinz.home.GroupReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class SensorGroupControllerTest
{
    @Resource
    private SensorGroupController sens;

    @Mock
    @Autowired
    private RoomRepository roomRepo;

    @Test
    void testInvalidParamNewRoom()
    {
        ResponseEntity<Boolean> resp = sens.addNewRoom("");

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testNewRoom()
    {
        long count = roomRepo.count();
        ResponseEntity<Boolean> resp = sens.addNewRoom("test");

        if(resp.getStatusCode() != HttpStatus.OK)
            fail();

        if(roomRepo.count() <= count)
            fail();
    }

    @Test
    void testInvalidParamRemoveRoom()
    {
        ResponseEntity<Boolean> resp = sens.removeRoom(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamGetRoom()
    {
        ResponseEntity<Room> resp = sens.getRoom(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamGetRoomReport()
    {
        ResponseEntity<GroupReport> resp = sens.getRoomReport(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamGetRoomTemperature()
    {
        ResponseEntity<Double> resp = sens.getRoomTemperature(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamGetRoomTargetTemperature()
    {
        ResponseEntity<Double> resp = sens.getRoomTargetTemperature(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamSetRoomTargetTemperature()
    {
        ResponseEntity<Boolean> resp = sens.setRoomTargetTemperature(-1, 0.0);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamGetRoomHeater()
    {
        ResponseEntity<Boolean> resp = sens.getRoomHeaterState(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamSetRoomHeaterForced()
    {
        ResponseEntity<Boolean> resp = sens.setRoomHeaterForcedState(-1, true);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamGetRoomHeaterForce()
    {
        ResponseEntity<Boolean> resp = sens.getRoomHeaterForcedState(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }

    @Test
    void testInvalidParamGetRoomPowerConsumption()
    {
        ResponseEntity<Double> resp = sens.getRoomPowerConsumption(-1);

        if(resp.getStatusCode() == HttpStatus.OK)
            fail();
    }
}