package cz.osu.swinz.home;

import cz.osu.swinz.database.Room;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomStatsGenerator
{
    //@PersistenceContext
    private EntityManager ent;
    private Map<Integer, String> months;

    public RoomStatsGenerator(EntityManager ent)
    {
        this.ent = ent;
        months = new HashMap<>();
        months.put(1, "leden");
        months.put(2, "únor");
        months.put(3, "březen");
        months.put(4, "duben");
        months.put(5, "květen");
        months.put(6, "červen");
        months.put(7, "červenec");
        months.put(8, "srpen");
        months.put(9, "září");
        months.put(10, "říjen");
        months.put(11, "listopad");
        months.put(12, "prosinec");
    }

    public double getAverageLightTwoWeeks(Room room)
    {
        BigDecimal averageLightTwoWeeks = (BigDecimal) ent.createNativeQuery("select avg(Count)\n" +
                "from(select count(*) as Count\n" +
                "from room_reports\n" +
                "where report_date between (now() - INTERVAL 14 day) and now() and room_id = " + room.getId() + " and is_light_on = 1\n" +
                "group by day(report_date)) as Counts;").getResultList().get(0);

        if(averageLightTwoWeeks == null)
            averageLightTwoWeeks = new BigDecimal(0);

        return averageLightTwoWeeks.doubleValue();
    }

    public int getHeatDaysInYear()
    {
        BigInteger heatDays = (BigInteger) ent.createNativeQuery("select count(distinct day(report_date))\n" +
                "        from room_reports\n" +
                "        where report_date >= (sysdate() - interval 1 year) and is_heater_on = 1;").getResultList().get(0);

        return heatDays.intValue();
    }

    public List<RoomStats> getMonthStats(Iterable<Room> rooms)
    {
        ArrayList<RoomStats> globalStats = new ArrayList<>();
        ArrayList<Integer> listOfMonths = new ArrayList<>();

        List availableMonths = ent.createNativeQuery("select distinct month(report_date) from room_reports;").getResultList();

        for(int i = 0; i < availableMonths.size(); i++)
        {
            Integer month = (Integer)availableMonths.get(i);
            listOfMonths.add(month);
        }

        for(Integer month : listOfMonths)
        {
            ArrayList<RoomMonthStatistics> resultList = new ArrayList();

            for(Room room : rooms)
            {

                BigInteger heaterMonth = (BigInteger) ent.createNativeQuery("select count(distinct day(report_date))\n" +
                        "from room_reports\n" +
                        "where month(report_date) = " + month.intValue() + " and is_heater_on = 1 and room_id = " + room.getId() + ";").getResultList().get(0);
                if(heaterMonth == null)
                    heaterMonth = new BigInteger("1");

                BigDecimal averageLight = (BigDecimal) ent.createNativeQuery("select avg(Count)\n" +
                        "from(select count(*) as Count\n" +
                        "from room_reports\n" +
                        "where month(report_date) = " + month.intValue() + " and room_id = " + room.getId() + " and is_light_on = 1\n" +
                        "group by day(report_date)) as Counts;").getResultList().get(0);
                if(averageLight == null)
                    averageLight = new BigDecimal(0);
                averageLight = averageLight.setScale(2, BigDecimal.ROUND_HALF_UP);

                if(averageLight == null)
                    averageLight = new BigDecimal(0);

                Double power = (Double) ent.createNativeQuery("select sum(dny)\n" +
                        "from (\n" +
                        "    select avg(power_consumption) as dny\n" +
                        "    from room_reports\n" +
                        "    where month(report_date) = " + month + " and room_id = " + room.getId() + "\n" +
                        "    group by day(report_date)\n" +
                        ") soucet;").getResultList().get(0);

                if(power == null)
                    power = new Double(0);

                resultList.add(new RoomMonthStatistics(
                        room.getName(),
                        heaterMonth,
                        averageLight,
                        new BigDecimal(power).setScale(2, BigDecimal.ROUND_HALF_UP)
                ));
            }
            globalStats.add(new RoomStats(months.get(month), resultList));
        }

        return globalStats;
    }
/*
    public List<RoomMonthStatistics> getRoomStats(Room room)
    {
        ArrayList<RoomMonthStatistics> resultList = new ArrayList();
        ArrayList<Integer> listOfMonths = new ArrayList<>();

        List availableMonths = ent.createNativeQuery("select distinct month(report_date) from room_reports;").getResultList();

        if(availableMonths.isEmpty())
            return null;

        for(int i = 0; i < availableMonths.size(); i++)
        {
            Integer month = (Integer)availableMonths.get(i);
            listOfMonths.add(month);
        }

        for (Integer month : listOfMonths)
        {
            //Object averageConsumption = ent.createNativeQuery("Select avg(power_consumption) from room_reports").getResultList().get(0);

            BigInteger heaterMonth = (BigInteger) ent.createNativeQuery("select count(distinct day(report_date))\n" +
                    "from room_reports\n" +
                    "where month(report_date) = " + month.intValue() + " and is_heater_on = 1 and room_id = " + room.getId() + ";").getResultList().get(0);
            if(heaterMonth == null)
                heaterMonth = new BigInteger("1");

            BigDecimal averageLight = (BigDecimal) ent.createNativeQuery("select avg(Count)\n" +
                    "from(select count(*) as Count\n" +
                    "from room_reports\n" +
                    "where month(report_date) = " + month.intValue() + " and room_id = " + room.getId() + " and is_light_on = 1\n" +
                    "group by day(report_date)) as Counts;").getResultList().get(0);
            if(averageLight == null)
                averageLight = new BigDecimal(0);
            averageLight = averageLight.setScale(2, BigDecimal.ROUND_HALF_UP);

            if(averageLight == null)
                averageLight = new BigDecimal(0);

            Double power = (Double) ent.createNativeQuery("select sum(dny)\n" +
                    "from (\n" +
                    "    select avg(power_consumption) as dny\n" +
                    "    from room_reports\n" +
                    "    where month(report_date) = " + month + " and room_id = " + room.getId() + "\n" +
                    "    group by day(report_date)\n" +
                    ") soucet;").getResultList().get(0);

            if(power == null)
                power = new Double(0);

            resultList.add(new RoomMonthStatistics(
                    room.getName(),
                    months.get(month),
                    heaterMonth,
                    averageLight,
                    new BigDecimal(power).setScale(2, BigDecimal.ROUND_HALF_UP)
                    ));
        }

        return resultList;
    }*/
}
