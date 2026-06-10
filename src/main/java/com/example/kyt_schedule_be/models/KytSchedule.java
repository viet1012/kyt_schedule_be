package com.example.kyt_schedule_be.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "HSE_KYT_SCHEDULE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KytSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fac")
	private String fac;

	@Column(name = "employee_id")
	private Long employeeId;

	@Column(name = "round_no")
	private Integer roundNo;

	@Column(name = "round_name")
	private String roundName;

	@Column(name = "kyt_date")
	private LocalDate kytDate;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
}