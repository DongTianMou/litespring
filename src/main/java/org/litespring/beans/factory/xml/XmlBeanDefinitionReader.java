package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import org.litespring.core.io.Resource;
import org.litespring.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

	public static final String ID_ATTRIBUTE = "id";

	public static final String CLASS_ATTRIBUTE = "class";

	public static final String SCOPE_ATTRIBUTE = "scope";

	public static final String PROPERTY_ELEMENT = "property";

	public static final String REF_ATTRIBUTE = "ref";

	public static final String VALUE_ATTRIBUTE = "value";

	public static final String NAME_ATTRIBUTE = "name";

	BeanDefinitionRegistry registry;

	public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

	public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

	protected final Log logger = LogFactory.getLog(getClass());

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry){
		this.registry = registry;
	}

	public void loadBeanDefinitions(Resource resource) {
		InputStream is = null;
		try{
			//获取inputStream
			is = resource.getInputStream();
			//解析流数据
			SAXReader reader = new SAXReader();
			Document doc = reader.read(is);
			//获取beans元素
			Element root = doc.getRootElement(); //<beans>
			Iterator<Element> iter = root.elementIterator();
			//遍历
			while(iter.hasNext()){
				Element ele = (Element)iter.next();
				String namespaceUri = ele.getNamespaceURI();
				if(this.isDefaultNamespace(namespaceUri)){
					parseDefaultElement(ele); //普通的bean
				} else if(this.isContextNamespace(namespaceUri)){
					parseComponentElement(ele); //例如<context:component-scan>
				}

			}
		} catch (Exception e) {
			throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(),e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isDefaultNamespace(String namespaceUri) {
		return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
	}
	public boolean isContextNamespace(String namespaceUri){
		return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
	}

	private void parseComponentElement(Element ele) {
		String basePackages = ele.attributeValue(BASE_PACKAGE_ATTRIBUTE);
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
		scanner.doScan(basePackages);

	}

	private void parseDefaultElement(Element ele) {
		//获取id
		String id = ele.attributeValue(ID_ATTRIBUTE);
		//获取class
		String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
		//获取BeanDefinition对象
		BeanDefinition bd = new GenericBeanDefinition(id, beanClassName);
		//获取scope
		if (ele.attribute(SCOPE_ATTRIBUTE)!=null) {
			bd.setScope(ele.attributeValue(SCOPE_ATTRIBUTE));
		}
		//传入element对象和BeanDefinition对象，拿到所有的PropertyValue存在BeanDefinition对象中
		parsePropertyElement(ele,bd);
		//注册，即放进map,以便get
		this.registry.registerBeanDefinition(id, bd);
	}

	private void parsePropertyElement(Element beanElem, BeanDefinition bd) {
		//获取property元素
		Iterator iter= beanElem.elementIterator(PROPERTY_ELEMENT);
		//遍历拿到所有的PropertyValue
		while(iter.hasNext()){
			Element propElem = (Element)iter.next();
			//获取name
			String propertyName = propElem.attributeValue(NAME_ATTRIBUTE);
			//name不为空
			if (!StringUtils.hasLength(propertyName)) {
				logger.fatal("Tag 'property' must have a 'name' attribute");
				return;
			}
			//传入element元素和BeanDefinition对象、name,拿到RuntimeBeanReference对象或者TypedStringValue对象
			Object val = parsePropertyValue(propElem, bd, propertyName);
			//构造PropertyValue
			PropertyValue pv = new PropertyValue(propertyName, val);
			//加入List<PropertyValue>
			bd.getPropertyValues().add(pv);
		}
	}

	private Object parsePropertyValue(Element ele, BeanDefinition bd, String propertyName) {
		String elementName = (propertyName != null) ?
				"<property> element for property '" + propertyName + "'" :
				"<constructor-arg> element";
		//ref是否为空
		boolean hasRefAttribute = (ele.attribute(REF_ATTRIBUTE)!=null);
		//value是否为空
		boolean hasValueAttribute = (ele.attribute(VALUE_ATTRIBUTE) !=null);
		//类型判断：ref  还是  value
		if (hasRefAttribute) {
			//拿到ref name
			String refName = ele.attributeValue(REF_ATTRIBUTE);
			if (!StringUtils.hasText(refName)) {
				logger.error(elementName + " contains empty 'ref' attribute");
			}
			RuntimeBeanReference ref = new RuntimeBeanReference(refName);
			//返回一个RuntimeBeanReference对象
			return ref;
		}else if (hasValueAttribute) {
			TypedStringValue valueHolder = new TypedStringValue(ele.attributeValue(VALUE_ATTRIBUTE));
			//返回一个TypedStringValue对象
			return valueHolder;
		}
		else {
			throw new RuntimeException(elementName + " must specify a ref or value");
		}
	}
}

