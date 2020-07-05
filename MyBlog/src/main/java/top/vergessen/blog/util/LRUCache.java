package top.vergessen.blog.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU 缓存队列
 * 参考: https://juejin.im/post/5d382c8de51d4577407b1e26
 * @author Vergessen
 * @date 2020/7/5 14:59.
 */
public class LRUCache<K, V> extends LinkedHashMap<K,V> {

    private final int MAX_CACHE_SIZE;

    public LRUCache(int cacheSize){
        // accessOrder要设置为true，开启后会get方法会有额外操作保证链表顺序按访问顺序逆序排列
        super((int) Math.ceil(cacheSize / 0.75) + 1,
                0.75f, true);
        MAX_CACHE_SIZE = cacheSize;
    }

    /**
     * 可能有多个线程同时访问，保证线程安全
     */
    @Override
    public synchronized V get(Object key) {
        return super.get(key);
    }
    @Override
    public synchronized V put(K key, V value) {
        return super.put(key, value);
    }

    /**
     * 删除最老的节点（头结点）
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 缓存大小超过阈值时进行LRU淘汰
        return size() > MAX_CACHE_SIZE;
    }
}
