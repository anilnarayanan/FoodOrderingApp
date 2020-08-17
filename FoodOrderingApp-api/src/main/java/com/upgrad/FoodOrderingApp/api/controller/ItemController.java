package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemEntity ItemEntity;

    // Method To Get Top 5 Items by Popularity Based on the number of times that item was Ordered
    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ItemListResponse>> getTopFiveItemForRestaurent(@PathVariable("restaurant_id") final String restaurantId ) throws RestaurantNotFoundException{
        int itemListCount = 0;
        List<ItemEntity> allTopItems = itemService.getMostOrderedItems(restaurantId);
        ItemListResponse itemListResponse = new ItemListResponse();


        for (ItemEntity n : allTopItems) {
            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .itemName(itemEntity.getItemName())
                    .price(itemEntity.getPrice())
                    .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
                itemListCount++;
            if (itemListCount <= 5) {
                itemListResponse.add(itemList);
            }
        }

        return new ResponseEntity<List<ItemListResponse>>(itemListResponse, HttpStatus.OK);
    }

}
