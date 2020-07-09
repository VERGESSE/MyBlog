package top.vergessen.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.vergessen.blog.domain.Message;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByMessage(Message message) {
        if (message.getIp() == null){
            throw new BlogException(ExceptionEnum.MESSAGE_UPDATE_ERROR);
        }
        int update = messageMapper.updateByPrimaryKeySelective(message);
        return update == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMsgById(Message message) {
        if (message.getIp() == null){
            return false;
        }
        int delete = messageMapper.deleteByPrimaryKey(message.getId());
        return delete == 1;
    }
}
