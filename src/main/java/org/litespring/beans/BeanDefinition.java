package org.litespring.beans;

import java.util.List;

public interface BeanDefinition {
   public static final String SCOPE_SINGLETON = "singleton";
   public static final String SCOPE_PROTOTYPE = "prototype";
   public static final String SCOPE_DEFAULT = "";

   boolean isSingleton();
   boolean isPrototype();
   String getScope();
   void setScope(String scope);

   String getBeanClassName();

   List<PropertyValue> getPropertyValues();

   String getID();
}
