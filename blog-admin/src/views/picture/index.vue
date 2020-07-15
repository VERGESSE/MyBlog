<template>
  <div class="picture-container shadow">
    <el-row>
      <el-col :span="5">
        <span style="font-size: 14px;color: #606266;font-family: 'Helvetica Neue',Helvetica,'PingFang SC','Hiragino Sans GB','Microsoft YaHei','微软雅黑',Arial,sans-serif;">
          图片浏览：
        </span>
        <el-select v-model="type" placeholder="请选择" @change="getPictures">
          <el-option
            v-for="item in types"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-col>

      <el-col span="5" :offset="14">
        <el-button
          type="primary"
          plain
          @click="addPicByUrl"
        >
          新增图片(链接)
        </el-button>
        <el-button
          type="primary"
          plain
        >
          <el-upload class="upload-demo" action="" :auto-upload="false" :show-file-list="false" :on-change="changeUpload">
            新增图片(文件)
          </el-upload>
        </el-button>
      </el-col>
    </el-row>
    <el-dialog title="图片剪裁" :visible.sync="dialogVisible" :close-on-press-escape="false" :close-on-click-modal="false" append-to-body>
      <div class="cropper-content">
        <div class="cropper" style="text-align:center">
          <vueCropper
            ref="cropper"
            :img="option.img"
            :output-size="option.size"
            :output-type="option.outputType"
            :info="true"
            :can-scale="option.canScale"
            :auto-crop="option.autoCrop"
            :auto-crop-width="option.autoCropWidth"
            :auto-crop-height="option.autoCropHeight"
            :fixed-box="option.fixedBox"
            :fixed="option.fixed"
            :fixed-number="option.fixedNumber"
            :can-move="option.canMove"
            :can-move-box="option.canMoveBox"
            :original="option.original"
            :center-box="option.centerBox"
            :info-true="option.infoTrue"
            :full="option.full"
            :enlarge="option.enlarge"
            :mode="option.mode"
          />
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="finish">确认</el-button>
      </div>
    </el-dialog>

    <el-row style="margin-top: 6px">
      <el-col v-for="pic in picturesInfo.list" :key="pic" :span="imgSetUp.slot" style="padding: 5px">
        <el-card shadow="hover" :body-style="{ padding: '10px' }">
          <el-image
            :style="{width: imgSetUp.width, height: imgSetUp.height}"
            :src="pic.pictureUrl"
            preview-src-list
            fit="cover"
          />
          <div style="padding: 8px;">
            <div class="time">{{ pic.createBy }}</div>
            <div class="bottom clearfix" align="right">
              <el-button type="text" class="button" @click="gotoPic(pic.pictureUrl)">
                查看大图
              </el-button>
              <el-button type="text" class="button" @click="copyUrl(pic.pictureUrl)">
                复制链接
              </el-button>
              <el-button type="text" class="button" @click="deletePicture(pic.id)">
                删除
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-pagination
      class="pagination"
      hide-on-single-page
      :current-page.sync="picturesInfo.pageNum"
      :page-size="picturesInfo.pageSize"
      layout="prev, pager, next"
      :total="picturesInfo.total"
      :page-count="picturesInfo.pages"
      @current-change="getPictures"
    />
  </div>
</template>

<script>
import { addPicByFile, addPicByUrl, getPicture, deletePicture } from '@/api/picture'
import { VueCropper } from 'vue-cropper'

export default {
  name: 'Picture',
  components: { VueCropper },
  data() {
    return {
      type: 1,
      types: [{
        label: '前台首页图片首页',
        value: 0
      }, {
        label: '文章题图',
        value: 1
      }, {
        label: '博客页头部图片',
        value: 2
      }, {
        label: '后台登录页图片',
        value: 3
      }],
      picturesInfo: {
        pageNum: 1,
        pageSize: 12,
        total: 0,
        list: []
      },
      // 裁剪组件的基础配置option
      option: {
        img: '', // 裁剪图片的地址
        outputSize: 0.5, // 裁剪生成图片的质量
        outputType: 'jpeg', // 裁剪生成图片的格式
        info: true, // 裁剪框的大小信息
        canScale: true, // 图片是否允许滚轮缩放
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 800, // 默认生成截图框宽度
        autoCropHeight: 800, // 默认生成截图框高度
        fixedBox: true, // 固定截图框大小 不允许改变
        fixed: true, // 是否开启截图框宽高固定比例
        fixedNumber: [0.65, 1], // 截图框的宽高比例
        canMove: true, // 上传图片是否可以移动
        canMoveBox: false, // 截图框能否拖动
        original: false, // 上传图片按照原始比例渲染
        centerBox: true, // 截图框是否被限制在图片里面
        infoTrue: true, // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
        full: false, // 是否输出原图比例的截图
        enlarge: '1', // 图片根据截图框输出比例倍数
        mode: 'contain' // 图片默认渲染方式
      },
      // 防止重复提交
      loading: false,
      dialogVisible: false,
      imgSetUp: { slot: 4, width: '300px', height: '400px' }
    }
  },
  created() {
    this.getPictures()
  },
  methods: {
    getPictures() {
      // 根据图片类型确定宽高比例
      if (this.type === 0) {
        this.imgSetUp = { slot: 4, width: '224px', height: '360px' }
      } else if (this.type === 1) {
        this.imgSetUp = { slot: 6, width: '352px', height: '210px' }
      } else if (this.type === 2) {
        this.imgSetUp = { slot: 8, width: '480px', height: '130px' }
      } else if (this.type === 3) {
        this.imgSetUp = { slot: 6, width: '352px', height: '200px' }
      }
      this.picturesInfo.type = this.type
      getPicture(this.picturesInfo).then(response => {
        this.picturesInfo = response
      })
    },
    addPicByUrl() {
      this.$prompt('请输入新的图片地址，记得宽高比正确哦', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        addPicByUrl({ type: this.type, url: value }).then(() => {
          this.$notify({
            title: '添加图片',
            message: '已添加: ' + value,
            type: 'success',
            duration: 3000
          })
          this.getPictures()
        })
      })
    },
    deletePicture(id) {
      deletePicture(id).then(() => {
        this.$notify({
          title: '删除图片成功',
          type: 'success',
          duration: 3000
        })
        this.getPictures()
      })
    },
    // 上传按钮 限制图片大小
    changeUpload(file) {
      const isLt5M = file.size / 1024 / 1024 < 20
      if (!isLt5M) {
        this.$message.error('上传文件大小不能超过 20MB!')
        return false
      }
      // 根据图片类型确定宽高比例
      if (this.type === 0) {
        this.option.fixedNumber = [0.65, 1]
        this.option.enlarge = '3'
      } else if (this.type === 1) {
        this.option.fixedNumber = [1.5, 1]
        this.option.enlarge = '1.2'
      } else if (this.type === 2) {
        this.option.fixedNumber = [3.9, 1]
        this.option.enlarge = '2.5'
      } else if (this.type === 3) {
        this.option.fixedNumber = [1.85, 1]
        this.option.enlarge = '3'
      }

      // 上传成功后将图片地址赋值给裁剪框显示图片
      this.$nextTick(() => {
        this.option.img = URL.createObjectURL(file.raw)
        this.dialogVisible = true
      })
    },
    // 点击裁剪，这一步是可以拿到处理后的地址
    finish() {
      // 获取截图的base64 数据
      this.$refs.cropper.getCropData((data) => {
        // 上传文件到服务器
        this.loading = true
        // const file = this.blobToFile(this.dataURLtoBlob(data), 'file')
        const formData = new FormData()
        formData.append('file', this.dataURLtoFile(data))
        formData.append('type', this.type)
        addPicByFile(formData).then(() => {
          this.loading = false
          this.dialogVisible = false
          this.$notify({
            title: '添加图片成功',
            type: 'success',
            duration: 3000
          })
          this.getPictures()
        })
      })
    },
    // 将base64转换为文件
    dataURLtoFile(dataurl, filename) {
      const arr = dataurl.split(',')
      const mime = arr[0].match(/:(.*?);/)[1]
      const bstr = atob(arr[1])
      let n = bstr.length
      const u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new File([u8arr], filename, { type: mime })
    },
    gotoPic(picUrl) {
      window.open(picUrl, '_blank')
    },
    // 复制图片链接到剪切板
    copyUrl(picUrl) {
      const textarea = document.createElement('textarea')
      textarea.textContent = picUrl
      textarea.style.position = 'fixed'
      document.body.appendChild(textarea)
      textarea.select()
      try {
        document.execCommand('copy')
      } catch (ex) {
        return false
      } finally {
        document.body.removeChild(textarea)
      }
      this.$notify({
        title: '图片链接已复制到剪切板',
        type: 'success',
        duration: 3000
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
  .picture {
    &-container {
      margin: 15px;

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
  .shadow{
    box-shadow: 0 0 6px rgba(0, 0, 0, .06);
    border-radius: 8px;
    padding: 0.5%;
    min-height: 800px;
  }
  .time{
    font-size: 15px;
    color: #909399;
    text-align: right;
  }
  .file-image{
    width: 2000px;
    height: 2000px;
    border: 1px solid #cccccc;
    margin: 15px 0;
  }

  /* 截图 */
  /* .cropper-content {} */
  .cropper {
    width: auto;
    height: 550px;
  }
</style>
