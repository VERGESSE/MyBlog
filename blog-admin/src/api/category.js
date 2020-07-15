import request from '@/utils/request'

// ============================= Category =======================================

export function getCategory() {
  return request({
    url: '/catetech/category',
    method: 'get'
  })
}
// 新增分类
export function addCategory(data) {
  return request({
    url: '/catetech/category',
    method: 'post',
    data
  })
}
export function updateCategory(data) {
  return request({
    url: '/catetech/category',
    method: 'put',
    data
  })
}
export function deleteCategory(data) {
  return request({
    url: '/catetech/category',
    method: 'delete',
    data
  })
}

// ============================= Technology =======================================

export function getTechnology() {
  return request({
    url: '/catetech/technology',
    method: 'get'
  })
}
// 新增技术栈
export function addTechnology(data) {
  return request({
    url: '/catetech/technology',
    method: 'post',
    data
  })
}
export function updateTechnology(data) {
  return request({
    url: '/catetech/technology',
    method: 'put',
    data
  })
}
export function deleteTechnology(data) {
  return request({
    url: '/catetech/technology',
    method: 'delete',
    data
  })
}
