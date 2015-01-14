package com.pispower.util;

import java.lang.reflect.Array;


public class ArraysUtil {
	/**
	 *添加元素到指定的数组对象中
	 * 
	 * @param array
	 * @param element
	 * @return 数组对象
	 */
	public static Object[] add(Object[] array, Object element) {
		Class<?> type;
		if (array != null) {
			type = array.getClass();
		} else if (element != null) {
			type = element.getClass();
		} else {
			type = Object.class;
		}
		Object[] newArray = (Object[]) copyArrayGrow1(array, type);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	private static Object copyArrayGrow1(Object array,
			Class<?> newArrayComponentType) {
		if (array != null) {
			int arrayLength = Array.getLength(array);
			Object newArray = Array.newInstance(array.getClass()
					.getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return Array.newInstance(newArrayComponentType, 1);
	}
}
