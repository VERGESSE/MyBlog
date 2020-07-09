package top.vergessen.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vergessen.blog.domain.Resource;
import top.vergessen.blog.mapper.ResourceMapper;
import top.vergessen.blog.service.ResourceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vergessen
 * @date 2020/7/5 16:13.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;

    @Override
    public String getRes(String key) {
        return resourceMapper.selectByPrimaryKey(key).getValue();
    }

    @Override
    public Map<String, String> getAllRes() {
        List<Resource> resources = resourceMapper.selectAll();
        HashMap<String, String> resMap = new HashMap<>(32);
        for (Resource resource : resources) {
            resMap.put(resource.getResKey(),resource.getValue());
        }
        return resMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean putRes(String key, String value) {
        Resource resource = Resource.builder().resKey(key).value(value).build();
        return resourceMapper.insertSelective(resource) > 1;
    }
}
