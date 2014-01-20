package com.captechconsulting.facade.v1_0.validators;

import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
@Pattern(regexp = "^([a-zA-Z]{2}(([-|,|.| ][0-9]{3}[-|,|.| ][0-9]{5})|([0-9]{8}))$)?", message = "{error.validation.package_number_invalid}")
public @interface PackageNumber {

}
