package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDAO restaurantDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private RestaurantCategoryDAO restaurantCategoryDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    //Method to get restaurants list by rating
    public List<RestaurantEntity> getAllRestaurantsByRating() {
        List<RestaurantEntity> restaurantListByRating = restaurantDAO.getAllRestaurantsByRating();
        return restaurantListByRating;
    }

    //Method to get restaurants list by Name entered by user. If restaurant name field entered by the customer is empty, throw RestaurantNotFoundException
    public List<RestaurantEntity> getRestaurantsByName(String restaurantName) throws RestaurantNotFoundException {
        if (restaurantName == null || restaurantName.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        List<RestaurantEntity> restaurantNamesList = restaurantDAO.getRestaurantsByName(restaurantName);
        return restaurantNamesList;
    }

    //Method to get restaurants list by Name based on category id
    public List<RestaurantEntity> getRestaurantsByCategory(String categoryId) throws CategoryNotFoundException {

        if (categoryId == null || categoryId.isEmpty()) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        CategoryEntity categoryEntity = categoryDAO.getCategoryByUUId(categoryId);

        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        //Calls getRestaurantsByCategory of restaurantCategoryDao to get list of RestaurantCategoryEntity
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDAO.getRestaurantsByCategory(categoryEntity);

        //Creating new restaurantEntity List and add only the restaurant for the corresponding category.
        List<RestaurantEntity> restaurantEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            restaurantEntities.add(restaurantCategoryEntity.getRestaurant());
        });
        return restaurantEntities;
    }

    /**
     * @param restaurantId
     * @return
     * @throws RestaurantNotFoundException
     */
    public RestaurantEntity restaurantByUUID(String restaurantId) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity;
        if (restaurantId == null || restaurantId.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        restaurantEntity = restaurantDAO.getRestaurantByUuid(restaurantId);
        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        } else {
            return restaurantEntity;
        }
    }
}
