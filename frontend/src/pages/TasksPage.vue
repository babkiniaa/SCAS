<template>
  <q-page :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
    <div class="q-pa-md">
      <q-card :class="isDarkMode ? 'bg-grey-9' : 'bg-white'">
        <q-card-section>
          <div class="text-h6">Task Queue Management</div>
        </q-card-section>

        <q-separator />

        <q-card-section>
          <div class="row q-col-gutter-md q-mb-md">
            <div class="col-md-6 col-sm-12">
              <q-input
                v-model="searchTerm"
                placeholder="Search tasks..."
                outlined
                dense
                clearable
              >
                <template v-slot:prepend>
                  <q-icon name="search" />
                </template>
              </q-input>
            </div>
            <div class="col-md-3 col-sm-6">
              <q-select
                v-model="statusFilter"
                :options="statusOptions"
                outlined
                dense
                label="Filter by status"
              />
            </div>
            <div class="col-md-3 col-sm-6">
              <q-btn
                color="primary"
                icon="refresh"
                label="Refresh"
                @click="fetchQueueTasks"
              />
            </div>
          </div>

          <q-table
            :rows="filteredTasks"
            :columns="columns"
            row-key="id"
            :loading="loading"
            :pagination="pagination"
            :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
            flat
            bordered
          >
            <template v-slot:body-cell-status="props">
              <q-td :props="props">
                <q-badge 
                  :color="getStatusColor(props.row.statusTask)"
                  :label="props.row.statusTask"
                />
              </q-td>
            </template>

            <template v-slot:body-cell-actions="props">
              <q-td :props="props">
                <q-btn
                  v-if="props.row.statusTask !== 'Run'"
                  dense
                  round
                  flat
                  icon="stop"
                  color="negative"
                  @click="banTask(props.row.id)"
                  class="q-mr-xs"
                >
                  <q-tooltip>Stop task</q-tooltip>
                </q-btn>
                <q-btn
                  dense
                  round
                  flat
                  icon="info"
                  color="info"
                  @click="showTaskDetails(props.row)"
                >
                  <q-tooltip>View details</q-tooltip>
                </q-btn>
              </q-td>
            </template>
          </q-table>
        </q-card-section>
      </q-card>
    </div>

    <!-- Task Details Dialog -->
    <q-dialog v-model="showTaskDetailsDialog">
      <q-card style="min-width: 500px">
        <q-card-section>
          <div class="text-h6">Task Details</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div v-if="selectedTask">
            <div class="row q-mb-sm">
              <div class="col-4 text-weight-bold">Task ID:</div>
              <div class="col-8">{{ selectedTask.id }}</div>
            </div>
            <div class="row q-mb-sm">
              <div class="col-4 text-weight-bold">Status:</div>
              <div class="col-8">
                <q-badge :color="getStatusColor(selectedTask.statusTask)">
                  {{ selectedTask.statusTask }}
                </q-badge>
              </div>
            </div>
            <div class="row q-mb-sm">
              <div class="col-4 text-weight-bold">Created At:</div>
              <div class="col-8">{{ formatDate(selectedTask.createdAt) }}</div>
            </div>
            <div class="row q-mb-sm">
              <div class="col-4 text-weight-bold">Updated At:</div>
              <div class="col-8">{{ formatDate(selectedTask.updatedAt) }}</div>
            </div>
            <div class="row q-mb-sm">
              <div class="col-4 text-weight-bold">Details:</div>
              <div class="col-8">
                <pre>{{ JSON.stringify(selectedTask.details, null, 2) }}</pre>
              </div>
            </div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Close" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import { Dark } from 'quasar'
import { getAllTask, deleteTask } from 'src/services/AdminService'

export default {
  name: 'TasksPage',
  data() {
    return {
      isDarkMode: Dark.isActive,
      queueTasks: [],
      loading: false,
      searchTerm: '',
      statusFilter: 'all',
      pagination: {
        rowsPerPage: 10
      },
      showTaskDetailsDialog: false,
      selectedTask: null,
      statusOptions: [
        { label: 'All', value: 'all' },
        { label: 'Running', value: 'Run' },
        { label: 'Pending', value: 'Pending' },
        { label: 'Completed', value: 'Completed' },
        { label: 'Failed', value: 'Failed' }
      ],
      columns: [
        {
          name: 'id',
          label: 'Task ID',
          field: 'id',
          align: 'left',
          sortable: true
        },
        {
          name: 'status',
          label: 'Status',
          field: 'statusTask',
          align: 'center',
          sortable: true
        },
        {
          name: 'createdAt',
          label: 'Created',
          field: 'createdAt',
          align: 'left',
          format: val => new Date(val).toLocaleString(),
          sortable: true
        },
        {
          name: 'updatedAt',
          label: 'Updated',
          field: 'updatedAt',
          align: 'left',
          format: val => new Date(val).toLocaleString(),
          sortable: true
        },
        {
          name: 'actions',
          label: 'Actions',
          align: 'center'
        }
      ]
    }
  },
  computed: {
    filteredTasks() {
      let tasks = [...this.queueTasks]
      
      // Filter by status
      if (this.statusFilter !== 'all') {
        tasks = tasks.filter(task => task.statusTask === this.statusFilter)
      }
      
      // Search filter
      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase()
        tasks = tasks.filter(task => 
          task.id.toLowerCase().includes(term) || 
          task.statusTask.toLowerCase().includes(term))
      }
      
      return tasks
    }
  },
  methods: {
    async fetchQueueTasks() {
      this.loading = true
      try {
        const response = await getAllTask()
        this.queueTasks = response.data
      } catch (error) {
        this.$q.notify({
          message: 'Failed to fetch queue tasks',
          color: 'negative',
          icon: 'error'
        })
      } finally {
        this.loading = false
      }
    },
    getStatusColor(status) {
      switch (status) {
        case 'Run': return 'primary'
        case 'Pending': return 'orange'
        case 'Completed': return 'positive'
        case 'Failed': return 'negative'
        default: return 'grey'
      }
    },
    async banTask(taskId) {
      this.$q.dialog({
        title: 'Confirm Stop',
        message: `Are you sure you want to stop task ${taskId}?`,
        cancel: true,
        persistent: true
      }).onOk(async() => {
        try {
          await deleteTask(taskId)
          this.queueTasks = this.queueTasks.filter(task => task.id !== taskId)
          this.$q.notify({
            message: `Task ${taskId} stopped successfully`,
            color: 'positive',
            icon: 'check_circle'
          })
        } catch (error) {
          this.$q.notify({
            message: `Failed to stop task ${taskId}`,
            color: 'negative',
            icon: 'error'
          })
        }
      })
    },
    showTaskDetails(task) {
      this.selectedTask = task
      this.showTaskDetailsDialog = true
    },
    formatDate(date) {
      return new Date(date).toLocaleString()
    }
  },
  created() {
    this.fetchQueueTasks()
  }
}
</script>

<style lang="scss" scoped>
pre {
  background-color: rgba(0, 0, 0, 0.05);
  padding: 10px;
  border-radius: 4px;
  max-height: 200px;
  overflow-y: auto;
}
</style>