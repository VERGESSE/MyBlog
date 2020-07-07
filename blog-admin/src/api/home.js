import request from '@/utils/request'

export function todayVisitors() {
  return request({
    url: '/sys/todayVisitors',
    method: 'get'
  })
}

export function yearVisitors() {
  return request({
    url: '/sys/yearVisitors',
    method: 'get'
  })
}

export function todayVisitorNum() {
  return request({
    url: '/sys/todayVisitorNum',
    method: 'get'
  })
}

export function yearVisitorNum() {
  return request({
    url: '/sys/yearVisitorNum',
    method: 'get'
  })
}
