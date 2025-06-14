package ua.nure.kryvko.roman.apz.greenhouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.nure.kryvko.roman.apz.user.User;

import java.util.List;
import java.util.Optional;

public interface GreenhouseRepository extends JpaRepository<Greenhouse, Integer> {
    List<Greenhouse> findByUser(User user);

    @Query("SELECT new ua.nure.kryvko.roman.apz.greenhouse.GreenhouseSummary(g.id, g.user.id, g.createdAt, g.name, g.latitude, g.longitude, COUNT(s.id)) " +
            "FROM Greenhouse g " +
            "JOIN g.sensors s " +
            "WHERE g.user.id = :id " +
            "GROUP BY g.id, g.user.id, g.createdAt, g.name, g.latitude, g.longitude")
    List<GreenhouseSummary> findSummaryByUserId(Integer id);

    @Query("SELECT new ua.nure.kryvko.roman.apz.greenhouse.GreenhouseSummary(g.id, g.user.id, g.createdAt, g.name, g.latitude, g.longitude, COUNT(s.id)) " +
            "FROM Greenhouse g " +
            "JOIN g.sensors s " +
            "WHERE g.id = :id " +
            "GROUP BY g.id, g.user.id, g.createdAt, g.name, g.latitude, g.longitude")
    Optional<GreenhouseSummary> findSummaryById(@Param("id") Integer id);
}
