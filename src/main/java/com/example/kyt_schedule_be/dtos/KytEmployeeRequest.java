package com.example.kyt_schedule_be.dtos;


import lombok.Data;

@Data
public class KytEmployeeRequest {
	private String fac;
	private String msnv;
	private String name;
	private String groupName;
	private String position;
}