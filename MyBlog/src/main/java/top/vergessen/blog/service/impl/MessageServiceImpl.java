package top.vergessen.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.vergessen.blog.domain.Message;
import top.vergessen.blog.mapper.MessageMapper;
import top.vergessen.blog.service.MessageService;

/**
 * @author Vergessen
 * @date 2020/7/4 21:43.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    @Override
    public Integer selectMessageCount() {
        return messageMapper.selectCountByExample(new Example(Message.class));
    }

    @Override
    public PageInfo<Message> selectPageMessage(Integer page, Integer size) {
        return PageHelper.startPage(page, size)
                .doSelectPageInfo(messageMapper::selectAllOrderByTime);
    }
}
