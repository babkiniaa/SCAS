<template>
  <q-page-container>
    <q-page class="flex flex-center bg-grey-2">
      <q-card class="q-pa-md shadow-2 my_card" bordered style="max-width: 400px;">
        <q-card-section class="text-center">
          <div class="text-grey-9 text-h5 text-weight-bold">Enter Email</div>
          <div class="text-grey-8">Please enter your email to verify your identity</div>
        </q-card-section>
        <q-form @submit="submitEmail">
          <q-card-section>
            <q-input dense
            outlined
            v-model="email"
            label="Email Address"
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
              class="full-width">
            </q-btn>
          </q-card-actions>
        </q-form>
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
import { sendVerificationEmail } from 'src/services/passwordServices'
export default {
  data () {
    return {
      email: '',
      isLoading: false,
      errors: {}
    }
  },
  methods: {
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
