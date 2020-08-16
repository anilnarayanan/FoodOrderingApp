package com.upgrad.FoodOrderingApp.service.dao;

import java.util.List;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDAO {
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * method to retrieve the list of  questions of a user from database
     *
     * @param restaurantId  restaurantId
     * @return  returns list of all items served by the restaurant
     */
    public List<RestaurantEntity> getItemsByRestaurant(final String restaurantId) {
        try {
            return entityManager.createNamedQuery("getItemsByRestaurentId", RestaurantEntity.class).setParameter("restaurant", restaurantId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
