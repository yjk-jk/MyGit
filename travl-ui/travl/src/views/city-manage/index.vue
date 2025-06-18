<template>
  <div class="city-container">
    <el-card style="width: 100%; margin-bottom: 20px" shadow>
      <template #header>
        <div class="card-header">
          <span>城市管理</span>
          <el-button type="primary" @click="showAddDialog">添加城市</el-button>
        </div>
      </template>

      <el-table :data="cityList" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="城市名称" width="120" />
        <el-table-column prop="country" label="国家" width="120" />
        <el-table-column prop="bestTimeToVisit" label="最佳访问时间" width="120" />
        <el-table-column prop="imageUrl" label="图片URL" width="120">
          <template #default="scope">
            <img :src="scope.row.imageUrl" style="width: 100px; height: 100px" />
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="120" />
        <el-table-column prop="updatedAt" label="更新时间" width="120" />
        <el-table-column prop="description" label="描述" width="200" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button size="small" @click="handleUpload(scope.row)">上传图片</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle">
      <el-form :model="cityForm" label-width="120px">
        <el-form-item label="城市名称">
          <el-input v-model="cityForm.name" />
        </el-form-item>
        <el-form-item label="国家">
          <el-input v-model="cityForm.country" />
        </el-form-item>
        <el-form-item label="最佳访问时间">
          <el-input v-model="cityForm.bestTimeToVisit" />
        </el-form-item>
        <el-form-item label="城市图片">
          <el-upload
            class="upload-demo"
            :auto-upload="false"
            :on-change="handleFileChange"
            :show-file-list="false"
          >
            <el-button type="primary">选择图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="cityForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 上传图片对话框 -->
    <el-dialog v-model="uploadDialogVisible" title="上传城市图片">
      <el-upload
        class="upload-demo"
        :auto-upload="false"
        :on-change="handleManualUpload"
        :show-file-list="false"
      >
        <el-button type="primary">点击上传</el-button>
      </el-upload>
    </el-dialog>
  </div>
  <Footer />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { getAllCities, addCity, updateCity, deleteCity, uploadCityImage } from '@/api/cities'
import Footer from '@/display/footer/index.vue'
const cityList = ref([])
const dialogVisible = ref(false)
const uploadDialogVisible = ref(false)
const currentCityId = ref(null)
const dialogTitle = ref('添加城市')
const uploadFile = ref(null)

const cityForm = ref({
  id: null,
  name: '',
  country: '',
  bestTimeToVisit: '',
  imageUrl: '',
  description: '',
})

const uploadUrl = ref('')

// 获取城市列表
const fetchCities = async () => {
  try {
    const res = await getAllCities()
    cityList.value = res.data.data
    console.log('城市列表:', cityList.value)
  } catch (error) {
    console.error('获取城市列表失败', error)
  }
}

// 显示添加对话框
const showAddDialog = () => {
  dialogTitle.value = '添加城市'
  cityForm.value = {
    id: null,
    name: '',
    country: '',
    bestTimeToVisit: '',
    imageUrl: '',
    description: '',
  }
  dialogVisible.value = true
}

// 编辑城市
const handleEdit = (row) => {
  dialogTitle.value = '编辑城市'
  cityForm.value = { ...row }
  dialogVisible.value = true
}

// 文件选择处理
const handleFileChange = (file) => {
  uploadFile.value = file.raw
}

// 提交表单
const submitForm = async () => {
  try {
    if (cityForm.value.id) {
      // 编辑城市
      await updateCity(cityForm.value)
      if (uploadFile.value) {
        await uploadCityImage(cityForm.value.id, uploadFile.value)
      }
    } else {
      // 添加城市
      const res = await addCity(cityForm.value)
      if (uploadFile.value) {
        await uploadCityImage(res.data.data.id, uploadFile.value)
      }
    }
    dialogVisible.value = false
    uploadFile.value = null
    fetchCities()
  } catch (error) {
    console.error('操作失败:', error)
  }
}

// 删除城市
const handleDelete = async (row) => {
  try {
    await deleteCity(row.id)
    fetchCities()
  } catch (error) {
    console.error('删除失败:', error)
  }
}

// 手动上传处理
const handleManualUpload = async (file) => {
  try {
    const formData = new FormData()
    formData.append('file', file.raw)
    await uploadCityImage(currentCityId.value, file.raw)
    uploadDialogVisible.value = false
    fetchCities()
  } catch (error) {
    console.error('上传失败:', error)
  }
}

// 上传图片
const handleUpload = (row) => {
  currentCityId.value = row.id
  uploadDialogVisible.value = true
}

const store = useStore()
const router = useRouter()

onMounted(() => {
  if (store.state.user.role !== 'admin') {
    ElMessageBox.alert('管理员才能访问此页面！请联系管理员', '提示', {
      confirmButtonText: '确定',
      callback: () => router.push('/'),
    })
    return
  }
  fetchCities()
})
</script>

<style scoped>
.city-container {
  width: 100%;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
