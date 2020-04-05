package cz.osu.swinz.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomReportRepository extends CrudRepository<RoomReport, Integer>
{
}
