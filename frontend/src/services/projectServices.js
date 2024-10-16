import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/projects'
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
export const createProject = async (projectData) => {
  return await api.post('/create', projectData)
}
