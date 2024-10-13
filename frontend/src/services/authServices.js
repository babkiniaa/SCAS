import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/auth'
})

export const registerUser = async (registrationData) => {
  return await api.post('/register', registrationData)
}

export const loginUser = async (loginData) => {
  console.log(loginData)
  return await api.post('/login', loginData, {
    withCredentials: true
  })
}

export const verifyUser = async (verificationCode) => {
  return await api.get('/verify', { params: { code: verificationCode } })
}
