package com.api.politizei.model;

import jakarta.persistence.MappedSuperclass;

import java.lang.annotation.*;

@MappedSuperclass
public abstract class PageModel {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface SortField {
        public String fieldName();
    }
}
