package com.rbi.wx.wechatpay.util.jsonobjectword;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ApiJsonObject<T> extends JSONObject{
    public ApiJsonObject() {
        super();
    }

    public ApiJsonObject(Map<String, Object> map) {
        super(map);
    }

    public ApiJsonObject(boolean ordered) {
        super(ordered);
    }

    public ApiJsonObject(int initialCapacity) {
        super(initialCapacity);
    }

    public ApiJsonObject(int initialCapacity, boolean ordered) {
        super(initialCapacity, ordered);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return super.get(key);
    }

    @Override
    public JSONObject getJSONObject(String key) {
        return super.getJSONObject(key);
    }

    @Override
    public JSONArray getJSONArray(String key) {
        return super.getJSONArray(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        return super.getObject(key, clazz);
    }

    @Override
    public <T> T getObject(String key, Type type) {
        return super.getObject(key, type);
    }

    @Override
    public <T> T getObject(String key, TypeReference typeReference) {
        return super.getObject(key, typeReference);
    }

    @Override
    public Boolean getBoolean(String key) {
        return super.getBoolean(key);
    }

    @Override
    public byte[] getBytes(String key) {
        return super.getBytes(key);
    }

    @Override
    public boolean getBooleanValue(String key) {
        return super.getBooleanValue(key);
    }

    @Override
    public Byte getByte(String key) {
        return super.getByte(key);
    }

    @Override
    public byte getByteValue(String key) {
        return super.getByteValue(key);
    }

    @Override
    public Short getShort(String key) {
        return super.getShort(key);
    }

    @Override
    public short getShortValue(String key) {
        return super.getShortValue(key);
    }

    @Override
    public Integer getInteger(String key) {
        return super.getInteger(key);
    }

    @Override
    public int getIntValue(String key) {
        return super.getIntValue(key);
    }

    @Override
    public Long getLong(String key) {
        return super.getLong(key);
    }

    @Override
    public long getLongValue(String key) {
        return super.getLongValue(key);
    }

    @Override
    public Float getFloat(String key) {
        return super.getFloat(key);
    }

    @Override
    public float getFloatValue(String key) {
        return super.getFloatValue(key);
    }

    @Override
    public Double getDouble(String key) {
        return super.getDouble(key);
    }

    @Override
    public double getDoubleValue(String key) {
        return super.getDoubleValue(key);
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        return super.getBigDecimal(key);
    }

    @Override
    public BigInteger getBigInteger(String key) {
        return super.getBigInteger(key);
    }

    @Override
    public String getString(String key) {
        return super.getString(key);
    }

    @Override
    public Date getDate(String key) {
        return super.getDate(key);
    }

    @Override
    public java.sql.Date getSqlDate(String key) {
        return super.getSqlDate(key);
    }

    @Override
    public Timestamp getTimestamp(String key) {
        return super.getTimestamp(key);
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public JSONObject fluentPut(String key, Object value) {
        return super.fluentPut(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        super.putAll(m);
    }

    @Override
    public JSONObject fluentPutAll(Map<? extends String, ?> m) {
        return super.fluentPutAll(m);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public JSONObject fluentClear() {
        return super.fluentClear();
    }

    @Override
    public Object remove(Object key) {
        return super.remove(key);
    }

    @Override
    public JSONObject fluentRemove(Object key) {
        return super.fluentRemove(key);
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<Object> values() {
        return super.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return super.entrySet();
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args);
    }

    @Override
    public Map<String, Object> getInnerMap() {
        return super.getInnerMap();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toJSONString() {
        return super.toJSONString();
    }

    @Override
    public void writeJSONString(Appendable appendable) {
        super.writeJSONString(appendable);
    }

    @Override
    public <T> T toJavaObject(Class<T> clazz) {
        return super.toJavaObject(clazz);
    }

    @Override
    public <T> T toJavaObject(Type type) {
        return super.toJavaObject(type);
    }

    @Override
    public <T> T toJavaObject(TypeReference typeReference) {
        return super.toJavaObject(typeReference);
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {

    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {

    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        return false;
    }

    @Override
    public Object replace(String key, Object value) {
        return null;
    }

    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        return null;
    }

    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return null;
    }

    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return null;
    }

    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
