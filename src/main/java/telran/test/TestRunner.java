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
		for (Method method : methods) {
			if(method.isAnnotationPresent(Test.class)) {
				invokeAnnotation(BeforeEach.class);
				invokeMethod(method);
			}
		}
	}

	private void invokeAnnotation(Class<? extends Annotation> clazz) {
		for (Method method : methods) {
			if (method.isAnnotationPresent(clazz)) {
				invokeMethod(method);
			}
		}
	}
	
	private void invokeMethod(Method method) {
		method.setAccessible(true);
		try {
			method.invoke(testObj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("error: " + e.getMessage());
		}
	}
}