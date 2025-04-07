<template>
  <q-layout view="lHh lpr lFf">
    <q-drawer show-if-above side="left" bordered>
      <q-list>
        <q-item clickable v-ripple>
          <q-item-section avatar>
            <q-icon name="dashboard" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Dashboard</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <q-page :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
        <div class="flex justify-end q-pa-md">
          <q-btn
            dense
            round
            :icon="isDarkMode ? 'light_mode' : 'dark_mode'"
            @click="toggleDarkMode"
            aria-label="Toggle Dark Mode"
            class="text-white"
            style="background-color: black;"
          />
        </div>
        <div class="grafana-container">
          <iframe src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=8&__feature.dashboardSceneSolo" class="grafana-iframe"></iframe>
          <iframe src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=9&__feature.dashboardSceneSolo" class="grafana-iframe"></iframe>
          <iframe src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=10&__feature.dashboardSceneSolo" class="grafana-iframe"></iframe>
          <iframe src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=11&__feature.dashboardSceneSolo" class="grafana-iframe"></iframe>
          <iframe src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=12&__feature.dashboardSceneSolo" class="grafana-iframe"></iframe>
          <iframe src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=13&__feature.dashboardSceneSolo" class="grafana-iframe"></iframe>
        </div>
        <q-card
          v-if="queueTasks.length > 0"
          :class="['q-pa-md shadow-2 my-card', isDarkMode ? 'bg-grey-8' : '']"
          bordered
          class="q-mt-md"
        >
          <q-card-section>
            <div class="row q-col-gutter-md items-center">
              <div class="col-12">
                <h6>Queue Tasks</h6>
              </div>
            </div>
            <q-list class="q-mt-sm">
              <q-item
                v-for="task in queueTasks"
                :key="task.id"
                class="q-pb-sm"
              >
                <q-item-section>{{ task.id }}</q-item-section>
                <q-item-section>{{ task.statusTask }}</q-item-section>
                <q-item-section side v-if="task.statusTask !== 'Run'">
                  <q-btn
                    label="Stop"
                    color="red"
                    icon="stop"
                    @click="banTask(task.id)"
                  />
                </q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </q-card>
        <div class="user-list q-pa-md">
          <div class="q-mb-md flex justify-between items-center">
            <q-input
              v-model="searchTerm"
              placeholder="Search users..."
              outlined
              dense
              class="q-mr-md"
            />
            <q-btn label="Search" @click="searchUsers" color="primary" />
          </div>
          <q-card
            v-for="user in filteredUsers"
            :key="user.id"
            :class="['q-pa-md shadow-2', isDarkMode ? 'bg-grey-8' : '']"
            bordered
            class="q-mb-md"
          >
            <q-card-section class="row items-center">
              <q-item-section avatar>
                <q-avatar>
                  <img v-if="user.avatarUrl" :src="user.avatarUrl" alt="User Avatar" />
                  <q-icon v-else name="person" class="text-white" />
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <div>{{ user.username }}</div>
                <div v-if="!user.enable" class="text-red">Banned</div>
              </q-item-section>
              <q-item-section side>
                <q-btn
                  :label="user.enable ? 'Ban' : 'Unban'"
                  :color="user.enable ? 'red' : 'green'"
                  :icon="user.enable ? 'block' : 'check_circle'"
                  @click.stop="toggleUserStatus(user)"
                />
              </q-item-section>
            </q-card-section>
          </q-card>
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { getAllTask, deleteTask, fetchUsers, blockUserById, unbanUserById } from 'src/services/AdminService'

export default {
  data() {
    return {
      isDarkMode: Dark.isActive,
      queueTasks: [],
      users: [],
      searchTerm: ''
    }
  },
  computed: {
    filteredUsers() {
      if (!this.searchTerm) return this.users
      return this.users.filter(user =>
        user.username.toLowerCase().includes(this.searchTerm.toLowerCase())
      )
    }
  },
  methods: {
    toggleDarkMode() {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async fetchUsers() {
      try {
        const response = await fetchUsers()
        this.users = response.data
      } catch (error) {
        this.$q.notify({ message: 'Failed to fetch users', color: 'red' })
      }
    },
    searchUsers() {
    },
    async toggleUserStatus(user) {
      try {
        if (user.enable) {
          await blockUserById(user.id)
          user.enable = false
        } else {
          await unbanUserById(user.id)
          user.enable = true
        }
        this.$q.notify({ message: 'User status updated successfully', color: 'green' })
      } catch (error) {
        this.$q.notify({ message: 'Failed to update user status', color: 'red' })
      }
    },
    async fetchQueueTasks() {
      try {
        const response = await getAllTask()
        this.queueTasks = response.data
      } catch (error) {
        this.$q.notify({ message: 'Failed to fetch queue tasks', color: 'red' })
      }
    },
    async banTask(taskId) {
      try {
        await deleteTask(taskId)
        this.queueTasks = this.queueTasks.filter(task => task.id !== taskId)
        this.$q.notify({ message: `Task ${taskId} banned successfully`, color: 'green' })
      } catch (error) {
        this.$q.notify({ message: `Failed to ban task ${taskId}`, color: 'red' })
      }
    }
  },
  created() {
    this.fetchUsers()
    this.fetchQueueTasks()
  }
}
</script>

<style scoped>
.grafana-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 20px;
  padding-right: 320px;
}
.grafana-iframe {
  width: 100%;
  height: 200px;
  border: none;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
.user-list {
  position: fixed;
  right: 0;
  top: 80px;
  width: 300px;
  background: white;
  padding: 10px;
  border-left: 1px solid #ddd;
  overflow-y: auto;
  max-height: calc(100vh - 60px);
}
.bg-dark {
  background-color: #121212;
}
</style>
