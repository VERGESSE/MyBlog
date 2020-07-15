import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/admin/login?username=' + data.username + '&password=' + data.password,
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/admin/user/info',
    method: 'get'
  })
}

export function getPic() {
  return request({
    url: '/admin/pic',
    method: 'get'
  })
}

export function getRes() {
  return request({
    url: '/admin/res',
    method: 'get'
  })
}

export function putRes(data) {
  return request({
    url: '/admin/res',
    method: 'post',
    data
  })
}
