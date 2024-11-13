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
            @click="goToCreateProject"
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
              <div v-if="projectStatus[project.id] === 'EndS'" class="full-width q-mt-md">
                <q-btn
                  label="View Report"
                  :class="isDarkMode ? 'bg-grey-6' : ''"
                  class="full-width"
                  @click="viewReport(project.id)"
                />
              </div>
              <div v-else-if="projectStatus[project.id] === 'NotFound'" class="full-width q-mt-md">
                <q-btn
                  label="Run"
                  :icon="playIcon"
                  :class="isDarkMode ? 'bg-grey-6' : ''"
                  class="q-mb-xs text-green"
                  @click="runProject(project.id)"
                />
              </div>
              <div v-else class="text-caption text-right q-mt-md">
                {{ projectStatus[project.id] }}
                color: yellow
              </div>
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
    </q-page-container>
  </q-layout>
</template>

<script>
import { getProjects } from 'src/services/projectServices'
import { Dark } from 'quasar'
import { getAvatar } from 'src/services/userServices'
import { getStatus } from 'src/services/analysisServeces'

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
      playIcon: 'play_arrow'
    }
  },
  methods: {
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
      this.$router.push('/home')
    },
    goToCreateProject () {
      this.$router.push('/create-project')
    },
    goToProfile () {
      const id = localStorage.getItem('currentId')
      this.$router.push(`/profile/${id}`)
    },
    formatDate (date) {
      return new Date(date).toLocaleDateString()
    },
    viewReport (projectId) {
      this.$router.push(`/report/${projectId}`)
    },
    runProject (projectId) {
      // Logic to run the project goes here
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
    }
  },
  created () {
    this.fetchUser()
    this.currentUserId = localStorage.getItem('currentId')
    this.loadProjects()
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
