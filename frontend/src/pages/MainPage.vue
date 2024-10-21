<template>
  <q-layout view="lHh lpr lFf">
    <q-header elevated class="bg-grey-9">
      <q-toolbar>
        <q-btn flat round dense icon="menu" @click="drawer = !drawer" />
        <q-toolbar-title class="text-white">Homepage</q-toolbar-title>
        <q-space />
        <q-btn dense round icon="search" @click="search" aria-label="Search" class="text-white" />
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
      <q-scroll-area class="fit" :horizontal-thumb-style="{ opacity: 0 }">
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
      </q-scroll-area>
    </q-drawer>
    <q-page-container>
      <q-page class="bg-grey-2">
        <q-card class="q-pa-md shadow-2 my-card" bordered style="max-width: 600px; margin: 16px auto;">
          <q-card-section class="text-center">
            <div class="text-grey-9 text-h5 text-weight-bold">Your Projects</div>
            <div class="text-grey-8">
              <span v-if="projects.length">Here are your current projects:</span>
              <span v-else>You have no projects yet</span>
            </div>
          </q-card-section>
          <q-card-section v-if="projects.length">
            <q-list bordered>
              <q-item v-for="project in projects" :key="project.id" clickable>
                <q-item-section>{{ project.name }}</q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
          <q-card-section v-else>
            <q-btn label="Create Project" color="primary" @click="goToCreateProject" class="q-mt-md full-width" />
          </q-card-section>
        </q-card>
      </q-page>
    </q-page-container>
  </q-layout>
</template>
<script>
export default {
  data () {
    return {
      userId: null,
      drawer: true,
      miniState: true,
      user: {
        avatar: null
      },
      projects: []
    }
  },
  mounted () {
    this.fetchUser()
    this.fetchProjects()
  },
  methods: {
    fetchUser () {
      this.user = {
        avatar: null
      }
    },
    fetchProjects () {
      this.projects = []
    },
    goToHome () {
      this.$router.push('/home')
    },
    goToCreateProject () {
      this.$router.push('/create-project')
    },
    goToAllProjects () {
      this.$router.push('/view-five-last-projects')
    },
    search () {
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
  margin: 0 auto;
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
