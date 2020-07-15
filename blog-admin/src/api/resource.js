import request from '@/utils/request'

// 根据键获取资源
export function getRes(key) {
  return request({
    url: '/resource/' + key,
    method: 'get'
  })
}

// 根据键更新资源
export function updateRes(key, value) {
  return request({
    url: '/resource/' + key + '?value=' + value,
    method: 'put'
  })
}
