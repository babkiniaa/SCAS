<template>
    <q-layout view="hHh Lpr lff"  class="shadow-2 rounded-borders">
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Profile</q-toolbar-title>
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
      <q-page>
        <div class="row user-and-projects">
          <div class="col-3 user-info">
            <q-card class="q-pa-xl full-height text-center">
              <q-avatar size="150px" class="q-mx-auto">
                <img v-if="user.avatar" :src="user.avatar" alt="User Avatar" />
                <q-icon v-else name="person" class="text-grey" />
              </q-avatar>
              <div class="text-h6 text-center q-mt-md">{{ user.username }}</div>
              <div class="text-caption text-center text-grey">{{ user.email }}</div>
              <q-btn
                flat
                color="primary"
                class="full-width q-mt-lg"
                icon="edit"
                label="Edit"
                @click="goToEdit"
              />
            </q-card>
          </div>
          <div class="col-9 project-list">
            <div class="text-h5 text-bold q-mb-md">Projects</div>
            <div class="row q-col-gutter-lg q-mt-md">
              <div
                class="col-12 col-md-6"
                v-for="project in projects"
                :key="project.id"
              >
                <q-card
                  clickable
                  class="q-pa-md project-card"
                  @click="pageProject(project.id)"
                >
                  <q-card-section>
                    <div class="text-body1 text-bold">{{ project.name }}</div>
                    <div class="text-caption text-grey">{{ project.description || 'No description' }}</div>
                  </q-card-section>
                </q-card>
              </div>
            </div>
          </div>
        </div>
          <div class="col-12">
            <div class="text-h6 q-mb-md" style="padding-left: 2%;">Activity</div>
            <div class="row q-gutter-md" style="padding-left: 2%;">
              <iframe
                v-for="(url, index) in [grafanaData.url1, grafanaData.url2, grafanaData.url3]"
                :key="index"
                :src="url"
                frameborder="0"
                width="30%"
                height="200px"
              ></iframe>
            </div>
          </div>
        <q-btn
          fab
          color="red"
          icon="logout"
          class="fixed-bottom-right q-mb-lg q-mr-lg"
          @click="logout"
        />
      </q-page>
      <q-dialog v-model="showCreateProjectModal">
          <create-project-form :isDarkMode="isDarkMode" />
    </q-dialog>
    </q-page-container>
  </q-layout>
</template>
<script>
import { getProjects } from 'src/services/projectServices'
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
      showCreateProjectModal: false,
      projectsDto: {
        count: 6,
        page: 0,
        sortingField: 'createdDate',
        userId: null,
        myProject: true,
        name: '',
        sortDirection: 'DESC'
      },
      grafanaData: {
        url1: null,
        url2: null,
        url3: null
      },
      projects: []
    }
  },
  components: {
    CreateProjectForm
  },
  async created () {
    this.currentUserId = (await getId()).data
    const profileId = this.$route.params.id
    await this.loadUserProfile(profileId)
    this.fetchProjects()
    this.fetchGrafanaChart()
  },
  methods: {
    pageProject (id) {
      this.$router.push({ name: 'project', params: { id } })
    },
    async fetchGrafanaChart () {
      const userId = (await getId()).data
      console.log(userId)
      const projectId = '1'
      const branch = 'all'
      const theme = this.isDarkMode ? 'dark' : 'light'
      this.grafanaData.url1 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=5&__feature.dashboardSceneSolo`
      this.grafanaData.url2 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=6&__feature.dashboardSceneSolo`
      this.grafanaData.url3 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=7&__feature.dashboardSceneSolo`
    },
    async fetchProjects () {
      try {
        this.projectsDto.userId = (await getId()).data
        console.log(this.projectsDto.userId)
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
.row.no-wrap {
  display: flex;
  flex-wrap: nowrap;
  gap: 16px;
}
.user-and-projects {
  display: flex;
  flex-direction: row;
  padding: 2%;
  gap: 16px;
}

.user-info {
  flex: 0 0 30%;
  max-width: 25%;
}
.project-list {
  flex: 1 1 70%;
}
.grafana-charts iframe {
  border: none;
}
.project-card {
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.fixed-bottom-right {
  position: fixed;
  bottom: 16px;
  right: 16px;
}
.q-gutter-md {
  gap: 16px;
}
</style>
