package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDAO {
    @PersistenceContext
    private EntityManager entityManager;

    //Method that returns the restaurants list based on their rating (high to low)
    public List<RestaurantEntity> getAllRestaurantsByRating() {
        try {
            return entityManager.createNamedQuery("getAllRestaurantsByRating", RestaurantEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Method that returns the restaurant details for the required uuid of restaurant
    public RestaurantEntity getRestaurantByUuid(String uuid) {
        try {
            RestaurantEntity restaurantDetail = entityManager.createNamedQuery("getRestaurantByUuid", RestaurantEntity.class).setParameter("uuid", uuid).getSingleResult();
            return restaurantDetail;
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Method that returns the restaurants list by the entered name
    public List<RestaurantEntity> getRestaurantsByName(String restaurantSearchName) {
        try {
            String restaurantName = "%" + restaurantSearchName.toLowerCase() + "%"; // to make a check with lower
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("getRestaurantsByName", RestaurantEntity.class).setParameter("restaurantName", restaurantName).getResultList();
            return restaurantEntities;
        } catch (NoResultException nre) {
            return null;
        }

    }

    //Update modifying restaurant details in DB
    public RestaurantEntity updateRestaurantEntity(RestaurantEntity restaurantEntity) {
        return entityManager.merge(restaurantEntity);
    }
}
