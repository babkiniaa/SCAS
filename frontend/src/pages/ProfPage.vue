<template>
    <q-layout view="hHh Lpr lff"  class="shadow-2 rounded-borders">
    <q-header elevated class="bg-black full-width">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Profile</q-toolbar-title>
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
            <q-item clickable v-ripple  @click="showCreateProjectModal = true">
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

    <q-page-container :class="isDarkMode ? 'bg-grey-9' : 'bg-grey-1'">
      <q-page class="q-pa-lg">
        <div class="row q-col-gutter-lg">
          <div class="col-md-3 col-sm-12">
            <q-card class="text-center" flat :class="isDarkMode ? 'bg-grey-8' : 'bg-white'">
              <q-card-section>
                <q-avatar size="350px" class="q-mx-auto">
                  <img v-if="user.avatar" :src="user.avatar" alt="User Avatar" />
                  <q-icon v-else name="person" class="text-grey" />
              </q-avatar>

                <div class="text-h5 q-mt-md text-weight-bold">{{ user.username }}</div>
                <div class="text-subtitle1 text-grey">{{ user.email }}</div>

                <q-chip v-if="user.about" outline color="primary" class="q-mt-sm">
                  {{ user.about }}
                </q-chip>

                <q-btn
                  unelevated
                  color="black"
                  class="full-width q-mt-lg"
                  icon="edit"
                  label="Edit Profile"
                  @click="goToEdit"
                />
              </q-card-section>
            </q-card>
          </div>

          <!-- Projects Section -->
          <div class="col-md-9 col-sm-12">
            <q-card flat :class="isDarkMode ? 'bg-grey-8' : 'bg-white'">
              <q-card-section>
                <div class="row items-center">
                  <div class="text-h5 text-weight-bold">My Projects</div>
                </div>

                <q-separator class="q-my-md" />

                <div class="row q-col-gutter-md">
                  <div
                    class="col-md-6 col-sm-12"
                    v-for="project in projects"
                    :key="project.id"
                  >

                  <q-card
              :class="['project-card', isDarkMode ? 'bg-grey-9 text-white' : 'bg-white',
                      {'border-left': true, 'border-primary': !isDarkMode, 'border-accent': isDarkMode}]"
              class="full-height" @click="pageProject(project.id)"
            >
              <q-card-section>
                <div class="text-h6 text-weight-bold ellipsis">{{ project.name }}</div>
                <q-separator class="q-my-sm" />
                <div class="text-caption text-grey ellipsis-2-lines" style="min-height: 40px;">
                  {{ project.description || 'No description provided' }}
                </div>
              </q-card-section>
              </q-card>
                    <!-- <q-card
                      bordered
                      class="cursor-pointer project-card"
                      :class="isDarkMode ? 'bg-grey-9' : 'bg-grey-1'"
                      @click="pageProject(project.id)"
                    >
                      <q-card-section>
                        <div class="row items-center no-wrap">
                          <div class="col">
                            <div class="text-h6 text-weight-bold">{{ project.name }}</div>
                            <div class="text-caption text-grey q-mt-xs">
                              {{ project.description || 'No description provided' }}
                            </div>
                          </div>
                          <div class="col-auto">
                            <q-icon name="chevron_right" color="grey" />
                          </div>
                        </div>
                      </q-card-section>
                    </q-card> -->
                  </div>
                </div>
              </q-card-section>
            </q-card>

            <!-- Activity Section -->
            <q-card class="q-mt-lg" flat :class="isDarkMode ? 'bg-grey-8' : 'bg-white'">
              <q-card-section>
                <div class="text-h5 text-weight-bold q-mb-md">Activity</div>
                <div class="row q-col-gutter-md">
                  <div class="col-md-4 col-sm-12" v-for="(url, index) in [grafanaData.url1, grafanaData.url2, grafanaData.url3]" :key="index">
                    <div class="grafana-chart-container">
                      <iframe
                        :src="url"
                        frameborder="0"
                        class="full-width"
                      ></iframe>
                    </div>
                  </div>
                </div>
              </q-card-section>
            </q-card>
          </div>
        </div>
      </q-page>

      <!-- Logout Button -->
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn
          fab
          color="negative"
          icon="logout"
          @click="logout"
          title="Logout"
        />
      </q-page-sticky>

      <!-- Create Project Modal -->
      <q-dialog v-model="showCreateProjectModal">
        <create-project-form :isDarkMode="isDarkMode" @close="showCreateProjectModal = false" />
      </q-dialog>
    </q-page-container>
  </q-layout>
</template>

<script>
import { getProjects } from 'src/services/projectServices'
import { getUserProfile, getId } from 'src/services/userServices'
import { Dark } from 'quasar'
import CreateProjectForm from 'src/pages/CreateProjectPage.vue'

export default {
  components: {
    CreateProjectForm
  },
  data() {
    return {
      drawer: false,
      miniState: true,
      user: {
        id: '',
        email: '',
        username: '',
        avatar: null,
        about: ''
      },
      currentUserId: null,
      isOwnProfile: false,
      isDarkMode: Dark.isActive,
      showCreateProjectModal: false,
      projectsDto: {
        count: 6,
        page: 0,
        sortingField: 'createdDate',
        userId: null,
        myProject: true,
        name: '',
        sortDirection: 'DESC'
      },
      grafanaData: {
        url1: null,
        url2: null,
        url3: null
      },
      projects: []
    }
  },
  async created() {
    this.currentUserId = (await getId()).data
    const profileId = this.$route.params.id
    await this.loadUserProfile(profileId)
    this.fetchProjects()
    this.fetchGrafanaChart()
  },
  methods: {
    pageProject(id) {
      this.$router.push({ name: 'project', params: { id } })
    },
    async fetchGrafanaChart() {
      const userId = (await getId()).data
      const projectId = '1'
      const branch = 'all'
      const theme = this.isDarkMode ? 'dark' : 'light'
      this.grafanaData.url1 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=5&__feature.dashboardSceneSolo`
      this.grafanaData.url2 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=6&__feature.dashboardSceneSolo`
      this.grafanaData.url3 = `http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=${userId}&var-projectId=${projectId}&var-branch=${branch}&refresh=5s&theme=${theme}&panelId=7&__feature.dashboardSceneSolo`
    },
    async fetchProjects() {
      try {
        this.projectsDto.userId = (await getId()).data
        const response = await getProjects(this.projectsDto)
        this.projects = response.data
      } catch (error) {
        this.$q.notify({
          message: 'Error loading projects',
          color: 'negative',
          icon: 'error'
        })
      }
    },
    toggleDarkMode() {
      Dark.toggle()
      this.isDarkMode = Dark.isActive
      this.fetchGrafanaChart()
    },
    async loadUserProfile(profileId) {
      try {
        const response = await getUserProfile(profileId)
        this.user = response.data
        this.isOwnProfile = this.currentUserId == this.user.id
      } catch (error) {
        this.$q.notify({
          message: 'Error loading profile',
          color: 'negative',
          icon: 'error'
        })
      }
    },
    goToHome() {
      const route = localStorage.getItem('role') === 'admin' ? '/admin' : '/home'
      this.$router.push(route)
    },
    goToAllProjects() {
      this.$router.push({ name: 'projects', params: { id: this.user.id } })
    },
    goToProfile() {
      this.$router.push(`/profile/${this.currentUserId}`)
    },
    goToEdit() {
      this.$router.push('/edit')
    },
    logout() {
      this.$q.dialog({
        title: 'Confirm Logout',
        message: 'Are you sure you want to logout?',
        cancel: true,
        persistent: true
      }).onOk(() => {
        localStorage.removeItem('jwtToken')
        this.$router.push('/login')
      })
    }
  }
}
</script>

<style scoped>
.project-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  height: 100%;
}

.project-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.grafana-chart-container {
  position: relative;
  padding-bottom: 75%; /* Aspect ratio */
  height: 0;
  overflow: hidden;
  border-radius: 8px;
  background: rgba(0,0,0,0.05);
}

.grafana-chart-container iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.text-subtitle1 {
  font-size: 1rem;
}

.q-chip {
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.q-card {
  border-radius: 12px;
}

.q-page-container {
  transition: background-color 0.3s ease;
}

.project-card {
  transition: transform 0.2s, box-shadow 0.2s;
  border-radius: 8px;
}

.project-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.border-left {
  border-left: 4px solid;
  border-radius: 30px;
}

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.ellipsis-2-lines {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
