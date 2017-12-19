package net.mapthinks.domain.base;

import javax.persistence.*;
import java.io.Serializable;

import java.util.Objects;

/**
 * Created by MacbookPro on 23/06/16.
 */
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public AbstractBaseEntity() {

    }

    public AbstractBaseEntity(Long id) {
        this.id = id;
    }

    @Transient
    private Long parentId;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        AbstractBaseEntity baseEntity = (AbstractBaseEntity) o;
        return Objects.equals(id, baseEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
