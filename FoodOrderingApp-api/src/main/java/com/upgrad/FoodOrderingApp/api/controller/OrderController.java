package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantity;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderResponse;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    ItemService itemService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    AddressService addressService;

    @Autowired
    RestaurantService restaurantService;


    /**
     * Fetch coupons by coupon name
     *
     * @param authorization
     * @param couponName
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/order/coupon/{coupon_name}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(
            @RequestHeader("authorization") final String authorization,
            @PathVariable(value = "coupon_name") final String couponName)
            throws AuthorizationFailedException, CouponNotFoundException {

        String bearerToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken);
        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().couponName(couponEntity.getCouponName()).id(UUID.fromString(couponEntity.getUuid())).percent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    /**
     * Fetch customer orders
     *
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     * @throws CouponNotFoundException
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/order",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getPastOrders(
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        CustomerEntity customerEntity;
        List<OrdersEntity> ordersEntityList;
        String bearerToken = authorization.split("Bearer ")[1];
        customerEntity = customerService.getCustomer(bearerToken);
        ordersEntityList = orderService.getOrdersByCustomers(customerEntity.getUuid());
        System.out.println(ordersEntityList);
        return null;
    }

    /**
     * @param authorization
     * @param saveOrderRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws PaymentMethodNotFoundException
     * @throws AddressNotFoundException
     * @throws RestaurantNotFoundException
     * @throws CouponNotFoundException
     * @throws ItemNotFoundException
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader(value = "authorization") final String authorization, @RequestBody(required = false) final SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, PaymentMethodNotFoundException, AddressNotFoundException, RestaurantNotFoundException, CouponNotFoundException, ItemNotFoundException {
        CouponEntity couponEntity;
        String couponId, paymentId, addressId, restaurantId;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        OrdersEntity saveOrders, ordersEntity;
        OrderItemEntity savedOrder;
        List<ItemQuantity> itemQuantityList;

        String bearerToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken);

        couponId = saveOrderRequest.getCouponId().toString()
        couponEntity = orderService.getCouponByCouponId(couponId);

        paymentId = saveOrderRequest.getPaymentId().toString();
        PaymentEntity paymentEntity = paymentService.getPaymentByUUID(paymentId);

        addressId = saveOrderRequest.getAddressId();
        AddressEntity addressEntity = addressService.getAddressByUUID(addressId, customerEntity);

        restaurantId = saveOrderRequest.getRestaurantId().toString();
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);

        ordersEntity = new OrdersEntity();
        ordersEntity.setUuid(UUID.randomUUID().toString());
        ordersEntity.setBill(saveOrderRequest.getBill().doubleValue());
        ordersEntity.setCoupon(couponEntity);
        ordersEntity.setDate(timestamp);
        ordersEntity.setAddress(addressEntity);
        ordersEntity.setCustomer(customerEntity);
        ordersEntity.setRestaurant(restaurantEntity);
        ordersEntity.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        ordersEntity.setPayment(paymentEntity);
        saveOrders = orderService.saveOrder(ordersEntity);
        itemQuantityList = saveOrderRequest.getItemQuantities();

        saveOrders = orderService.saveOrder(ordersEntity);
        for (ItemQuantity itemQuantity : itemQuantityList) {
            OrderItemEntity tempOrderItem = new OrderItemEntity();
            ItemEntity tempItem = itemService.getItemByUUID(itemQuantity.getItemId().toString());
            tempOrderItem.setOrderId(ordersEntity);
            tempOrderItem.setItemId(tempItem);
            tempOrderItem.setPrice(itemQuantity.getPrice());
            tempOrderItem.setQuantity(itemQuantity.getQuantity());
            savedOrder = orderService.saveOrderItem(tempOrderItem);
        }
        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(saveOrders.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.CREATED);
    }

}