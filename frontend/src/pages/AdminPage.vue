<template>
  <q-layout view="lHh lpr lFf">
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
        <q-card
          :class="['q-pa-md', 'shadow-2', 'my-card', isDarkMode ? 'bg-grey-8' : '']"
          bordered
          clickable
          @click="showQueueTasks"
        >
          <q-card-section>
            <div class="row q-col-gutter-md items-center">
              <div class="col-6">
                <h6>Projects in Queue for Analysis</h6>
              </div>
              <div class="col-6 text-right">
                <span class="text-h3 text-primary">
                  {{ queueCount }}
                </span>
              </div>
            </div>
          </q-card-section>
        </q-card>

        <q-card
          :class="['q-pa-md shadow-2 my-card', isDarkMode ? 'bg-grey-8' : '']"
          bordered
          clickable
          @click="showUserList"
        >
          <q-card-section>
            <div class="row q-col-gutter-md items-center">
              <div class="col-6">
                <h6>Users in System</h6>
              </div>
              <div class="col-6 text-right">
                <span class="text-h3 text-primary">
                  {{ userCount }}
                </span>
              </div>
            </div>
          </q-card-section>
        </q-card>

        <q-card
          v-if="showQueue && queueTasks.length > 0"
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
                <q-item-section side>
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
        <q-card
          v-if="showUsers && users.length > 0"
          :class="['q-pa-md shadow-2 my-card', isDarkMode ? 'bg-grey-8' : '']"
          bordered
          class="q-mt-md"
        >
          <q-card-section>
            <div class="row q-col-gutter-md items-center">
              <div class="col-12">
                <h6>User List</h6>
              </div>
            </div>
            <q-list class="q-mt-sm">
              <q-item
                v-for="user in users"
                :key="user.id"
                class="q-pb-sm"
                clickable
                @click="navigateToProfile(user.id)"
              >
                <q-item-section avatar>
                  <q-avatar class="q-ml-md">
                    <img v-if="user.avatar" :src="user.avatar" alt="User Avatar" />
                    <q-icon v-else name="person" class="text-white" />
                  </q-avatar>
                </q-item-section>
                <q-item-section>{{ user.username }}</q-item-section>
                <q-item-section side>
                  <q-btn
                    label="Block"
                    color="red"
                    icon="block"
                    @click.stop="blockUser(user.id)"
                  />
                </q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
import { Dark } from 'quasar'
import { getCount, getAllTask, deleteTask, fetchUsers, blockUserById } from 'src/services/AdminService'

export default {
  data () {
    return {
      isDarkMode: Dark.isActive,
      queueCount: 0,
      queueTasks: [],
      showQueue: false,
      userCount: 0,
      users: [],
      showUsers: false
    }
  },
  methods: {
    toggleDarkMode () {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    async fetchUserList () {
      try {
        const response = await fetchUsers()
        this.users = response.data
        this.userCount = response.data.length
      } catch (error) {
        this.$q.notify({ message: 'Failed to fetch users', color: 'red' })
      }
    },
    showUserList () {
      this.showUsers = !this.showUsers
    },
    async blockUser (userId) {
      try {
        await blockUserById(userId)
        this.$q.notify({
          message: `User with ID ${userId} blocked successfully`,
          color: 'green'
        })
        this.users = this.users.filter((user) => user.id !== userId)
        this.userCount -= 1
      } catch (error) {
        this.$q.notify({ message: `Failed to block user ${userId}`, color: 'red' })
      }
    },
    async fetchQueueCount () {
      try {
        const response = await getCount()
        this.queueCount = response.data
      } catch (error) {
        this.$q.notify({ message: 'Failed to fetch queue count', color: 'red' })
      }
    },
    async showQueueTasks () {
      if (this.queueCount > 0) {
        try {
          if (this.showQueue) {
            const response = await getAllTask()
            this.queueTasks = response.data
          }
          this.showQueue = !this.showQueue
        } catch (error) {
          this.$q.notify({ message: 'Failed to fetch queue tasks', color: 'red' })
        }
      }
    },
    async banTask (taskId) {
      try {
        await deleteTask(taskId)
        this.$q.notify({
          message: `Task ${taskId} banned successfully`,
          color: 'green'
        })
        this.queueTasks = this.queueTasks.filter((task) => task.id !== taskId)
      } catch (error) {
        this.$q.notify({ message: `Failed to ban task ${taskId}`, color: 'red' })
      }
    },
    navigateToProfile (userId) {
      this.$router.push(`/profile/${userId}`)
    }
  },
  created () {
    this.fetchUserList()
    this.fetchQueueCount()
  }
}
</script>

<style scoped>
.my-card {
  max-width: 800px;
  margin: 20px auto;
}
.bg-dark {
  background-color: #121212;
}
.text-white {
  color: #ffffff;
}
</style>
