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
        :content-class="isDarkMode ? 'bg-black' : 'bg-grey-8'"
      >
        <q-scroll-area class="fit" :horizontal-thumb-style="{ opacity: 0 }">
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
        </q-scroll-area>
      </q-drawer>
      <q-page-container :class="isDarkMode ? 'dark-bg' : 'bg-grey-3'">
        <q-page style="margin-top: 15px;">
          <q-card :class="['q-pa-md', 'shadow-2', 'my-card', isDarkMode ? 'bg-grey-8' : '']" bordered>
            <q-card-section class="text-center">
              <div :class="{ 'text-white': isDarkMode, 'text-grey-9': !isDarkMode, 'text-h5': true, 'text-weight-bold': true }">Your Projects</div>
              <div :class="isDarkMode ? 'text-grey-4' : 'text-grey-8'">
                <span v-if="projects.length">Here are your current projects:</span>
                <span v-else>You have no projects yet</span>
              </div>
            </q-card-section>
            <q-card-section v-if="projects.length">
              <q-list bordered>
                <q-item v-for="project in projects" :key="project.id" clickable>
                  <q-item-section :class="isDarkMode ? 'text-white' : ''">{{ project.name }}</q-item-section>
                </q-item>
              </q-list>
            </q-card-section>
            <q-card-section v-else>
              <q-btn label="Create Project" color="dark" @click="showCreateProjectModal = true" :class="isDarkMode ? 'bg-grey-6' : ''" class="q-mt-md full-width" />
            </q-card-section>
          </q-card>
        </q-page>
        <q-dialog v-model="showCreateProjectModal">
          <create-project-form :isDarkMode="isDarkMode" />
        </q-dialog>
      </q-page-container>
    </q-layout>
</template>
<script>
import { Dark } from 'quasar'
import { getProjects } from 'src/services/projectServices'
import { getAvatar } from 'src/services/userServices'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'
export default {
  data () {
    return {
      drawer: true,
      miniState: true,
      user: {
        avatar: null
      },
      projects: [],
      isDarkMode: Dark.isActive,
      projectsDto: {
        count: 3,
        page: 0,
        sortingField: 'createdDate',
        userId: localStorage.getItem('currentId'),
        myProject: true,
        name: '',
        sortDirection: 'DESC'
      },
      showCreateProjectModal: false
    }
  },
  components: {
    CreateProjectForm
  },
  methods: {
    async fetchProjects () {
      try {
        const response = await getProjects(this.projectsDto)
        this.projects = response.data
      } catch (error) {
        this.$q.notify({ message: 'Error loading projects', color: 'red' })
      }
    },
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async fetchUser () {
      const response = await getAvatar(localStorage.getItem('currentId'))
      this.user.avatar = response.data
    },
    goToHome () {
      this.$router.push('/home')
    },
    goToAllProjects () {
      const id = localStorage.getItem('currentId')
      this.$router.push({ name: 'projects', params: { id } })
    },
    search () {},
    goToProfile () {
      const userId = localStorage.getItem('currentId')
      this.$router.push(`/profile/${userId}`)
    }
  },
  mounted () {
    this.fetchUser()
    this.fetchProjects()
  }
}
</script>

<style scoped>
.my-card {
  max-width: 600px;
  margin: 0 auto;
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
