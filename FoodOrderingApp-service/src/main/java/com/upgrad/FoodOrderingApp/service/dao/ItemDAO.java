package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemByUUID(String uuid) {
        try {
            ItemEntity itemEntity = entityManager
                    .createNamedQuery("getItemByUUID", ItemEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
            return itemEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<ItemEntity> getTopItems(RestaurantEntity restaurantEntity, int top) {
        try {
            List<Object[]> items = entityManager.createQuery("select i, count(oi.itemId) as c " +
                            "from OrdersEntity o, OrderItemEntity oi, ItemEntity i" +
                            " where o.restaurant = :restaurant and o.uuid = oi.orderId group by  oi.itemId order by c desc",
                    Object[].class)
                    .setParameter("restaurant", restaurantEntity)
                    .setMaxResults(top)
                    .getResultList();

            List<ItemEntity> itemEntities = new ArrayList<>();

            for (Object[] objArray : items) {
                itemEntities.add((ItemEntity) objArray[0]);
            }

            return itemEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
