import request from '@/utils/request'

export function messageNum() {
  return request({
    url: '/message/messageNum',
    method: 'get'
  })
}

export function getMsg(pageInfo) {
  return request({
    url: '/message/getMsg?page=' + pageInfo.pageNum + '&size=' + pageInfo.pageSize,
    method: 'get'
  })
}
