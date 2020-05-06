package cz.osu.swinz;

import cz.osu.swinz.Controllers.SensorGroupController;
import cz.osu.swinz.database.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class SensorGroupControllerUnitTest
{
    private Room testRoom;

    @Resource
    SensorGroupController sens;

    @BeforeEach
    private void prepare()
    {
        try
        {
            Random rnd = new Random();
            String name = Integer.toString(rnd.nextInt());

            sens.addNewRoom(name);
            Iterable<Room> list = sens.getAllRooms();

            for (Room r : list)
            {
                if(r.getName() == name)
                    testRoom = r;
                r.setHeaterState(false);
                r.setForceHeater(false);
                r.setTargetTemperature(0);
            }
        }
        catch (Exception e)
        {
            System.err.println("Unable to add test data");
        }
    }

    @Test
    public void testSetRoomHeaterForced()
    {
        try
        {
            sens.setRoomHeaterForced(testRoom.getId(), true);
            if(!sens.getRoom(testRoom.getId()).isForceHeater())
                fail();
        }
        catch (Exception e)
        {
            fail();
        }
    }

    @Test
    public void testSetTargetTemperature()
    {
        try
        {
            sens.setRoomTargetTemperature(testRoom.getId(), 50);
            System.out.println("target: " + sens.getRoom(testRoom.getId()));
            if(sens.getRoom(testRoom.getId()).getTargetTemperature() != 50)
                fail();
        }
        catch (Exception e)
        {
            fail();
        }
    }

    @Test
    public void testAddRoom()
    {
        try
        {
            Random rnd = new Random();
            String name = Integer.toString(rnd.nextInt());

            sens.addNewRoom(name);

            int before = 0;
            int after = 0;
            boolean found = false;

            Iterable<Room> iter = sens.getAllRooms();
            for (Room r : iter)
            {
                before++;
            }

            sens.addNewRoom(name);

            iter = sens.getAllRooms();

            for (Room r : iter)
            {
                if(r.getName().equals(name))
                    found = true;

                after++;
            }

            if((before == after) && !found)
                fail();
        }
        catch (Exception e)
        {
            fail();
        }
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
