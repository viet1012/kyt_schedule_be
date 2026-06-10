package com.example.kyt_schedule_be.dtos;

import lombok.Data;
import java.time.LocalDate;


@Data
public class GenerateKytRequest {
	private String fac;
	private LocalDate startDate;
}