package com.example.kyt_schedule_be.controllers;

import com.example.kyt_schedule_be.dtos.GenerateKytRequest;
import com.example.kyt_schedule_be.dtos.KytEmployeeRequest;
import com.example.kyt_schedule_be.dtos.KytScheduleResponse;
import com.example.kyt_schedule_be.models.KytEmployee;
import com.example.kyt_schedule_be.services.KytService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kyt")
@RequiredArgsConstructor
@CrossOrigin("*")
public class KytController {

	private final KytService kytService;

	@GetMapping("/employees")
	public List<KytEmployee> getEmployees(
			@RequestParam(defaultValue = "Fac_2") String fac
	) {
		return kytService.getEmployees(fac);
	}

	@PostMapping("/employees")
	public KytEmployee addEmployee(@RequestBody KytEmployeeRequest req) {
		return kytService.addEmployee(req);
	}

	@DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		kytService.deleteEmployee(id);
	}


	@GetMapping("/schedules/all")
	public List<KytScheduleResponse> getAllSchedules(
			@RequestParam(defaultValue = "Fac_2") String fac
	) {
		return kytService.getAllSchedules(fac);
	}

	@GetMapping("/schedules/latest")
	public List<KytScheduleResponse> getLatestSchedules(
			@RequestParam(defaultValue = "Fac_2") String fac
	) {
		return kytService.getLatestSchedules(fac);
	}

	@GetMapping("/schedules")
	public List<KytScheduleResponse> getSchedules(
			@RequestParam(defaultValue = "Fac_2") String fac,
			@RequestParam(required = false) Integer roundNo
	) {
		return kytService.getSchedulesByRoundNo(fac, roundNo);
	}

	@PostMapping("/generate")
	public List<KytScheduleResponse> generateSchedule(
			@RequestBody GenerateKytRequest req
	) {
		return kytService.generateSchedule(req);
	}
}