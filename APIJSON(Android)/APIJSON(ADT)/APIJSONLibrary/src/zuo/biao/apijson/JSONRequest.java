/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson;

import static zuo.biao.apijson.StringUtil.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**encapsulator for request JSONObject
 * @author Lemon
 * @see #toArray
 * @see RequestUtil
 * @use JSONRequest request = new JSONRequest(...);
 * <br> request.put(...);//not a must
 * <br> request.toArray(...);//not a must
 */
public class JSONRequest extends JSONObject {

	private static final long serialVersionUID = -2223023180338466812L;

	public JSONRequest() {
		super();
	}
	/**
	 * encode = true
	 * {@link #JSONRequest(String, Object)}
	 * @param object
	 */
	public JSONRequest(Object object) {
		this(null, object);
	}
	/**
	 * encode = true
	 * {@link #JSONRequest(String, Object, boolean)}
	 * @param name
	 * @param object
	 */
	public JSONRequest(String name, Object object) {
		this(name, object, true);
	}
	/**
	 * {@link #JSONRequest(String, Object, boolean)}
	 * @param object
	 * @param encode
	 */
	public JSONRequest(Object object, boolean encode) {
		this(null, object, encode);
	}
	/**
	 * {@link #put(String, Object, boolean)}
	 * @param name
	 * @param object
	 * @param encode
	 */
	public JSONRequest(String name, Object object, boolean encode) {
		this();
		put(name, object, encode);
	}



	public static final String KEY_TAG = "tag";
	/**set tag
	 * @param tag
	 * @return
	 */
	public JSONObject setTag(String tag) {
		put(KEY_TAG, tag);
		return this;
	}
	public String getTag() {
		return getString(KEY_TAG);
	}




	//array object <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String KEY_COUNT = "count";
	public static final String KEY_PAGE = "page";

	public JSONRequest setCount(int count) {
		put(KEY_COUNT, count);
		return this;
	}
	public int getCount() {
		return getIntValue(KEY_COUNT);
	}

	public JSONRequest setPage(int page) {
		put(KEY_PAGE, page);
		return this;
	}
	public int getPage() {
		return getIntValue(KEY_PAGE);
	}
	//array object >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>




	/**
	 * @param value
	 * @param parts path = parts[0] + "/" + parts[1] + "/" + parts[2] + ...
	 * @return
	 */
	public Object putPath(String key, String... parts) {
		String path = "";
		if (parts != null) {
			for (int i = 0; i < parts.length; i++) {
				path += (i > 0 ? "/" : "") + parts[i];
			}
		}
		return put(key, path);
	}

	/**
	 * decode = true
	 * @param key
	 * return {@link #get(Object, boolean)}
	 */
	@Override
	public Object get(Object key) {
		return get(key, true);
	}
	/**
	 * @param key
	 * @param decode if decode && value instanceof String, value = URLDecoder.decode((String) value, UTF_8)
	 * @return 
	 */
	public Object get(Object key, boolean decode) {
		Object value = super.get(key);
		if (decode && value instanceof String) {
			try {
				return URLDecoder.decode((String) value, UTF_8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * encode = true
	 * @param value
	 * @return {@link #put(String, Object)}
	 */
	public Object put(Object value) {
		return put(value, true);
	}
	/**
	 * @param value
	 * @param encode
	 * @return {@link #put(String, Object, boolean)}
	 */
	public Object put(Object value, boolean encode) {
		return put(null, value, encode);
	}	
	/**
	 * encode = true
	 * @param key
	 * @param value
	 * return {@link #put(String, Object, boolean)}
	 */
	@Override
	public Object put(String key, Object value) {
		return put(key, value, true);
	}
	/**
	 * @param key => StringUtil.isNotEmpty(key, true) ? key : value.getClass().getSimpleName()
	 * @param value URLEncoder.encode((String) value, UTF_8);
	 * @param encode if value instanceof String >> value = URLEncoder.encode((String) value, UTF_8);
	 * @return
	 */
	public Object put(String key, Object value, boolean encode) {
		if (encode && value instanceof String) {
			try {
				value = URLEncoder.encode((String) value, UTF_8);
				//just encode /, not need to encode [] 	? URLEncoder.encode(key, UTF_8) 
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return super.put(StringUtil.isNotEmpty(key, true) ? key : value.getClass().getSimpleName(), value);
	}


	/**create a parent JSONObject named KEY_ARRAY
	 * @param count
	 * @param page
	 * @return {KEY_ARRAY:this}
	 */
	public JSONRequest toArray(int count, int page) {
		return toArray(count, page, null);
	}
	/**create a parent JSONObject named name+KEY_ARRAY
	 * @param count
	 * @param page
	 * @param name
	 * @return {name+KEY_ARRAY : this}
	 */
	public JSONRequest toArray(int count, int page, String name) {
		return new JSONRequest(StringUtil.getString(name) + KEY_ARRAY, this.setCount(count).setPage(page));
	}


	/**设置搜索
	 * @param key
	 * @param value
	 */
	public void putSearch(String key, String value) {
		putSearch(key, value, SEARCH_TYPE_CONTAIN_FULL);
	}
	/**设置搜索
	 * @param key
	 * @param value
	 * @param type
	 */
	public void putSearch(String key, String value, int type) {
		if (key == null) {
			key = "";
		}
		if (key.endsWith("$") == false) {
			key += "$";
		}
		put(key, getSearch(value, type), true);
	}

	public static final int SEARCH_TYPE_CONTAIN_FULL = 0;
	public static final int SEARCH_TYPE_CONTAIN_ORDER = 1;
	public static final int SEARCH_TYPE_CONTAIN_SINGLE = 2;
	public static final int SEARCH_TYPE_CONTAIN_ANY = 3;
	public static final int SEARCH_TYPE_START = 4;
	public static final int SEARCH_TYPE_END = 5;
	public static final int SEARCH_TYPE_START_SINGLE = 6;
	public static final int SEARCH_TYPE_END_SINGLE = 7;
	public static final int SEARCH_TYPE_PART_MATCH = 8;
	/**
	 * SQL中NOT LIKE就行？？
	 */
	public static final int SEARCH_TYPE_NO_CONTAIN = 9;
	/**
	 * SQL中NOT LIKE就行？？
	 */
	public static final int SEARCH_TYPE_NO_PART_MATCH = 10;
	/**获取搜索值
	 * @param key
	 * @return
	 */
	public static String getSearch(String key) {
		return getSearch(key, SEARCH_TYPE_CONTAIN_FULL);
	}
	/**获取搜索值
	 * @param key
	 * @param type
	 * @return
	 */
	public static String getSearch(String key, int type) {
		return getSearch(key, type, true);
	}
	/**获取搜索值
	 * @param key
	 * @param type
	 * @param ignoreCase
	 * @return
	 */
	public static String getSearch(String key, int type, boolean ignoreCase) {
		if (key == null) {
			return null;
		}
		switch (type) {
		case SEARCH_TYPE_CONTAIN_SINGLE:
			return "_" + key + "_";
		case SEARCH_TYPE_CONTAIN_ORDER:
			char[] cs = key.toCharArray();
			if (cs == null) {
				return null;
			}
			String s = "%";
			for (int i = 0; i < cs.length; i++) {
				s += cs[i] + "%";
			}
			return s;
		case SEARCH_TYPE_START:
			return key + "%";
		case SEARCH_TYPE_END:
			return "%" + key;
		case SEARCH_TYPE_START_SINGLE:
			return key + "_";
		case SEARCH_TYPE_END_SINGLE:
			return "_" + key;
		case SEARCH_TYPE_NO_CONTAIN:
			return "[^" + key + "]";
		case SEARCH_TYPE_NO_PART_MATCH:
			cs = key.toCharArray();
			if (cs == null) {
				return null;
			}
			s = "";
			for (int i = 0; i < cs.length; i++) {
				s += getSearch("" + cs[i], SEARCH_TYPE_NO_CONTAIN, ignoreCase);
			}
			return s;
		default:
			return "%" + key + "%";
		}
	}

	
}
