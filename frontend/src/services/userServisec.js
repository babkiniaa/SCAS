import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/user'
})
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwtToken')
    console.log(token)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)
export function getUserProfile () {
  return api.get('/profile')
}
export function updateUserProfile (userData) {
  return api.put('/profile', userData)
}
