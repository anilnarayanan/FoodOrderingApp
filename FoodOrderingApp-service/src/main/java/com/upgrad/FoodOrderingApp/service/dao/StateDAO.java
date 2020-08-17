package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to find UUID in state table
     *
     * @param uuid Randomly generated UUID
     * @return State entity object
     */
    public StateEntity getStateByUUID(String uuid) {
//        StateEntity stateEntity;
        try {
            StateEntity stateEntity =
                    entityManager
                            .createNamedQuery("getStateByUuid", StateEntity.class)
                            .setParameter("uuid", uuid)
                            .getSingleResult();
            return stateEntity;

        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Fetches all states
     *
     * @return Null
     */
    public List<StateEntity> getAllStates(){
        try {
            List<StateEntity> stateEntities = entityManager.createNamedQuery("getAllStates",StateEntity.class).getResultList();
            return stateEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}
