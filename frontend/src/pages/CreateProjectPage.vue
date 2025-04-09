<template>
  <q-layout view="hHh Lpr lff">
    <q-card :class="['q-pa-md', 'shadow-2', 'my-card', isDarkMode ? 'bg-grey-8' : '']" bordered style="max-width: 600px; margin: 0px auto;">
      <q-form @submit="submitCreateProject">
        <q-card-section class="text-center q-pb-sm">
          <q-icon name="create_new_folder" size="md" color="primary" />
          <div class="text-h6 q-mt-sm" :class="isDarkMode ? 'text-white' : 'text-dark'">Create New Project</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-input 
            v-model="projectName" 
            outlined 
            label="Project Name *" 
            dense
            lazy-rules
            :rules="[val => !!val || 'Field is required']"
            :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
          />
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-select
            v-model="selectedVisibility"
            :options="visibilityOptions"
            outlined
            dense
            label="Visibility *"
            :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
          />
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-input 
            v-model="gitHubLink" 
            outlined 
            label="GitHub Repository URL *" 
            dense
            lazy-rules
            :rules="[val => !!val || 'Field is required']"
            :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
          />
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-input 
            v-model="projectDescription" 
            outlined 
            label="Description" 
            type="textarea"
            dense
            :class="isDarkMode ? 'bg-grey-9 text-white' : 'bg-white'"
          />
        </q-card-section>

        <q-card-actions class="q-px-md q-pt-none">
          <q-btn 
            label="Create Project" 
            color="primary" 
            class="full-width" 
            type="submit" 
            :loading="isCreating"
            :disabled="isCreating"
          />
        </q-card-actions>
      </q-form>
    </q-card>

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
          <q-btn label="Run Analysis" color="primary" @click="startAnalysis" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { createProject } from 'src/services/projectServices'
import { getAnalizator, reportCreate } from 'src/services/analysisServeces'
import { getId } from 'src/services/userServices'

export default {
  data() {
    return {
      isDarkMode: Dark.isActive,
      projectName: '',
      selectedVisibility: 'public',
      gitHubLink: '',
      projectDescription: '',
      isCreating: false,
      userId: null,
      vis: true,
      idProject: null,
      analyzers: {},
      selectedAnalyzerCategory: null,
      selectedAnalyzers: [],
      availableAnalyzers: [],
      showModal: false,
      branchName: null,
      commitHash: null,
      visibilityOptions: [
        { label: 'Public', value: 'public' },
        { label: 'Private', value: 'private' }
      ]
    }
  },
  watch: {
    selectedAnalyzerCategory(newVal) {
      this.availableAnalyzers = this.analyzers[newVal] || []
    },
    selectedVisibility(newVal) {
      this.vis = newVal === 'public'
    }
  },
  methods: {
    toggleDarkMode() {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async submitCreateProject() {
      this.isCreating = true
      try {
        const response = await createProject({
          name: this.projectName,
          source: 'GitHub',
          visibility: this.vis,
          url: this.gitHubLink,
          description: this.projectDescription,
          userId: this.userId
        })
        this.$q.notify({
          message: 'Project created successfully',
          color: 'positive',
          position: 'top'
        })
        this.idProject = response.data
        this.showModal = true
      } catch (error) {
        this.$q.notify({
          message: 'Failed to create project',
          color: 'negative',
          position: 'top'
        })
      } finally {
        this.isCreating = false
      }
    },
    async startAnalysis() {
      this.isCreating = true
      try {
        await reportCreate({
          idProject: this.idProject,
          needReports: this.selectedAnalyzers,
          branch: this.branchName,
          commit: this.commitHash
        })
        this.showModal = false
        this.$q.notify({
          message: 'Analysis started successfully',
          color: 'positive',
          position: 'top'
        })
        this.$router.push(`/projects/${this.userId}`)
      } catch (error) {
        this.$q.notify({
          message: 'Failed to start analysis',
          color: 'negative',
          position: 'top'
        })
      } finally {
        this.isCreating = false
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
          position: 'top'
        })
      }
    },
    async fetchId() {
      this.userId = (await getId()).data
    }
  },
  created() {
    this.fetchId()
    this.fetchAnalyzers()
  }
}
</script>

<style scoped>
.my-card {
  border-radius: 8px;
}
</style>