<template>
  <q-layout view="hHh lpR fF" class="shadow-2 rounded-borders">
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
    <q-page-container :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
      <div v-if="projectData != null" class="q-mt-md">
        <q-page style="margin-top: 15px;">
          <q-card :class="['q-pa-md', 'shadow-2', 'my-card', isDarkMode ? 'bg-grey-8' : '']" bordered>
            <q-card-section>
              <div class="text-h5 text-center q-mb-md">
                {{ projectData.name }}
              </div>
              <div class="text-subtitle1 text-center q-mb-md">
                {{ projectData.description }}
              </div>
              <div class="text-caption text-center">
                Visibility: {{ projectData.visibility ? 'Public' : 'Private' }} | Created on: {{ formatDate(projectData.createdDate) }}
              </div>
            </q-card-section>
            <div v-if="reportData.dependencyCustoms.length">
              <q-card-section>
                <div class="text-h6 text-weight-bold q-mb-md">OWASP Vulnerabilities Found</div>
                <div v-for="owasp in reportData.dependencyCustoms" :key="owasp.name" class="q-my-md q-pa-sm border-box shadow-1 rounded-borders">
                  <div v-for="dep in owasp.reportList" :key="dep.name">
                    <div class="text-body1 text-weight-bold">{{ dep.name }} - {{ dep.version }}</div>
                    <p>License: {{ dep.license }}</p>
                    <p>Ecosystem: {{ dep.ecosystem }}</p>
                    <p>Discription: {{ dep.discription }}</p>
                    <p v-if="dep.isVirtual">This is a virtual package.</p>
                    <ul>
                      <li v-for="vulnerability in dep.owaspVulnerabilities" :key="vulnerability.name">
                        <strong>{{ vulnerability.name }}</strong>: {{ vulnerability.description }}
                        <p><em>Notes:</em> {{ vulnerability.notes }}</p>
                      </li>
                    </ul>
                  </div>
                </div>
              </q-card-section>
            </div>
            <div v-if="reportData.ruleViolationCustoms.length">
              <q-card-section>
                <div class="text-h6 text-weight-bold q-mb-md">PMD Violations Found</div>
                <div v-for="pmd in reportData.ruleViolationCustoms" :key="pmd.name" class="q-my-md q-pa-sm border-box shadow-1 rounded-borders">
                  <div v-for="violation in pmd.reportList" :key="violation.name">
                    <div class="text-body1 text-weight-bold">{{ violation.name }}</div>
                    <p>Priority: {{ violation.priority }}</p>
                    <p>Message: {{ violation.message }}</p>
                    <p>File: {{ violation.fileName }} (Line: {{ violation.beginLine }}-{{ violation.endLine }}, Col: {{ violation.beginColumn }}-{{ violation.endColumn }})</p>
                    <p>{{ violation.description }}</p>
                  </div>
                </div>
              </q-card-section>
            </div>
          </q-card>
        </q-page>
      </div>
      <q-dialog v-model="showCreateProjectModal">
          <create-project-form :isDarkMode="isDarkMode" />
    </q-dialog>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { getAvatar } from 'src/services/userServices'
import { getProject } from 'src/services/projectServices'
import { getReport, save } from 'src/services/analysisServeces'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'
export default {
  data () {
    return {
      drawer: true,
      miniState: true,
      user: {
        avatar: null
      },
      isDarkMode: Dark.isActive,
      projectData: null,
      projectId: null,
      reportData: null,
      showCreateProjectModal: false
    }
  },
  components: {
    CreateProjectForm
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
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
    },
    async fetchUser () {
      const response = await getAvatar(localStorage.getItem('currentId'))
      this.user.avatar = response.data
    },
    async fetchProject () {
      save(this.projectId)
      console.log(this.projectId)
      const response = await getProject(this.projectId)
      this.projectData = response.data
      console.log(this.projectData)
      const response1 = await getReport(this.projectId)
      this.reportData = response1.data
    },
    formatDate (date) {
      return new Date(date).toLocaleDateString()
    }
  },
  mounted () {
    this.projectId = this.$route.params.id
    this.fetchUser()
    this.fetchProject()
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
