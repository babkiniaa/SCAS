<template>
  <q-layout view="hHh Lpr lff" class="shadow-2 rounded-borders">
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">All Projects</q-toolbar-title>
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
        <q-item clickable v-ripple @click="showCreateProjectModal = true">
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
    <q-page-container :class="isDarkMode ? 'bg-white' : 'bg-white'">
      <div class="row no-wrap full-height">
        <div class="col-6 q-pa-md">
          <q-card :class="['project-details-card', isDarkMode ? 'bg-grey-8 text-white' : 'bg-white']">
            <q-card-section>
              <div class="text-h6">{{ project.name }}</div>
              <div :class="isDarkMode ? 'text-grey-5' : 'text-body1'" class="q-my-md">
                {{ project.description }}
              </div>
              <div class="row items-center justify-between">
                <q-btn flat label="Visit URL" icon="link" color="primary" v-if="project.url" @click="openUrl(project.url)" />
                <div class="q-mt-md" :class="isDarkMode ? 'text-grey-4' : 'text-caption'">
                  Created: {{ formatDate(project.createdDate) }}
                </div>
              </div>
            </q-card-section>
            <q-separator spaced />
            <q-card-section>
              <div class="row items-center q-mb-md">
                <q-btn-dropdown
                  :label="selectedBranch || 'All Branches'"
                  :split="false"
                  color="primary"
                  flat
                  dense
                >
                  <q-list>
                    <q-item clickable @click="filterReports(null)">
                      <q-item-section>All Branches</q-item-section>
                    </q-item>
                    <q-item
                      v-for="branch in branches"
                      :key="branch"
                      clickable
                      @click="filterReports(branch)">
                      <q-item-section>{{ branch }}</q-item-section>
                    </q-item>
                  </q-list>
                </q-btn-dropdown>
              </div>
              <q-list v-if="filteredReports.length" bordered class="q-my-lg">
                <q-item
                  v-for="report in filteredReports"
                  :key="report.id"
                  clickable
                  v-ripple
                  class="report-item"
                  @click="viewReport(report.id)"
                >
                  <q-item-section>
                    <div>{{ formatDate(report.createdDate) }}</div>
                    <div class="text-caption">{{ report.branch || 'No branch' }}</div>
                  </q-item-section>
                </q-item>
              </q-list>
              <div v-else class="text-center text-grey-6 q-my-md">No reports available</div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-6 q-pa-md">
          <div class="grafana-container">
            <iframe :src="grafanaData.url1" class="grafana-iframe1"></iframe>
            <div class="row q-mt-md">
              <iframe :src="grafanaData.url2" class="grafana-iframe"></iframe>
              <iframe :src="grafanaData.url3" class="grafana-iframe"></iframe>
            </div>
            <div class="row q-mt-md">
              <iframe :src="grafanaData.url4" class="grafana-iframe"></iframe>
            </div>
          </div>
        </div>
      </div>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { getAvatar, getId } from 'src/services/userServices'
import { reportCreate, getReports } from 'src/services/analysisServeces'
import { getProject } from 'src/services/projectServices'
export default {
  data () {
    return {
      project: {},
      reports: [],
      userId: null,
      filteredReports: [],
      branches: [],
      selectedBranch: null,
      isDarkMode: Dark.isActive,
      drawer: true,
      miniState: false,
      grafanaData: {
        url1: null,
        url2: null,
        url3: null,
        url4: null
      },
      user: {
        avatar: null
      }
    }
  },
  methods: {
    viewReport (id) {
      this.$router.push(`/report/${id}`)
    },
    async fetchGrafanaChart () {
      this.userId = (await getId()).data
      const projectId = this.$route.params.id
      const branch = this.selectedBranch ? encodeURIComponent(this.selectedBranch) : 'all'
      const theme = this.isDarkMode ? 'dark' : 'light'
      this.grafanaData.url1 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${this.userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=2&__feature.dashboardSceneSolo`
      this.grafanaData.url2 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${this.userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=1&__feature.dashboardSceneSolo`
      this.grafanaData.url3 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${this.userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=3&__feature.dashboardSceneSolo`
      this.grafanaData.url4 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${this.userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=4&__feature.dashboardSceneSolo`
    },
    GoToAdmin () {
      this.$router.push('/admin')
    },
    startAnalysis (id) {
      this.showModal = true
      this.projectId = id
    },
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async fetchProject () {
      try {
        const projectId = this.$route.params.id
        const response = await getProject(projectId)
        this.project = response.data
      } catch (error) {
        this.$q.notify({ message: 'Failed to load project details', color: 'red' })
      }
    },
    async fetchReports () {
      try {
        const projectId = this.$route.params.id
        const response = await getReports(projectId)
        this.reports = response.data
        this.branches = [...new Set(this.reports.map((report) => report.branch))]
        this.filteredReports = this.reports
      } catch (error) {
        this.$q.notify({ message: 'Failed to load reports', color: 'red' })
      }
    },
    filterReports (branch) {
      this.selectedBranch = branch
      this.filteredReports = branch
        ? this.reports.filter((report) => report.branch === branch)
        : this.reports
      this.fetchGrafanaChart()
    },
    nextPage () {
      this.projectsDto.page += 1
      this.loadProjects()
    },
    previousPage () {
      if (this.projectsDto.page > 0) {
        this.projectsDto.page -= 1
        this.loadProjects()
      }
    },
    goToHome () {
      console.log(localStorage.getItem('role'))
      if (localStorage.getItem('role') === 'admin') {
        this.$router.push('/admin')
      } else {
        this.$router.push('/home')
      }
    },
    goToProfile () {
      const id = this.userId
      this.$router.push(`/profile/${id}`)
    },
    formatDate (date) {
      return new Date(date).toLocaleString()
    },
    async runProject () {
      try {
        await reportCreate({
          idProject: this.projectId,
          needReports: this.selectedAnalyzers
        })
        this.showModal = false
        this.$q.notify({ message: 'Project created successfully', color: 'green' })
      } catch (error) {
        this.$q.notify({ message: 'Failed to create project', color: 'red' })
      } finally {
        this.$router.push(`/projects/${this.userId}`)
      }
    },
    async fetchUser () {
      const response = await getAvatar(this.userId)
      this.user.avatar = response.data
    },
    openUrl (url) {
      window.open(url, '_blank')
    },
    goToAllProjects () {
      this.$router.push(`/projects/${this.userId}`)
    }
  },
  toggleDarkMode () {
    Dark.set(!this.isDarkMode)
    this.isDarkMode = Dark.isActive
  },
  async fetchId () {
    this.userId = (await getId()).data
  },
  created () {
    this.fetchUser()
    this.currentUserId = this.userId
    this.fetchProject()
    this.fetchReports()
    this.fetchGrafanaChart()
  }
}
</script>

<style>
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
  color: #ffffff !important;
}
.text-grey-5 {
  color: #7f8c8d !important;
}
.project-card {
  max-width: 100%;
  margin: 6px auto;
  padding: 8px;
}
.dark-bg {
  background-color: #1d1d1d !important;
}
.main-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px;
}
.project-details-card {
  margin: 20px auto;
  padding: 20px;
  border: none;
}
.q-page {
  padding: 24px;
}
.text-body1 {
  font-size: 14px;
}
.text-h6 {
  font-size: 18px;
}

.grafana-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 18px;
}
.grafana-iframe {
  width: 45%;
  padding: rehbkmobr5px;
  margin-right: 40px;
  height: 240px;
  border: none;
  border-radius: 8px;
}
.grafana-iframe1 {
  width: 95%;
  height: 400px;
  border: none;
  border-radius: 8px;
}
.project-details-card {
  height: 100%;
}
.report-item {
  transition: background-color 0.3s;
}
.report-item:hover {
  background-color: var(--q-color-primary-lighten5);
}
</style>
