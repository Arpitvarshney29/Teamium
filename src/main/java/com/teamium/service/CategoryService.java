package com.teamium.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.teamium.constants.Constants;
import com.teamium.domain.Category;
import com.teamium.domain.prod.Record;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.CategoryRepository;

/**
 * Service to manage category configuration
 * 
 * @author Teamium
 *
 */
@Service
public class CategoryService {

	private AuthenticationService authenticationService;
	private CategoryRepository categoryRepository;

	@Autowired
	@Lazy
	private RecordService recordService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public CategoryService(CategoryRepository categoryRepository, AuthenticationService authenticationService) {
		this.categoryRepository = categoryRepository;
		this.authenticationService = authenticationService;
	}

	/**
	 * To save or update category
	 * 
	 * @param category
	 * 
	 * @return category object
	 */
	public Category saveOrUpdateCategory(Category category) {
		logger.info(" Inside CategoryService:: saveOrUpdateCategory() , To save or update category , category : "
				+ category);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to save/update category.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		if (category.getId() != null && category.getId() != 0) {
			this.getCategoryById(category.getId());
		}
		if (StringUtils.isBlank(category.getName())) {
			logger.info("Please provide a valid category");
			throw new UnprocessableEntityException("Please provide a valid category");
		}

		Category validateCategory = categoryRepository.findByNameIgnoreCase(category.getName());
		if (category.getId() == null && validateCategory != null) {
			logger.info("Category already exist with given name : " + category.getName());
			throw new UnprocessableEntityException("Category already exist with given name");
		}
		if (category.getId() != null && validateCategory != null
				&& (category.getId().longValue() != validateCategory.getId().longValue())) {
			logger.info("Category already exist with given name : " + category.getName());
			throw new UnprocessableEntityException("Category already exist with given name");
		}
		logger.info("Saving category with name : " + category.getName());
		return categoryRepository.save(category);
	}

	/**
	 * Method to get category by id
	 * 
	 * @param id
	 * 
	 * @return the category object
	 */
	public Category getCategoryById(Long id) {
		logger.info(" Inside CategoryService:: getCategoryById() , To get category by id, categoryId : " + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get category by id.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Category category = categoryRepository.findOne(id);
		if (category == null) {
			logger.info("Category not found with id : " + id);
			throw new NotFoundException("Category not found");
		}
		return category;
	}

	/**
	 * To get list of category.
	 * 
	 * @return list of category
	 */
	public List<Category> getAllCategory() {
		logger.info(" Inside CategoryService:: getAllCategory() , To get list of all categorys");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get all category.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		return categoryRepository.getAllCategory();
	}

	/**
	 * Method to get all category name
	 * 
	 * @return list of category name
	 */
	public List<String> getAllCategoryName() {
		logger.info(" Inside CategoryService:: getAllCategoryName() , To get list of all categorys name");
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to get all category names.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		return categoryRepository.getAllCategoryName();
	}

	/**
	 * To delete category by id.
	 * 
	 * @param id
	 */
	public void deleteCategory(long id) {
		logger.info(" Inside CategoryService:: deleteCategory() , To delete category by id : " + id);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to delete category.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Category category = this.getCategoryById(id);
		List<Record> list = recordService.getRecordByCategory(category);
		if (list != null && !list.isEmpty()) {
			logger.info("Cannot delete project-category as it is already assigned on record");
			throw new UnprocessableEntityException(
					"Cannot delete project-category as it is already assigned on record");
		}
		categoryRepository.delete(id);
		logger.info("Successfully deleted");
	}

	/**
	 * Method to get Category by name by IgnoreCase
	 * 
	 * @param name
	 * 
	 * @return the category object
	 */
	public Category findCategoryByName(String name) {
		logger.info("Inside CategoryService :: findCategoryByName(), To get category by name : " + name);
		if (!authenticationService.isAdmin()) {
			logger.warn("An invalid user:" + authenticationService.getAuthenticatedUser().getFirstName()
					+ " tried to find category by name.");
			throw new UnprocessableEntityException(Constants.INVALID_ROLE_MESSAGE);
		}
		Category category = categoryRepository.findByNameIgnoreCase(name);
		logger.info("Returning after finding category by name");
		return category;
	}

}
