<template>
  <q-layout view="lHh lpr lFf">
    <q-header elevated class="bg-grey-9">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Create project</q-toolbar-title>
        <q-space />
        <q-btn dense round icon="search" aria-label="Search" class="text-white" />
        <q-avatar size="42px" class="q-ml-md">
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
          <q-item-section class="text-white">Home</q-item-section>
        </q-item>
        <q-item clickable v-ripple @click="goToCreateProject">
          <q-item-section avatar>
            <q-icon name="add_circle" />
          </q-item-section>
          <q-item-section class="text-white">Create Project</q-item-section>
        </q-item>
        <q-item clickable v-ripple @click="goToAllProjects">
          <q-item-section avatar>
            <q-icon name="folder_open" />
          </q-item-section>
          <q-item-section class="text-white">All Projects</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>
    <q-page-container>
      <q-page class="q-pa-md">
        <q-card class="q-pa-lg my-card" bordered style="max-width: 900px; margin: 0 auto;">
          <q-form @submit="submitCreateProject">
            <q-card-section class="row q-col-gutter-md">
              <div class="col-8">
                <q-input v-model="projectName" outlined label="name" dense />
              </div>
              <div class="col-4">
                <q-btn-dropdown
                  color="primary"
                  :label="selectedVisibility ? 'Visibility: ' + selectedVisibility : 'Select Visibility'"
                >
                  <q-list>
                    <q-item clickable v-close-popup @click="selectVisibility('public')">
                      <q-item-section>
                        <q-item-label>Public</q-item-label>
                      </q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup @click="selectVisibility('private')">
                      <q-item-section>
                        <q-item-label>Private</q-item-label>
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-btn-dropdown>
              </div>
            </q-card-section>
            <q-card-section class="row q-col-gutter-md q-pt-none">
              <div class="col-12">
                <q-btn-dropdown
                  color="primary"
                  :label="selectedSource ? 'Source: ' + selectedSource : 'Select Source'"
                >
                  <q-list>
                    <q-item clickable v-close-popup @click="selectSource('GitHub')">
                      <q-item-section>
                        <q-item-label>GitHub</q-item-label>
                      </q-item-section>
                    </q-item>
                    <q-item clickable v-close-popup @click="selectSource('Upload')">
                      <q-item-section>
                        <q-item-label>Upload</q-item-label>
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-btn-dropdown>
              </div>
            </q-card-section>
            <q-card-section v-if="selectedSource === 'GitHub'" class="row q-col-gutter-md q-pt-none">
              <div class="col-12">
                <q-input v-model="gitHubLink" outlined label="GitHub link" dense />
              </div>
            </q-card-section>

            <q-card-section v-if="selectedSource === 'Upload'" class="row q-col-gutter-md q-pt-none">
              <div class="col-12">
                <q-btn label="Upload file" color="primary" @click="handleFileUpload" />
              </div>
            </q-card-section>
            <q-card-section>
              <q-input v-model="projectDescription" outlined label="Description" type="textarea" />
            </q-card-section>
            <q-card-section class="text-center">
              <q-btn label="Create and start searching for vulnerabilities" color="primary" class="q-mt-md full-width" type="submit" />
            </q-card-section>
          </q-form>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>
<script>
import { createProject } from 'src/services/projectServices'

export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      user: {
        avatar: null
      },
      projectName: '',
      selectedSource: '',
      sourceOptions: ['GitHub', 'Upload'],
      visibilityOptions: ['public', 'private'],
      selectedVisibility: '',
      gitHubLink: '',
      projectDescription: ''
    }
  },
  methods: {
    selectVisibility (visibility) {
      this.selectedVisibility = visibility
    },
    selectSource (source) {
      this.selectedSource = source
    },

    async submitCreateProject () {
      try {
        const response = await createProject({
          name: this.projectName,
          source: this.selectedSource,
          visibility: this.selectedVisibility,
          gitHubLink: this.gitHubLink,
          description: this.projectDescription
        })
        this.$q.notify({ message: response.data, color: 'green' })
        this.$router.push('/analysis')
      } catch (error) {
        this.$q.notify({ message: error.response.data, color: 'red' })
      }
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
      this.$router.push('/projects')
    }
  }
}
</script>
<style scoped>
.my-card {
  max-width: 600px;
  margin: 16px auto;
}
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
</style>
