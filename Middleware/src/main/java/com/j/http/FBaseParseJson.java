/**
 * 
 */
package com.j.http;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jiangxuewu
 *
 */
public abstract class FBaseParseJson {
	//获取根节点的string
	protected abstract String getRootString();
	
	//获取list节点的string
	protected abstract String getListNodeString();
	
	/**
	 * 获取列表节点的string,子类可以重载
	 */
	protected abstract String getListSingleNodeString();
	
	//获取存储列表
	protected abstract <T> List<T> getStoreList();
	
	//解析节点
	protected abstract <T> T parseNode(JSONObject object);
	
	/**
	 * 判断解析的节点是否是root节点，比如从"jsonData"节点的开始解析，还是从"jsonData"的上一层开始解析，
	 * 默认是从上一层开始解析，所以需要取一下root节点
	 * @return
	 */
	protected boolean isParseFromRootObject(){
		return false;
	}
	
	/**
	 * 解析json数据，转成列表
	 * @param <T>
	 * @param object
	 * @return
	 */
	protected <T> List<T> parseList(JSONObject object){
		if(null == object) {
			return null;
		}
		
		//返回结果列表
		List<T> resultList = null;
		
		try {
			//如果已经是root节点了，无需取一次root节点
			JSONObject rootObject = object;
			if(!isParseFromRootObject()){
				String rootNode = getRootString();
				if(null == rootNode || rootNode.length() <= 0){
					return null;
				}
				
				if(!object.has(rootNode)){
					return null;
				}
				
				rootObject = object.getJSONObject(rootNode);
				if(null == rootObject){
					return null;
				}
			}
			
			//获取list 节点
			String listNode = getListNodeString();		
			if(null == listNode || listNode.length() <= 0){
				return null;
			}

			if(!rootObject.has(listNode)){
				return null;
			}
			
			JSONArray objArray = rootObject.getJSONArray(listNode);
			if(null == objArray || objArray.length() <= 0){
				return null;
			}
			
			//获取存储列表
			resultList = getStoreList();
			if(null == resultList){
				return null;
			}
			
			//遍历解析
			int count = objArray.length();
			for(int i = 0; i < count; i++){
				JSONObject nodeObject = objArray.getJSONObject(i);
				if(null == nodeObject){
					continue;
				}
				
				T node = parseNode(nodeObject);
				if(null != node) {
					resultList.add(node);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	
	protected <T> List<T> parseListGet(JSONObject object) {
		if(null == object) {
			return null;
		}
		//返回结果列表
		List<T> resultList = null;
		
		return resultList;
	}
}
