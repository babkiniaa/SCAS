<template>
  <q-page-container>
    <q-page class="flex flex-center bg-grey-2">
      <q-card class="q-pa-md shadow-2 my_card" bordered style="max-width: 400px;">
        <q-card-section class="text-center">
          <div class="text-grey-9 text-h5 text-weight-bold">Registration</div>
          <div class="text-grey-8">Create an account to get started</div>
        </q-card-section>
        <q-form @submit="submitRegister">
          <q-card-section>
            <q-input dense outlined v-model="email" label="Email Address"></q-input>
            <q-input dense outlined class="q-mt-md" v-model="username" label="Login"></q-input>
            <q-input dense outlined class="q-mt-md" v-model="password" type="password" label="Password"></q-input>
            <q-input dense outlined class="q-mt-md" v-model="passwordConfirm" type="password" label="Confirm Password"></q-input>
          </q-card-section>
          <q-card-actions>
            <q-btn
              :loading="isLoading"
              :disable="isLoading"
              type="submit"
              style="border-radius: 8px;"
              color="dark"
              rounded
              size="md"
              label="Sign up"
              no-caps
              class="full-width">
            </q-btn>
          </q-card-actions>
        </q-form>
        <q-card-section class="text-center q-pt-none">
          <div class="text-grey-8">Already have an account?
            <q-btn
              flat
              @click="goToLogin"
              label="Login."
              div
              class="text-dark text-weight-bold"
              style="text-decoration: none"
              :disable="isLoading"
            />
          </div>
        </q-card-section>
      </q-card>
    </q-page>
    <q-dialog v-model="isLoading" persistent>
      <q-card>
        <q-card-section class="row items-center justify-center">
          <q-spinner size="50px" />
          <div class="q-ml-md">We are sending you an email...</div>
        </q-card-section>
      </q-card>
    </q-dialog>
  </q-page-container>
</template>
<script>
import { registerUser } from 'src/services/authServices'
export default {
  data () {
    return {
      email: '',
      username: '',
      password: '',
      passwordConfirm: '',
      isLoading: false
    }
  },
  methods: {
    async submitRegister () {
      this.isLoading = true
      try {
        const response = await registerUser({
          email: this.email,
          username: this.username,
          password: this.password,
          passwordConfirm: this.passwordConfirm
        })
        this.$q.notify({ message: response.data, color: 'green' })
        setTimeout(() => {
          this.goToLogin()
        }, 1500)
      } catch (error) {
        this.$q.notify({ message: error.response.data, color: 'red' })
      } finally {
        this.isLoading = false
      }
    },
    goToLogin () {
      this.$router.push('/login')
    }
  }
}
</script>
