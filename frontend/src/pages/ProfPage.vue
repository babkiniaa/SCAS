<template>
  <q-layout view="lHh lpr lFf">
    <!-- Header -->
    <q-header elevated class="bg-grey-9">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Profile</q-toolbar-title>
        <q-space />
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
      content-class="bg-grey-9"
    >
      <q-list padding>
        <q-item clickable v-ripple @click="goToHome">
          <q-item-section avatar>
            <q-icon name="home" />
          </q-item-section>
          <q-item-section class="text-black">Home</q-item-section>
        </q-item>
        <q-item clickable v-ripple @click="goToCreateProject">
          <q-item-section avatar>
            <q-icon name="add_circle" />
          </q-item-section>
          <q-item-section class="text-black">Create Project</q-item-section>
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
    </q-page-container>
  </q-layout>
</template>
<script>
import { getUserProfile } from 'src/services/userServices'
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
      isOwnProfile: false
    }
  },
  async created () {
    this.currentUserId = localStorage.getItem('currentId')
    const profileId = this.$route.params.id
    await this.loadUserProfile(profileId)
  },
  methods: {
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
      this.$router.push('/home')
    },
    goToCreateProject () {
      this.$router.push('/create-project')
    },
    goToAllProjects () {
      this.$router.push('/projects')
    },
    goToProfile () {
      const id = localStorage.getItem('currentId')
      console.log(id)
      this.$router.push(`/profile/${id}`)
    },
    goToEdit () {
      this.$router.push('/edit')
    },
    logout () {
      localStorage.removeItem('jwtToken')
      localStorage.removeItem('currentId')
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
.q-drawer__content {
  background-color: #2e2e2e;
}
.q-toolbar {
  background-color: #1f1f1f;
}
.q-list .q-item-section {
  color: white;
}
.q-btn {
  min-width: 150px;
}
.q-avatar {
  cursor: pointer;
}
.avatar {
  margin-left: 10%;
}
.bottom-buttons {
  position: fixed;
  bottom: 16px;
  right: 16px;
}
</style>
