package com.example.user.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

	private Long id;
	private String username;
	private String password;
	private Integer role;
}
