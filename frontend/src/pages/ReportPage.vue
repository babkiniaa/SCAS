<template href="https://fonts.googleapis.com/css?family=Roboto&display=swap" rel="stylesheet">
  <q-layout>
    <q-header elevated :class="isDarkMode ? 'bg-grey-10' : 'bg-grey-9'" class="full-width">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Homepage</q-toolbar-title>
        <q-space />
        <q-btn dense round icon="search" @click="search" aria-label="Search" class="text-white" />
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
      :content-class="isDarkMode ? 'bg-black' : 'bg-grey-8'"
    >
      <q-scroll-area class="fit" :horizontal-thumb-style="{ opacity: 0 }">
        <q-list padding>
          <q-item clickable v-ripple @click="goToHome">
            <q-item-section avatar>
              <q-icon name="home" :class="isDarkMode ? 'text-white' : 'text-black'" />
            </q-item-section>
            <q-item-section :class="isDarkMode ? 'text-white' : 'text-black'">Home</q-item-section>
          </q-item>
          <q-item clickable v-ripple @click="showCreateProjectModal = true">
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
    <q-page-container :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
      <div v-if="reportData != null" class="q-mt-md">
        <q-page style="margin-top: 15px;">
          <q-card :class="['q-pa-md', 'shadow-2', 'my-card', isDarkMode ? 'bg-grey-8' : '']" bordered>
            <q-btn-dropdown
              label="Select Report"
              dense
              class="q-mr-sm"
              no-caps
              flat
              color="primary"
            >
              <q-list>
                <q-item clickable v-ripple @click="selectReport = 'all'">
                  <q-item-section>All</q-item-section>
                </q-item>
                <q-item clickable v-ripple @click="selectReport = 'PMD'">
                  <q-item-section>PMD</q-item-section>
                </q-item>
                <q-item clickable v-ripple @click="selectReport = 'OWAPS'">
                  <q-item-section>OWAPS</q-item-section>
                </q-item>
                <q-item clickable v-ripple @click="selectReport = 'StopBugs'">
                  <q-item-section>StopBugs</q-item-section>
                </q-item>
                <q-item clickable v-ripple @click="selectReport = 'CheckStyle'">
                  <q-item-section>CheckStyle</q-item-section>
                </q-item>
              </q-list>
            </q-btn-dropdown>
            <div class="q-mb-md text-right">
              <q-btn
                outline
                color="primary"
                :icon="viewMode === 'document' ? 'description' : 'table_chart'"
                label="Toggle View"
                @click="toggleViewMode"
              />
            </div>

            <div v-if="viewMode === 'document'">
              <q-card-section v-if="reportData.dependencyCustoms.length && (selectReport ===  'all' || selectReport === 'OWAPS')">
                <div class="text-h6 text-weight-bold q-mb-md">OWASP Vulnerabilities Found</div>
                <div
                  v-for="dep in reportData.dependencyCustoms"
                  :key="dep.name"
                  class="q-my-md q-pa-sm border-box shadow-1 rounded-borders"
                >
                  <div class="text-body1 text-weight-bold">{{ dep.name }} - {{ dep.version }}</div>
                  <p>License: {{ dep.license }}</p>
                  <p>Ecosystem: {{ dep.ecosystem }}</p>
                  <p>Description: {{ dep.description }}</p>
                  <p v-if="dep.isVirtual">This is a virtual package.</p>
                  <ul>
                    <li v-for="vulnerability in dep.owaspVulnerabilities" :key="vulnerability.name">
                      <strong>{{ vulnerability.name }}</strong>: {{ vulnerability.description }}
                      <p><em>Notes:</em> {{ vulnerability.notes }}</p>
                    </li>
                  </ul>
                </div>
              </q-card-section>
              <q-card-section v-if="reportData.ruleViolationCustoms.length && (selectReport ===  'all' || selectReport === 'PMD')">
                <div class="text-h6 text-weight-bold q-mb-md">PMD Violations Found</div>
                <div
                  v-for="violation in reportData.ruleViolationCustoms"
                  :key="violation.name"
                  class="q-my-md q-pa-sm border-box shadow-1 rounded-borders"
                >
                  <div class="text-body1 text-weight-bold">{{ violation.name }}</div>
                  <p>Priority: {{ violation.priority }}</p>
                  <p>Message: {{ violation.message }}</p>
                  <p>File: {{ violation.fileName }} (Line: {{ violation.beginLine }}-{{ violation.endLine }}, Col: {{ violation.beginColumn }}-{{ violation.endColumn }})</p>
                  <p>{{ violation.description }}</p>
                </div>
              </q-card-section>
              <q-card-section v-if="reportData.bugInstanceCustoms.length && (selectReport ===  'all' || selectReport === 'StopBugs')">
                <div class="text-h6 text-weight-bold q-mb-md">Bug Instances Found</div>
                <div
                  v-for="bugInstance in reportData.bugInstanceCustoms"
                  :key="bugInstance.instanceHash"
                  class="q-my-md q-pa-sm border-box shadow-1 rounded-borders"
                >
                  <div class="text-body1 text-weight-bold">Type: {{ bugInstance.type }}</div>
                  <p>Priority: {{ bugInstance.priority }}</p>
                  <p>Hash Code: {{ bugInstance.cachedHashCode }}</p>
                  <p>New Hash: {{ bugInstance.instanceHash }}</p>
                  <p>Category: {{ bugInstance.category }} / {{ bugInstance.category }}</p>
                  <p>Message: {{ bugInstance.message }}</p>
                  <p>Source: {{ bugInstance.source }}</p>
                  <p>
                    Introduced by Change of Existing Class:
                    <q-badge color="green" v-if="bugInstance.introducedByChangeOfExistingClass">Yes</q-badge>
                    <q-badge color="red" v-else>No</q-badge>
                  </p>
                  <p>
                    Removed by Change of Persisting Class:
                    <q-badge color="green" v-if="bugInstance.removedByChangeOfPersistingClass">Yes</q-badge>
                    <q-badge color="red" v-else>No</q-badge>
                  </p>
                </div>
              </q-card-section>
              <q-card-section v-if="reportData.violationCustoms.length && (selectReport ===  'all' || selectReport === 'CheckStyle')">
                <div class="text-h6 text-weight-bold q-mb-md">Code Violations Found</div>
                <div
                  v-for="violation in reportData.violationCustoms"
                  :key="violation.key"
                  class="q-my-md q-pa-sm border-box shadow-1 rounded-borders"
                >
                  <div class="text-body1 text-weight-bold">Module: {{ violation.moduleId }}</div>
                  <p>Line: {{ violation.lineNo }}</p>
                  <p>Column: {{ violation.columnNo }}</p>
                  <p>Char Index: {{ violation.columnCharIndex }}</p>
                  <p>Token Type: {{ violation.tokenType }}</p>
                  <p>Key: {{ violation.key }}</p>
                  <p>Bundle: {{ violation.bundle }}</p>
                  <p>Source: {{ violation.source }}</p>
                  <p>Custom Message: {{ violation.customMessage }}</p>
                </div>
              </q-card-section>
            </div>
            <div v-else>
              <q-card class="shadow-2 q-pa-md" bordered style="width: 100%;">
                <q-card-section v-if="reportData.ruleViolationCustoms.length && (selectReport ===  'all' || selectReport === 'PMD')">
                  <q-btn-dropdown
                    label="Filter by File"
                    dense
                    class="q-mb-md"
                    no-caps
                    flat
                    color="primary"
                  >
                    <q-list>
                      <q-item clickable v-ripple @click="filterByFile(null)">
                        <q-item-section>Clear Filter</q-item-section>
                      </q-item>
                      <q-item
                        v-for="file in fileOptions"
                        :key="file.value"
                        clickable
                        v-ripple
                        @click="filterByFile(file.value)"
                      >
                        <q-item-section>{{ file.label }}</q-item-section>
                      </q-item>
                      </q-list>
                  </q-btn-dropdown>
                  <q-table
                    :rows="filteredRows"
                    :columns="columns"
                    row-key="uniqueKey"
                    flat
                    dense
                    class="full-width"
                    :rows-per-page-options="[0]"
                  >
                    <template v-slot:body-cell-priority="props">
                      <q-td>{{ props.row.priority }}</q-td>
                    </template>
                    <template v-slot:body-cell-name="props">
                      <q-td>{{ props.row.name }}</q-td>
                    </template>
                    <template v-slot:body-cell-message="props">
                      <q-td>{{ props.row.message }}</q-td>
                    </template>
                    <template v-slot:body-cell-fileName="props">
                      <q-td>{{ props.row.fileName }}</q-td>
                    </template>
                    <template v-slot:body-cell-lineRange="props">
                      <q-td>
                        {{ props.row.beginLine }} - {{ props.row.endLine }}
                      </q-td>
                    </template>
                  </q-table>
                </q-card-section>
                <q-card-section v-if="reportData.dependencyCustoms.length && (selectReport ===  'all' || selectReport === 'OWAPS')">
                <div class="text-h6 text-weight-bold q-mb-md">OWAPS Vulnerabilities</div>
                <q-table
                  :rows="reportData.dependencyCustoms"
                  :columns="owapsColumns"
                  row-key="name"
                  flat
                  dense
                  class="full-width"
                  :rows-per-page-options="[0]"
                />
              </q-card-section>

              <!-- Bug Instances Table -->
              <q-card-section v-if="reportData.bugInstanceCustoms.length && (selectReport ===  'all' || selectReport === 'StopBugs')">
                <div class="text-h6 text-weight-bold q-mb-md">Bug Instances</div>
                <q-table
                  :rows="reportData.bugInstanceCustoms"
                  :columns="bugColumns"
                  row-key="instanceHash"
                  flat
                  dense
                  class="full-width"
                  :rows-per-page-options="[0]"
                />
              </q-card-section>

              <!-- Code Violations Table -->
              <q-card-section v-if="reportData.violationCustoms.length && (selectReport ===  'all' || selectReport === 'CheckStyle')">
                <div class="text-h6 text-weight-bold q-mb-md">Code Violations</div>
                <q-table
                  :rows="reportData.violationCustoms"
                  :columns="violationColumns"
                  row-key="key"
                  flat
                  dense
                  class="full-width"
                  :rows-per-page-options="[0]"
                />
              </q-card-section>
              </q-card>
            </div>
          </q-card>
        </q-page>
      </div>
      <q-dialog v-model="showCreateProjectModal">
        <create-project-form :isDarkMode="isDarkMode" />
      </q-dialog>
    </q-page-container>
  </q-layout>
</template>
<script>
import { Dark } from 'quasar'
import { getAvatar, getId } from 'src/services/userServices'
import { getReport } from 'src/services/analysisServeces'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'
export default {
  data () {
    return {
      drawer: true,
      miniState: true,
      user: {
        avatar: null
      },
      isDarkMode: Dark.isActive,
      projectId: null,
      reportData: null,
      showCreateProjectModal: false,
      viewMode: 'table_chart',
      userId: null,
      selectedFile: null,
      columns: [
        { name: 'name', label: 'Name', field: 'name', sortable: true, align: 'left' },
        { name: 'priority', label: 'Priority', field: 'priority', sortable: true, sortMethod: (a, b) => this.sortPriority(a, b), align: 'left' },
        { name: 'message', label: 'Message', field: 'message', sortable: false, align: 'left' },
        { name: 'fileName', label: 'File', field: 'fileName', sortable: true, align: 'left' },
        { name: 'lineRange', label: 'Lines (Begin-End)', field: 'lineRange', align: 'left' }
      ],
      owapsColumns: [
        { name: 'name', label: 'Name', field: 'name', align: 'left' },
        { name: 'version', label: 'Version', field: 'version', align: 'left' },
        { name: 'license', label: 'License', field: 'license', align: 'left' },
        { name: 'ecosystem', label: 'Ecosystem', field: 'ecosystem', align: 'left' },
        { name: 'description', label: 'Description', field: 'description', align: 'left' }
      ],
      bugColumns: [
        { name: 'type', label: 'Type', field: 'type', align: 'left' },
        { name: 'priority', label: 'Priority', field: 'priority', align: 'left' },
        { name: 'cachedHashCode', label: 'Hash Code', field: 'cachedHashCode', align: 'left' },
        { name: 'category', label: 'Category', field: 'category', align: 'left' },
        { name: 'message', label: 'Message', field: 'message', align: 'left' },
        { name: 'source', label: 'Source', field: 'source', align: 'left' }
      ],
      violationColumns: [
        { name: 'moduleId', label: 'Module', field: 'moduleId', align: 'left' },
        { name: 'lineNo', label: 'Line', field: 'lineNo', align: 'left' },
        { name: 'columnNo', label: 'Column', field: 'columnNo', align: 'left' },
        { name: 'key', label: 'Key', field: 'key', align: 'left' },
        { name: 'customMessage', label: 'Custom Message', field: 'customMessage', align: 'left' }
      ],
      fileOptions: [],
      filteredRows: [],
      selectReport: 'all'
    }
  },
  components: {
    CreateProjectForm
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    toggleViewMode () {
      this.viewMode = this.viewMode === 'document' ? 'table' : 'document'
    },
    goToHome () {
      if (localStorage.getItem('role') === 'admin') {
        this.$router.push('/admin')
      } else {
        this.$router.push('/home')
      }
    },
    goToAllProjects () {
      const id = this.userId
      this.$router.push({ name: 'projects', params: { id } })
    },
    search () {},
    goToProfile () {
      const userId = this.userId
      this.$router.push(`/profile/${userId}`)
    },
    async fetchUser () {
      const response = await getAvatar(this.userId)
      this.user.avatar = response.data
    },
    async fetchProject () {
      const response = await getReport(this.projectId)
      this.reportData = response.data
      this.initializeFileOptions()
      this.filterByFile()
    },
    initializeFileOptions () {
      const files = [...new Set(this.reportData.ruleViolationCustoms.map((item) => item.fileName))]
      this.fileOptions = files.map((file) => ({ label: file, value: file }))
    },
    filterByFile (fileValue) {
      this.selectedFile = fileValue
      if (this.selectedFile) {
        this.filteredRows = this.reportData.ruleViolationCustoms.filter(
          (item) => item.fileName === this.selectedFile
        )
      } else {
        this.filteredRows = this.reportData.ruleViolationCustoms
      }
    },
    sortPriority (a, b) {
      console.log(a)
      const priorityOrder = {
        High: 1,
        'Medium High': 2,
        Medium: 3,
        'Medium Low': 4,
        Low: 5
      }
      return (priorityOrder[a] || 999) - (priorityOrder[b] || 999)
    },
    sortTable (column) {
      if (this.sortBy === column) {
        this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc'
      } else {
        this.sortBy = column
        this.sortOrder = 'asc'
      }
      this.reportData.ruleViolationCustoms.sort((a, b) => {
        const order = this.sortOrder === 'asc' ? 1 : -1
        if (a[column] < b[column]) return -order
        if (a[column] > b[column]) return order
        return 0
      })
    },
    async fetchId () {
      this.userId = (await getId()).data
      this.projectId = this.$route.params.id
      this.fetchUser()
      this.fetchProject()
    }
  },
  mounted () {
    this.fetchId()
  }
}
</script>

<style scoped>
.my-card {
  max-width: 1000px;
  margin: 0 auto;
}
.q-table {
  font-size: 1.2rem;
}
.wide-card {
  max-width: 100%;
}
.q-toolbar-title {
  font-size: 20px;
}
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
  color: white !important;
}
.bg-grey-11 {
  background-color: #1d1d1d;
}
.dark-bg {
  background-color: #1d1d1d !important;
}
</style>
