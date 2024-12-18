<template>
    <q-layout view="hHh Lpr lff"  class="shadow-2 rounded-borders">
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Homepage</q-toolbar-title>
        <q-space />
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
            <div :class="{ 'text-white': isDarkMode, 'text-grey-9': !isDarkMode, 'text-h5': true, 'text-weight-bold': true }">
              Welcome to SCAS - the service for analyzing your Java code! Happy auditing!
            </div>
          </q-card-section>
        </q-card>

        <div class="row q-px-md " style="padding-top: 2%; padding-left: 2%;">
          <q-input
            v-model="nameProject"
            filled
            dense
            placeholder="Search projects..."
            class="col-4"
          />
          <q-btn
            label="Search"
            color="primary"
            class="q-ml-sm"
            @click="fetchProjectOwnUser"
          />
        </div>
          <div class="row q-col-gutter-md q-mt-md" style="padding-left: 2%; padding-right: 2%
          ;">
            <div
              class="col-10 col-md-4"
              v-for="project in projectsOwnUser"
              :key="project.id"
            >
              <q-card class="q-pa-md shadow-1" bordered @click="pageProject(project.id)">
                <q-card-section>
                  <div class="text-h6 text-weight-bold">
                    {{ project.name }}
                  </div>
                  <div class="text-subtitle2">
                    Bugs: {{ project.countBugs }}
                  </div>
                </q-card-section>
              </q-card>
            </div>
          </div>
          <div class="row justify-between q-mt-md">
            <q-btn
              v-if="page > 0"
              icon="arrow_back"
              @click="previousPage"
            />
            <q-btn
              v-if="projectsOwnUser.length === 40"
              icon="arrow_forward"
              @click="nextPage"
            />
          </div>
        </q-page>
        <q-dialog v-model="showCreateProjectModal">
          <create-project-form :isDarkMode="isDarkMode" />
        </q-dialog>
      </q-page-container>
    </q-layout>
</template>
<script>
import { Dark } from 'quasar'
import { getProjects, allProjects } from 'src/services/projectServices'
import { getAvatar, getId } from 'src/services/userServices'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'
export default {
  data () {
    return {
      drawer: true,
      miniState: true,
      userId: null,
      user: {
        avatar: null
      },
      projects: [],
      projectsOwnUser: [],
      isDarkMode: Dark.isActive,
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
      showCreateProjectModal: false,
      page: 0,
      nameProject: ''
    }
  },
  components: {
    CreateProjectForm
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
    async fetchUser () {
      const response = await getAvatar((await getId()).data)
      this.user.avatar = response.data
    },
    goToHome () {
      if (localStorage.getItem('role') === 'admin') {
        this.$router.push('/admin')
      } else {
        this.$router.push('/home')
      }
    },
    goToAllProjects () {
      const id = this.userId
      this.$router.push({ name: 'projects', params: { id } })
    },
    search () {},
    goToProfile () {
      const userId = this.userId
      this.$router.push(`/profile/${userId}`)
    },
    async fetchId () {
      this.userId = (await getId()).data
    },
    nextPage () {
      this.page += 1
      this.fetchProjectOwnUser()
    },
    previousPage () {
      if (this.page > 0) {
        this.page -= 1
        this.fetchProjectOwnUser()
      }
    },
    async fetchProjectOwnUser () {
      console.log(this.nameProject)
      const response = await allProjects(this.page, this.nameProject)
      this.projectsOwnUser = response.data
    }
  },
  mounted () {
    this.fetchId()
    this.fetchProjectOwnUser()
    this.fetchUser()
    this.fetchProjects()
    this.fetchGrafanaChart()

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
.grafana-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
  padding: 150px;
}
.grafana-iframe {
  width: 450px;
  height: 200px;
  border: none;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
</style>
