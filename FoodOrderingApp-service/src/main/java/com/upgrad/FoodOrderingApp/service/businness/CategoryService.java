package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private RestaurantDAO restaurantDAO;
    @Autowired
    private RestaurantCategoryDAO restaurantCategoryDao;

    @Autowired
    private CategoryDAO categoryDAO;

    /* Method returns categories for a restaurant and returns list of categories. Its takes restaurantUuid as the input.
    If error throws exception with error code and error message.
    */
    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantUuid){

        //Calling getRestaurantByUuid to get Restaurant Detail
        RestaurantEntity restaurantDetail = restaurantDAO.getRestaurantByUuid(restaurantUuid);

        //Calling getCategoriesByRestaurant of restaurantCategoryDao to get list of restaurant category entities
        List<RestaurantCategoryEntity> restaurantCategoryDetails = restaurantCategoryDao.getCategoriesByRestaurant(restaurantDetail);

        //Creating the list of the Category Details to be returned.
        List<CategoryEntity> categoryList= new LinkedList<>();
        restaurantCategoryDetails.forEach(restaurantCategoryEntity -> {
            categoryList.add(restaurantCategoryEntity.getCategory());
        });
        return categoryList;
    }

    public List<CategoryEntity> getAllCategoriesOrderedByName(){

        List<CategoryEntity> categoryEntities = categoryDAO.getAllCategoriesOrderedByName();
        return categoryEntities;
    }

    public CategoryEntity getCategoryById(String categoryUUID) throws CategoryNotFoundException {
        if(categoryUUID == null || categoryUUID.isEmpty()){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        //Calls getCategoryByUuid of categoryDao to get CategoryEntity
        CategoryEntity categoryEntity = categoryDAO.getCategoryByUUId(categoryUUID);

        if(categoryEntity == null){ //Checking for categoryEntity to be null or empty to throw exception.
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        return categoryEntity;
    }

}
