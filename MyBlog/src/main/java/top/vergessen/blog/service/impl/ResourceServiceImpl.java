package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vergessen.blog.domain.Resource;
import top.vergessen.blog.mapper.ResourceMapper;
import top.vergessen.blog.service.ResourceService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Vergessen
 * @date 2020/7/5 16:13.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceServiceImpl implements ResourceService {

    /**
     * 用于存储所有的资源信息(按插入顺序保存)
     */
    private LinkedHashMap<String,String> map = new LinkedHashMap<>();
    /**
     * 读写锁保证修改和写入的线程安全
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private final ResourceMapper resourceMapper;

    /**
     * Spring容器加载完成后执行此初始化动作
     */
    @PostConstruct
    public void init(){
        List<Resource> resourceList = resourceMapper.selectAllOrderByTime();
        for (Resource resource : resourceList) {
            map.put(resource.getResKey(), resource.getValue());
        }
    }

    @Override
    public String getRes(String key) {
        try {
            lock.readLock().lock();
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Resource> getAllRes() {
        try {
            lock.readLock().lock();
            ArrayList<Resource> resources = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resources.add(Resource.builder()
                                .resKey(entry.getKey())
                                .value(entry.getValue())
                                .build());
            }
            return resources;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean putRes(String key, String value) {
        try {
            lock.writeLock().lock();
            if (map.get(key) == null){
                return false;
            }
            map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
        Resource resource = Resource.builder().resKey(key).value(value).build();
        return resourceMapper.updateByPrimaryKeySelective(resource) > 1;
    }
}
