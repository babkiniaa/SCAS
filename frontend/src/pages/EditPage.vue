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
            <q-btn
              flat
              class="q-mt-md"
              :label="user.avatar ? 'Update Avatar' : 'Upload Avatar'"
              color="primary"
              @click="uploadAvatar"
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
import { getUserProfile, updateUserProfile } from 'src/services/userServices'
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
      }
    }
  },
  async created () {
    await this.loadUserProfile()
  },
  methods: {
    async loadUserProfile () {
      try {
        const response = await getUserProfile(localStorage.getItem('currentId'))
        this.user = response.data
      } catch (error) {
        this.$q.notify({ message: 'Error loading profile', color: 'red' })
      }
    },
    async updateUser () {
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
    goToCreateProject () {
      this.$router.push('/create-project')
    },
    goToAllProjects () {
      const id = localStorage.getItem('currentId')
      this.$router.push({ name: 'projects', params: { id } })
    },
    uploadAvatar () {
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
