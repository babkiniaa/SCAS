<template>
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
      <q-card :class="['q-pa-md', 'shadow-2', isDarkMode ? 'bg-grey-8 text-white' : '', 'my_card']" bordered style="max-width: 400px;">
        <q-card-section class="text-center">
          <div :class="isDarkMode ? 'text-white' : 'text-grey-9'">Enter Email</div>
          <div :class="isDarkMode ? 'text-grey-5' : 'text-grey-8'">Please enter your email to verify your identity</div>
        </q-card-section>
        <q-form @submit="submitEmail">
          <q-card-section>
            <q-input
              dense
              outlined
              v-model="email"
              label="Email Address"
              :class="isDarkMode ? 'bg-grey-9 text-white' : ''"
              :error="!!errors.email"
              :error-message="errors.email"
            ></q-input>
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
              label="Send Email"
              no-caps
              class="full-width"
            ></q-btn>
          </q-card-actions>
        </q-form>
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
</template>

<script>
import { Dark } from 'quasar'
import { sendVerificationEmail } from 'src/services/passwordServices'

export default {
  data () {
    return {
      email: '',
      isLoading: false,
      errors: {},
      isDarkMode: Dark.isActive
    }
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async submitEmail () {
      this.isLoading = true
      this.errors = {}
      try {
        const response = await sendVerificationEmail({ email: this.email })
        this.$q.notify({ message: response.data, color: 'green' })
        this.$router.push('/reset-password-page')
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data
        } else {
          this.$q.notify({ message: 'Error of server', color: 'red' })
        }
      } finally {
        this.isLoading = false
      }
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
