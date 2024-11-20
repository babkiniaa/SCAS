<template>
    <q-layout view="hHh Lpr lff">
        <q-card :class="['q-pa-md', 'shadow-2', 'my-card', isDarkMode ? 'bg-grey-8' : '']" bordered>
          <q-form @submit="submitCreateProject">
            <q-card-section class="row q-col-gutter-md">
              <div class="col-12">
                <q-input v-model="projectName" outlined label="Project Name" dense :class="isDarkMode ? 'bg-grey-9 text-white' : ''" />
              </div>
            </q-card-section>
            <q-card-section class="row q-col-gutter-md q-pt-none">
              <div class="col-6">
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
              <div class="col-6">
                <q-btn-dropdown
                  color="primary"
                  :class="isDarkMode ? 'bg-grey-6' : ''"
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
              <q-btn label="Start Analysis" color="primary" :class="isDarkMode ? 'bg-grey-6' : ''" class="q-mt-md full-width"
              clickable v-ripple
              @click="showModal = true"
               :disable="!projectCreated"
              />
            </q-card-section>
          </q-form>
        </q-card>
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
                  @click="startAnalysis"
                />
            </q-card-section>
          </q-card>
        </q-dialog>
    </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { createProject } from 'src/services/projectServices'
import { getAnalizator, reportCreate } from 'src/services/analysisServeces'
export default {
  data () {
    return {
      isDarkMode: Dark.isActive,
      projectName: '',
      selectedSource: '',
      selectedVisibility: 'public',
      gitHubLink: '',
      projectDescription: '',
      isCreating: false,
      projectCreated: false,
      userId: localStorage.getItem('currentId'),
      vis: true,
      idProject: null,
      analyzers: {},
      selectedAnalyzerCategory: null,
      selectedAnalyzers: [],
      availableAnalyzers: [],
      showModal: false,
      playIcon: 'play_arrow'
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
        const response = await createProject({
          name: this.projectName,
          source: this.selectedSource,
          visibility: this.vis,
          url: this.gitHubLink,
          description: this.projectDescription,
          userId: this.userId

        })
        this.$q.notify({ message: 'Project created successfully', color: 'green' })
        this.projectCreated = true
        this.idProject = response.data
      } catch (error) {
        this.$q.notify({ message: 'Failed to create project', color: 'red' })
      } finally {
        this.isCreating = false
      }
    },
    async startAnalysis () {
      try {
        await reportCreate({
          idProject: this.idProject,
          needReports: this.selectedAnalyzers
        })
        this.$q.notify({ message: 'Project created successfully', color: 'green' })
      } catch (error) {
        this.$q.notify({ message: 'Failed to create project', color: 'red' })
      } finally {
        this.$router.push(`/projects/${this.userId}`)
      }
    },
    handleFileUpload () {
      this.$q.notify({ message: 'File upload clicked', color: 'blue' })
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
    this.fetchAnalyzers()
  }
}
</script>

<style scoped>
.no-scroll-dropdown {
  max-height: 200px;
  overflow-y: auto;
}
.my-card {
  max-width: 800px;
  margin: 100px auto;
}
.bg-dark {
  background-color: #121212;
}
.text-white {
  color: #ffffff;
}
</style>
