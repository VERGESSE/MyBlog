<template>
  <div class="analyze-container">
    <div class="temp">待做 敬请期待 ...</div>
  </div>
</template>

<script>
export default {
  name: 'Analyze'
}
</script>

<style lang="scss" scoped>
  .analyze {
    &-container {
      margin: 30px;
      .temp{
        text-align: center;
        margin-top: 200px;
        font-size: 50px;
        color: #99a9bf;
      }
    }
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
</style>
