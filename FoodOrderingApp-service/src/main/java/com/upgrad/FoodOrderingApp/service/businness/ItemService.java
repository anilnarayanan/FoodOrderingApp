package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDAO;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemDAO itemDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getMostOrderedItems(String restaurantId) throws RestaurantNotFoundException {

        List<ItemEntity> itemList = itemDAO.getItemsByRestaurant(restaurantId);
        if (itemList == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        /*Methods to retrieve popular items based on the restaurant*/
            List<ItemEntity> itemEntityList = new ArrayList<>();
            for (OrdersEntity ordersEntity : orderDao.getOrdersByRestaurant(restaurantId)) {
                for (OrderItemEntity orderItemEntity : orderItemDao.getItemsByOrder(ordersEntity)) {
                    itemEntityList.add(orderItemEntity.getItem());
                }
            }
        return itemEntityList;
}
