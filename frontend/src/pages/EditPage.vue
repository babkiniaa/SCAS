<template>
   <q-layout view="hHh Lpr lff"  class="shadow-2 rounded-borders">
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Homepage</q-toolbar-title>
        <q-space />
        <q-btn dense round icon="search" @click="search" aria-label="Search" class="text-white" />
        <q-btn
          dense
          round
          :icon="isDarkMode ? 'light_mode' : 'dark_mode'"
          @click="toggleDarkMode"
          aria-label="Toggle Dark Mode"
          class="text-white q-ml-sm"
        />
        <q-avatar size="42px" class="q-ml-md" @click="goToProfile">
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
      :content-class="isDarkMode ? 'bg-dark' : 'bg-grey-9'"
    >
      <q-list padding>
        <q-item clickable v-ripple @click="goToHome">
          <q-item-section avatar>
            <q-icon name="home" :class="isDarkMode ? 'text-white' : 'text-black'" />
          </q-item-section>
          <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">Home</q-item-section>
        </q-item>
        <q-item clickable v-ripple @click="goToCreateProject">
          <q-item-section avatar>
            <q-icon name="add_circle" :class="isDarkMode ? 'text-white' : 'text-black'" />
          </q-item-section>
          <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">Create Project</q-item-section>
        </q-item>
        <q-item clickable v-ripple @click="goToAllProjects">
          <q-item-section avatar>
            <q-icon name="folder_open" :class="isDarkMode ? 'text-white' : 'text-black'" />
          </q-item-section>
          <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">All Projects</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>
    <q-page-container :class="isDarkMode ? 'dark-bg' : 'bg-grey-3'">
      <q-page style="margin-top: 15px;">
        <div class="profile-container q-py-lg q-px-md row">
          <div class="col-4 q-pa-md">
            <q-avatar size="140px" class="q-mb-md avatar">
              <img v-if="user.avatar" :src="user.avatar" alt="User Avatar" />
              <q-icon v-else name="person" />
            </q-avatar>
            <q-btn
              flat
              class="q-mt-md"
              :label="user.avatar ? 'Update Avatar' : 'Upload Avatar'"
              color="primary"
              @click="uploadAvatar"
            />
            <q-btn
              v-if="user.avatar"
              flat
              class="q-mt-sm"
              label="Delete Avatar"
              color="negative"
              @click="deleteAvatar"
            />
          </div>
          <div class="col-8 q-pa-md">
            <div class="q-mb-md row">
              <q-input
                v-model="user.username"
                outlined
                label="Username"
                dense
                class="col"
              />
              <q-icon
                name="edit"
                class="cursor-pointer q-ml-sm"
              />
            </div>
            <div class="col text-h6">Email:</div>
            <div class="col text-body1">{{ user.email }}</div>
            <div class="q-mb-md row">
            </div>
          </div>
          <div class="col-12 q-mt-md">
            <div class="row">
              <q-input
                v-model="user.about"
                outlined
                label="About Me"
                type="textarea"
                autogrow
                dense
                rows="5"
                class="col"
              />
              <q-icon
                name="edit"
                class="cursor-pointer q-ml-sm"
              />
            </div>
          </div>
          <div class="bottom-buttons row justify-end q-mt-md">
            <q-btn label="Save" color="primary" class="q-mr-md" @click="updateUser" />
          </div>
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
</template>
<script>
import { getUserProfile, updateUserProfile, uploadUserAvatar, deleteUserAvatar } from 'src/services/userServices'
import { Dark } from 'quasar'
export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      user: {
        email: '',
        username: '',
        avatar: null,
        about: ''
      },
      isDarkMode: Dark.isActive
    }
  },
  async created () {
    await this.loadUserProfile()
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async loadUserProfile () {
      try {
        const response = await getUserProfile(localStorage.getItem('currentId'))
        this.user = response.data
        this.avatar = response.data.avatar
      } catch (error) {
        this.$q.notify({ message: 'Error loading profile', color: 'red' })
      }
    },
    async updateUser () {
      try {
        await updateUserProfile(this.user)
        this.$q.notify({ message: 'Profile updated successfully', color: 'green' })
        this.goToProfile()
      } catch (error) {
        this.$q.notify({ message: 'Error updating profile', color: 'red' })
      }
    },
    async uploadAvatar () {
      const fileInput = document.createElement('input')
      fileInput.type = 'file'
      fileInput.onchange = async () => {
        const file = fileInput.files[0]
        const formData = new FormData()
        formData.append('file', file)
        try {
          await uploadUserAvatar(formData)
          this.$q.notify({ message: 'Avatar uploaded successfully', color: 'green' })
          await this.loadUserProfile()
        } catch (error) {
          this.$q.notify({ message: 'Error uploading avatar', color: 'red' })
        }
      }
      fileInput.click()
    },
    async deleteAvatar () {
      try {
        await deleteUserAvatar()
        this.$q.notify({ message: 'Avatar deleted successfully', color: 'green' })
        await this.loadUserProfile()
      } catch (error) {
        this.$q.notify({ message: 'Error deleting avatar', color: 'red' })
      }
    },
    goToHome () {
      this.$router.push('/home')
    },
    goToCreateProject () {
      this.$router.push('/create-project')
    },
    goToAllProjects () {
      const id = localStorage.getItem('currentId')
      this.$router.push({ name: 'projects', params: { id } })
    },
    goToProfile () {
      const userId = localStorage.getItem('currentId')
      this.$router.push(`/profile/${userId}`)
    }
  }
}
</script>

<style scoped>
.profile-container {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
.q-toolbar-title {
  font-size: 20px;
}
.bg-dark {
  background-color: #121212;
}
.bg-grey-8 {
  background-color: #3a3a3a;
}
.bg-grey-6 {
  background-color: #4a4a4a;
}
.text-white {
  color: white !important;
}
.bg-grey-11 {
  background-color: #1d1d1d;
}
.dark-bg {
  background-color: #1d1d1d !important;
}
</style>
