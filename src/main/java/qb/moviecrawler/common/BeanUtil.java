package qb.moviecrawler.common;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import qb.moviecrawler.database.model.DownloadLink;
import qb.moviecrawler.database.model.Movie;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 描述：实体工具类
 * author qiaobin   2016/10/11 16:25.
 */
public class BeanUtil {

    /**
      * 功能描述：对象转Map
      * @author qiaobin
      * @date 2016/10/11  16:26
      * @param obj 待转对象
      */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        Field[] superDeclaredFields =  obj.getClass().getSuperclass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        for (Field field : superDeclaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * 功能描述：Map转对象
     * @author qiaobin
     * @date 2016/10/11  16:26
     * @param map
     * @param beanClass
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
        for (Field field : superFields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    /**
      * 功能描述：对象合并
      * @author qiaobin
      * @date 2016/10/11  16:30
      * @param combinedObj 被合并对象
      * @param obj 合并对象
      */
    public static Object combineObject(Object combinedObj, Object obj) throws Exception {
        Map<String, Object> combinedMap = objectToMap(combinedObj);
        Map<String, Object> objMap = objectToMap(obj);
        for (String key : objMap.keySet()) {
            if (combinedMap.containsKey(key) && (null != objMap.get(key))) {
                //删除旧KEY
                combinedMap.remove(key);
                combinedMap.put(key, objMap.get(key));
            }
        }
        return mapToObject(combinedMap, combinedObj.getClass());
    }

    /**
      * 功能描述：对象合并
      * @author qiaobin
      * @date 2016/10/11  16:30
      * @param combinedObj 被合并对象
      * @param objMap 合并对象
      */
    public static Object combineObject(Object combinedObj, Map<String, Object> objMap) throws Exception {
        Map<String, Object> combinedMap = objectToMap(combinedObj);
        for (String key : objMap.keySet()) {
            if (combinedMap.containsKey(key) && (null != objMap.get(key))) {
                //删除旧KEY
                combinedMap.remove(key);
                combinedMap.put(key, objMap.get(key));
            }
        }
        return mapToObject(combinedMap, combinedObj.getClass());
    }

    /**
      * 功能描述：对象合并
      * @author qiaobin
      * @date 2016/10/11  16:30
      * @param combinedMap 被合并对象
      * @param objMap 合并对象
      */
    public static Map<String, Object> combineMap(Map<String, Object> combinedMap, Map<String, Object> objMap) throws Exception {
        for (String key : objMap.keySet()) {
            if (combinedMap.containsKey(key) && (null != objMap.get(key).toString())) {
                //删除旧KEY
                combinedMap.remove(key);
                combinedMap.put(key, objMap.get(key).toString());
            }
        }
        return combinedMap;
    }


    /**
     * 功能描述：电影信息补全
     * @author qiaobin
     * @date 2016/10/11  16:30
     * @param savedMovie 被合并对象
     * @param movie 合并对象
     */
    public static Movie combineMovie(Movie savedMovie, Movie movie){
        try {
            List<DownloadLink> combineList = new ArrayList<>();
            //-----------比较下载链接，并生成链接合集
            if (null != savedMovie && !CollectionUtils.isEmpty(movie.getLinks())) {   //如果有记录存在， 比较下载链接，如果不存在新链接则保存
                List<DownloadLink> savedLinks = savedMovie.getLinks(); //保存的下载链接
                List<DownloadLink> newLinks = movie.getLinks();  //新爬取的下载链接
                if (CollectionUtils.isEmpty(savedLinks)) {  //如果库中没有下载链接
                    combineList = newLinks;
                } else { //如果库中有下载链接，那么将新的连接合并进来
                    for (DownloadLink newLink : newLinks) {
                        int index = 0;
                        DownloadLink l = null;
                        for (DownloadLink savedLink : savedLinks) {
                            l = newLink;
                            if (savedLink.getLink().equals(newLink.getLink())) {
                                index++;
                                continue;
                            }
                        }
                        if (index == 0) {
                            combineList.add(l);
                        }
                    }
                    combineList.addAll(savedLinks);
                }
            }
            //-----------对象合并，填补不为空字段
            Map<String, Object> combinedMap = objectToMap(savedMovie);
            Map<String, Object> objMap = objectToMap(movie);
            for (String key : combinedMap.keySet()) {
                try {
                    if (!StringUtils.isNotEmpty(combinedMap.get(key).toString()) && (!key.equals("links")) && (!key.equals("comments")) && StringUtils.isNotEmpty(objMap.get(key).toString())) {
                        combinedMap.put(key, objMap.get(key));
                    }
                } catch (Exception e) {
                }
            }
            Movie newMovie = (Movie)BeanUtil.mapToObject(combinedMap, Movie.class);
            newMovie.setLinks(combineList);
            newMovie.setLastTime(new Date());
            return newMovie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
