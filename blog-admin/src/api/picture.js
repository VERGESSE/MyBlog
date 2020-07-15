import request from '@/utils/request'

export function addPicByUrl(params) {
  return request({
    url: '/picture/url',
    method: 'post',
    params: params
  })
}

export function addPicByFile(data) {
  return request({
    url: '/picture/file',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    method: 'post',
    data
  })
}

export function getPicture(params) {
  return request({
    url: '/picture/picture?page=' + params.pageNum + '&size=' + params.pageSize + '&type=' + params.type,
    method: 'get'
  })
}

export function deletePicture(id) {
  return request({
    url: '/picture/picture?id=' + id,
    method: 'delete'
  })
}

