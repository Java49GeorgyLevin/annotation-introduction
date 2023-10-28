package telran.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import telran.test.annotation.BeforeEach;
import telran.test.annotation.Test;

public class TestRunner implements Runnable {
	private Object testObj;
	Method[] methods;

	public TestRunner(Object testObj) {
		super();
		this.testObj = testObj;
	}

	@Override
	public void run() {
		Class<?> clazz = testObj.getClass();
		methods = clazz.getDeclaredMethods();		
		invokeAnnotation(Test.class);
	}

	private void invokeAnnotation(Class<? extends Annotation> clazz) {
		for (Method method : methods) {
			if (method.isAnnotationPresent(clazz)) {
				method.setAccessible(true);
				if(clazz != BeforeEach.class) { 
					invokeAnnotation(BeforeEach.class);
					}
				try {
					method.invoke(testObj);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					System.out.println("error: " + e.getMessage());
				}
			}
		}
	}

}