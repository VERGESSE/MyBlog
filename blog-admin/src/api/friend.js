import request from '@/utils/request'

export function addFriend(data) {
  return request({
    url: '/friend/friend-n',
    method: 'post',
    data
  })
}

export function getFriend() {
  return request({
    url: '/friend/friend',
    method: 'get'
  })
}

export function updateFriend(data) {
  return request({
    url: '/friend/friend',
    method: 'put',
    data
  })
}

export function deleteFriend(id) {
  return request({
    url: '/friend/friend?id=' + id,
    method: 'delete'
  })
}

export function getFriendNotCheck() {
  return request({
    url: '/friend/friend-n',
    method: 'get'
  })
}

