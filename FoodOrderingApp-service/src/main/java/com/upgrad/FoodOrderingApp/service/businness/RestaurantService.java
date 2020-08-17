package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDAO;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDAO;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
    public List<RestaurantEntity> restaurantsByRating(){
        List<RestaurantEntity> restaurantListByRating = restaurantDAO.getAllRestaurantsByRating();
        return restaurantListByRating;
    }

    //Method to get restaurants list by Name entered by user. If restaurant name field entered by the customer is empty, throw RestaurantNotFoundException
    public List<RestaurantEntity> restaurantsByName(String restaurantName)throws RestaurantNotFoundException{
        if(restaurantName == null || restaurantName.isEmpty()){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }
        List<RestaurantEntity> restaurantNamesList = restaurantDAO.getRestaurantsByName(restaurantName);
        return restaurantNamesList;
    }

    //Method to get restaurants list by Name based on category id
    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {

        if(categoryId == null || categoryId.isEmpty()){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }

        CategoryEntity categoryEntity = categoryDAO.getCategoryByUUId(categoryId);

        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
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

    public RestaurantEntity restaurantByUUID(String restaurantUUID)throws RestaurantNotFoundException{
        if(restaurantUUID == null||restaurantUUID.isEmpty()){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDAO.getRestaurantByUuid(restaurantUUID);

        if (restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double newRating) throws InvalidRatingException {

        //Allowing ratings value only if it is or between 1.0 and 5.0
        if (newRating < 1.0 || newRating > 5.0) {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }

        //Re-calculating average rating including the new rating
        //Also updating number of customers ratings
        Double newAverageRating = (
                ((restaurantEntity.getNumberCustomersRated()*restaurantEntity.getCustomerRating())+newRating)/
                        (restaurantEntity.getNumberCustomersRated()+1));
        restaurantEntity.setNumberCustomersRated(restaurantEntity.getNumberCustomersRated() + 1);

        restaurantEntity.setCustomerRating(newAverageRating);
        return restaurantDAO.updateRestaurantEntity(restaurantEntity);
    }
}
