package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        validateCoupon(couponName);
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken);
        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().couponName(couponEntity.getCouponName()).id(UUID.fromString(couponEntity.getUuid())).percent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }
    private void validateCoupon(String couponName) throws CouponNotFoundException {
        if (couponName == null || couponName == "") {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
    }
    /**
     *Fetch customer orders
     *
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     * @throws CouponNotFoundException
     */
    /*@CrossOrigin
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
    }
    */
}