package org.litespring.stereotype;

import java.lang.annotation.*;

@Target(ElementType.TYPE) /** Class, interface (including annotation type), or enum declaration :应用在类上*/
@Retention(RetentionPolicy.RUNTIME)/**运行时生效*/
@Documented
public @interface Component {

	/**
	 * The value may indicate a suggestion for a logical component name,
	 * to be turned into a Spring bean in case of an autodetected component.
	 * @return the suggested component name, if any
	 */
	String value() default "";

}