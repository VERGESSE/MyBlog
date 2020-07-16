<template>
  <div class="comment-container shadow">
    <el-collapse v-model="activeName" accordion @change="handleChange">
      <el-collapse-item
        v-for="article in articles"
        :key="article.id"
        :title="article.title"
        :name="article.id"
      >
        <div v-for="comment in comments" :key="comment.id" class="comments">
          <div style="color: #3a8ee6">
            <div style="float: left">{{ comment.name }} : </div>
            <div style="margin-left: 100px">{{ comment.createBy }}</div>
            <el-button
              type="danger"
              size="small"
              class="comment-btn"
              round
              plain
              @click="deleteComment(comment.name,comment.id)"
            >
              删除
            </el-button>
          </div>
          <div>{{ comment.content }}</div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
import { getArticleHasComment, getCommentsByArticleId, deleteComment } from '@/api/article'

export default {
  name: 'Comment',
  data() {
    return {
      articles: [],
      activeName: '',
      comments: []
    }
  },
  created() {
    getArticleHasComment().then(response => {
      this.articles = response
      this.handleChange(response[0].id)
    })
  },
  methods: {
    handleChange(val) {
      console.log(val)
      getCommentsByArticleId(val).then(response => {
        this.comments = response
      })
    },
    deleteComment(activeName, id) {
      this.$confirm('此操作将永久删除该评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteComment(id).then(() => {
          this.$notify({
            title: '删除评论成功',
            type: 'success',
            duration: 3000
          })
          getArticleHasComment().then(response => {
            this.articles = response
            this.handleChange(response[0].id)
            this.activeName = activeName
          })
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .comment {
    &-container {
      margin: 30px;
      .comments{
        box-shadow: 0 2px 4px rgba(0, 0, 0, .08);
        border-radius: 10px;
        padding: 6px;
        margin-bottom: 6px;
      }
      .comment-btn{
        float: right;
        margin-bottom: 10px;
      }
    }
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
  .shadow{
    box-shadow: 0 0 6px rgba(0, 0, 0, .06);
    border-radius: 8px;
    padding: 1%
  }
</style>
