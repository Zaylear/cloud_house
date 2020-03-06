package com.rbi.interactive.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import java.util.ArrayList;
import java.util.List;

public class EncapsulationTreeUtil {

    /**
     * 把列表转换为树结构
     *
     * @param originalList 原始list数据
     * @param keyName 作为唯一标示的字段名称
     * @return 组装后的集合
     */
    public static <T> List<T> getTree(List<T> originalList, String keyName,String parentFieldName,String childrenFieldName) throws Exception {

        // 获取根节点，即找出父节点为空的对象
        List<T> topList = new ArrayList<>();
        for (int i = 0; i < originalList.size(); i++) {
            T t = originalList.get(i);
            String parentId = BeanUtils.getProperty(t, parentFieldName);
            if (StringUtils.isBlank(parentId)) {
                topList.add(t);
            }
        }

        // 将根节点从原始list移除，减少下次处理数据
        originalList.removeAll(topList);

        // 递归封装树
        fillTree(topList, originalList, keyName, parentFieldName, childrenFieldName);

        return topList;
    }



    /**
     * 封装树
     *
     * @param parentList 要封装为树的父对象集合
     * @param originalList 原始list数据
     * @param keyName 作为唯一标示的字段名称
     * @param parentFieldName 模型中作为parent字段名称
     * @param childrenFieldName 模型中作为children的字段名称
     */
    public static <T> void fillTree(List<T> parentList, List<T> originalList, String keyName, String parentFieldName, String childrenFieldName) throws Exception {
        for (int i = 0; i < parentList.size(); i++) {
            List<T> children = fillChildren(parentList.get(i), originalList, keyName, parentFieldName, childrenFieldName);
            if (children.isEmpty()) {
                continue;
            }
            originalList.removeAll(children);
            fillTree(children, originalList, keyName, parentFieldName, childrenFieldName);
        }
    }


    /**
     * 封装子对象
     *
     * @param parent 父对象
     * @param originalList 待处理对象集合
     * @param keyName 作为唯一标示的字段名称
     * @param parentFieldName 模型中作为parent字段名称
     * @param childrenFieldName 模型中作为children的字段名称
     */
    public static <T> List<T> fillChildren(T parent, List<T> originalList, String keyName, String parentFieldName, String childrenFieldName) throws Exception {
        List<T> childList = new ArrayList<>();
        String parentId = BeanUtils.getProperty(parent, keyName);
        for (int i = 0; i < originalList.size(); i++) {
            T t = originalList.get(i);
            String childParentId = BeanUtils.getProperty(t, parentFieldName);
            if (parentId.equals(childParentId)) {
                childList.add(t);
            }
        }
        if (!childList.isEmpty()) {
            FieldUtils.writeDeclaredField(parent, childrenFieldName, childList, true);
        }
        return childList;
    }



    /**

     - listToTree
     - <p>方法说明<p>
     - 将JSONArray数组转为树状结构
     - @param arr 需要转化的数据
     - @param id 数据唯一的标识键值
     - @param pid 父id唯一标识键值
     - @param child 子节点键值
     - @return JSONArray
     */
    public static JSONArray listToTree(JSONArray arr, String id, String pid, String child){
        JSONArray r = new JSONArray();
        JSONObject hash = new JSONObject();
        //将数组转为Object的形式，key为数组中的id
        for(int i=0;i<arr.size();i++){
            JSONObject json = (JSONObject) arr.get(i);
            hash.put(json.getString(id), json);
        }
        //遍历结果集
        for(int j=0;j<arr.size();j++){
            //单条记录
            JSONObject aVal = (JSONObject) arr.get(j);
            //在hash中取出key为单条记录中pid的值
            JSONObject hashVP = (JSONObject) hash.get(aVal.get(pid).toString());
            //如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
            if(hashVP!=null){
                //检查是否有child属性
                if(hashVP.get(child)!=null){
                    JSONArray ch = (JSONArray) hashVP.get(child);
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }else{
                    JSONArray ch = new JSONArray();
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }
            }else{
                r.add(aVal);
            }
        }
        return r;
    }

}
