<template>
  <q-layout view="lHh lpr lFf">
    <q-page-container>
      <q-page :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
        <div class="flex justify-end q-pa-md">
          <q-btn
            dense
            round
            :icon="isDarkMode ? 'light_mode' : 'dark_mode'"
            @click="toggleDarkMode"
            aria-label="Toggle Dark Mode"
            class="text-white"
            style="background-color: black;"
          />
        </div>
        <q-card :class="['q-pa-md', 'shadow-2', 'my_card', isDarkMode ? 'bg-grey-8 text-white' : '']" bordered style="max-width: 400px;">
          <q-card-section class="text-center">
            <div :class="isDarkMode ? 'text-white' : 'text-grey-9'">Sign in</div>
            <div :class="isDarkMode ? 'text-grey-5' : 'text-grey-8'">Sign in below to access your account</div>
          </q-card-section>
          <q-form @submit="submitLogin">
            <q-card-section>
              <q-input
                dense
                outlined
                v-model="username"
                label="Email"
                :class="isDarkMode ? 'bg-grey-8 text-white' : ''"
                :error="!!errors.username"
                :error-message="errors.username"
              ></q-input>
              <q-input
                dense
                outlined
                v-model="password"
                type="password"
                label="Password"
                :class="isDarkMode ? 'bg-grey-8 text-white' : ''"
                :error="!!errors.password"
                :error-message="errors.password"
              ></q-input>
            </q-card-section>
            <q-card-actions>
              <q-btn
                type="submit"
                style="border-radius: 8px;"
                color="dark"
                rounded
                size="md"
                label="Sign in"
                no-caps
                class="full-width"
              ></q-btn>
            </q-card-actions>
          </q-form>
          <q-card-section class="text-center q-pt-none">
            <div :class="isDarkMode ? 'text-grey-5' : 'text-grey-8'">Don't have an account yet?
              <q-btn
                flat
                @click="goToRegister"
                label="Sign up."
                div
                class="text-dark text-weight-bold"
                :class="isDarkMode ? 'text-white' : 'text-dark'"
                style="text-decoration: none"
              />
            </div>
            <div :class="[isDarkMode ? 'text-grey-5' : 'text-grey-8', 'q-mt-md']">
              <q-btn
                flat
                @click="goToResetPassword"
                label="Forgot Password?"
                div
                class="text-dark text-weight-bold"
                :class="isDarkMode ? 'text-white' : 'text-dark'"
                style="text-decoration: none"
                :disable="isLoading"
              />
            </div>
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { loginUser } from 'src/services/authServices'

export default {
  data () {
    return {
      username: '',
      password: '',
      errors: {},
      isDarkMode: Dark.isActive
    }
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
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
        this.$q.notify({ message: 'Welcome', color: 'green' })
        // eslint-disable-next-line eqeqeq
        if (response.data.role == 'ADMIN') {
          localStorage.setItem('role', 'admin')
          this.$router.push('/admin')
        } else {
          this.$router.push('/home')
        }
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

<style scoped>
.my_card {
  max-width: 600px;
  margin: 16px auto;
}
.bg-dark {
  background-color: #121212;
}
.text-white {
  color: #ffffff;
}
</style>
