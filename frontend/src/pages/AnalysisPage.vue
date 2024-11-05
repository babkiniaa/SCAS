<template>
  <q-layout view="hHh Lpr lff">
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">
      <q-toolbar>
        <q-btn flat round dense icon="arrow_back" @click="goBack" />
        <q-toolbar-title :class="isDarkMode ? 'text-white' : ''">Analysis Progress</q-toolbar-title>
        <q-space />
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
      :content-class="isDarkMode ? 'bg-black' : 'bg-grey-9'"
    >
      <q-scroll-area class="fit" :horizontal-thumb-style="{ opacity: 0 }">
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
      </q-scroll-area>
    </q-drawer>
    <q-page-container :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
      <q-page class="flex flex-center column q-pa-md">
        <q-card :class="['shadow-2', 'q-pa-md', 'my-card', isDarkMode ? 'bg-grey-8' : '']" bordered>
          <q-card-section>
            <div class="text-center">
              <q-spinner-dots v-if="!allResponsesReceived" color="primary" size="50px" />
              <div class="text-h6 q-mt-md" :class="isDarkMode ? 'text-white' : ''">
                {{ allResponsesReceived ? 'Analysis Complete!' : 'Conducting Analysis...' }}
              </div>
            </div>
          </q-card-section>

          <q-card-section v-if="allResponsesReceived" class="text-center">
            <q-btn
              label="View Report"
              color="primary"
              class="q-mt-md full-width"
              @click="viewReport"
            />
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { getAvatar } from 'src/services/userServices'
import { reportCreate } from 'src/services/analysisServeces'
import { getProject, connect, connect1 } from 'src/services/projectServices'
export default {
  data () {
    return {
      isDarkMode: Dark.isActive,
      allResponsesReceived: false,
      analysisResponses: [],
      projectData: null,
      idReport: null
    }
  },
  methods: {
    viewReport () {
      this.$router.push('/report')
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
    goToCreateProject () {
      this.$router.push('/create-project')
    },
    goToAllProjects () {
      const id = localStorage.getItem('currentId')
      this.$router.push(`/projects/${id}`)
    },
    goToProfile () {
      const userId = localStorage.getItem('currentId')
      this.$router.push(`/profile/${userId}`)
    },
    async startAnalysis () {
      const response = await getProject(localStorage.getItem('currentProject'))
      this.projectData = response.data
      const response1 = await reportCreate(this.projectData)
      this.idReport = response1.data
      await connect({
        projectId: localStorage.getItem('currentProject'),
        reportId: this.idReport
      })
      await connect1({
        projectId: localStorage.getItem('currentProject'),
        reportId: this.idReport
      })
      this.allResponsesReceived = true
    }
  },
  created () {
    this.fetchUser()
    this.startAnalysis()
  }
}
</script>

<style scoped>
.my-card {
  max-width: 400px;
  margin: auto;
}
.bg-dark {
  background-color: #121212;
}
.text-white {
  color: #ffffff;
}
</style>
