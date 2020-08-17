package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDAO;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemService {

    @Autowired
    ItemDAO itemDao;

    /**
     *
     * @param restaurantEntity
     * @return
     */
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {

        List<ItemEntity> itemEntities = itemDao.getTopItems(restaurantEntity, 5);

        return itemEntities;
    }

    /**
     * Fetch item by UUID
     * @param itemUuid
     * @return
     * @throws ItemNotFoundException
     */
    public ItemEntity getItemByUUID(String itemUuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemByUUID(itemUuid);
        if(itemEntity == null){
            throw new ItemNotFoundException("INF-003","No item by this id exist");
        } else {
            return itemEntity;
        }
    }
}