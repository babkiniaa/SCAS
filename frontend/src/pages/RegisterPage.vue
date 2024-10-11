<template>
<q-page class="q-pa-md bg-white flex flex-center">
  <q-card class="q-pa-md bg-blue-grey-2 q-ma-auto" style="max-width: 400px; border-radius: 16px;">
    <q-card-section class="text-center">
      <div class="text-h4 text-indigo-14 text-weight-bolder">SCAS</div>
    </q-card-section>
    <q-card-section>
      <div class="text-h5 text-indigo-14 text-center text-weight-bolder">Registration</div>
    </q-card-section>
    <q-form @submit="submitRegister">
      <q-card-section>
        <q-input filled v-model="email" label="Email" />
        <q-input filled v-model="login" label="Login" />
        <q-input filled v-model="password" type="password" label="Password" />
        <q-input filled v-model="passwordConfirm" type="password" label="Confirm Password" />
      </q-card-section>
      <q-card-actions align="center">
        <q-btn type="submit" label="Sing up" color="primary" />
      </q-card-actions>
    </q-form>
    <q-card-section align="center">
      <q-btn flat @click="goToLogin" label="Already have an account? Login" div class ="text-h6 text-indigo-14 text-center text-weight-bolder" />
    </q-card-section>
  </q-card>
</q-page>
</template>
<script>
import { registerUser } from 'src/services/authServices'
export default {
  data () {
    return {
      email: '',
      login: '',
      password: '',
      passwordConfirm: ''
    }
  },
  methods: {
    async submitRegister () {
      try {
        const response = await registerUser({
          email: this.email,
          login: this.login,
          password: this.password,
          passwordConfirm: this.passwordConfirm
        })
        this.$q.notify({ message: response.data, color: 'green' })
        setTimeout(1500)
        this.goToLogin()
      } catch (error) {
        this.$q.notify({ message: error.response.data, color: 'red' })
      }
    },
    goToLogin () {
      this.$router.push('/login')
    }
  }
}
</script>
