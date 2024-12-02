import axios from 'axios'

const api = axios.create({
  baseURL: 'https://manager-img-production.up.railway.app/user'
})
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwtToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)
export function getUserProfile (id) {
  return api.get(`/profile/${id}`)
}
export function updateUserProfile (userData) {
  return api.put('/profile', userData)
}
export function uploadUserAvatar (formData) {
  return api.post('/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
export function deleteUserAvatar () {
  return api.delete('/avatar')
}

export function getAvatar (id) {
  return api.get(`/avatar/${id}`)
}
