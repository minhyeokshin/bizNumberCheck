<script setup>
import { ref } from 'vue'
import axios from 'axios'

const businessNumber = ref('')
const selectedFile = ref(null)
const loading = ref(false)
const result = ref(null)
const error = ref(null)

const validateImageFile = (file) => {
  // 허용된 이미지 파일 형식
  const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg']
  
  // 파일 크기 제한 (5MB)
  const maxSize = 5 * 1024 * 1024

  if (!allowedTypes.includes(file.type)) {
    error.value = '이미지 파일만 업로드 가능합니다. (JPEG, PNG)'
    return false
  }

  if (file.size > maxSize) {
    error.value = '파일 크기는 5MB 이하여야 합니다.'
    return false
  }

  return true
}

const handleFileUpload = (event) => {
  const file = event.target.files[0]
  error.value = null
  
  if (file && validateImageFile(file)) {
    selectedFile.value = file
  } else {
    // 파일이 유효하지 않은 경우 input 초기화
    event.target.value = ''
    selectedFile.value = null
  }
}

const validateBusinessNumber = async () => {
  if (!businessNumber.value || !selectedFile.value) {
    error.value = '사업자 등록번호와 사업자 등록증을 모두 입력해주세요.'
    return
  }

  loading.value = true
  error.value = null
  result.value = null

  try {
    const formData = new FormData()
    formData.append('businessNumber', businessNumber.value)
    formData.append('file', selectedFile.value)

    const response = await axios.post('http://localhost:8080/api/bizCheck', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    result.value = response.data
  } catch (err) {
    error.value = err.response?.data?.message || '검증 중 오류가 발생했습니다.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="container">
    <h1>사업자 등록번호 검증</h1>
    
    <div class="input-section">
      <div class="input-group">
        <label for="businessNumber">사업자 등록번호</label>
        <input 
          type="text" 
          id="businessNumber"
          v-model="businessNumber"
          placeholder="사업자 등록번호를 입력하세요 (예: 123-45-67890)"
          maxlength="12"
        />
      </div>

      <div class="input-group">
        <label for="businessLicense">사업자 등록증</label>
        <input 
          type="file" 
          id="businessLicense"
          accept="image/jpeg,image/png,image/jpg"
          @change="handleFileUpload"
        />
        <p class="file-info" v-if="selectedFile">
          선택된 파일: {{ selectedFile.name }}
          <span class="file-size">({{ (selectedFile.size / 1024 / 1024).toFixed(2) }}MB)</span>
        </p>
        <p class="file-hint">* JPEG, PNG 파일만 가능 (최대 5MB)</p>
      </div>

      <button 
        @click="validateBusinessNumber" 
        class="submit-btn"
        :disabled="loading"
      >
        {{ loading ? '검증 중...' : '검증하기' }}
      </button>

      <div v-if="error" class="error-message">
        {{ error }}
      </div>

      <div v-if="result" class="result-section">
        <h2>검증 결과</h2>
        <div class="result-grid">
          <div class="result-item">
            <h3>입력한 사업자 번호</h3>
            <p>{{ businessNumber }}</p>
          </div>
          <div class="result-item">
            <h3>OCR로 인식한 사업자 번호</h3>
            <p>{{ result.ocrNumber || '인식 실패' }}</p>
          </div>
          <div class="result-item ai-result">
            <h3>AI 판독 결과</h3>
            <p :class="result.valid ? 'TRUE' : 'FALSE'">
              {{ result.valid ? '유효한 사업자 등록증' : '유효하지 않은 사업자 등록증' }} <br><br>
              {{ result.message }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 2rem;
}

.input-section {
  background-color: #f8f9fa;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.input-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
}

input[type="text"] {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

input[type="file"] {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
}

.file-info {
  margin-top: 0.5rem;
  font-size: 0.9rem;
  color: #666;
}

.file-hint {
  margin-top: 0.5rem;
  font-size: 0.8rem;
  color: #666;
  font-style: italic;
}

.file-size {
  color: #666;
  font-size: 0.9rem;
  margin-left: 0.5rem;
}

.submit-btn {
  width: 100%;
  padding: 1rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background-color: #45a049;
}

.submit-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.error-message {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 4px;
  text-align: center;
}

.result-section {
  margin-top: 2rem;
  padding: 1.5rem;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.result-section h2 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 1.5rem;
}

.result-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

.result-item {
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 6px;
  text-align: center;
}

.result-item h3 {
  color: #2c3e50;
  font-size: 1rem;
  margin-bottom: 0.5rem;
}

.result-item p {
  font-size: 1.1rem;
  font-weight: 500;
  margin: 0;
}

.result-item p.valid {
  color: #2e7d32;
}

.result-item p.invalid {
  color: #c62828;
}

/* AI 판독 결과 박스가 두 칸 차지 */
.ai-result {
  grid-column: 1 / span 2;
}

@media (max-width: 768px) {
  .result-grid {
    grid-template-columns: 1fr;
  }
  .ai-result {
    grid-column: auto;
  }
}
</style>
