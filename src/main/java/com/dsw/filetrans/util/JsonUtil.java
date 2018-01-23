package com.dsw.filetrans.util;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

	private static Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED)
			.serializeSpecialFloatingPointValues().create();

	public static <T> String Object2Json(T o) {
		String json = gson.toJson(o);
		return json;
	}

	public static <T> T json2Object(String json, Class<T> clazz) throws Exception {
		T o = gson.fromJson(json, clazz);
		return o;
	}

	public static <T> List<T> json2List(String json, Class<T[]> type) throws Exception {
		T[] list = gson.fromJson(json, type);
		return Arrays.asList(list);
	}

}
