package net.mapthinks.service;

import net.mapthinks.domain.base.AbstractBaseEntity;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by MacbookPro on 10.09.2016.
 */
@Service
@Transactional
public class JpaService {

    @Inject
    private JpaContext jpaContext;

    public <A extends AbstractBaseEntity> AbstractBaseEntity findOne(AbstractBaseEntity value){
        return jpaContext.getEntityManagerByManagedType(value.getClass()).find(value.getClass(),value.getId());
    }

    public <A extends AbstractBaseEntity> AbstractBaseEntity save(AbstractBaseEntity value){
        if(value==null)
            return value;

        if(value.getId()!=null)
            value = jpaContext.getEntityManagerByManagedType(value.getClass()).merge(value);
        else
            jpaContext.getEntityManagerByManagedType(value.getClass()).persist(value);
        return value;
    }




}
