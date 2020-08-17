package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    //To get the list of RestaurantCategoryEntity from the db by restaurant
    public List<RestaurantCategoryEntity> getCategoriesByRestaurant(RestaurantEntity restaurantDetail) {
        try {
            List<RestaurantCategoryEntity> restaurantCategoryEntity = entityManager.createNamedQuery("getRestaurantByRestaurantEntity", RestaurantCategoryEntity.class).setParameter("restaurant", restaurantDetail).getResultList();
            return restaurantCategoryEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

    //To get the list of RestaurantCategoryEntity from the db by category
    public List<RestaurantCategoryEntity> getRestaurantsByCategory(CategoryEntity categoryDetail) {
        try {
            List<RestaurantCategoryEntity> restaurantCategoryEntity = entityManager.createNamedQuery("getRestaurantsByCategory",RestaurantCategoryEntity.class).setParameter("category",categoryDetail).getResultList();
            return restaurantCategoryEntity;
        }catch (NoResultException nre){
            return null;
        }
    }
}