<template>
  <div class="list-container" style="box-shadow: 0 0 6px rgba(0, 0, 0, .06);border-radius: 8px; padding: 1%">
    <el-table
      :data="articleList.list"
      stripe
      style="width: 100%"
    >
      <el-table-column label="文章ID" prop="id" min-width="150" align="center" />
      <el-table-column label="文章标题" prop="title" min-width="350" align="center" />
      <el-table-column label="置顶" prop="isTop" min-width="60" align="center">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.isTop === 1 ? 'success' : 'info'"
            disable-transitions
          >
            {{ scope.row.isTop === 0 ? '否' : '是' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column lable="文章标签" prop="tags" min-width="260" align="center">
        <template slot="header" slot-scope="scope">
          <el-select
            v-model="category"
            filterable
            clearable
            :value="scope"
            placeholder="文章标签"
            @change="getArticles"
          >
            <el-option
              v-for="item in allCategory"
              :key="item.id"
              :label="item.name"
              :value="item.name"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.number }} 文章</span>
            </el-option>
          </el-select>
        </template>
        <template slot-scope="scope">
          <el-tag v-for="tag in scope.row.tags.split('|')" :key="tag" size="mini" style="margin: 3px">
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="阅读量" prop="traffic" min-width="70" align="center" />
      <el-table-column label="创建时间" prop="createBy" min-width="180" align="center" />
      <el-table-column min-width="200" prop="search">
        <template slot="header" slot-scope="scope">
          <el-input
            v-model.trim="search"
            type="text"
            placeholder="输入关键字搜索"
            @input="getArticles(scope.row)"
          >
            <i slot="suffix" class="el-input__icon el-icon-search" />
          </el-input>
        </template>
        <template slot-scope="scope">
          <el-button size="mini" type="info" round @click="articleDetail(scope.row)">
            详情
          </el-button>
          <el-button size="mini" type="success" round @click="editArticle(scope.row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" round @click="deleteArticle(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination"
      hide-on-single-page
      :current-page.sync="articleList.pageNum"
      :page-size="articleList.pageSize"
      layout="prev, pager, next, total"
      :total="articleList.total"
      :page-count="articleList.pages"
      @current-change="getArticles"
    />
  </div>
</template>

<script>
import { getArticleInfo, deleteArticle } from '@/api/article'
import { getCategory } from '@/api/category'

export default {
  data() {
    return {
      articleList: {
        pageNum: 1,
        pageSize: 12,
        total: 0,
        list: []
      },
      search: '',
      category: '',
      allCategory: [],
      host: 'http://127.0.0.1'
    }
  },
  mounted() {
    this.getCategory()
    this.getArticles()
    this.host = 'http://' + window.location.host
  },
  methods: {
    getArticles() {
      this.articleList.search = this.search
      this.articleList.category = this.category
      getArticleInfo(this.articleList).then(response => {
        this.articleList = response
      })
    },
    articleDetail(row) {
      window.open(this.host + '/article/' + row.id, '_blank')
    },
    editArticle(row) {
      this.$router.push({ name: 'CreateArticle', params: { articleId: row.id }})
    },
    deleteArticle(row) {
      this.$confirm('此操作将永久删除博文, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteArticle(row.id).then(() => {
          this.$notify({
            title: '删除文章',
            message: '删除文章成功: ' + row.title,
            type: 'success',
            duration: 3000
          })
          this.getArticles()
        })
      })
    },
    getCategory() {
      getCategory().then(response => {
        this.allCategory = response
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .list {
    &-container {
      margin: 20px;
      .pagination{
        margin-top: 7px;
        text-align: center;
      }
    }
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
</style>
