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
            <div :class="isDarkMode ? 'text-white' : 'text-grey-9'">Registration</div>
            <div :class="isDarkMode ? 'text-grey-5' : 'text-grey-8'">Create an account to get started</div>
          </q-card-section>
          <q-form @submit="submitRegister">
            <q-card-section>
              <q-input
                dense
                outlined
                v-model="email"
                label="Email"
                :class="isDarkMode ? 'bg-grey-8 text-white' : ''"
                :error="!!errors.email"
                :error-message="errors.email"
              ></q-input>
              <q-input
                dense
                outlined
                v-model="username"
                label="Login"
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
              <q-input
                dense
                outlined
                v-model="passwordConfirm"
                type="password"
                label="Confirm Password"
                :class="isDarkMode ? 'bg-grey-8 text-white' : ''"
                :error="!!errors.passwordConfirm"
                :error-message="errors.passwordConfirm"
              ></q-input>
            </q-card-section>
            <q-card-actions>
              <q-btn
                :loading="isLoading"
                :disable="isLoading"
                type="submit"
                color="dark"
                rounded
                size="md"
                label="Sign up"
                no-caps
                class="full-width"
                style="border-radius: 8px;"
              ></q-btn>
            </q-card-actions>
          </q-form>
          <q-card-section class="text-center q-pt-none">
            <div :class="isDarkMode ? 'text-grey-5' : 'text-grey-8'">Already have an account?
              <q-btn
                flat
                @click="goToLogin"
                label="Login."
                div
                class="text-weight-bold"
                :class="isDarkMode ? 'text-white' : 'text-dark'"
                style="text-decoration: none"
                :disable="isLoading"
              />
            </div>
            <div :class="[isDarkMode ? 'text-grey-5' : 'text-grey-8', 'q-mt-md']">
              <q-btn
                flat
                @click="goToResetPassword"
                label="Forgot Password?"
                div
                class="text-weight-bold"
                :class="isDarkMode ? 'text-white' : 'text-dark'"
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
            <div class="q-ml-md" :class="isDarkMode ? 'text-white' : ''">We are sending you an email...</div>
          </q-card-section>
        </q-card>
      </q-dialog>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { registerUser } from 'src/services/authServices'

export default {
  data() {
    return {
      email: '',
      username: '',
      password: '',
      passwordConfirm: '',
      isLoading: false,
      errors: {},
      isDarkMode: Dark.isActive
    }
  },
  methods: {
    toggleDarkMode() {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async submitRegister() {
      this.isLoading = true
      this.errors = {}
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
        if (error.response && error.response.data) {
          this.errors = error.response.data
        } else {
          this.$q.notify({ message: 'Error of server', color: 'red' })
        }
      } finally {
        this.isLoading = false
      }
    },
    goToLogin() {
      this.$router.push('/login')
    },
    goToResetPassword() {
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
