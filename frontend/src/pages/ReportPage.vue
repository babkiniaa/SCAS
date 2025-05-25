<template>
  <q-layout view="hHh Lpr lff" class="shadow-2 rounded-borders">
    <q-header elevated class="bg-black full-width">
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
    </q-drawer>

    <q-page-container :class="isDarkMode ? 'bg-grey-9' : 'bg-grey-1'">

      <div v-if="reportData != null">
        <q-page class="q-pa-lg">
          <q-card flat :class="isDarkMode ? 'bg-grey-8' : 'bg-white'">
            <q-card-section class="row items-center justify-between q-pb-none">
              <div class="text-h5 text-weight-bold">
                <q-icon name="assignment" class="q-mr-sm" />
                Analysis Results
              </div>
              <q-btn-toggle
                v-model="viewMode"
                toggle-color="secondary"
                :options="[
                  {label: 'Table View', value: 'table', icon: 'table_chart'},
                  {label: 'Document View', value: 'document', icon: 'description'}
                ]"
                dense
                class="q-mr-sm"
              />
              <q-btn-group rounded>
                <q-btn
                  v-for="report in reportTypes"
                  :key="report.value"
                  :label="report.label"
                  :color="selectReport === report.value ? 'primary' : (isDarkMode ? 'grey-7' : 'grey-4')"
                  :text-color="selectReport === report.value ? 'white' : (isDarkMode ? 'white' : 'dark')"
                  @click="selectReport = report.value"
                  dense
                  no-caps
                />
              </q-btn-group>
            </q-card-section>

            <q-separator class="q-mt-sm" />

            <q-card-section>
              <div v-if="viewMode === 'document'" class="q-gutter-y-lg">
                <!-- OWASP Section -->
                <q-card
                  v-if="reportData.dependencyCustoms.length && (selectReport === 'all' || selectReport === 'OWASP')"
                  flat
                  :class="isDarkMode ? 'bg-grey-10' : 'bg-blue-1'"
                >
                  <q-card-section>
                    <div class="row items-center">
                      <q-icon name="security" color="negative" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">OWASP Vulnerabilities</div>
                      <q-chip color="red" text-color="white" class="q-ml-sm">
                        {{ reportData.dependencyCustoms.length }} issues
                      </q-chip>
                    </div>
                  </q-card-section>

                  <q-card-section class="q-pt-none">
                    <q-list separator>
                      <q-item
                        v-for="dep in reportData.dependencyCustoms"
                        :key="dep.name"
                        class="q-my-sm rounded-borders"
                        :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
                      >
                        <q-item-section>
                          <q-item-label class="text-weight-bold">
                            {{ dep.name }} @{{ dep.version }}
                          </q-item-label>
                          <q-item-label caption>
                            <q-icon name="copyright" size="xs" class="q-mr-xs" />
                            {{ dep.license || 'Unknown license' }}
                          </q-item-label>

                          <q-expansion-item
                            v-if="dep.owaspVulnerabilities && dep.owaspVulnerabilities.length"
                            label="Vulnerabilities"
                            icon="warning"
                            switch-toggle-side
                            class="q-mt-sm"
                            :header-class="isDarkMode ? 'text-red-4' : 'text-red'"
                          >
                            <q-card :class="isDarkMode ? 'bg-grey-9' : 'bg-white'">
                              <q-card-section>
                                <div
                                  v-for="vuln in dep.owaspVulnerabilities"
                                  :key="vuln.name"
                                  class="q-pa-sm q-mb-sm rounded-borders"
                                  :class="isDarkMode ? 'bg-red-10' : 'bg-red-1'"
                                >
                                  <div class="row items-center">
                                    <q-icon name="warning" color="red" class="q-mr-sm" />
                                    <div class="text-weight-bold">{{ vuln.name }}</div>
                                  </div>
                                  <div class="q-ml-xl q-mt-xs">
                                    {{ vuln.description }}
                                    <div v-if="vuln.notes" class="text-caption q-mt-xs">
                                      <q-icon name="info" size="xs" />
                                      {{ vuln.notes }}
                                    </div>
                                  </div>
                                </div>
                              </q-card-section>
                            </q-card>
                          </q-expansion-item>
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </q-card-section>
                </q-card>

                <!-- PMD Section -->
                <q-card
                v-if="reportData.ruleViolationCustoms.length && (selectReport === 'all' || selectReport === 'PMD')"
                flat
                :class="isDarkMode ? 'bg-grey-10' : 'bg-purple-1'"
              >
                <q-card-section>
                  <div class="row items-center">
                    <q-icon name="code" color="purple" size="md" class="q-mr-sm" />
                    <div class="text-h6 text-weight-bold">Code Quality Issues</div>
                    <q-chip color="purple" text-color="white" class="q-ml-sm">
                      {{ reportData.ruleViolationCustoms.length }} violations
                    </q-chip>
                  </div>
                </q-card-section>

                <q-card-section class="q-pt-none">
                  <q-list separator>
                    <q-item
                      v-for="violation in reportData.ruleViolationCustoms"
                      :key="violation.name + violation.fileName + violation.beginLine"
                      class="q-my-sm rounded-borders"
                      :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
                    >
                      <q-item-section>
                        <q-item-label class="text-weight-bold">
                          <q-icon
                            :name="priorityIcons[violation.priority] || 'priority_high'"
                            :color="priorityColors[violation.priority] || 'grey'"
                            class="q-mr-sm"
                          />
                          {{ violation.name }}
                        </q-item-label>
                        <q-item-label caption>
                          <q-icon name="insert_drive_file" size="xs" class="q-mr-xs" />
                          {{ violation.fileName }} (Lines {{ violation.beginLine }}-{{ violation.endLine }})
                        </q-item-label>

                        <q-item-label class="q-mt-sm">
                          {{ violation.message }}
                        </q-item-label>

                        <q-item-label v-if="violation.description" class="q-mt-sm text-caption">
                          {{ violation.description }}
                        </q-item-label>
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-card-section>
              </q-card>

                <!-- Bug Instances Section -->
                <q-card
                  v-if="reportData.bugInstanceCustoms.length && (selectReport === 'all' || selectReport === 'StopBugs')"
                  flat
                  :class="isDarkMode ? 'bg-grey-10' : 'bg-orange-1'"
                >
                  <q-card-section>
                    <div class="row items-center">
                      <q-icon name="bug_report" color="orange" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">Potential Bugs</div>
                      <q-chip color="orange" text-color="white" class="q-ml-sm">
                        {{ reportData.bugInstanceCustoms.length }} instances
                      </q-chip>
                    </div>
                  </q-card-section>

                  <q-card-section class="q-pt-none">
                    <q-list separator>
                      <q-item
                        v-for="bug in reportData.bugInstanceCustoms"
                        :key="bug.instanceHash"
                        class="q-my-sm rounded-borders"
                        :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
                      >
                        <q-item-section>
                          <div class="row items-center">
                            <q-icon
                              name="warning"
                              :color="bug.priority === 'High' ? 'red' : 'orange'"
                              class="q-mr-sm"
                            />
                            <div class="text-weight-bold">{{ bug.type }}</div>
                            <q-chip
                              dense
                              :color="bug.priority === 'High' ? 'red' : 'orange'"
                              text-color="white"
                              class="q-ml-sm"
                            >
                              {{ bug.priority }}
                            </q-chip>
                          </div>

                          <q-item-label caption class="q-mt-xs">
                            {{ bug.message }}
                          </q-item-label>

                          <div class="row q-mt-xs">
                            <q-chip
                              v-if="bug.introducedByChangeOfExistingClass"
                              dense
                              icon="add_circle"
                              color="green"
                              text-color="white"
                            >
                              Introduced by change
                            </q-chip>

                            <q-chip
                              v-if="bug.removedByChangeOfPersistingClass"
                              dense
                              icon="remove_circle"
                              color="red"
                              text-color="white"
                              class="q-ml-xs"
                            >
                              Removed by change
                            </q-chip>
                          </div>
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </q-card-section>
                </q-card>

                <!-- CheckStyle Section -->
                <q-card
                  v-if="reportData.violationCustoms.length && (selectReport === 'all' || selectReport === 'CheckStyle')"
                  flat
                  :class="isDarkMode ? 'bg-grey-10' : 'bg-cyan-1'"
                >
                  <q-card-section>
                    <div class="row items-center">
                      <q-icon name="format_indent_increase" color="teal" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">Style Violations</div>
                      <q-chip color="teal" text-color="white" class="q-ml-sm">
                        {{ reportData.violationCustoms.length }} issues
                      </q-chip>
                    </div>
                  </q-card-section>

                  <q-card-section class="q-pt-none">
                    <q-list separator>
                      <q-item
                        v-for="violation in reportData.violationCustoms"
                        :key="violation.key"
                        class="q-my-sm rounded-borders"
                        :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
                      >
                        <q-item-section>
                          <q-item-label class="text-weight-bold">
                            {{ violation.moduleId }} (Line {{ violation.lineNo }})
                          </q-item-label>
                          <q-item-label caption>
                            {{ violation.key }}
                          </q-item-label>

                          <q-item-label class="q-mt-sm">
                            {{ violation.customMessage || 'No specific message provided' }}
                          </q-item-label>
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </q-card-section>
                </q-card>

                <!-- DeadCode Section -->
                <q-card
                  v-if="reportData.summaryCustoms.length && (selectReport === 'all' || selectReport === 'DieDead')"
                  flat
                  :class="isDarkMode ? 'bg-grey-10' : 'bg-cyan-1'"
                >
                  <q-card-section>
                    <div class="row items-center">
                      <q-icon name="format_indent_increase" color="teal" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">Dead sections</div>
                      <q-chip color="teal" text-color="white" class="q-ml-sm">
                        {{ reportData.violationCustoms.length }} issues
                      </q-chip>
                    </div>
                  </q-card-section>

                  <q-card-section class="q-pt-none">
                    <q-list separator>
                      <q-item
                        v-for="summary in reportData.summaryCustoms"
                        :key="summary.key"
                        class="q-my-sm rounded-borders"
                        :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
                      >
                        <q-item-section>
                          <q-item-label class="text-weight-bold">
                            {{ summary.methodOrField }}
                          </q-item-label>
                          <q-item-label caption>
                            {{ summary.key }}
                          </q-item-label>

                          <q-item-label class="text-weight-bold">
                            {{  summary.before }}
                          </q-item-label>
                          <q-item-label class="text-weight-bold">
                            {{  summary.after }}
                          </q-item-label>
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </q-card-section>
                </q-card>
              </div>

              <!-- Table View -->
              <div v-else>
                <!-- PMD Table -->
                <q-card
                  v-if="reportData.ruleViolationCustoms.length && (selectReport === 'all' || selectReport === 'PMD')"
                  flat
                  class="q-mb-md"
                >
                  <q-card-section>
                    <div class="row items-center q-mb-md">
                      <q-icon name="code" color="purple" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">Code Quality Issues</div>
                      <q-chip color="purple" text-color="white" class="q-ml-sm">
                        {{ reportData.ruleViolationCustoms.length }} violations
                      </q-chip>
                    </div>

                    <div class="row q-mb-md">
                      <q-select
                        v-model="selectedFile"
                        :options="fileOptions"
                        label="Filter by file"
                        dense
                        outlined
                        clearable
                        style="min-width: 250px"
                        class="q-mr-sm"
                      />

                      <q-select
                        v-model="selectedPriority"
                        :options="priorityOptions"
                        label="Filter by priority"
                        dense
                        outlined
                        clearable
                        style="min-width: 200px"
                      />
                    </div>

                    <q-table
                      :rows="filteredRows"
                      :columns="pmdColumns"
                      row-key="uniqueKey"
                      flat
                      bordered
                      :loading="loading"
                      :filter="filter"
                      v-model:pagination="pagination"
                      class="sticky-header-table"
                      :class="isDarkMode ? 'bg-grey-9' : ''"
                    >
                      <template v-slot:body-cell-priority="props">
                        <q-td :props="props">
                          <q-chip
                            dense
                            :color="priorityColors[props.row.priority] || 'grey'"
                            text-color="white"
                            :icon="priorityIcons[props.row.priority] || 'priority_high'"
                          >
                            {{ props.row.priority }}
                          </q-chip>
                        </q-td>
                      </template>

                      <template v-slot:body-cell-fileName="props">
                        <q-td :props="props">
                          <div class="text-weight-medium">{{ props.row.fileName }}</div>
                          <div class="text-caption">
                            Lines {{ props.row.beginLine }}-{{ props.row.endLine }}
                          </div>
                        </q-td>
                      </template>

                      <template v-slot:body-cell-actions="props">
                        <q-td :props="props">
                          <q-btn
                            flat
                            round
                            dense
                            icon="info"
                            color="primary"
                            @click="showViolationDetails(props.row)"
                          />
                        </q-td>
                      </template>
                    </q-table>
                  </q-card-section>
                </q-card>

                <!-- OWASP Table -->
                <q-card
                  v-if="reportData.dependencyCustoms.length && (selectReport === 'all' || selectReport === 'OWASP')"
                  flat
                  class="q-mb-md"
                >
                  <q-card-section>
                    <div class="row items-center q-mb-md">
                      <q-icon name="security" color="red" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">OWASP Vulnerabilities</div>
                      <q-chip color="red" text-color="white" class="q-ml-sm">
                        {{ reportData.dependencyCustoms.length }} dependencies
                      </q-chip>
                    </div>

                    <q-table
                        :rows="reportData.dependencyCustoms"
                        :columns="owaspColumns"
                        row-key="name"
                        flat
                        bordered
                        :loading="loading"
                        v-model:pagination="pagination"
                        class="sticky-header-table"
                        :class="isDarkMode ? 'bg-grey-9' : ''"
                      >
                      <template v-slot:body-cell-vulnerabilities="props">
                        <q-td :props="props">
                          <q-badge
                            v-if="props.row.owaspVulnerabilities && props.row.owaspVulnerabilities.length"
                            color="red"
                            class="q-px-sm q-py-xs"
                          >
                            {{ props.row.owaspVulnerabilities.length }} vulns
                          </q-badge>
                          <q-badge v-else color="green" class="q-px-sm q-py-xs">
                            Secure
                          </q-badge>
                        </q-td>
                      </template>

                      <template v-slot:body-cell-actions="props">
                        <q-td :props="props">
                          <q-btn
                            v-if="props.row.owaspVulnerabilities && props.row.owaspVulnerabilities.length"
                            flat
                            round
                            dense
                            icon="warning"
                            color="red"
                            @click="showVulnerabilityDetails(props.row)"
                          />
                        </q-td>
                      </template>
                    </q-table>
                  </q-card-section>
                </q-card>

                <!-- Bug Instances Table -->
                <q-card
                  v-if="reportData.bugInstanceCustoms.length && (selectReport === 'all' || selectReport === 'StopBugs')"
                  flat
                  class="q-mb-md"
                >
                  <q-card-section>
                    <div class="row items-center q-mb-md">
                      <q-icon name="bug_report" color="orange" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">Potential Bugs</div>
                      <q-chip color="orange" text-color="white" class="q-ml-sm">
                        {{ reportData.bugInstanceCustoms.length }} instances
                      </q-chip>
                    </div>

                    <q-table
                      :rows="reportData.bugInstanceCustoms"
                      :columns="bugColumns"
                      row-key="instanceHash"
                      flat
                      bordered
                      :loading="loading"
                      v-model:pagination="pagination"
                      class="sticky-header-table"
                      :class="isDarkMode ? 'bg-grey-9' : ''"
                    >
                      <template v-slot:body-cell-priority="props">
                        <q-td :props="props">
                          <q-chip
                            dense
                            :color="props.row.priority === 'High' ? 'red' : 'orange'"
                            text-color="white"
                            icon="warning"
                          >
                            {{ props.row.priority }}
                          </q-chip>
                        </q-td>
                      </template>

                      <template v-slot:body-cell-actions="props">
                        <q-td :props="props">
                          <q-btn
                            flat
                            round
                            dense
                            icon="info"
                            color="primary"
                            @click="showBugDetails(props.row)"
                          />
                        </q-td>
                      </template>
                    </q-table>
                  </q-card-section>
                </q-card>

                <!-- CheckStyle Table -->
                <q-card
                  v-if="reportData.violationCustoms.length && (selectReport === 'all' || selectReport === 'CheckStyle')"
                  flat
                >
                  <q-card-section>
                    <div class="row items-center q-mb-md">
                      <q-icon name="format_indent_increase" color="teal" size="md" class="q-mr-sm" />
                      <div class="text-h6 text-weight-bold">Style Violations</div>
                      <q-chip color="teal" text-color="white" class="q-ml-sm">
                        {{ reportData.violationCustoms.length }} issues
                      </q-chip>
                    </div>

                    <q-table
                      :rows="reportData.violationCustoms"
                      :columns="styleColumns"
                      row-key="key"
                      flat
                      bordered
                      :loading="loading"
                      v-model:pagination="pagination"
                      class="sticky-header-table"
                      :class="isDarkMode ? 'bg-grey-9' : ''"
                    >
                      <template v-slot:body-cell-location="props">
                        <q-td :props="props">
                          <div class="text-weight-medium">{{ props.row.moduleId }}</div>
                          <div class="text-caption">
                            Line {{ props.row.lineNo }}, Col {{ props.row.columnNo }}
                          </div>
                        </q-td>
                      </template>
                    </q-table>
                  </q-card-section>
                </q-card>
              </div>
            </q-card-section>
          </q-card>
        </q-page>
      </div>

      <div v-else class="flex flex-center" style="height: 80vh">
        <q-spinner-cube color="primary" size="3em" />
      </div>

      <q-dialog v-model="showCreateProjectModal">
        <create-project-form :isDarkMode="isDarkMode" />
      </q-dialog>

      <q-dialog v-model="showDetailsDialog" maximized>
        <q-card :class="isDarkMode ? 'bg-grey-9' : ''">
          <q-toolbar class="bg-primary text-white">
            <q-toolbar-title>
              <q-icon :name="detailsIcon" class="q-mr-sm" />
              {{ detailsTitle }}
            </q-toolbar-title>
            <q-btn flat round dense icon="close" v-close-popup />
          </q-toolbar>

          <q-card-section class="q-pt-none">
            <pre class="code-block">{{ detailsContent }}</pre>
          </q-card-section>

          <q-card-actions align="right">
            <q-btn flat label="Close" color="primary" v-close-popup />
          </q-card-actions>
        </q-card>
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
  components: {
    CreateProjectForm
  },

  data() {
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
      viewMode: 'table',
      userId: null,
      selectedFile: null,
      selectedPriority: null,
      loading: false,
      filter: '',
      selectReport: 'all',
      showDetailsDialog: false,
      detailsTitle: '',
      detailsContent: '',
      detailsIcon: 'info',

      // Report type options
      reportTypes: [
        { label: 'All Reports', value: 'all' },
        { label: 'OWASP', value: 'OWASP' },
        { label: 'PMD', value: 'PMD' },
        { label: 'Bugs', value: 'StopBugs' },
        { label: 'Style', value: 'CheckStyle' },
        { label: 'Dead', value: 'Die Dead Enough' }
      ],

      // Priority mapping for visual cues
      priorityColors: {
        High: 'red',
        'Medium High': 'orange',
        Medium: 'yellow',
        'Medium Low': 'blue',
        Low: 'green'
      },

      priorityIcons: {
        High: 'priority_high',
        'Medium High': 'keyboard_arrow_up',
        Medium: 'remove',
        'Medium Low': 'keyboard_arrow_down',
        Low: 'low_priority'
      },

      // Table columns
      pmdColumns: [
        {
          name: 'priority',
          label: 'Priority',
          field: 'priority',
          sortable: true,
          align: 'left'
        },
        {
          name: 'name',
          label: 'Rule',
          field: 'name',
          sortable: true,
          align: 'left'
        },
        {
          name: 'fileName',
          label: 'Location',
          field: 'fileName',
          sortable: true,
          align: 'left'
        },
        {
          name: 'message',
          label: 'Message',
          field: 'message',
          sortable: false,
          align: 'left'
        },
        {
          name: 'actions',
          label: '',
          field: '',
          sortable: false,
          align: 'right'
        }
      ],

      owaspColumns: [
        {
          name: 'name',
          label: 'Dependency',
          field: 'name',
          sortable: true,
          align: 'left'
        },
        {
          name: 'version',
          label: 'Version',
          field: 'version',
          sortable: true,
          align: 'left'
        },
        {
          name: 'license',
          label: 'License',
          field: 'license',
          sortable: true,
          align: 'left'
        },
        {
          name: 'vulnerabilities',
          label: 'Status',
          field: row => row.owaspVulnerabilities ? row.owaspVulnerabilities.length : 0,
          sortable: true,
          align: 'center'
        },
        {
          name: 'actions',
          label: '',
          field: '',
          sortable: false,
          align: 'right'
        }
      ],

      bugColumns: [
        {
          name: 'type',
          label: 'Type',
          field: 'type',
          sortable: true,
          align: 'left'
        },
        {
          name: 'priority',
          label: 'Priority',
          field: 'priority',
          sortable: true,
          align: 'left'
        },
        {
          name: 'category',
          label: 'Category',
          field: 'category',
          sortable: true,
          align: 'left'
        },
        {
          name: 'message',
          label: 'Message',
          field: 'message',
          sortable: false,
          align: 'left'
        },
        {
          name: 'actions',
          label: '',
          field: '',
          sortable: false,
          align: 'right'
        }
      ],

      styleColumns: [
        {
          name: 'location',
          label: 'Location',
          field: 'moduleId',
          sortable: true,
          align: 'left'
        },
        {
          name: 'key',
          label: 'Rule',
          field: 'key',
          sortable: true,
          align: 'left'
        },
        {
          name: 'customMessage',
          label: 'Message',
          field: 'customMessage',
          sortable: false,
          align: 'left'
        }
      ],

      // Pagination
      pagination: {
        sortBy: 'priority',
        descending: false,
        page: 1,
        rowsPerPage: 10
      }
    }
  },

  computed: {
    fileOptions() {
      const files = [...new Set(this.reportData.ruleViolationCustoms.map(item => item.fileName))]
      return files.map(file => ({ label: file, value: file }))
    },

    priorityOptions() {
      const priorities = [...new Set(this.reportData.ruleViolationCustoms.map(item => item.priority))]
      return priorities.map(p => ({ label: p, value: p }))
    },

    filteredRows() {
      let rows = this.reportData.ruleViolationCustoms

      if (this.selectedFile) {
        rows = rows.filter(item => item.fileName === this.selectedFile)
      }

      if (this.selectedPriority) {
        rows = rows.filter(item => item.priority === this.selectedPriority)
      }

      return rows
    }
  },

  methods: {
    toggleDarkMode() {
      Dark.toggle()
      this.isDarkMode = Dark.isActive
    },

    goToHome() {
      const route = localStorage.getItem('role') === 'admin' ? '/admin' : '/home'
      this.$router.push(route)
    },

    goToAllProjects() {
      this.$router.push({ name: 'projects', params: { id: this.userId } })
    },

    goToProfile() {
      this.$router.push(`/profile/${this.userId}`)
    },

    async fetchUser() {
      try {
        const response = await getAvatar(this.userId)
        this.user.avatar = response.data
      } catch (error) {
        console.error('Error fetching user avatar:', error)
      }
    },

    async fetchProject() {
      this.loading = true
      try {
        const response = await getReport(this.projectId)
        this.reportData = response.data
      } catch (error) {
        console.error('Error fetching report:', error)
        this.$q.notify({
          type: 'negative',
          message: 'Failed to load analysis report'
        })
      } finally {
        this.loading = false
      }
    },

    showViolationDetails(violation) {
      this.detailsTitle = `Code Quality Issue: ${violation.name}`
      this.detailsIcon = 'code'
      this.detailsContent = JSON.stringify({
        Description: violation.description,
        File: violation.fileName,
        Location: `Lines ${violation.beginLine}-${violation.endLine}`,
        Priority: violation.priority,
        Message: violation.message,
        Rule: violation.name
      }, null, 2)
      this.showDetailsDialog = true
    },

    showVulnerabilityDetails(dependency) {
      this.detailsTitle = `Security Vulnerability: ${dependency.name}@${dependency.version}`
      this.detailsIcon = 'security'
      this.detailsContent = JSON.stringify({
        Package: `${dependency.name}@${dependency.version}`,
        License: dependency.license,
        Ecosystem: dependency.ecosystem,
        Description: dependency.description,
        Vulnerabilities: dependency.owaspVulnerabilities || []
      }, null, 2)
      this.showDetailsDialog = true
    },

    showBugDetails(bug) {
      this.detailsTitle = `Potential Bug: ${bug.type}`
      this.detailsIcon = 'bug_report'
      this.detailsContent = JSON.stringify({
        Type: bug.type,
        Priority: bug.priority,
        Category: bug.category,
        Message: bug.message,
        Source: bug.source,
        IntroducedByChange: bug.introducedByChangeOfExistingClass,
        RemovedByChange: bug.removedByChangeOfPersistingClass
      }, null, 2)
      this.showDetailsDialog = true
    },

    async fetchId() {
      try {
        this.userId = (await getId()).data
        this.projectId = this.$route.params.id
        await this.fetchUser()
        await this.fetchProject()
      } catch (error) {
        console.error('Error initializing data:', error)
      }
    }
  },

  mounted() {
    this.fetchId()
  }
}
</script>

<style lang="scss" scoped>
.sticky-header-table {
  /* height or max-height is important */
  height: calc(100vh - 300px);

  .q-table__top,
  .q-table__bottom,
  thead tr:first-child th {
    background-color: inherit;
  }

  thead tr th {
    position: sticky;
    z-index: 1;
  }

  thead tr:first-child th {
    top: 0;
  }

  &.q-table--loading thead tr:last-child th {
    top: 48px;
  }
}

.code-block {
  white-space: pre-wrap;
  font-family: monospace;
  background: rgba(0,0,0,0.05);
  padding: 16px;
  border-radius: 4px;
  max-height: 70vh;
  overflow: auto;
}

body.body--dark {
  .code-block {
    background: rgba(255,255,255,0.05);
  }
}

.q-item {
  transition: all 0.3s ease;

  &:hover {
    transform: translateX(2px);
  }
}

.q-card {
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0,0,0,0.15) !important;
  }
}

.q-table {
  th {
    font-weight: bold;
    text-transform: uppercase;
    font-size: 0.8rem;
    letter-spacing: 0.5px;
  }

  tr:hover {
    background-color: rgba(0,0,0,0.02) !important;
  }
}

body.body--dark {
  .q-table {
    tr:hover {
      background-color: rgba(255,255,255,0.02) !important;
    }
  }
}
</style>
