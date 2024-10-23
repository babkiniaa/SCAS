<template>
  <q-layout view="lHh lpr lFf">
    <q-header elevated class="bg-grey-9">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Create project</q-toolbar-title>
        <q-space />
        <q-btn dense round icon="search" aria-label="Search" class="text-white" />
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
              <q-btn
                label="Create Project"
                color="primary"
                class="q-mt-md full-width"
                type="submit"
                @click="submitCreateProject"
                :disable="isCreating"
              />
              <q-btn
                label="Start Analysis"
                color="green"
                class="q-mt-md full-width"
                @click="startAnalysis"
                :disable="!projectCreated"
              />
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
      visibilityProject: true,
      projectName: '',
      selectedSource: '',
      sourceOptions: ['GitHub', 'Upload'],
      visibilityOptions: ['public', 'private'],
      selectedVisibility: 'public',
      gitHubLink: '',
      projectDescription: '',
      isCreating: false,
      projectCreated: false,
      createdProjectId: null
    }
  },
  methods: {
    selectVisibility (visibility) {
      this.selectedVisibility = visibility
      this.visibilityProject = visibility === 'public'
    },
    selectSource (source) {
      this.selectedSource = source
    },
    async submitCreateProject () {
      this.isCreating = true
      console.log(this.visibilityProject)
      try {
        await createProject({
          name: this.projectName,
          source: this.selectedSource,
          visibility: this.visibilityProject,
          gitHubLink: this.gitHubLink,
          description: this.projectDescription
        })
        this.$q.notify({ message: 'Project created successfully', color: 'green' })
        this.projectCreated = true
      } catch (error) {
        this.$q.notify({ message: 'Failed to create project', color: 'red' })
      } finally {
        this.isCreating = false
      }
    },
    async startAnalysis () {
      try {
        // await startProjectAnalysis(this.createdProjectId)
        this.$q.notify({ message: 'Analysis started successfully', color: 'green' })
      } catch (error) {
        this.$q.notify({ message: 'Failed to start analysis', color: 'red' })
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
      const id = localStorage.getItem('currentId')
      this.$router.push({ name: 'projects', params: { id } })
    },
    goToProfile () {
      const userId = localStorage.getItem('currentId')
      this.$router.push(`/profile/${userId}`)
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
