package com.becoder.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.repository.CategoryRepository;
import com.becoder.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
		
//		Category category = new Category();
//	    category.setName(categoryDto.getName());
//	    category.setDescription(categoryDto.getDescription());
//	    category.setIsActive(categoryDto.getIsActive());
		Category category = mapper.map(categoryDto, Category.class);
	    
	    category.setIsDeleted(false); // Assumes this is a field in your Category entity
	    category.setCreatedBy(1); // Set appropriate creator ID
	    category.setCreatedOn(new Date()); // Set the creation date
	    
	    // Save the entity to the repository
	    Category saveCategory = categoryRepo.save(category);
	    if(ObjectUtils.isEmpty(saveCategory)) {
	    	return false;
	    }
	    
	    return true;
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepo.findAll();
		List<CategoryDto> categoryDtoList = categories.stream().map(cat->mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}
	
	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepo.findByIsActiveTrue();
		List<CategoryResponse> categoryList = categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
		return categoryList;
	}

}
