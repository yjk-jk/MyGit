<template>
  <div></div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const router = useRouter()

onMounted(() => {
  ElMessageBox.alert('暂不支持此功能！', '提示', {
    confirmButtonText: '确定',
    callback: () => {
      router.go(-1) // 返回上一页
    },
  })
})
</script>
