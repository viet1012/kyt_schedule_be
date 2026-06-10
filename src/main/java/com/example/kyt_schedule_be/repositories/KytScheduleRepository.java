package com.example.kyt_schedule_be.repositories;

import com.example.kyt_schedule_be.models.KytSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KytScheduleRepository extends JpaRepository<KytSchedule, Long> {

	List<KytSchedule> findByFacOrderByRoundNoDescKytDateAscIdAsc(String fac);

	@Query("""
			    SELECT COALESCE(MAX(k.roundNo), 0)
			    FROM KytSchedule k
			    WHERE k.fac = :fac
			""")
	Integer getMaxRoundNoByFac(@Param("fac") String fac);

	List<KytSchedule> findByFacAndRoundNoOrderByKytDateAscIdAsc(
			String fac,
			Integer roundNo
	);
}