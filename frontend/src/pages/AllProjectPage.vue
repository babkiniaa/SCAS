<template>
  <q-layout view="lHh lpr lFf">
    <!-- Header -->
    <q-header elevated class="bg-grey-9">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">All Projects</q-toolbar-title>
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
        <div class="row q-mb-md items-center justify-center">
          <div class="search-sort-container row q-gutter-md col-8 justify-center">
            <q-input
              outlined
              debounce="300"
              v-model="projectsDto.searchTerm"
              placeholder="Search projects"
              @input="onSearch"
              class="col-5"
            >
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
            <q-select
              outlined
              v-model="projectsDto.sortingField"
              :options="sortingOptions"
              label="Sort by"
              class="col-5"
              @input="onSortChange"
            />
          </div>
          <q-btn
            outlined
            label="Create Project"
            icon="add_circle"
            class="q-ml-md sticky-create-btn"
            color="blue"
            @click="goToCreateProject"
          />
        </div>
        <div v-if="projects.length" class="q-mt-md">
          <q-card
            v-for="(project, index) in projects"
            :key="index"
            class="q-mb-md"
          >
            <q-card-section>
              <div class="text-h6">{{ project.nameProject }}</div>
              <div class="text-body1 q-mt-xs">{{ project.description }}</div>
              <div class="text-caption q-mt-sm">Created: {{ formatDate(project.createdDate) }}</div>
              <q-badge
                v-if="isOwnProject"
                :color="project.visibility ? 'green' : 'yellow'"
                class="q-ml-md q-mt-sm"
              >
                {{ project.visibility ? 'Public' : 'Private' }}
              </q-badge>
            </q-card-section>
            <q-card-actions align="right">
              <q-btn label="View Report" color="primary" @click="viewReport(project)" />
            </q-card-actions>
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
export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      projects: [],
      user: {
        avatar: null
      },
      projectsDto: {
        count: 10,
        page: 0,
        sortingField: 'createdDate',
        searchTerm: '',
        userId: '',
        myProject: true
      },
      sortingOptions: [
        { label: 'Date', value: 'createdDate' },
        { label: 'Name', value: 'nameProject' }
      ],
      isOwnProject: false,
      currentUserId: null
    }
  },
  async created () {
    this.currentUserId = localStorage.getItem('currentId')
    await this.loadProjects()
  },
  methods: {
    async loadProjects () {
      try {
        console.log(this.currentUserId)
        // eslint-disable-next-line eqeqeq
        if (this.projectsDto.userId != this.$route.params.profileId) {
          this.projectsDto.myProject = false
          this.projectsDto.userId = this.$route.params.profileId
        }
        const response = await getProjects(this.projectsDto)
        this.projects = response.data
        this.isOwnProject = this.projectsDto.userId === localStorage.getItem('currentId')
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
    viewReport (project) {
      this.$router.push(`/project/${project.id}/report`)
    },
    onSearch () {
      this.projectsDto.page = 0
      this.loadProjects()
    },
    goToAllProjects () {
      this.$router.push('/projects')
    },
    onSortChange () {
      this.projectsDto.page = 0
      this.loadProjects()
    }
  }
}
</script>
<style scoped>
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
.q-input,
.q-select,
.q-btn {
  max-width: 100%;
}
.sticky-create-btn {
  position: sticky;
  top: 10px;
  z-index: 1;
}
.q-gutter-md {
  gap: 20px;
}
.search-sort-container {
  justify-content: center;
  display: flex;
}
</style>
