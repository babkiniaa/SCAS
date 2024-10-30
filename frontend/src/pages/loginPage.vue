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
          <q-input
              dense
              outlined
              v-model="username"
              label="Email"
              :error="!!errors.username"
              :error-message="errors.username"
            ></q-input>
            <q-input
              dense
              outlined
              v-model="password"
              type="password"
              label="Password"
              :error="!!errors.password"
              :error-message="errors.password"
            ></q-input>
        </q-card-section>
        <q-card-actions>
          <q-btn type="submit" style="border-radius: 8px;" color="dark" rounded size="md" label="Sign in" no-caps class="full-width"></q-btn>
      </q-card-actions>
      </q-form>
      <q-card-section class="text-center q-pt-none">
        <div class="text-grey-8">Don't have an account yet?
          <q-btn flat
          @click="goToRegister"
          label="Sign up."
          div
          class ="text-dark text-weight-bold"
          style="text-decoration: none"
          />
        </div>
        <div class="text-grey-8 q-mt-md">
          <q-btn flat
            @click="goToResetPassword"
            label="Forgot Password?"
            div
            class="text-dark text-weight-bold"
            style="text-decoration: none"
            :disable="isLoading"
          />
        </div>
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
      password: '',
      errors: {}
    }
  },
  methods: {
    async submitLogin () {
      try {
        this.errors = {}
        const response = await loginUser({
          username: this.username,
          password: this.password
        })
        const token = response.data.accessToken
        localStorage.setItem('jwtToken', token)
        const currentUserId = response.data.currentId
        localStorage.setItem('currentId', currentUserId)
        this.$q.notify({ message: response.data, color: 'green' })
        this.$router.push('/home')
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data
        } else {
          this.$q.notify({ message: 'Error of server', color: 'red' })
        }
      }
    },
    goToRegister () {
      this.$router.push('/register')
    },
    goToResetPassword () {
      this.$router.push('/reset-password')
    }
  }
}
</script>
