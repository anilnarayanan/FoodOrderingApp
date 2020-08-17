package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private CustomerAddressDAO customerAddressDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private CustomerDao customerDAO;

    @Autowired
    CustomerAuthDao customerAuthDao;

    /**
     * Method to save address
     *
     * @param addressEntity
     * @param stateEntity
     * @return
     * @throws SaveAddressException
     * @throws AddressNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity addressEntity, StateEntity stateEntity) throws AddressNotFoundException, SaveAddressException, SignUpRestrictedException {
        //    Block to check for null or empty values in fields

        if (addressEntity.getFlatBuilNo() == null || addressEntity.getFlatBuilNo().isEmpty()
                || addressEntity.getLocality() == null || addressEntity.getLocality().isEmpty()
                || addressEntity.getCity() == null || addressEntity.getCity().isEmpty()
                || addressEntity.getPincode() == null || addressEntity.getPincode().isEmpty()) {
            System.out.println("I am in");
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }

        //    Block to check the validity of pincode
        System.out.println(addressEntity.getPincode().length());
        if (!addressEntity.getPincode().matches("[0-9]+")) {

            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }

        if (addressEntity.getPincode().length() > 6) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }

        //    Block to check the existence of UUID in table
        System.out.println("Hi" + this.getStateByUUID(stateEntity.getUuid()));
        if (this.getStateByUUID(stateEntity.getUuid()) == null) {
            throw new SignUpRestrictedException("ANF-002", "No state by this id");
        }

        if (stateEntity == null) {
            throw new SignUpRestrictedException("ANF-002", "No state by this id");
        }

        //    On success, return AddressEntity object
        return addressDAO.saveAddress(addressEntity);
    }

    /**
     * Method to find UUID in the State table
     *
     * @param uuid
     * @return On success, returns State entity object. On failure, returns NULL
     */
    public StateEntity getStateByUUID(String uuid) throws AddressNotFoundException {
        StateEntity stateEntity = stateDAO.getStateByUUID(uuid);
        System.out.println("stateEntity " + stateEntity);
//        System.out.println("uuid" + stateEntity.getUuid());
        if (stateEntity == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        } else
        return stateEntity;
    }

    /**
     * Method to save customer address in table
     *
     * @param addressEntity
     * @param customerEntity
     * @return CustomerAddress entity object
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity saveCustomerAddressEntity(
            AddressEntity addressEntity, CustomerEntity customerEntity) {
        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setAddress(addressEntity);
        customerAddressEntity.setCustomer(customerEntity);

        return customerAddressDAO.saveCustomerAddress(customerAddressEntity);
    }


    /**
     * @param addressUUID
     * @param customerEntity
     * @return
     * @throws AuthorizationFailedException
     * @throws AddressNotFoundException
     */
    public AddressEntity getAddressByUUID(String addressUUID, CustomerEntity customerEntity) throws AuthorizationFailedException, AddressNotFoundException {
        AddressEntity addressEntity;
        CustomerAddressEntity customerAddressEntity;

        if (addressUUID == null) {
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }
        addressEntity = addressDAO.getAddressByUUID(addressUUID);
        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }
        customerAddressEntity = customerAddressDAO.getCustomerAddressByAddress(addressEntity);
        if (customerAddressEntity.getCustomer().getUuid() == customerEntity.getUuid()) {
            return addressEntity;
        } else {
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }

    }

    /**
     * @param addressEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        AddressEntity deletedAddressEntity;
        List<OrdersEntity> ordersEntities = orderDAO.getOrdersByAddress(addressEntity);

        if (ordersEntities == null || ordersEntities.isEmpty()) {
            deletedAddressEntity = addressDAO.deleteAddress(addressEntity);
            return deletedAddressEntity;
        } else {
            addressEntity.setActive(0);
            AddressEntity updatedAddressActiveStatus = addressDAO.updateAddressActiveStatus(addressEntity);
            return updatedAddressActiveStatus;
        }
    }

    /**
     * Fetches all states
     *
     * @return
     */
    public List<StateEntity> getAllStates() {
        List<StateEntity> stateEntities = stateDAO.getAllStates();
        return stateEntities;
    }

    /**
     * @param customerEntity
     * @return
     */
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) {
        List<AddressEntity> addressEntities = new LinkedList<>();
        List<CustomerAddressEntity> customerAddressEntities = customerAddressDAO.getAllCustomerAddressByCustomer(customerEntity);
        if (customerAddressEntities != null) {
            customerAddressEntities.forEach(customerAddressEntity -> {
                addressEntities.add(customerAddressEntity.getAddress());
            });
        }
        return addressEntities;
    }
}
