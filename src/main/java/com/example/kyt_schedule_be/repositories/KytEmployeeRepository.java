package com.example.kyt_schedule_be.repositories;

import com.example.kyt_schedule_be.models.KytEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KytEmployeeRepository extends JpaRepository<KytEmployee, Long> {

	List<KytEmployee> findByStatusOrderBySortNoAsc(String status);

	List<KytEmployee> findByFacAndStatusOrderBySortNoAsc(
			String fac,
			String status
	);
}