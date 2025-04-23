import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/password'
})

export const sendVerificationEmail = async(email) => {
  return await api.post('/change', email)
}

export const changePassword = async(newPassword) => {
  return await api.post('/change-password', newPassword)
}
