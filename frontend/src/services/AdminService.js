import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/admin'
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
export function getCount () {
  return api.get('/agent/count-queue')
}

export function getAllTask () {
  return api.get('/agent/get-run-task')
}

export function deleteTask (taskId) {
  return api.post(`/agent/task/ban/${taskId}`)
}

export function fetchUsers () {
  return api.get('/users')
}

export function blockUserById (userId) {
  return api.post(`/blockUser?userId=${userId}`)
}
