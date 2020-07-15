<template>
  <div class="dashboard-container shadow">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './admin'

export default {
  name: 'Dashboard',
  components: { adminDashboard },
  data() {
    return {
      currentRole: 'adminDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (!this.roles.includes('admin')) {
      this.currentRole = 'editorDashboard'
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 14px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.shadow{
  box-shadow: 0 0 6px rgba(0, 0, 0, .06);
  border-radius: 8px;
  padding: 0.5%
}
</style>
