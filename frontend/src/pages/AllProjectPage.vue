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
          <q-icon v-else name="person" class="text-black" />
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
    <q-page-container :class="isDarkMode ? 'bg-dark' : 'bg-grey-3'">
      <q-page style="margin-top: 15px;">
        <div class="row q-mb-md items-center justify-center">
          <div class="search-sort-container row q-gutter-md col-8 justify-center">
            <q-input
              outlined
              debounce="300"
              v-model="projectsDto.name"
              placeholder="Search projects"
              @input="onSearch"
              :class="isDarkMode ? 'bg-dark text-white' : 'text-black'"
              class="col-5"
            >
              <template v-slot:append>
                <q-icon name="search" :class="isDarkMode ? 'text-white' : 'text-black'" />
              </template>
            </q-input>
            <q-btn-dropdown
              outlined
              label="Sort by"
              :options="sortingOptions"
              @click="onSortChange($event)"
              color="dark"
              :class="isDarkMode ? 'bg-grey-6' : ''" class="col-5"
            >
              <q-list>
                <q-item
                  v-for="option in sortingOptions"
                  :key="option.value"
                  clickable
                  v-ripple
                  @click="onSortChange(option.value)"
                >
                  <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">{{ option.label }}</q-item-section>
                </q-item>
              </q-list>
            </q-btn-dropdown>
          </div>
          <q-btn
            outlined
            label="Create Project"
            icon="add_circle"
            :class="isDarkMode ? 'bg-grey-6' : ''" class="q-ml-md sticky-create-btn"
            color="dark"
            @click="showCreateProjectModal = true"
          />
        </div>
        <div v-if="projects.length" class="q-mt-md">
          <q-card
            v-for="(project, index) in projects"
            :key="index"
            :class="['project-card', isDarkMode ? 'bg-grey-8 text-white' : 'bg-white']"
            class="q-my-sm q-px-sm q-py-xs"
          >
            <div class="row items-center justify-between">
            <q-card-section class="q-pa-sm">
              <div :class="[isDarkMode ? 'text-white' : 'text-black', 'text-h6']">{{ project.name }}</div>
              <div :class="isDarkMode ? 'text-grey-4' : 'text-body1'" class="q-mt-xs">{{ project.description }}</div>
              <div :class="isDarkMode ? 'text-grey-5' : 'text-caption'" class="q-mt-sm">
                Created: {{ formatDate(project.createdDate) }}
              </div>
              <q-badge
                v-if="isOwnProject"
                :color="project.visibility ? 'green' : 'yellow'"
                class="q-ml-md q-mt-sm"
              >
                {{ project.visibility ? 'Public' : 'Private' }}
              </q-badge>
            </q-card-section>
            <q-card-actions class="column items-end justify-center">
              <div v-if="projectStatus[project.id] === 'EndS' || projectStatus[project.id] === 'NotFound'" class="full-width q-mt-md">
                <div class="row items-center">
                  <q-btn
                    label="Run"
                    :icon="playIcon"
                    :class="isDarkMode ? 'bg-grey-6' : ''"
                    class="q-mb-xs text-green"
                    @click="startAnalysis(project.id)"
                  />
                  <q-btn
                    label="View Report"
                    :class="isDarkMode ? 'bg-grey-6' : ''"
                    class="q-ml-md"
                    @click="viewReport(project.id)"
                  />
                </div>
              </div>
              <q-card
              v-else
              class="column items-end justify-center"
              style="border: 1px solid yellow; background-color: #fffde7; max-width: 200px;"
            >
              <q-card-section class="text-center">
                <div class="text-black text-subtitle2 font-weight-bold">
                  {{ projectStatus[project.id] }}
                </div>
              </q-card-section>
            </q-card>
            </q-card-actions>
            </div>
          </q-card>
        </div>
        <div class="row justify-between q-mt-md">
          <q-btn v-if="projectsDto.page > 0" icon="arrow_back" @click="previousPage" />
          <q-btn
            v-if="projects.length === projectsDto.count"
            icon="arrow_forward"
            @click="nextPage"
          />
        </div>
      </q-page>
      <q-dialog v-model="showCreateProjectModal">
          <create-project-form :isDarkMode="isDarkMode" />
    </q-dialog>
    <q-dialog v-model="showModal">
          <q-card style="width: 450px; height: 250px; padding: 16px;">
            <q-card-section class="text-center">
              <h6 style="margin: 0;">Select Analizator</h6>
            </q-card-section>

            <q-card-section class="row q-col-gutter-md q-pt-none items-start" style="padding: 0 10px;">
              <div class="col-6">
                <q-btn-dropdown
                  color="primary"
                  :label="selectedAnalyzerCategory ? 'Category: ' + selectedAnalyzerCategory : 'Select Analyzer Category'"
                  :class="isDarkMode ? 'bg-grey-6 text-white' : ''"
                >
                  <q-list>
                    <q-item
                      v-for="(category, index) in Object.keys(analyzers)"
                      :key="index"
                      clickable
                      v-ripple
                      @click="selectAnalyzerCategory(category)"
                    >
                      <q-item-section>
                        <q-item-label>{{ category }}</q-item-label>
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-btn-dropdown>
              </div>
              <div class="col-6" v-if="availableAnalyzers.length">
                <q-option-group
                  v-model="selectedAnalyzers"
                  :options="availableAnalyzers.map(analyzer => ({ label: analyzer, value: analyzer }))"
                  type="checkbox"
                  label="Select Analyzers"
                  dense
                  :class="isDarkMode ? 'bg-grey-9 text-white' : ''"
                />
              </div>
            </q-card-section>
            <q-card-section class="text-center" style="padding: 30px; margin-top: auto;">
              <q-btn
                  label="Run"
                  :icon="playIcon"
                  :class="isDarkMode ? 'bg-grey-6' : ''"
                  class="q-mb-xs text-green"
                  @click="runProject"
                />
            </q-card-section>
          </q-card>
        </q-dialog>
        <q-dialog v-model="showModalReport">
          <q-card>
            <q-card-section>
                <q-list bordered>
                  <q-item v-for="report in listReporst" :key="report.id" clickable @click="showReport(report.id)">
                    <q-item-section :class="isDarkMode ? 'text-white' : ''"> {{ formatDate(report.createdDate) }} </q-item-section>
                  </q-item>
                </q-list>
            </q-card-section>
          </q-card>
        </q-dialog>
    </q-page-container>
  </q-layout>
</template>

<script>
import { getProjects } from 'src/services/projectServices'
import { Dark } from 'quasar'
import { getAvatar } from 'src/services/userServices'
import { getStatus, reportCreate, getReports, getAnalizator } from 'src/services/analysisServeces'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'
export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      projects: [],
      projectStatus: {},
      user: {
        avatar: null
      },
      projectsDto: {
        count: 4,
        page: 0,
        sortingField: 'createdDate',
        userId: localStorage.getItem('currentId'),
        myProject: true,
        name: '',
        sortDirection: 'DESC'
      },
      sortingOptions: [
        { label: 'Date', value: 'createdDate' },
        { label: 'Name', value: 'name' }
      ],
      isOwnProject: false,
      currentUserId: null,
      isDarkMode: Dark.isActive,
      playIcon: 'play_arrow',
      showModal: false,
      showCreateProjectModal: false,
      analyzers: {},
      selectedAnalyzerCategory: null,
      selectedAnalyzers: [],
      availableAnalyzers: [],
      showModalReport: false,
      listReporst: null,
      projectId: null
    }
  },
  components: {
    CreateProjectForm
  },
  methods: {
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
    async loadProjects () {
      try {
        // eslint-disable-next-line eqeqeq
        if (this.projectsDto.userId != this.$route.params.id) {
          this.projectsDto.myProject = false
          this.projectsDto.userId = this.$route.params.id
        }
        // eslint-disable-next-line eqeqeq
        if (this.projectsDto.sortingField == 'name') {
          this.projectsDto.sortDirection = 'ASC'
        }
        const response = await getProjects(this.projectsDto)
        this.projects = response.data
        this.isOwnProject = this.projectsDto.userId === localStorage.getItem('currentId')

        // Fetch status for each project
        for (const project of this.projects) {
          console.log(project.id)
          console.log(this.projects)
          const statusResponse = await getStatus(project.id)
          this.projectStatus[project.id] = statusResponse.data || null
        }
      } catch (error) {
        this.$q.notify({ message: 'Error loading projects', color: 'red' })
      }
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
      const id = localStorage.getItem('currentId')
      this.$router.push(`/profile/${id}`)
    },
    formatDate (date) {
      return new Date(date).toLocaleString()
    },
    async viewReport (projectId) {
      this.listReporst = (await getReports(projectId)).data
      this.showModalReport = true
    },
    showReport (reportId) {
      this.$router.push(`/report/${reportId}`)
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
        this.$router.push(`/projects/${localStorage.getItem('currentId')}`)
      }
    },
    onSearch () {
      this.projectsDto.page = 0
      this.loadProjects()
    },
    goToAllProjects () {
      const id = localStorage.getItem('currentId')
      this.$router.push({ name: 'projects', params: { id } })
    },
    onSortChange (selectedValue) {
      if (typeof selectedValue === 'string') {
        this.projectsDto.sortingField = selectedValue
        this.projectsDto.page = 0
        this.loadProjects()
      }
    },
    async fetchUser () {
      const response = await getAvatar(localStorage.getItem('currentId'))
      this.user.avatar = response.data
    },
    async fetchAnalyzers () {
      try {
        const response = await getAnalizator()
        this.analyzers = response.data
      } catch (error) {
        this.$q.notify({ message: 'Failed to fetch analyzers', color: 'red' })
      }
    },
    selectAnalyzerCategory (category) {
      this.selectedAnalyzerCategory = category
      this.availableAnalyzers = this.analyzers[category] || []
      this.selectedAnalyzers = []
    }
  },
  created () {
    this.fetchUser()
    this.currentUserId = localStorage.getItem('currentId')
    this.loadProjects()
    this.fetchAnalyzers()
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
  max-width: 90%;
  margin: 6px auto;
  padding: 8px;
}
.dark-bg {
  background-color: #1d1d1d !important;
}
</style>
