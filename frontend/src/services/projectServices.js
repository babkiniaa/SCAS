import axios from 'axios'

const api = axios.create({
  baseURL: 'https://manager-img-production.up.railway.app/project'
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

export function getProject (id) {
  return api.get(`/get-project/${id}`, id)
}

export function connect (ids) {
  return api.post('/connecting-report-owasp', ids)
}

export function connect1 (ids) {
  return api.post('/connecting-report-pmd', ids)
}
