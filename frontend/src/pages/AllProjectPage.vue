<template>
  <q-layout view="hHh Lpr lff" class="shadow-2 rounded-borders">
    <!-- Измененный хедер с черным цветом -->
    <q-header elevated class="bg-black full-width">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" class="text-white" />
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
        <q-avatar size="42px" class="q-ml-md cursor-pointer" @click="goToProfile">
          <img v-if="user.avatar" :src="user.avatar" alt="User Avatar" />
          <q-icon v-else name="person" size="28px" color="white" />
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
      :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'"
    >
      <q-list padding>
        <q-item clickable v-ripple @click="goToHome" class="rounded-borders" :class="isDarkMode ? 'hover:bg-grey-9' : 'hover:bg-grey-3'">
          <q-item-section avatar>
            <q-icon name="home" :color="isDarkMode ? 'white' : 'black'" />
          </q-item-section>
          <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">Home</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="showCreateProjectModal = true" class="rounded-borders" :class="isDarkMode ? 'hover:bg-grey-9' : 'hover:bg-grey-3'">
          <q-item-section avatar>
            <q-icon name="add_circle" :color="isDarkMode ? 'white' : 'black'" />
          </q-item-section>
          <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">Create Project</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="goToAllProjects" class="rounded-borders" :class="isDarkMode ? 'hover:bg-grey-9' : 'hover:bg-grey-3'">
          <q-item-section avatar>
            <q-icon name="folder_open" :color="isDarkMode ? 'white' : 'black'" />
          </q-item-section>
          <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">All Projects</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>
    
    <q-page-container :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-2'">
      <q-page class="q-pa-md">
        <div class="row justify-between items-center q-mb-md">
          <q-input
            v-model="projectsDto.name"
            outlined
            dense
            placeholder="Search projects..."
            class="col-grow"
            :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
            @keyup.enter="onSearch"
          >
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
          
          <q-select
            v-model="projectsDto.sortingField"
            :options="sortingOptions"
            option-label="label"
            option-value="value"
            outlined
            dense
            label="Sort by"
            map-options
            emit-value
            class="q-ml-md"
            style="min-width: 150px;"
            :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
            @update:model-value="onSortChange"
          />
        </div>
        
        <div v-if="projects.length" class="row q-col-gutter-md">
          <div v-for="(project, index) in projects" :key="index" class="col-12 col-md-6 col-lg-4">
            <q-card
              :class="['project-card', isDarkMode ? 'bg-grey-9 text-white' : 'bg-white', 
                      {'border-left': true, 'border-primary': !isDarkMode, 'border-accent': isDarkMode}]"
              class="full-height"
            >
              <q-card-section>
                <div class="text-h6 text-weight-bold ellipsis">{{ project.name }}</div>
                <q-separator class="q-my-sm" />
                <div class="text-caption text-grey ellipsis-2-lines" style="min-height: 40px;">
                  {{ project.description || 'No description provided' }}
                </div>
                
                <div class="row justify-between items-center q-mt-sm">
                  <div :class="isDarkMode ? 'text-grey-5' : 'text-grey-7'" class="text-caption">
                    {{ formatDate(project.createdDate) }}
                  </div>
                  <q-badge
                    v-if="isOwnProject"
                    :color="project.visibility ? 'green' : 'yellow'"
                    :text-color="project.visibility ? 'white' : 'black'"
                  >
                    {{ project.visibility ? 'Public' : 'Private' }}
                  </q-badge>
                </div>
              </q-card-section>
              
              <q-card-actions class="q-px-md q-pb-md">
  <div class="full-width">
    <div v-if="projectStatus[project.id]?.stat === 'EndS' || projectStatus[project.id]?.stat === 'NotFound'" class="row justify-between">
      <q-btn
        label="Run Analysis"
        icon="play_arrow"
        color="primary"
        dense
        no-caps
        @click="startAnalysis(project.id)"
      />
      <q-btn
        label="View"
        color="secondary"
        dense
        no-caps
        @click="viewProject(project.id)"
      />
    </div>
    
    <div v-else class="text-center">
      <q-chip
        :color="getStatusColor(projectStatus[project.id]?.stat)"
        text-color="white"
        dense
        class="full-width justify-center"
        style="white-space: normal; height: auto; min-height: 42px; padding: 4px 8px;"
      >
        <div class="text-center full-width">
          <div>Status: {{ getStatusText(projectStatus[project.id]?.stat) }}</div>
          <div v-if="projectStatus[project.id]?.message" style="font-size: 0.8em;">
            {{ projectStatus[project.id]?.message }}
          </div>
        </div>
      </q-chip>
    </div>
  </div>
</q-card-actions>
            </q-card>
          </div>
        </div>
        
        <div v-else class="column items-center justify-center" style="height: 60vh;">
          <q-icon name="folder_off" size="xl" :color="isDarkMode ? 'grey-6' : 'grey-5'" />
          <div :class="isDarkMode ? 'text-grey-6' : 'text-grey-7'" class="q-mt-md text-h6">
            No projects found
          </div>
          <q-btn
            label="Create Project"
            color="primary"
            icon="add"
            class="q-mt-md"
            @click="showCreateProjectModal = true"
          />
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
      
      <q-dialog v-model="showCreateProjectModal" @hide="loadProjects">
        <create-project-form 
          :isDarkMode="isDarkMode" 
          @project-created="loadProjects"
          @close="showCreateProjectModal = false"
        />
      </q-dialog>
      <q-dialog v-model="showModal" persistent>
        <q-card :class="isDarkMode ? 'bg-grey-8' : ''" style="width: 450px; max-width: 90vw;">
          <q-card-section class="row items-center q-pb-none">
            <q-icon name="play_circle_outline" size="sm" color="primary" class="q-mr-sm" />
            <span class="text-h6" :class="isDarkMode ? 'text-white' : ''">Start Analysis</span>
            <q-space />
            <q-btn icon="close" flat round dense v-close-popup />
          </q-card-section>

          <q-card-section class="q-pt-md">
            <div class="row q-col-gutter-sm">
              <div class="col-12">
                <q-select
                  v-model="selectedAnalyzerCategory"
                  :options="Object.keys(analyzers)"
                  outlined
                  dense
                  label="Analyzer Category"
                  :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
                />
              </div>
              
              <div class="col-12" v-if="availableAnalyzers.length">
                <q-select
                  v-model="selectedAnalyzers"
                  :options="availableAnalyzers"
                  outlined
                  dense
                  multiple
                  label="Select Analyzers"
                  :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
                  use-chips
                />
              </div>
            </div>

            <div class="row q-col-gutter-sm q-mt-sm">
              <div class="col-12">
                <q-input
                  v-model="branchName"
                  outlined
                  dense
                  label="Branch"
                  prefix="origin/"
                  :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
                />
              </div>
              
              <div class="col-12">
                <q-input
                  v-model="commitHash"
                  outlined
                  dense
                  label="Commit Hash"
                  placeholder="Optional"
                  :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
                />
              </div>
            </div>
          </q-card-section>

          <q-card-actions align="right" class="q-pa-md">
            <q-btn label="Cancel" flat color="grey" v-close-popup class="q-mr-sm" />
            <q-btn label="Run Analysis" color="primary" @click="runProject" />
          </q-card-actions>
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
import { getAvatar, getId } from 'src/services/userServices'
import { getStatus, reportCreate, getAnalizator } from 'src/services/analysisServeces'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'

export default {
  components: {
    CreateProjectForm
  },
  data() {
    return {
      drawer: false,
      miniState: true,
      projects: [],
      projectStatus: {},
      user: {
        avatar: null
      },
      projectsDto: {
        count: 12,
        page: 0,
        sortingField: 'createdDate',
        userId: null,
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
      showModal: false,
      showCreateProjectModal: false,
      analyzers: {},
      selectedAnalyzerCategory: null,
      selectedAnalyzers: [],
      availableAnalyzers: [],
      projectId: null,
      branchName: null,
      commitHash: null
    }
  },
  watch: {
    selectedAnalyzerCategory(newVal) {
      this.availableAnalyzers = this.analyzers[newVal] || []
    }
  },
  methods: {
    nextPage() {
      this.projectsDto.page += 1
      this.loadProjects()
    },
    previousPage() {
      if (this.projectsDto.page > 0) {
        this.projectsDto.page -= 1
        this.loadProjects()
      }
    },
    getStatusColor(status) {
      switch (status) {
        case 'Run':
          return 'green'
        case 'TODO':
          return 'yellow'
        case 'Err':
        case 'Ban':
          return 'red'
        default:
          return 'grey'
      }
    },

    getStatusText(status) {
      switch (status) {
        case 'Run':
          return 'Running'
        case 'TODO':
          return 'In Queue'
        case 'Err':
          return 'Error'
        case 'Ban':
          return 'Banned'
        case 'EndS':
          return 'Completed'
        case 'NotFound':
          return 'Not Started'
        default:
          return status || 'Unknown'
      }
    },
    truncateMessage(message, maxLength = 50) {
      if (!message) return ''
      return message.length > maxLength 
        ? `${message.substring(0, maxLength)}...` 
        : message
    },
    async loadProjects() {
      try {
        if (this.projectsDto.userId != this.$route.params.id) {
          this.projectsDto.myProject = false
          this.projectsDto.userId = this.$route.params.id
        }
        
        if (this.projectsDto.sortingField == 'name') {
          this.projectsDto.sortDirection = 'ASC'
        } else {
          this.projectsDto.sortDirection = 'DESC'
        }
        
        const response = await getProjects(this.projectsDto)
        this.projects = response.data
        this.isOwnProject = this.projectsDto.userId === (await getId()).data

        for (const project of this.projects) {
          try {
            const statusResponse = await getStatus(project.id)
            this.projectStatus[project.id] = statusResponse.data || { stat: 'Unknown' }
          } catch (error) {
            this.projectStatus[project.id] = { stat: 'Error', message: 'Failed to fetch status' }
          }
        }
      } catch (error) {
        this.$q.notify({
          message: 'Error loading projects',
          color: 'negative',
          icon: 'error'
        })
      }
    },
    toggleDarkMode() {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
      localStorage.setItem('darkMode', this.isDarkMode)
    },
    goToHome() {
      const route = localStorage.getItem('role') === 'admin' ? '/admin' : '/home'
      this.$router.push(route)
    },
    goToProfile() {
      this.$router.push(`/profile/${this.userId}`)
    },
    goToAllProjects() {
      this.$router.push(`/projects/${this.userId}`)
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      })
    },
    onDialogHide() {
      this.showModal = false
    },
    startAnalysis(id) {
      this.projectId = id
      this.showModal = true
    },
    async runProject() {
      try {
        await reportCreate({
          idProject: this.projectId,
          needReports: this.selectedAnalyzers,
          branch: this.branchName,
          commit: this.commitHash
        })
        
        this.$q.notify({
          message: 'Analysis started successfully',
          color: 'positive',
          icon: 'check_circle'
        })
        
        this.showModal = false
        this.loadProjects()
      } catch (error) {
        this.$q.notify({
          message: 'Failed to start analysis',
          color: 'negative',
          icon: 'error'
        })
      }
    },
    viewProject(projectId) {
      this.$router.push(`/project/${projectId}`)
    },
    onSearch() {
      this.projectsDto.page = 0
      this.loadProjects()
    },
    onSortChange() {
      this.projectsDto.page = 0
      this.loadProjects()
    },
    async fetchUser() {
      try {
        const response = await getAvatar(this.userId)
        this.user.avatar = response.data
      } catch (error) {
        console.error('Error fetching user avatar:', error)
      }
    },
    async fetchAnalyzers() {
      try {
        const response = await getAnalizator()
        this.analyzers = response.data
      } catch (error) {
        this.$q.notify({
          message: 'Failed to fetch analyzers',
          color: 'negative',
          icon: 'error'
        })
      }
    },
    async fetchId() {
      try {
        const response = await getId()
        this.userId = response.data
        this.projectsDto.userId = this.userId
        await this.fetchUser()
        await this.fetchAnalyzers()
        this.loadProjects()
      } catch (error) {
        console.error('Error fetching user ID:', error)
      }
    }
  },
  created() {
    this.isDarkMode = localStorage.getItem('darkMode') === 'true' || Dark.isActive
    Dark.set(this.isDarkMode)
    this.fetchId()
  }
}
</script>

<style scoped>
.project-card {
  transition: transform 0.2s, box-shadow 0.2s;
  border-radius: 8px;
}

.project-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.border-left {
  border-left: 4px solid;
}

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.ellipsis-2-lines {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.q-pagination {
  border-radius: 8px;
  padding: 8px;
  background: rgba(255, 255, 255, 0.1);
}

.bg-dark {
  background-color: #121212;
}

.bg-grey-9 {
  background-color: #1e1e1e;
}

.bg-grey-8 {
  background-color: #2d2d2d;
}

.status-message {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.project-card .q-chip {
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.q-chip--dense.multiline {
  white-space: normal;
  height: auto;
  min-height: 42px;
  padding: 4px 8px;
  line-height: 1.3;
}

.q-chip--dense.multiline .chip-content {
  width: 100%;
  text-align: center;
}
</style>