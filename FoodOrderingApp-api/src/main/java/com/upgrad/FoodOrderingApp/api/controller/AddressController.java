package com.upgrad.FoodOrderingApp.api.controller;

//import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
//import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    /**
     * Method to save address of a customer
     *
     * @param authorization
     * @param saveAddressRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     * @throws SaveAddressException
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/address",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(
            @RequestHeader("authorization") final String authorization,
            @RequestBody(required = false) SaveAddressRequest saveAddressRequest)
            throws AuthorizationFailedException, AddressNotFoundException, SignUpRestrictedException, SaveAddressException {

        AddressEntity addressEntity = new AddressEntity();
        StateEntity stateEntity;
        String bearerToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken);
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        AddressEntity addressCommittedEntity = addressService.saveAddress(addressEntity, stateEntity);
        addressService.saveCustomerAddressEntity(addressCommittedEntity, customerEntity);
        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                                                    .id(addressCommittedEntity.getUuid())
                                                    .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }


    /**
     * @param authorization
     * @param uuid
     * @return
     */
    @CrossOrigin
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/address/{address_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader("authorization") final String authorization, @PathVariable(value = "address_id") final String uuid) throws AuthorizationFailedException, AddressNotFoundException {
        String bearerToken = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(bearerToken);
        AddressEntity addressEntity = addressService.getAddressByUUID(uuid, customerEntity);
        AddressEntity deletedAddressEntity = addressService.deleteAddress(addressEntity);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                .id(UUID.fromString(deletedAddressEntity.getUuid()))
                .status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);

    }

    /**
     * Fetches list of states from database
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        StatesListResponse statesListResponse;
        List<StateEntity> stateEntities = addressService.getAllStates();
        if (!stateEntities.isEmpty()) {
            List<StatesList> statesLists = new LinkedList<>();
            stateEntities.forEach(stateEntity -> { StatesList statesList = new StatesList()
                                                    .id(UUID.fromString(stateEntity.getUuid()))
                                                    .stateName(stateEntity.getStateName());
            statesLists.add(statesList);
            });
            statesListResponse = new StatesListResponse().states(statesLists);
            return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
        }
        return new ResponseEntity<StatesListResponse>(new StatesListResponse(), HttpStatus.OK);
    }


}
