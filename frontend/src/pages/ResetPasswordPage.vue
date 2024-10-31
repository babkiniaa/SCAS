<template>
  <q-layout view="lHh lpr lFf">
    <q-page-container>
      <q-page class="flex flex-center bg-grey-2">
        <q-card class="q-pa-md shadow-2" bordered style="max-width: 400px;">
          <q-card-section class="text-center">
            <div class="text-grey-9 text-h5 text-weight-bold">Reset Password</div>
            <div class="text-grey-8">A 6-digit code has been sent to your email for password reset</div>
          </q-card-section>
          <q-card-section>
            <div class="row justify-center q-mt-md">
              <q-input
                v-for="(digit, index) in codeDigits"
                :key="index"
                ref="codeInput"
                maxlength="1"
                dense
                outlined
                v-model="codeDigits[index]"
                class="q-mx-xs text-center"
                style="width: 2.5em;"
                @input="moveToNextField(index)"
              />
            </div>
            <div v-if="errors.token" class="text-negative text-caption text-center q-mt-xs">{{ errors.token }}</div>
          </q-card-section>
          <q-form @submit="submitResetPassword">
            <q-card-section>
              <q-input dense
              outlined
              v-model="password"
              type="password"
              label="New Password"
              :error="!!errors.password"
              :error-message="errors.password"
              class="q-mt-md"/>
              <q-input dense
              outlined
              v-model="confirmPassword"
              type="password"
              label="Confirm Password"
              :error="!!errors.passwordConfirm"
              :error-message="errors.passwordConfirm"
              class="q-mt-md" />
            </q-card-section>
            <q-card-actions>
              <q-btn type="submit" color="dark" rounded size="md" label="Reset Password" no-caps class="full-width"/>
            </q-card-actions>
          </q-form>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>
<script>
import { changePassword } from 'src/services/passwordServices'
export default {
  data () {
    return {
      codeDigits: Array(6).fill(''),
      password: '',
      confirmPassword: '',
      errors: {}
    }
  },
  methods: {
    async submitResetPassword () {
      try {
        await changePassword({
          password: this.password,
          passwordConfirm: this.confirmPassword,
          token: this.codeDigits.join('')
        })
        this.$q.notify({ message: 'Password reset successfully', color: 'green' })
        this.goToLogin()
      } catch (error) {
        if (error.response && error.response.data) {
          this.errors = error.response.data
        } else {
          this.$q.notify({ message: 'Error of server', color: 'red' })
        }
      }
    },
    goToLogin () {
      this.$router.push('/login')
    },
    moveToNextField (index) {
      if (this.codeDigits[index].length === 1 && index < this.codeDigits.length - 1) {
        this.$refs.codeInput[index + 1].focus()
      }
    }
  }
}
</script>
