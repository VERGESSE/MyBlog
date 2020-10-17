<template>
  <div class="information-container shadow">
    <el-row style="margin: 10px">
      <el-col span="3" :offset="21">
        <el-button
          type="primary"
          plain
          @click="putRes"
        >
          修改个人信息
        </el-button>
      </el-col>
    </el-row>
    <el-form label-width="100px">
      <el-form-item
        v-for="item in value"
        :key="item.resKey"
        :label="item.resKey"
        prop="summary"
      >
        <el-input
          v-model="item.value"
          type="textarea"
          autosize
          placeholder="请输入文章简介"
          style="width: 660px"
          show-word-limit
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getRes, putRes } from '@/api/user'
export default {
  name: 'Information',
  data() {
    return {
      value: ''
    }
  },
  created() {
    getRes().then(response => {
      this.value = response
    })
  },
  methods: {
    putRes() {
      putRes(this.value).then(() => {
        this.$notify({
          title: '修改个人信息成功',
          type: 'success',
          duration: 3000
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .information {
    &-container {
      margin: 15px;
    }
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
  .shadow{
    box-shadow: 0 0 6px rgba(0, 0, 0, .06);
    border-radius: 8px;
    padding: 0.5%;
    min-height: 800px;
  }
  .editor-container{
    position: relative;
    height: 100%;
  }
</style>
