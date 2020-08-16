package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {
        List<RestaurantEntity> allRestaurants = restaurantService.getAllRestaurantsByRating();
        return restaurantLists(allRestaurants);
    }

    //Common function to get restaurant lists with address and categories in required order
    private ResponseEntity<RestaurantListResponse> restaurantLists(List<RestaurantEntity> restaurantEntities){
        //declaring a list to store all restaurants
        List<RestaurantList> allRestaurantsListDetails = new LinkedList<>();

        for (RestaurantEntity restaurantEntityDetails : restaurantEntities) {
            //Iterate for each restaurant entity

            //Retrieve the categories for each restaurant and should be separated by comma
            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntityDetails.getUuid());
            String categories = new String();
            ListIterator<CategoryEntity> categoryListIterator = categoryEntities.listIterator();
            while (categoryListIterator.hasNext()) {
                categories = categories + categoryListIterator.next().getCategoryName();
                if (categoryListIterator.hasNext()) {
                    categories = categories + ", ";
                }
            }

            //Creating the RestaurantDetailsResponseAddressState for the RestaurantList
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntityDetails.getAddress().getState().getUuid()))
                    .stateName(restaurantEntityDetails.getAddress().getState().getStateName());

            //Creating the RestaurantDetailsResponseAddress for the RestaurantList
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntityDetails.getAddress().getUuid()))
                    .city(restaurantEntityDetails.getAddress().getCity())
                    .flatBuildingName(restaurantEntityDetails.getAddress().getFlatBuilNumber())
                    .locality(restaurantEntityDetails.getAddress().getLocality())
                    .pincode(restaurantEntityDetails.getAddress().getPinCode())
                    .state(restaurantDetailsResponseAddressState);

            //Creating RestaurantList to add to list of RestaurantList
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntityDetails.getUuid()))
                    .restaurantName(restaurantEntityDetails.getRestaurantName())
                    .averagePrice(restaurantEntityDetails.getAvgprice())
                    .categories(categories)
                    .customerRating(BigDecimal.valueOf(restaurantEntityDetails.getCustomerRating()))
                    .numberCustomersRated(restaurantEntityDetails.getNumberCustomersRated())
                    .photoURL(restaurantEntityDetails.getPhotoUrl())
                    .address(restaurantDetailsResponseAddress);
            //Adding it to the list
            allRestaurantsListDetails.add(restaurantList);
        }
        //Creating the RestaurantListResponse by adding the list of RestaurantList
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(allRestaurantsListDetails);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/name/{restaurant_name}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByName(@PathVariable("restaurant_name") String restaurantName) throws RestaurantNotFoundException {
        // Calling getRestaurantsByName method to fetch the list of restaurant entities matching with the entered name
        List<RestaurantEntity> allRestaurants = restaurantService.getRestaurantsByName(restaurantName);
        return restaurantLists(allRestaurants);
        }

    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/name/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByNameNull() throws RestaurantNotFoundException {
        // Calling getRestaurantsByName method to fetch the list of restaurant entities matching with the entered name
        List<RestaurantEntity> allRestaurants = restaurantService.getRestaurantsByName(null);
        return restaurantLists(allRestaurants);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/category/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryId(@PathVariable(value = "category_id")String categoryUUID) throws CategoryNotFoundException {
        //Calls restaurantByCategory method of restaurantService to get the list of restaurant entity.
        List<RestaurantEntity> allRestaurants = restaurantService.getRestaurantsByCategory(categoryUUID);
        return restaurantLists(allRestaurants);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/category/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryIdNULL() throws CategoryNotFoundException {
        //Calls restaurantByCategory method of restaurantService to get the list of restaurant entity.
        List<RestaurantEntity> allRestaurants = restaurantService.getRestaurantsByCategory(null);
        return restaurantLists(allRestaurants);
    }
}
