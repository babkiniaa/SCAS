<template>
  <q-layout view="lHh lpr lFf">
    <q-header elevated class="bg-grey-9">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Vulnerability search "{{ projectName }}"</q-toolbar-title>
        <q-space />
        <q-btn dense round icon="search" aria-label="Search" class="text-white" />
        <q-avatar size="42px" class="q-ml-md">
          <img v-if="user.avatar" :src="user.avatar" alt="User Avatar" />
          <q-icon v-else name="person" class="text-white" />
        </q-avatar>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="drawer" show-if-above :mini="miniState" @mouseenter="miniState = false" @mouseleave="miniState = true" :width="200" bordered content-class="bg-grey-9">
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
        <q-card bordered class="q-pa-lg my-card" style="max-width: 900px; margin: 0 auto;">
          <q-card-section class="row q-col-gutter-md">
            <q-item class="col-12" v-for="task in tasks" :key="task.label">
              <q-item-section avatar>
                <q-circular-progress v-if="task.status === 'in-progress'" indeterminate size="30px" color="primary" />
                <q-icon v-else-if="task.status === 'success'" name="check_circle" color="green" />
                <q-icon v-else-if="task.status === 'error'" name="cancel" color="red" />
              </q-item-section>
              <q-item-section>{{ task.label }}</q-item-section>
            </q-item>
          </q-card-section>

          <q-card-section class="text-center">
            <q-btn v-if="allTasksComplete" label="View the report" color="primary" @click="viewReport" />
            <q-btn flat label="Back" color="primary" @click="goToAllProjects" />
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>
<script>
import { downloadRepository, runSpotBugs, runPMD, runOWASP, runCheckstyle, deleteProject } from 'src/services/analysisServices'
export default {
  data () {
    return {
      drawer: false,
      miniState: true,
      projectName: '',
      projectId: null,
      user: {
        avatar: null
      },
      tasks: [
        { label: 'Downloading from the repository', status: 'in-progress', method: downloadRepository },
        { label: 'Checking dependencies for vulnerabilities', status: 'pending', method: runSpotBugs },
        { label: 'Running PMD analysis', status: 'pending', method: runPMD },
        { label: 'Running OWASP check', status: 'pending', method: runOWASP },
        { label: 'Running Checkstyle analysis', status: 'pending', method: runCheckstyle }
      ]
    }
  },
  computed: {
    allTasksComplete () {
      return this.tasks.every(task => task.status === 'success')
    }
  },
  async created () {
    this.projectId = this.$route.params.projectId
    this.projectName = 'Your Project Name'
    await this.runAnalysisTasks()
  },
  methods: {
    async runAnalysisTasks () {
      for (let i = 0; i < this.tasks.length; i++) {
        const task = this.tasks[i]
        try {
          this.tasks[i].status = 'in-progress'
          await task.method(this.projectId)
          this.tasks[i].status = 'success'
        } catch (error) {
          this.tasks[i].status = 'error'
          break
        }
      }
      if (this.allTasksComplete) {
        await deleteProject(this.projectId)
      }
    },
    viewReport () {
      this.$router.push({ name: 'report', params: { projectId: this.projectId } })
    },
    goToAllProjects () {
      this.$router.push('/projects')
    },
    goToHome () {
      this.$router.push('/home')
    },
    goToCreateProject () {
      this.$router.push('/create-project')
    }
  }
}
</script>
