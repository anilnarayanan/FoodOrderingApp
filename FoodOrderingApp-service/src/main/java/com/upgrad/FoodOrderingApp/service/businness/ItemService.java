package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Service
public class ItemService {

    @Autowired
    ItemDAO itemDao;

    @Autowired
    private RestaurantDAO restaurantDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private RestaurantItemDAO restaurantItemDAO;

    @Autowired
    private CategoryItemDAO categoryItemDAO;

    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {

        List<ItemEntity> itemEntities = itemDao.getTopItems(restaurantEntity, 5);

        return itemEntities;
    }

    /**
     * Fetch items by Category and restaurant
     *
     * @param restaurantUUID
     * @param categoryUUID
     * @return
     */
    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUUID, String categoryUUID) {

        RestaurantEntity restaurantEntity = restaurantDAO.getRestaurantByUuid(restaurantUUID);

        CategoryEntity categoryEntity = categoryDAO.getCategoryByUUId(categoryUUID);


        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDAO.getItemsByRestaurant(restaurantEntity);


        List<CategoryItemEntity> categoryItemEntities = categoryItemDAO.getItemsByCategory(categoryEntity);


        List<ItemEntity> itemEntities = new LinkedList<>();

        restaurantItemEntities.forEach(restaurantItemEntity -> {
            categoryItemEntities.forEach(categoryItemEntity -> {
                if (restaurantItemEntity.getItem().equals(categoryItemEntity.getItemId())) {
                    itemEntities.add(restaurantItemEntity.getItem());
                }
            });
        });
        return itemEntities;
    }

    /**
     * Fetch item by UUID
     *
     * @param itemUuid
     * @return
     * @throws ItemNotFoundException
     */
    public ItemEntity getItemByUUID(String itemUuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemByUUID(itemUuid);
        if (itemEntity == null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        } else {
            return itemEntity;
        }
    }
}