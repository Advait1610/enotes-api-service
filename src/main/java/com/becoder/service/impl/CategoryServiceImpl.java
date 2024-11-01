package com.becoder.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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
		
		if(ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false); // Assumes this is a field in your Category entity
			category.setCreatedBy(1); // Set appropriate creator ID
			category.setCreatedOn(new Date()); // Set the creation date			
		}else {
			updateCategory(category);
		}
	    
	    
	    // Save the entity to the repository
	    Category saveCategory = categoryRepo.save(category);
	    if(ObjectUtils.isEmpty(saveCategory)) {
	    	return false;
	    }
	    
	    return true;
	}

	private void updateCategory(Category category) {
		Optional<Category> findById = categoryRepo.findById(category.getId());
		if(findById.isPresent()) {
			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());
			
			category.setUpdatedBy(1);
			category.setUpdatedOn(new Date());
		}
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepo.findByIsDeletedFalse();
		List<CategoryDto> categoryDtoList = categories.stream().map(cat->mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}
	
	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryResponse> categoryList = categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
		return categoryList;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) {
		Optional<Category> categoryById = categoryRepo.findByIdAndIsDeletedFalse(id);
		if(categoryById.isPresent()) {
			Category category = categoryById.get();
			return mapper.map(category, CategoryDto.class);
		}
		
		return null;
	}
	
	@Override
	public Boolean deleteCategory(Integer id) {
		Optional<Category> categoryById = categoryRepo.findById(id);
		if(categoryById.isPresent()) {
			Category category = categoryById.get();
			category.setIsDeleted(true);
			categoryRepo.save(category);
			return true;
		}
		
		return false;
	}

}
