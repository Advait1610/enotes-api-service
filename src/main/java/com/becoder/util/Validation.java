package com.becoder.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;

import com.becoder.exception.ValidationException;


@Component
public class Validation {
	public void categoryValidation(CategoryDto categoryDto) {

		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
//			throw new IllegalArgumentException("Category Object/JSON should not be null or empty");	
		} else {

			// validation name field
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name", "name field is empty or null");
			} else {
				if (categoryDto.getName().length() < 10) {
					error.put("name", "name length minimum 10 required");
				}
				if (categoryDto.getName().length() > 100) {
					error.put("name", "name length maximum 100");
				}
			}

			// validation description
			if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("Description", "description field is empty or null");
			}

			// validation isActive
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive", "isActive field is empty or null");
			} else {
				if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive().booleanValue() != Boolean.FALSE.booleanValue()) {
					error.put("IsActive", "Invalid IsActive field");
				}
			}
		}
		
		if (!error.isEmpty()) {
			throw new ValidationException(error);
		} 
		
	}
}
