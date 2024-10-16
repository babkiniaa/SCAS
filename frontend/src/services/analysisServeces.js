import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/analysis'
})

export function downloadRepository (projectId) {
  return api.post('/report-download', { projectId })
}
export function runSpotBugs (projectId) {
  return api.post('/spotbugs-start', { projectId })
}
export function runPMD (projectId) {
  return api.post('/pmd-start', { projectId })
}
export function runOWASP (projectId) {
  return api.post('/owasp-start', { projectId })
}
export function runCheckstyle (projectId) {
  return api.post('/checkstyle-start', { projectId })
}
export function deleteProject (projectId) {
  return api.post('/delete-file', { projectId })
}
