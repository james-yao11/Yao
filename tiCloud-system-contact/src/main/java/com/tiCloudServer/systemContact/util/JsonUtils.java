package com.tiCloudServer.systemContact.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


import com.google.gson.*;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Field;
import java.util.*;

/**
 * @ClassName JsonUtils
 * @Description Json工具类
 */
public class JsonUtils {

        public static final Gson gson = getGson();

        public static Gson getGson() {
            Gson gson = new GsonBuilder().create();
            try {
                Field factories = Gson.class.getDeclaredField("factories");
                factories.setAccessible(true);
                Object o = factories.get(gson);
                Class<?>[] declaredClasses = Collections.class.getDeclaredClasses();
                for (Class c : declaredClasses) {
                    if ("java.util.Collections$UnmodifiableList".equals(c.getName())) {
                        Field listField = c.getDeclaredField("list");
                        listField.setAccessible(true);
                        List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) listField.get(o);
                        int i = list.indexOf(ObjectTypeAdapter.FACTORY);
                        list.set(i, GsonTypeAdaptor.FACTORY);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gson;
        }

        /**
         * Object转JSONString
         */
        public static String objectTOJSONString(Object object) {
            return JSON.toJSONString(object);
        }

        /**
         * 对象转换成json字符串
         */
        public static String beanToJson(Object object) {
            if (object == null) {
                return null;
            }
            Gson gson = new Gson();
            return gson.toJson(object);
        }

        /**
         * Map转成json
         */
        public static String mapToJson(Map<String, Object> map) {
            Gson gson = new Gson();
            return gson.toJson(map, Map.class);
        }

        /**
         * result转成json
         */
        public static String ResultToJson(ResultJson resultJson) {
            Gson gson = new Gson();
            return gson.toJson(resultJson, ResultJson.class);
        }

        /**
         * json转成map
         */
        public static Map<String, Object> jsonToMap(String jsonString) {
            Map<String, Object> stringHashMap = new HashMap<>();
            return gson.fromJson(jsonString, stringHashMap.getClass());
        }

        /**
         * jsonString 转 JsonObject
         */
        public static JsonObject jsonToJsonObject(String jsonString) {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            return gson.fromJson(jsonString, jsonObject.getClass());
        }

        /**
         * JsonObject转LinkedHashMap
         */
        public static LinkedHashMap<String, String> jsonToMapJsonObject(JsonObject json) {
            Gson gson = new Gson();
            LinkedHashMap<String, String> stringHashMap = new LinkedHashMap<>();
            return gson.fromJson(String.valueOf(json), stringHashMap.getClass());
        }

        /**
         * 实体类对象 转 Map
         */
        public static Map<String, Object> objectMap(Object obj) {
            Map<String, Object> map = new HashMap<>();
            if (obj == null) {
                return map;
            }
            Class<? extends Object> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(obj));
                }
            } catch (Exception ignored) {
            }
            return map;
        }

        /**
         * Object 转List<实体>
         */
        public static <T> List<T> castList(Object object, Class<T> clazz) {
            List<T> result = new ArrayList<>();
            if (object instanceof List<?>) {
                for (Object o : (List<?>) object) {
                    result.add(clazz.cast(o));
                }
                return result;
            }
            return null;
        }

        /**
         * json字符串转换成对象
         */
        public static <T> T jsonTOBean(String jsonString, Class<T> cls) {
            T t = null;
            try {
                t = JSON.parseObject(jsonString, cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return t;
        }

        /**
         * json字符串转换成List集合 (需要实体类)
         */
        public static <T> List<T> jsonTOList(String jsonString, Class cls) {
            List list = null;
            try {
                list = JSON.parseArray(jsonString, cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        /**
         * List集合转换成json字符串
         */
        public static String listTOJson(Object obj) {
            return JSONArray.toJSONString(obj, true);
        }

        /**
         * json转List (不需要实体类)
         */
        public static JSONArray jsonTOList(String jsonStr) {
            return JSON.parseArray(jsonStr);
        }

}
