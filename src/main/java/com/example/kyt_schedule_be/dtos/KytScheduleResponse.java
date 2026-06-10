package com.example.kyt_schedule_be.dtos;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class KytScheduleResponse {
	private Long id;
	private String fac;
	private Long employeeId;
	private String msnv;
	private String name;
	private String groupName;
	private String position;
	private Integer roundNo;
	private String roundName;
	private LocalDate kytDate;
}
