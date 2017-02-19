package com.imagesearch.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Scope;



/**
 * @author Gilad Opher
 */
@Scope
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScoped {
}

