package cn.mn.mn;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

public class ListUtil {
    public static boolean isEmpty(List list) {
        if (list == null || list.size() < 1) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Collection c) {
        return !isNotEmpty(c);
    }
    public static boolean isNotEmpty(Collection c) {
        if (c != null && c.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isBank(List list) {
        return !isNotBank(list);
    }
    public static boolean isNotBank(List list) {
        if (list != null) {
            list.removeAll(Collections.singleton(null));
            if(list.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBank(Collection list) {
        return !isNotBank(list);
    }
    public static boolean isNotBank(Collection list) {
        if (list != null) {
            list.removeAll(Collections.singleton(null));
            if(list.size() > 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * 将list转换为根据某个属性的值作为key的map
     * @param list
     * @param fieldName4Key
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> listToMap(List<V> list,String fieldName4Key,Class<V> clazz) {
        Map<K, V> map = null;
        if (list != null) {
            try {
                map = new HashMap<K, V>(list.size());
                PropertyDescriptor propDesc = new PropertyDescriptor(fieldName4Key, clazz);
                Method methodGetKey = propDesc.getReadMethod();
                for (int i = 0; i < list.size(); i++) {
                    V value = list.get(i);
                    @SuppressWarnings("unchecked")
                    K key = (K) methodGetKey.invoke(list.get(i));
                    map.put(key, value);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("field can't match the key!");
            }
        }
        return map;
    }

    /**
     *
     * @param list
     * @param fieldName4Key
     * @param c
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, List<V>> listToMapList(List<V> list,String fieldName4Key,Class<V> c) {
        Map<K, List<V>> map = null;
        if (list != null) {
            try {
                map = new HashMap<K, List<V>>(list.size());
                PropertyDescriptor propDesc = new PropertyDescriptor(fieldName4Key, c);
                Method methodGetKey = propDesc.getReadMethod();
                for (int i = 0; i < list.size(); i++) {
                    V value = list.get(i);
                    @SuppressWarnings("unchecked")
                    K key = (K) methodGetKey.invoke(list.get(i));
                    if (map.get(key)!=null) {
                        map.get(key).add(value);
                    } else {
                        map.put(key, new ArrayList(Arrays.asList(value)));
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("field can't match the key!");
            }
        }
        return map;
    }

    /**
     * 将根据
     * @param src key集合
     * @param target 数据来源的map
     * @param <K>   key的类型
     * @param <T>   数据来源的类型
     * @return
     */
    public static <K,T> List<T> arrayToList(K[] src,Map<K,T> target) {
        List<T> result = new ArrayList<>();
        for (K obj : src) {
          T t = target.get(obj);
          if (t != null) {
              result.add(t);
          }
        }
        return result;
    }
}
