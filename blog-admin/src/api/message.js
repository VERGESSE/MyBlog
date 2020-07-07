import request from '@/utils/request'

export function messageNum() {
  return request({
    url: '/message/messageNum',
    method: 'get'
  })
}

export function getMsg(pageInfo) {
  return request({
    url: '/message/msg?page=' + pageInfo.pageNum + '&size=' + pageInfo.pageSize,
    method: 'get'
  })
}

export function changeShow(data) {
  return request({
    url: '/message/msg',
    method: 'put',
    data
  })
}

export function deleteMsg(data) {
  return request({
    url: '/message/msg',
    method: 'delete',
    data
  })
}
