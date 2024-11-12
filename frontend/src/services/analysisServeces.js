import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080'
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
export function reportCreate (projectData) {
  return api.post('/report/create', projectData)
}

export function getAnalizator () {
  return api.get('/analysis/get-hashmap')
}
