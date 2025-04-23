<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated :class="isDarkMode ? 'bg-dark' : 'bg-primary'">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />

        <q-toolbar-title>
          Admin Dashboard
        </q-toolbar-title>

        <q-space />

        <q-btn
          dense
          round
          :icon="isDarkMode ? 'light_mode' : 'dark_mode'"
          @click="toggleDarkMode"
          aria-label="Toggle Dark Mode"
          class="q-mr-sm"
        />

        <q-btn-dropdown flat round icon="person">
          <q-list>
            <q-item clickable v-close-popup>
              <q-item-section>Profile</q-item-section>
            </q-item>
            <q-item clickable v-close-popup @click="logout">
              <q-item-section>Logout</q-item-section>
            </q-item>
          </q-list>
        </q-btn-dropdown>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      :class="isDarkMode ? 'bg-grey-9' : 'bg-grey-3'"
    >
      <q-list>
        <q-item-label header>
          Navigation
        </q-item-label>

        <q-item
          v-for="link in essentialLinks"
          :key="link.title"
          clickable
          v-ripple
          :active="currentRoute === link.link"
          @click="navigateTo(link.link)"
          active-class="active-link"
        >
          <q-item-section avatar>
            <q-icon :name="link.icon" />
          </q-item-section>

          <q-item-section>
            <q-item-label>{{ link.title }}</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>
  
<script>
import { Dark } from 'quasar'
  
export default {
  name: 'MainLayout',
  data() {
    return {
      leftDrawerOpen: false,
      isDarkMode: Dark.isActive,
      essentialLinks: [
        {
          title: 'Dashboard',
          icon: 'dashboard',
          link: '/admin/dashboard'
        },
        {
          title: 'Users',
          icon: 'people',
          link: '/admin/users'
        },
        {
          title: 'Tasks',
          icon: 'list_alt',
          link: '/admin/tasks'
        }
      ]
    }
  },
  computed: {
    currentRoute() {
      return this.$route.path
    }
  },
  methods: {
    toggleDarkMode() {
      Dark.set(!this.isDarkMode)
      this.isDarkMode = Dark.isActive
    },
    navigateTo(route) {
      this.$router.push(route)
    },
    logout() {
      // Implement logout logic
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="scss" scoped>
.active-link {
  background-color: rgba(0, 0, 0, 0.1);
  color: primary;
  font-weight: bold;
}
</style>