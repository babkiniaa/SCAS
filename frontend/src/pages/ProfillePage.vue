<template>
  <q-layout view="lHh lpr lFf">
    <q-header elevated class="bg-grey-9">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">User Profile</q-toolbar-title>
        <q-space />
        <q-avatar size="42px" class="q-ml-md">
          <img v-if="user.avatar" :src="user.avatar" alt="User Avatar" />
          <q-icon v-else name="person" class="text-white" />
        </q-avatar>
      </q-toolbar>
    </q-header>
    <q-drawer
      v-model="drawer"
      show-if-above
      :mini="miniState"
      @mouseenter="miniState = false"
      @mouseleave="miniState = true"
      :width="200"
      :breakpoint="500"
      bordered
      content-class="bg-grey-9"
    >
      <q-list padding>
        <q-item clickable v-ripple @click="goToHome">
          <q-item-section avatar>
            <q-icon name="home" />
          </q-item-section>
          <q-item-section class="text-black">Home</q-item-section>
        </q-item>
        <q-item clickable v-ripple @click="goToAllProjects">
          <q-item-section avatar>
            <q-icon name="folder_open" />
          </q-item-section>
          <q-item-section class="text-black">All Projects</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>
    <q-page-container>
      <q-page class="q-pa-md">
        <q-card class="q-pa-lg my-card" bordered style="max-width: 900px; margin: 0 auto;">
          <q-form @submit="submitProfile">
            <q-card-section class="row q-col-gutter-md">
              <div class="col-12">
                <q-input v-model="user.email" outlined label="Email" dense />
              </div>
            </q-card-section>
            <q-card-section class="row q-col-gutter-md q-pt-none">
              <div class="col-12">
                <q-input v-model="user.login" outlined label="Login" dense />
              </div>
            </q-card-section>
            <q-card-section>
              <q-btn label="Save changes" color="primary" class="q-mt-md full-width" type="submit" />
            </q-card-section>
          </q-form>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>
<script>
import { getUserProfile, updateUserProfile } from 'src/services/userServisec'
export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      user: {
        email: '',
        login: '',
        avatar: null
      }
    }
  },
  async created () {
    await this.loadUserProfile()
  },
  methods: {
    async loadUserProfile () {
      try {
        const response = await getUserProfile()
        this.user = response.data
      } catch (error) {
        this.$q.notify({ message: 'Error loading profile', color: 'red' })
      }
    },
    async submitProfile () {
      try {
        await updateUserProfile(this.user)
        this.$q.notify({ message: 'Profile updated successfully', color: 'green' })
      } catch (error) {
        this.$q.notify({ message: 'Error updating profile', color: 'red' })
      }
    },
    goToHome () {
      this.$router.push('/home')
    },
    goToAllProjects () {
      this.$router.push('/projects')
    }
  }
}
</script>
<style scoped>
.my-card {
  max-width: 600px;
  margin: 16px auto;
}
.q-toolbar-title {
  font-size: 20px;
}
.q-drawer__content {
  background-color: #2e2e2e;
}
.q-toolbar {
  background-color: #1f1f1f;
}
.q-list .q-item-section {
  color: white;
}
</style>
