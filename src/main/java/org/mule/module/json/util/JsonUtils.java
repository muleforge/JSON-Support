/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.json.util;

import org.mule.module.json.transformers.ObjectBean;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.math.BigDecimal;

import net.sf.json.JsonConfig;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * TODO
 */
public class JsonUtils
{

    /**
     * logger used by this class
     */
    protected static final Log logger = LogFactory.getLog(JsonUtils.class);

    public static final String PATTERN_ARRAY = "\\[(.*)\\]";
    public static final String PATTERN_OBJECT = "\\{(.*)\\}";
    public static final String PATTERN_STRING = "(\"|\')(.*)(\"|\')";

    public static Object[] convertJsonToArray(String jsonString, Class targetClass, Map classMap)
    {

        if (classMap == null)
        {
            classMap = new HashMap();
        }

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setArrayMode(JsonConfig.MODE_OBJECT_ARRAY);
        jsonConfig.setClassMap(classMap);

        JSON json = JSONSerializer.toJSON(jsonString, jsonConfig);

        return (Object[]) JSONArray.toArray((JSONArray) json, targetClass.getComponentType(), classMap);
    }

    public static Object convertJsonToBean(String jsonString, Class targetClass)
    {
        return convertJsonToBean(jsonString, targetClass, null);
    }

    /**
     * @param jsonString Either an object, an array, or null. Otherwise, invalid JSON
     *            string occurs.
     * @param targetClass
     * @param classMap
     * @return
     */
    public static Object convertJsonToBean(String jsonString, Class targetClass, Map classMap)
    {

        if (classMap == null)
        {
            classMap = new HashMap();
        }
        JSON json = JSONSerializer.toJSON(jsonString);
        Object bean = JSONObject.toBean((JSONObject) json, targetClass, classMap);

        return bean;
    }

    public static List convertJsonToList(String jsonString,
                                         Class targetClass,
                                         Class targetComponentClass,
                                         Map classMap)
    {

        if (classMap == null)
        {
            classMap = new HashMap();
        }

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setArrayMode(JsonConfig.MODE_LIST);
        jsonConfig.setClassMap(classMap);

        JSON json = JSONSerializer.toJSON(jsonString, jsonConfig);

        return JSONArray.toList((JSONArray) json, targetComponentClass, classMap);
    }

    public static BigDecimal convertJsonToNumber(String jsonString)
    {
        return NumberUtils.createBigDecimal(jsonString);
    }

    /**
     * Converts a number array to a BigDecimal array. This is to overcome JSON-lib
     * returning either Integer, Double or other possible number types. Please take
     * note that BigDecimal is more resource-intensive.
     *
     * @param jsonString
     * @return
     */
    public static BigDecimal[] convertJsonToNumberArray(String jsonString)
    {

        Object[] objs = convertJsonToArray(jsonString, Object[].class, null);
        BigDecimal[] bds = new BigDecimal[objs.length];

        for (int i = 0; i < objs.length; i++)
        {
            bds[i] = new BigDecimal(objs[i].toString());
        }

        return bds;
    }

    public static String convertJsonToString(String jsonString)
    {

        Pattern pattern = Pattern.compile("^" + PATTERN_STRING + "$");
        Matcher matcher = pattern.matcher(jsonString);

        if (matcher.matches())
        {
            jsonString = matcher.group(2);
        }

        return jsonString;
    }

    /**
     * @param jsonString
     * @param targetClass
     * @param classMap
     * @return
     */
    public static Object convertJsonToJavaObject(String jsonString,
                                                 Class targetClass,
                                                 Class targetComponentClass,
                                                 Map classMap)
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("Converting jsonString to Java object: jsonString=" + jsonString + ", targetClass="
                         + targetClass + ", classMap=" + classMap);
        }
        Object returnValue = null;

        if (isArray(jsonString))
        {
            if (List.class.isAssignableFrom(targetClass))
            {
                returnValue = convertJsonToList(jsonString, targetClass, targetComponentClass, classMap);
            }
            else
            {
                returnValue = convertJsonToArray(jsonString, targetClass, classMap);
            }
        }
        else if (isObject(jsonString))
        {
            returnValue = convertJsonToBean(jsonString, targetClass, classMap);
        }
        else if (isBoolean(jsonString))
        {
            returnValue = Boolean.valueOf(jsonString);
        }
        else if (isNumber(jsonString))
        {
            returnValue = convertJsonToNumber(jsonString);
        }
        else if (isNull(jsonString))
        {
            returnValue = null;
        }
        else if (isString(jsonString))
        {
            returnValue = convertJsonToString(jsonString);
        }
        else
        {
            throw new IllegalArgumentException(
                "Provided JSON string matches neither of the known type: jsonString=" + jsonString);
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("Converted and returning value '" + returnValue + "', of type '"
                         + ((returnValue != null) ? returnValue.getClass().getName() : null) + "'");
        }
        if (logger.isWarnEnabled())
        {

            if (targetClass != null && !targetClass.isInstance(returnValue))
            {
                logger.warn("Converted type '" + (returnValue == null ? null : returnValue.getClass())
                            + "' doesn't match with target type '" + targetClass + "'");
            }

        }

        return returnValue;
    }

    public static String convertJavaObjectToJson(Object object)
    {

        if (object == null)
        {
            return "null";
        }

        if (object instanceof String)
        {
            return "'" + object + "'";
        }

        if (object instanceof Boolean || object instanceof Number)
        {
            return object.toString();
        }

        // Else, we try some luck then
        return JSONSerializer.toJSON(object).toString();
    }

    /**
     * Checks if the JSON string is an array. It does not validate the syntax of the
     * entire string.
     *
     * @param jsonString
     * @return
     */
    public static boolean isArray(String jsonString)
    {
        return isMatched(jsonString.trim(), "^" + PATTERN_ARRAY + "$");
    }

    public static boolean isBoolean(String jsonString)
    {
        return jsonString.equals("true") || jsonString.equals("false");
    }

    public static boolean isNull(String jsonString)
    {
        return jsonString.equals("null");
    }

    /**
     * Checks if the JSON string is a number. It does not validate the syntax of the
     * entire string.
     *
     * @param jsonString
     * @return
     */
    public static boolean isNumber(String jsonString)
    {
        return NumberUtils.isNumber(jsonString);
    }

    /**
     * Checks if the JSON string is an object. It does not validate the syntax of the
     * entire string.
     *
     * @param jsonString
     * @return
     */
    public static boolean isObject(String jsonString)
    {
        return isMatched(jsonString.trim(), "^" + PATTERN_OBJECT + "$");
    }

    /**
     * Checks if the JSON string is a string. It does not validate the syntax of the
     * entire string.
     *
     * @param jsonString
     * @return
     */
    public static boolean isString(String jsonString)
    {
        return !isNumber(jsonString) && !isBoolean(jsonString) && !isNull(jsonString) && !isArray(jsonString)
               && !isObject(jsonString);
    }

    private static boolean isMatched(String string, String patternString)
    {

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(string);

        return matcher.matches();
    }

    /**
     * Converts a DynaBean to intended Java object.
     * @param clazz
     * @param dynaBean
     * @return
     */
    public static Object getObjectFromDynaBean(Class clazz, DynaBean dynaBean)
    {

        Object returnValue = null;

        if (clazz == null || dynaBean == null)
        {
            throw new NullPointerException("Class instance or DynaBean instance should not be null, clazz="
                                           + clazz + ", dynaBean=" + dynaBean);
        }

        try
        {
            returnValue = clazz.newInstance();
            org.apache.commons.beanutils.BeanUtils.copyProperties(returnValue, dynaBean);
        }
        catch (Exception e)
        {
            logger.warn("Could not convert '" + dynaBean + "' due to " + e.toString());
            return null;
        }

        return returnValue;
    }

    public static Object getObjectFromDynaBean(String className, DynaBean dynaBean)
        throws ClassNotFoundException
    {

        Class clazz = Class.forName(className);
        return getObjectFromDynaBean(clazz, dynaBean);
    }

    /**
     * Handy method to convert a jsonString (of type ObjectBean) to an object (its
     * getObject() method). Please take note that list type will be converted to
     * Object[] not List or its subtype.
     *
     * @param jsonString
     * @return
     */
    public static Object getObjectFromJsonString(String jsonString)
    {

        Object returnValue = null;

        try
        {
            ObjectBean objectBean = (ObjectBean) JsonUtils.convertJsonToJavaObject(jsonString,
                ObjectBean.class, null, null);
            Class clazz = Class.forName(objectBean.getType());
            Object object = objectBean.getObject();

            if (object == null)
            {
                return null;
            }

            if (object instanceof DynaBean)
            {
                returnValue = getObjectFromDynaBean(clazz, (DynaBean) object);
            }
            else
            {
                returnValue = object;
            }

            if (logger.isDebugEnabled())
            {
                logger.debug("Returning value of type " + ((returnValue == null) ? null : returnValue.getClass()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Could not obtain object due to exception", e);
        }

        return returnValue;
    }
}
