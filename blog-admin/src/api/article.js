import request from '@/utils/request'

// 获取博文总数
export function articleNum() {
  return request({
    url: '/article/num',
    method: 'get'
  })
}

export function createArticle(data) {
  return request({
    url: '/article/article',
    method: 'post',
    data
  })
}

export function updateArticle(id, data) {
  return request({
    url: '/article/' + id,
    method: 'put',
    data
  })
}

export function getArticleById(id) {
  return request({
    url: '/article/' + id,
    method: 'get'
  })
}

export function deleteArticle(id) {
  return request({
    url: '/article/' + id,
    method: 'delete'
  })
}

export function getArticleInfo(page) {
  return request({
    url: '/article/article?page=' + page.pageNum + '&size=' + page.pageSize + '&search=' + page.search + '&category=' + page.category,
    method: 'get'
  })
}

export function getArticleHasComment() {
  return request({
    url: '/article/commentArticle',
    method: 'get'
  })
}

export function getCommentsByArticleId(id) {
  return request({
    url: '/article/comment/' + id,
    method: 'get'
  })
}

export function deleteComment(id) {
  return request({
    url: '/article/comment/' + id,
    method: 'delete'
  })
}

// ========================== 外链API ==============================
export function addLink(data) {
  return request({
    url: '/article/link-n',
    method: 'post',
    data
  })
}

export function getLink(page) {
  return request({
    url: '/article/link?page=' + page.pageNum + '&size=' + page.pageSize,
    method: 'get'
  })
}

export function updateLink(data) {
  return request({
    url: '/article/link',
    method: 'put',
    data
  })
}

export function deleteLink(id) {
  return request({
    url: '/article/link?id=' + id,
    method: 'delete'
  })
}

export function getLinkNotCheck(page) {
  return request({
    url: '/article/link-n?page=' + page.pageNum + '&size=' + page.pageSize,
    method: 'get'
  })
}

