package com.upgrad.FoodOrderingApp.api.controller;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
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
@RequestMapping("/")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET, path = "restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {
        List<RestaurantEntity> allRestaurants = restaurantService.restaurantsByRating();
        return restaurantLists(allRestaurants);
    }

    //Common function to get restaurant lists with address and categories in required order
    private ResponseEntity<RestaurantListResponse> restaurantLists(List<RestaurantEntity> restaurantEntities) {
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
                    .flatBuildingName(restaurantEntityDetails.getAddress().getFlatBuilNo())
                    .locality(restaurantEntityDetails.getAddress().getLocality())
                    .pincode(restaurantEntityDetails.getAddress().getPincode())
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

    @RequestMapping(method = RequestMethod.GET, path = "restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByName(@PathVariable("restaurant_name") String restaurantName) throws RestaurantNotFoundException {
        // Calling getRestaurantsByName method to fetch the list of restaurant entities matching with the entered name
        List<RestaurantEntity> allRestaurants = restaurantService.restaurantsByName(restaurantName);
        return restaurantLists(allRestaurants);
    }

    @RequestMapping(method = RequestMethod.GET, path = "restaurant/name/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByNameNull() throws RestaurantNotFoundException {
        // Calling getRestaurantsByName method to fetch the list of restaurant entities matching with the entered name
        List<RestaurantEntity> allRestaurants = restaurantService.restaurantsByName(null);
        return restaurantLists(allRestaurants);
    }

    @RequestMapping(method = RequestMethod.GET, path = "restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryId(@PathVariable(value = "category_id") String categoryUUID) throws CategoryNotFoundException {
        //Calls restaurantByCategory method of restaurantService to get the list of restaurant entity.
        List<RestaurantEntity> allRestaurants = restaurantService.restaurantByCategory(categoryUUID);
        return restaurantLists(allRestaurants);
    }

    @RequestMapping(method = RequestMethod.GET, path = "restaurant/category/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryIdNULL() throws CategoryNotFoundException {
        //Calls restaurantByCategory method of restaurantService to get the list of restaurant entity.
        List<RestaurantEntity> allRestaurants = restaurantService.restaurantByCategory(null);
        return restaurantLists(allRestaurants);
    }


    @RequestMapping(method = RequestMethod.GET, path = "restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByRestaurantId(@PathVariable(value = "restaurant_id") final String restaurantUUID) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUUID);
        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantUUID);
        List<CategoryList> categoryLists = new LinkedList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            List<ItemEntity> itemEntities = itemService.getItemsByCategoryAndRestaurant(restaurantUUID, categoryEntity.getUuid());
            List<ItemList> itemLists = new LinkedList<>();
            itemEntities.forEach(itemEntity -> {
                ItemList itemList = new ItemList()
                        .id(UUID.fromString(itemEntity.getUuid()))
                        .itemName(itemEntity.getItemName())
                        .price(itemEntity.getPrice())
                        .itemType(ItemList.ItemTypeEnum.valueOf(itemEntity.getType().getValue()));
                itemLists.add(itemList);
            });

            //Creating new category list to add listof category list
            CategoryList categoryList = new CategoryList()
                    .itemList(itemLists)
                    .id(UUID.fromString(categoryEntity.getUuid()))
                    .categoryName(categoryEntity.getCategoryName());

            //adding to the categoryLists
            categoryLists.add(categoryList);
        }

        //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                .id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()))
                .stateName(restaurantEntity.getAddress().getState().getStateName());

        //Creating the RestaurantDetailsResponseAddress for the RestaurantList
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                .id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                .city(restaurantEntity.getAddress().getCity())
                .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNo())
                .locality(restaurantEntity.getAddress().getLocality())
                .pincode(restaurantEntity.getAddress().getPincode())
                .state(restaurantDetailsResponseAddressState);

        //Creating the RestaurantDetailsResponse by adding the list of categoryList and other details.
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse()
                .restaurantName(restaurantEntity.getRestaurantName())
                .address(restaurantDetailsResponseAddress)
                .averagePrice(restaurantEntity.getAvgprice())
                .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                .numberCustomersRated(restaurantEntity.getNumberCustomersRated())
                .id(UUID.fromString(restaurantEntity.getUuid()))
                .photoURL(restaurantEntity.getPhotoUrl())
                .categories(categoryLists);
        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "restaurant/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByRestaurantIdNULL() throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(null);
        return new ResponseEntity<RestaurantDetailsResponse>(new RestaurantDetailsResponse(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(@RequestHeader ("authorization")final String authorization,@PathVariable(value = "restaurant_id")final String restaurantUuid,@RequestParam(value = "customer_rating")final Double customerRating) throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {

        //Access the accessToken from the request Header
        final String accessToken = authorization.split("Bearer ")[1];

        //Calls customerService getCustomerMethod to check the validity of the customer.this methods returns the customerEntity.
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        //Calls restaurantByUUID method of restaurantService to get the restaurant entity.
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUuid);

        //Calls updateRestaurantRating and passes restaurantentity found and customer rating and return the updated entity.
        RestaurantEntity updatedRestaurantEntity = restaurantService.updateRestaurantRating(restaurantEntity,customerRating);

        //Creating RestaurantUpdatedResponse containing the UUID of the updated Restaurant and the success message.
        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(restaurantUuid))
                .status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse,HttpStatus.OK);
    }
}
