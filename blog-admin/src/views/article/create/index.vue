<template>
  <div class="create-container" style="box-shadow: 0 0 6px rgba(0, 0, 0, .06);border-radius: 8px; padding: 1%">
    <span
      style="color: #303133;font-size: 25px;margin-left: 20px"
    >{{ update ? '更新博文' : '新增博文' }}
    </span>
    <el-divider><i class="el-icon-tickets" /></el-divider>
    <el-form ref="article" :model="article" :rules="rules" label-width="100px" class="add-article">
      <el-form-item label="文章标题" prop="title">
        <el-input
          v-model="article.title"
          type="text"
          maxlength="50"
          style="width: 450px"
          placeholder="请输入文章标题"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="文章简介" prop="summary">
        <el-input
          v-model="article.summary"
          type="textarea"
          maxlength="150"
          placeholder="请输入文章简介"
          style="width: 660px"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="置顶">
        <el-switch v-model="article.isTop" />
      </el-form-item>
      <el-form-item label="分类" prop="tags">
        <el-select
          v-model="article.tags"
          multiple
          filterable
          multiple-limit="5"
          style="width: 450px"
          placeholder="请选择分类"
        >
          <el-option
            v-for="item in allCategory"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          />
        </el-select>
        <span style="margin-left: 15px;font-size: 13px;color: #909399;">
          * 第一个选择的标签将会作为主标签在博客详情首页展示 最多5项 *
        </span>
      </el-form-item>
      <el-form-item label="文章内容" prop="content">
        <markdown-editor
          ref="markdownEditor"
          v-model="article.content"
          language="zh"
          height="500px"
        />
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button type="success" round @click="submitArticle('article')">
          {{ update ? '更新' : '发布' }}
        </el-button>
        <el-button type="warning" round @click="resetArticle('article')">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import MarkdownEditor from '@/components/MarkdownEditor'
import { getCategory } from '@/api/category'
import { createArticle, getArticleById, updateArticle } from '@/api/article'

export default {
  name: 'CreateArticle',
  components: { MarkdownEditor },
  data() {
    return {
      update: false,
      allCategory: [],
      article: {
        title: '',
        summary: '',
        isTop: 0,
        content: '',
        tags: []
      },
      rules: {
        title: [
          { required: true, message: '标题内容不能为空', trigger: 'blur' }
        ],
        summary: [
          { required: true, message: '简介内容不能为空', trigger: 'change' },
          { min: 5, message: '简介内容不少于5个字符', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '文章主体不能为空', trigger: 'blur' }
        ],
        tags: [
          { required: true, message: '类别个数大于一个', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    const articleId = this.$route.params.articleId
    if (articleId != null) {
      getArticleById(articleId).then(response => {
        response.isTop = response.isTop === 1
        this.article = response
      })
      this.update = true
    }
    this.getCategory()
  },
  methods: {
    submitArticle(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.article.isTop = this.article.isTop ? 1 : 0
          if (!this.update) {
            this.createArticle(this.article).then(() => {
              this.resetArticle(formName)
            })
          } else {
            this.updateArticle(this.article).then(() => {
              this.resetArticle(formName)
            })
          }
        } else {
          return false
        }
      })
    },
    resetArticle(formName) {
      this.$refs[formName].resetFields()
      if (this.update) {
        this.$router.push('/article/list')
      }
    },
    getCategory() {
      getCategory().then(response => {
        this.allCategory = response
      })
    },
    createArticle(article) {
      createArticle(article).then(() => {
        this.$notify({
          title: '新增博文',
          message: '新增博文成功: ' + article.title,
          type: 'success',
          duration: 3000
        })
        this.$router.push('/article/list')
      }).catch(() => {
        this.$notify.error({
          title: '新增博文',
          message: '新增博文失败: ' + article.title,
          duration: 3000
        })
      })
    },
    updateArticle(article) {
      updateArticle(article.id, article).then(() => {
        this.$notify({
          title: '更新博文',
          message: '更新博文成功: ' + article.title,
          type: 'success',
          duration: 3000
        })
        this.$router.push('/article/list')
      }).catch(() => {
        this.$notify.error({
          title: '更新博文',
          message: '更新博文失败: ' + article.title,
          duration: 3000
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .create {
    &-container {
      margin: 30px;
      .add-article{
        margin-top: 20px;
      }
    }
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
</style>
