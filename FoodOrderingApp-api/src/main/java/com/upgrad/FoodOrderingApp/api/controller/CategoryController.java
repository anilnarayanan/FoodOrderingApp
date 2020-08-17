package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //This Request method is a GET request and will not require any parameters from the user.
    //It retrieves all the categories present in the database, ordered by their name
    @RequestMapping(method = RequestMethod.GET, path = "category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories() {
        //Calling getAllCategoriesOrderedByName that returns the list of category entities
        List<CategoryEntity> categoryEntities = categoryService.getAllCategoriesOrderedByName();
        if(!categoryEntities.isEmpty()) { //Checking that the Category Entities exists
            //Creating the categoryListResponses list for response
            List<CategoryListResponse> categoryListResponses = new LinkedList<>();
            //Iterating each category entity and adding the categoryListResponse to the categoryListResponses list
            categoryEntities.forEach(categoryEntity -> {
                CategoryListResponse categoryListResponse = new CategoryListResponse()
                        .categoryName(categoryEntity.getCategoryName())
                        .id(UUID.fromString(categoryEntity.getUuid()));
                categoryListResponses.add(categoryListResponse);
            });
            //Adding all the categoryListResponses list to display in response
            CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(categoryListResponses);
            return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
        }
        else{
            //If Category Entities doesn't exist, returns empty
            return new ResponseEntity<CategoriesListResponse>(new CategoriesListResponse(), HttpStatus.OK);
        }
    }

    //This GET Request Method is to fetch all items within the category id entered by user. Throws exception if category id entered doesn't exist
    @RequestMapping(method = RequestMethod.GET,path = "category/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable(value = "category_id")final String categoryUUID) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.getCategoryById(categoryUUID);
        return categoryListWithItemsList(categoryEntity);
    }

    //This GET Request Method is return empty list when category id is not entered by user
    @RequestMapping(method = RequestMethod.GET,path = "category/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryByIdNULL() throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.getCategoryById(null);
        return categoryListWithItemsList(categoryEntity);
    }

    //Common function that returns the CategoryDetailsResponse with Categories and items associated with category
    private ResponseEntity<CategoryDetailsResponse> categoryListWithItemsList(CategoryEntity categoryEntity){
        List<ItemEntity> itemEntities = categoryEntity.getItems();
        List<ItemList> itemLists = new LinkedList<>();
        itemEntities.forEach(itemEntity -> {
            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .price(itemEntity.getPrice())
                    .itemName(itemEntity.getItemName())
                    .itemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
            itemLists.add(itemList);
        });

        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse()
                .categoryName(categoryEntity.getCategoryName())
                .id(UUID.fromString(categoryEntity.getUuid()))
                .itemList(itemLists);
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse,HttpStatus.OK);
    }
}
