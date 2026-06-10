package com.example.kyt_schedule_be.services;

import com.example.kyt_schedule_be.dtos.GenerateKytRequest;
import com.example.kyt_schedule_be.dtos.KytEmployeeRequest;
import com.example.kyt_schedule_be.dtos.KytScheduleResponse;
import com.example.kyt_schedule_be.models.KytEmployee;
import com.example.kyt_schedule_be.models.KytSchedule;
import com.example.kyt_schedule_be.repositories.KytEmployeeRepository;
import com.example.kyt_schedule_be.repositories.KytScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KytService {

	private final KytEmployeeRepository employeeRepo;
	private final KytScheduleRepository scheduleRepo;

	public List<KytEmployee> getEmployees(String fac) {
		return employeeRepo.findByFacAndStatusOrderBySortNoAsc(
				fac,
				"ACTIVE"
		);
	}

	public KytEmployee addEmployee(KytEmployeeRequest req) {
		String fac = normalizeFac(req.getFac());

		int sortNo = employeeRepo
				.findByFacAndStatusOrderBySortNoAsc(fac, "ACTIVE")
				.size() + 1;

		KytEmployee emp = KytEmployee.builder()
				.fac(fac)
				.msnv(req.getMsnv())
				.name(req.getName())
				.groupName(req.getGroupName())
				.position(req.getPosition())
				.status("ACTIVE")
				.sortNo(sortNo)
				.createdAt(LocalDateTime.now())
				.build();

		return employeeRepo.save(emp);
	}

	public void deleteEmployee(Long id) {
		KytEmployee emp = employeeRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		emp.setStatus("INACTIVE");
		emp.setUpdatedAt(LocalDateTime.now());

		employeeRepo.save(emp);
	}

	public List<KytScheduleResponse> getLatestSchedules(String fac) {
		String finalFac = normalizeFac(fac);

		Integer latestRoundNo = scheduleRepo.getMaxRoundNoByFac(finalFac);

		if (latestRoundNo == null || latestRoundNo <= 0) {
			return List.of();
		}

		return getSchedulesByRoundNo(finalFac, latestRoundNo);
	}

	public List<KytScheduleResponse> getSchedulesByRoundNo(
			String fac,
			Integer roundNo
	) {
		String finalFac = normalizeFac(fac);

		if (roundNo == null || roundNo <= 0) {
			return getLatestSchedules(finalFac);
		}

		List<KytEmployee> employees =
				employeeRepo.findByFacAndStatusOrderBySortNoAsc(
						finalFac,
						"ACTIVE"
				);

		List<KytSchedule> schedules =
				scheduleRepo.findByFacAndRoundNoOrderByKytDateAscIdAsc(
						finalFac,
						roundNo
				);

		return schedules.stream()
				.map(s -> toScheduleResponse(s, employees))
				.toList();
	}

	public List<KytScheduleResponse> getAllSchedules(String fac) {
		String finalFac = normalizeFac(fac);

		List<KytEmployee> employees =
				employeeRepo.findByFacAndStatusOrderBySortNoAsc(
						finalFac,
						"ACTIVE"
				);

		List<KytSchedule> schedules =
				scheduleRepo.findByFacOrderByRoundNoDescKytDateAscIdAsc(finalFac);

		return schedules.stream()
				.map(s -> toScheduleResponse(s, employees))
				.toList();
	}

	@Transactional
	public List<KytScheduleResponse> generateSchedule(GenerateKytRequest req) {
		String fac = normalizeFac(req.getFac());

		Integer maxRoundNo = scheduleRepo.getMaxRoundNoByFac(fac);
		int nextRoundNo = maxRoundNo == null ? 1 : maxRoundNo + 1;

		List<KytEmployee> employees =
				employeeRepo.findByFacAndStatusOrderBySortNoAsc(
						fac,
						"ACTIVE"
				);

		LocalDate currentDate = moveToNextKytDay(req.getStartDate());

		for (KytEmployee emp : employees) {
			KytSchedule row = KytSchedule.builder()
					.fac(fac)
					.employeeId(emp.getId())
					.roundNo(nextRoundNo)
					.kytDate(currentDate)
					.createdAt(LocalDateTime.now())
					.build();

			scheduleRepo.save(row);

			currentDate = moveToNextKytDay(currentDate.plusDays(1));
		}

		return getSchedulesByRoundNo(fac, nextRoundNo);
	}

	private KytScheduleResponse toScheduleResponse(
			KytSchedule s,
			List<KytEmployee> employees
	) {
		KytEmployee emp = employees.stream()
				.filter(e -> e.getId().equals(s.getEmployeeId()))
				.findFirst()
				.orElse(null);

		Integer roundNo = s.getRoundNo();

		return KytScheduleResponse.builder()
				.id(s.getId())
				.fac(s.getFac())
				.employeeId(s.getEmployeeId())
				.msnv(emp != null ? emp.getMsnv() : "")
				.name(emp != null ? emp.getName() : "")
				.groupName(emp != null ? emp.getGroupName() : "")
				.position(emp != null ? emp.getPosition() : "")
				.roundNo(roundNo)
				.roundName(roundNo == null ? "" : "Round " + roundNo)
				.kytDate(s.getKytDate())
				.build();
	}

	private String normalizeFac(String fac) {
		if (fac == null || fac.isBlank()) {
			return "Fac_2";
		}

		return fac.trim();
	}

	private boolean isKytDay(LocalDate date) {
		DayOfWeek day = date.getDayOfWeek();

		return day == DayOfWeek.TUESDAY ||
				day == DayOfWeek.WEDNESDAY ||
				day == DayOfWeek.THURSDAY ||
				day == DayOfWeek.FRIDAY;
	}

	private LocalDate moveToNextKytDay(LocalDate date) {
		LocalDate d = date;

		while (!isKytDay(d)) {
			d = d.plusDays(1);
		}

		return d;
	}


}