<template>
    <q-layout view="hHh Lpr lff"  class="shadow-2 rounded-borders">
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">
      <q-toolbar>
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
        <q-item clickable v-ripple  @click="showCreateProjectModal = true">
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
            <div class="q-mt-md">
              <div class="text-h6">About Me:</div>
              <div class="text-body1">{{ user.about || 'No description provided' }}</div>
            </div>
          </div>
          <div class="col-8 q-pa-md">
            <div class="q-mb-md row">
              <div class="col text-h6">Username:</div>
              <div class="col text-body1">{{ user.username }}</div>
            </div>
            <div class="q-mb-md row">
              <div class="col text-h6">Email:</div>
              <div class="col text-body1">{{ user.email }}</div>
            </div>
            <div class="row q-mb-md">
              <q-btn label="View All Projects" color="primary" @click="goToAllProjects" />
            </div>
            <div v-if="isOwnProfile" class="bottom-buttons row justify-end q-mt-md">
              <q-btn label="Edit" color="primary" class="q-mr-md" @click="goToEdit" />
              <q-btn label="Out" color="red" @click="logout" />
            </div>
          </div>
        </div>
      </q-page>
      <q-dialog v-model="showCreateProjectModal">
          <create-project-form :isDarkMode="isDarkMode" />
    </q-dialog>
    </q-page-container>
  </q-layout>
</template>
<script>
import { getUserProfile, getId } from 'src/services/userServices'
import { Dark } from 'quasar'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'
export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      user: {
        id: '',
        email: '',
        username: '',
        avatar: null,
        about: ''
      },
      currentUserId: null,
      isOwnProfile: false,
      isDarkMode: Dark.isActive,
      showCreateProjectModal: false
    }
  },
  components: {
    CreateProjectForm
  },
  async created () {
    this.currentUserId = (await getId()).data
    const profileId = this.$route.params.id
    await this.loadUserProfile(profileId)
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async loadUserProfile (profileId) {
      try {
        const response = await getUserProfile(profileId)
        this.user = response.data
        console.log(this.currentUserId)
        console.log(this.user.id)
        // eslint-disable-next-line eqeqeq
        if (this.currentUserId == this.user.id) {
          this.isOwnProfile = true
        }
        console.log(this.isOwnProfile)
      } catch (error) {
        this.$q.notify({ message: 'Error loading profile', color: 'red' })
      }
    },
    goToHome () {
      if (localStorage.getItem('role') === 'admin') {
        this.$router.push('/admin')
      } else {
        this.$router.push('/home')
      }
    },
    goToAllProjects () {
      const id = this.user.id
      this.$router.push({ name: 'projects', params: { id } })
    },
    goToMyProjects () {
      const id = this.currentUserId
      this.$router.push({ name: 'projects', params: { id } })
    },
    goToProfile () {
      const id = this.currentUserId
      console.log(id)
      this.$router.push(`/profile/${id}`)
    },
    goToEdit () {
      this.$router.push('/edit')
    },
    logout () {
      localStorage.removeItem('jwtToken')
      this.$router.push('/login')
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
