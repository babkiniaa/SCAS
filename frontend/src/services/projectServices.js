import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/project'
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
export function createProject (projectData) {
  return api.post('/create', projectData)
}

export function getProjects (projectData) {
  return api.post('/get-projects', projectData)
}
