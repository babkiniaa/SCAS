<template>

<q-page-container>
  <q-page class="flex flex-center bg-grey-2">
    <q-card class="q-pa-md shadow-2 my_card" bordered>
      <q-card-section class="text-center">
        <div class="text-grey-9 text-h5 text-weight-bold">Sign in</div>
        <div class="text-grey-8">Sign in below to access your account</div>
      </q-card-section>
      <q-form @submit="submitLogin">
        <q-card-section>
          <q-input dense outlined v-model="username" label="Login or Email Address"></q-input>
          <q-input dense outlined class="q-mt-md" v-model="password" type="password" label="Password"></q-input>
        </q-card-section>
        <q-card-actions>
          <q-btn type="submit" style="border-radius: 8px;" color="dark" rounded size="md" label="Sign in" no-caps class="full-width"></q-btn>
      </q-card-actions>
      </q-form>
      <q-card-section class="text-center q-pt-none">
        <div class="text-grey-8">Don't have an account yet?
          <q-btn flat @click="goToRegister" label="Signup." div class ="text-dark text-weight-bold" style="text-decoration: none" /></div>
      </q-card-section>
    </q-card>
  </q-page>
</q-page-container>
</template>
<script>
import { loginUser } from 'src/services/authServices'
export default {
  data () {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    async submitLogin () {
      try {
        const response = await loginUser({
          username: this.username,
          password: this.password
        })
        const token = response.data.accessToken
        console.log(token)
        localStorage.setItem('jwtToken', token)
        console.log(localStorage.getItem('jwtToken'))
        this.$q.notify({ message: response.data, color: 'green' })
      } catch (error) {
        this.$q.notify({ message: error.response.data, color: 'red' })
      }
    },
    goToRegister () {
      this.$router.push('/register')
    }
  }
}
</script>
