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

    // Method To Get Top 5 Items by Popularity Based on the number of times that item was ordered
    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ItemListResponse>> getTopFiveItemForRestaurent(@PathVariable("restaurant_id") final String restaurantId ) throws RestaurantNotFoundException{

        final List<ItemEntity> allTopFiveItems = itemService.getTopFiveItems(restaurantId);

        //toggle with model used in <> - already used -  ItemList, ItemListResponse ,ItemQuantityResponse
        List<ItemListResponse> responseTopFiveItems = new ArrayList<>();

        for (ItemEntity n : allTopFiveItems) {
            // toggle with model instantiated - already used  - ItemListResponse,ItemQuantityResponse already used
            ItemListResponse item = new ItemListResponse();

                    // Needs work - uncomment after finding the right response modal
                    /*item.setItem(n.getId());
                    item.setItemName(n.getItemName();
                    item.setPrice(n.getPrice());
                    item.setItemType(n.getType());*/
            responseTopFiveItems.add(item);
        }
        //toggle with the model in <>
        return new ResponseEntity<List<ItemListResponse>>(responseTopFiveItems, HttpStatus.OK);
    }

}
