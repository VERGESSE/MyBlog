package top.vergessen.blog.service;

import top.vergessen.blog.domain.Resource;

import java.util.List;

/**
 * 资源映射服务
 * @author Vergessen
 * @date 2020/7/5 16:12.
 */
public interface ResourceService {

    /**
     * 根据传入的key获取对应的资源值
     * @param key key
     * @return
     */
    String getRes(String key);

    /**
     * 获取资源的映射值
     * @return 全部资源值
     */
    List<Resource> getAllRes();

    /**
     * 根据传入信息新增或修改资源值
     * @param key key
     * @param value key
     * @return 返回是否添加成功
     */
    boolean putRes(String key, String value);
}
