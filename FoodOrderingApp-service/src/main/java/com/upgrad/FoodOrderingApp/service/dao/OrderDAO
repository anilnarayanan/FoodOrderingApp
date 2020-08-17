package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDAO {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param addressEntity
     * @return
     */
    public List<OrdersEntity> getOrdersByAddress(AddressEntity addressEntity) {
        try {
            List<OrdersEntity> ordersEntities = entityManager.createNamedQuery("getOrdersByAddress", OrdersEntity.class)
                    .setParameter("address", addressEntity).getResultList();
            return ordersEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * @param customerEntity
     * @return
     */
    public List<OrdersEntity> getOrdersByCustomers(CustomerEntity customerEntity) {
        List<OrdersEntity> ordersEntityList;
        try {
            ordersEntityList = entityManager.createNamedQuery("getOrdersByCustomers", OrdersEntity.class)
                    .setParameter("customer", customerEntity)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
        return ordersEntityList;
    }

    /* Method to fetch Orders based on restaurant object */
    /**
     * @param restaurantEntity
     * @return
     */
    public List<OrderEntity> getOrdersByRestaurant(String restaurantUuId) {

        try {
            return entityManager.createNamedQuery("ordersByRestaurant", OrdersEntity.class).setParameter("restaurant", restaurantUuId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
