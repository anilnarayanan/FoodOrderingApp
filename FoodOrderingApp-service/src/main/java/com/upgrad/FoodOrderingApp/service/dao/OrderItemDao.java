package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderItemDao {

    @PersistenceContext
    private EntityManager entityManager;


    //method to fetch items tied to an order
    public List<OrderItemEntity> getItemsByOrder(OrdersEntity ordersEntity) {
        try {
            return entityManager.createNamedQuery("itemsByOrder", OrderItemEntity.class).setParameter("orderId", ordersEntity.getId().toString()).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
