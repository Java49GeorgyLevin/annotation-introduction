package telran.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import telran.test.annotation.BeforeEach;
import telran.test.annotation.Test;

public class TestRunner implements Runnable {
	private Object testObj;

	public TestRunner(Object testObj) {
		super();
		this.testObj = testObj;
	}

	@Override
	public void run() {
		Class<?> clazz = testObj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		Method[] beforeEachMethods = getBeforeEachMethods(methods);
		for (Method method : methods) {
			if (method.isAnnotationPresent(Test.class)) {
				method.setAccessible(true);
				runMethod(method, beforeEachMethods);
			}
		}
	}

	private void runMethod(Method method, Method[] beforeEachMethods) {
		try {
			Arrays.stream(beforeEachMethods).forEach(m -> {
				try {
					m.setAccessible(true);
					m.invoke(testObj);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			});
			method.invoke(testObj);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private Method[] getBeforeEachMethods(Method[] methods) {
		return Arrays.stream(methods).filter(m -> m.isAnnotationPresent(BeforeEach.class)).toArray(Method[]::new);
	}

}