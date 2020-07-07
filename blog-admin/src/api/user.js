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
    url: '/admin/getPic',
    method: 'get'
  })
}

