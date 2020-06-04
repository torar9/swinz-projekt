package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.Room;
import cz.osu.swinz.database.RoomReport;
import cz.osu.swinz.database.RoomReportRepository;
import cz.osu.swinz.database.RoomRepository;
import cz.osu.swinz.home.sensors.LightSensor;
import cz.osu.swinz.home.sensors.PowerConsumptionSensor;
import cz.osu.swinz.home.sensors.TemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@EnableScheduling
@RestController
public class DatabaseController
{
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private RoomReportRepository reportRepo;

    @Scheduled(fixedDelay=60000)//Automaticke ukladani databaze po x milisekundách
    private void saveReports()
    {
        for(Room e : roomRepo.findAll())
        {
            RoomReport rep = new RoomReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), e.getHeaterState(), LightSensor.isLightOn(), e);
            reportRepo.save(rep);
        }
    }
}
