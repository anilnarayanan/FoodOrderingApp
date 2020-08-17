package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
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
        List<OrdersEntity> ordersEntities;
        try {
            ordersEntities = entityManager.createNamedQuery("getOrdersByAddress", OrdersEntity.class)
                    .setParameter("address", addressEntity)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
        return ordersEntities;
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

    /**
     * Persist order to the DB
     *
     * @param ordersEntity
     * @return
     */
    public OrdersEntity saveOrder(OrdersEntity ordersEntity) {
        entityManager.persist(ordersEntity);
        return ordersEntity;
    }
}
