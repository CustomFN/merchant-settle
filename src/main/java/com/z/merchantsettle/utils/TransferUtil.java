package com.z.merchantsettle.utils;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class TransferUtil {

    public static void transferAll(Object fromObj,Object toObj) {
        transfer(fromObj, toObj, true);
    }

    public static void transfer(Object fromObj,Object toObj, boolean isAllTransfer) {
        Field[] fromField  = fromObj.getClass().getDeclaredFields();
        Field[] toFields  = toObj.getClass().getDeclaredFields();
        Map<String, Field> toFieldMap = Maps.newHashMap();
        for (Field field : toFields) {
            toFieldMap.put(field.getName().replaceAll("_", "").toLowerCase(), field);
        }
        for(Field f : fromField){
            if(!isAllTransfer && f.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            String fieldNameParse = f.getName().replaceAll("_", "").toLowerCase();
            Field toField = toFieldMap.get(fieldNameParse);
            if(toField != null){
                try {
                    setValue(toObj, toField, getValue(fromObj, f));
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    private static String getValue(Object o1,Field f1) throws IllegalAccessException {
        f1.setAccessible(true);
        String str = f1.getGenericType().toString();
        if(str.equals("int")){
            return f1.get(o1).toString();
        }else if(str.equals("double")){
            return String.valueOf(f1.getDouble(o1));
        }else if(str.equals("float")){
            return String.valueOf(f1.getFloat(o1));
        }else if(str.equals("boolean")){
            return String.valueOf(f1.getBoolean(o1));
        }else if(str.equals("long")){
            return String.valueOf(f1.getLong(o1));
        }else if(str.equals("byte")){
            return String.valueOf(f1.getByte(o1));
        }else{
            return String.valueOf(f1.get(o1));
        }
    }

    private static void setValue(Object o1,Field f1,String value) throws IllegalAccessException {
        f1.setAccessible(true);
        String str = f1.getGenericType().toString();
        if(str.equals("int")) {
            f1.setInt(o1, NumberUtil.formatNumber(value, 0));
        }else if(str.equals("class java.lang.Integer")){
            f1.set(o1, NumberUtil.formatNumber(value, 0));
        }else if(str.equals("long")) {
            f1.setLong(o1, NumberUtil.formatLong(value, 0L));
        }else if(str.equals("class java.lang.Long")){
            f1.set(o1, NumberUtil.formatLong(value, 0L));
        }else if(str.equals("double")){
            f1.setDouble(o1, NumberUtil.formatDouble(value, 0D));
        }else if(str.equals("class java.lang.Double")){
            f1.set(o1, NumberUtil.formatDouble(value, 0D));
        }else if(str.equals("float")){
            f1.setFloat(o1, NumberUtil.formatFolat(value, 0F));
        }else if(str.equals("class java.lang.Float")){
            f1.set(o1, NumberUtil.formatFolat(value, 0F));
        }else if(str.equals("boolean")){
            f1.setBoolean(o1,value.equalsIgnoreCase("true")?true:false);
        }else if(str.equals("class java.lang.Boolean")){
            f1.set(o1, value.equalsIgnoreCase("true") ? true : false);
        }else  if(str.equals("class java.lang.String")){
            f1.set(o1, value==null||value.equals("null")?"":value);
        }else if(str.equals("byte")){
            f1.setByte(o1,NumberUtil.formatByte(value,(byte)0));
        }else if(str.equals("class java.lang.Byte")) {
            f1.set(o1,NumberUtil.formatByte(value,(byte)0));
        }else if(str.equals("class java.lang.Short")) {
            f1.set(o1,NumberUtil.formatShort(value, (short) 0));
        }else if(str.equals("short")){
            f1.setShort(o1, NumberUtil.formatShort(value, (short)0));
        }
    }

}
