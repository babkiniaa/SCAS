<template>
  <q-layout view="hHh Lpr lff"  class="shadow-2 rounded-borders">
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title :class="isDarkMode ? 'text-white' : ''">Create Project</q-toolbar-title>
        <q-space />
        <q-btn dense round icon="search" aria-label="Search" class="text-white" />
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
    <q-page-container  :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
      <q-page style="margin-top: 15px;">
        <q-card :class="['q-pa-md', 'shadow-2', 'my-card', isDarkMode ? 'bg-grey-8' : '']" bordered>
          <q-form @submit="submitCreateProject">
            <q-card-section class="row q-col-gutter-md">
              <div class="col-8">
                <q-input v-model="projectName" outlined label="Project Name" dense :class="isDarkMode ? 'bg-grey-9 text-white' : ''" />
              </div>
              <div class="col-4">
                <q-btn-dropdown
                  color="primary"
                  :class="isDarkMode ? 'bg-grey-6' : ''"
                  :label="selectedVisibility ? 'Visibility: ' + selectedVisibility : 'Select Visibility'"
                >
                  <q-list>
                    <q-item clickable v-close-popup @click="selectVisibility('public')">
                      <q-item-section><q-item-label>Public</q-item-label></q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup @click="selectVisibility('private')">
                      <q-item-section><q-item-label>Private</q-item-label></q-item-section>
                    </q-item>
                  </q-list>
                </q-btn-dropdown>
              </div>
            </q-card-section>

            <q-card-section class="row q-col-gutter-md q-pt-none">
              <div class="col-12">
                <q-btn-dropdown
                  :class="isDarkMode ? 'bg-grey-6' : ''"
                  color="primary"
                  :label="selectedSource ? 'Source: ' + selectedSource : 'Select Source'"
                >
                  <q-list>
                    <q-item clickable v-close-popup @click="selectSource('GitHub')">
                      <q-item-section><q-item-label>GitHub</q-item-label></q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup @click="selectSource('Upload')">
                      <q-item-section><q-item-label>Upload</q-item-label></q-item-section>
                    </q-item>
                  </q-list>
                </q-btn-dropdown>
              </div>
            </q-card-section>

            <q-card-section v-if="selectedSource === 'GitHub'" class="row q-col-gutter-md q-pt-none">
              <div class="col-12">
                <q-input v-model="gitHubLink" outlined label="GitHub Link" dense :class="isDarkMode ? 'bg-grey-9 text-white' : ''" />
              </div>
            </q-card-section>

            <q-card-section v-if="selectedSource === 'Upload'" class="row q-col-gutter-md q-pt-none">
              <div class="col-12">
                <q-btn label="Upload File" color="primary" @click="handleFileUpload" />
              </div>
            </q-card-section>

            <q-card-section>
              <q-input v-model="projectDescription" outlined label="Description" type="textarea" :class="isDarkMode ? 'bg-grey-9 text-white' : ''" />
            </q-card-section>

            <q-card-section class="text-center">
              <q-btn label="Create Project" color="primary" :class="isDarkMode ? 'bg-grey-6' : ''" class="q-mt-md full-width" type="submit" :disable="isCreating" />
              <q-btn label="Start Analysis" color="primary" :class="isDarkMode ? 'bg-grey-6' : ''" class="q-mt-md full-width" @click="startAnalysis" :disable="!projectCreated" />
            </q-card-section>
          </q-form>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { createProject } from 'src/services/projectServices'
import { getAvatar } from 'src/services/userServices'
export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      user: {
        avatar: null
      },
      isDarkMode: Dark.isActive,
      projectName: '',
      selectedSource: '',
      selectedVisibility: 'public',
      gitHubLink: '',
      projectDescription: '',
      isCreating: false,
      projectCreated: false,
      userId: localStorage.getItem('currentId'),
      vis: true
    }
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    selectVisibility (visibility) {
      // eslint-disable-next-line eqeqeq
      if (visibility == 'private') {
        this.vis = false
      }
      this.selectedVisibility = visibility
    },
    selectSource (source) {
      this.selectedSource = source
    },
    async submitCreateProject () {
      this.isCreating = true
      try {
        await createProject({
          name: this.projectName,
          source: this.selectedSource,
          visibility: this.vis,
          gitHubLink: this.gitHubLink,
          description: this.projectDescription,
          userId: this.userId

        })
        this.$q.notify({ message: 'Project created successfully', color: 'green' })
        this.projectCreated = true
      } catch (error) {
        this.$q.notify({ message: 'Failed to create project', color: 'red' })
      } finally {
        this.isCreating = false
      }
    },
    startAnalysis () {
      this.$q.notify({ message: 'Analysis started successfully', color: 'green' })
    },
    handleFileUpload () {
      this.$q.notify({ message: 'File upload clicked', color: 'blue' })
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
    async fetchUser () {
      const response = await getAvatar(localStorage.getItem('currentId'))
      this.user.avatar = response.data
    }
  },
  created () {
    this.fetchUser()
  }
}
</script>

<style scoped>
.my-card {
  max-width: 600px;
  margin:  auto;
}
.bg-dark {
  background-color: #121212;
}
.text-white {
  color: #ffffff;
}
</style>
