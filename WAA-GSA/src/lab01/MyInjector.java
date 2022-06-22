package lab01;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import javax.management.AttributeNotFoundException;

import org.reflections.Reflections;

import lab01.annotation.MyAutowired;
import lab01.annotation.MyBean;

public class MyInjector {
	private Map<Class, Object> container = new HashMap<>();
	

	public void scanForBeans() {
		// search
	
		String packageName = "lab01";
      
		Set<Class> classList = findAllClassesUsingClassLoader(packageName);
		for(Class c:classList) {			
			if(c.isAnnotationPresent(MyBean.class)) {
				try {
					container.put(c, c.newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public void scanForAutowired() {
		String packageName = "lab01";
      
		Set<Class> classList = findAllClassesUsingClassLoader(packageName);
		for(Class c:classList) {			
			if(c.isAnnotationPresent(MyAutowired.class)) {
				try {
					container.put(c, c.newInstance());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public Set<Class> findAllClassesUsingClassLoader(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClass(line, packageName))
				.collect(Collectors.toSet());
	}

	private Class getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
			// handle the exception
		}
		return null;
	}
	
	public Object getBean(Class clazz) throws AttributeNotFoundException {
		Object c = container.get(clazz);
		if(c==null) {
			throw new AttributeNotFoundException();
		}
		return c;
	}
	
}
