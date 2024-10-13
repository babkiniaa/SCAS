<template>
  <q-page class="q-pa-md">
    <q-card class="q-pa-md q-ma-auto" style="max-width: 400px;">
      <q-card-section>
        <h2>Email Verification</h2>
        <p>Please wait while we verify your email...</p>
      </q-card-section>
    </q-card>
  </q-page>
</template>
<script>
import axios from 'axios'
export default {
  data () {
    return {
      verificationCode: this.$route.query.code
    }
  },
  mounted () {
    this.verifyEmail()
  },
  methods: {
    async verifyEmail () {
      try {
        const response = await axios.get(`/auth/verify?code=${this.verificationCode}`)
        if (response.status === 200) {
          this.$q.notify({
            message: 'Verification successful. You can now log in.',
            color: 'positive'
          })
          this.$router.push('/login')
        }
      } catch (error) {
        this.$q.notify({
          message: error.response.data || 'Verification failed.',
          color: 'negative'
        })
      }
    }
  }
}
</script>
