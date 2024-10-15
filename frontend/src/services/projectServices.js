import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/projects'
})

export const createProject = async (projectData) => {
  return await api.post('/create', projectData)
}
