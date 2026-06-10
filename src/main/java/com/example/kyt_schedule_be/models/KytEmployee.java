package com.example.kyt_schedule_be.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HSE_KYT_EMPLOYEE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KytEmployee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fac")
	private String fac;

	private String msnv;

	private String name;

	@Column(name = "group_name")
	private String groupName;

	private String position;

	private String status;

	@Column(name = "sort_no")
	private Integer sortNo;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}