package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDAO;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    public List<PaymentEntity> getAllPaymentMethods() {
        List<PaymentEntity> paymentEntities = paymentDAO.getAllPaymentMethods();
        return paymentEntities;
    }

    /**
     * @param paymentId
     * @return
     * @throws PaymentMethodNotFoundException
     */
    public PaymentEntity getPaymentByUUID(String paymentId) throws PaymentMethodNotFoundException {

        PaymentEntity paymentEntity = paymentDAO.getPaymentByUUID(paymentId);
        if (paymentEntity == null) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        } else {
            return paymentEntity;
        }
    }
}
