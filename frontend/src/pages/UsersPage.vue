<template>
  <q-page :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
    <div class="q-pa-md">
      <q-card :class="isDarkMode ? 'bg-grey-9' : 'bg-white'">
        <q-card-section>
          <div class="row items-center justify-between">
            <div class="text-h6">User Management</div>
            <q-btn 
              color="primary" 
              icon="add" 
              label="Add User" 
              @click="showAddUserDialog = true"
            />
          </div>
        </q-card-section>

        <q-separator />

        <q-card-section>
          <div class="row q-col-gutter-md q-mb-md">
            <div class="col-md-6 col-sm-12">
              <q-input
                v-model="searchTerm"
                placeholder="Search users..."
                outlined
                dense
                clearable
                @update:model-value="searchUsers"
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
                @update:model-value="filterUsers"
              />
            </div>
            <div class="col-md-3 col-sm-6">
              <q-select
                v-model="sortBy"
                :options="sortOptions"
                outlined
                dense
                label="Sort by"
                @update:model-value="sortUsers"
              />
            </div>
          </div>

          <q-table
            :rows="filteredUsers"
            :columns="columns"
            row-key="id"
            :loading="loading"
            :filter="searchTerm"
            :pagination="pagination"
            :class="isDarkMode ? 'bg-grey-9' : 'bg-white'"
            flat
            bordered
          >
            <template v-slot:body-cell-avatar="props">
              <q-td :props="props">
                <q-avatar size="40px">
                  <img v-if="props.row.avatarUrl" :src="props.row.avatarUrl" />
                  <q-icon v-else name="person" size="24px" />
                </q-avatar>
              </q-td>
            </template>

            <template v-slot:body-cell-status="props">
              <q-td :props="props">
                <q-badge 
                  :color="props.row.enable ? 'positive' : 'negative'"
                  :label="props.row.enable ? 'Active' : 'Banned'"
                />
              </q-td>
            </template>

            <template v-slot:body-cell-actions="props">
              <q-td :props="props">
                <q-btn
                  dense
                  round
                  flat
                  :icon="props.row.enable ? 'block' : 'check_circle'"
                  :color="props.row.enable ? 'negative' : 'positive'"
                  @click="toggleUserStatus(props.row)"
                  class="q-mr-xs"
                >
                  <q-tooltip>
                    {{ props.row.enable ? 'Ban user' : 'Unban user' }}
                  </q-tooltip>
                </q-btn>
              </q-td>
            </template>
          </q-table>
        </q-card-section>
      </q-card>
    </div>

    <!-- Add User Dialog -->
    <q-dialog v-model="showAddUserDialog">
      <q-card style="min-width: 400px">
        <q-card-section>
          <div class="text-h6">Add New User</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-form @submit="addUser">
            <q-input
              v-model="newUser.username"
              label="Username"
              outlined
              dense
              class="q-mb-sm"
              :rules="[val => !!val || 'Username is required']"
            />
            <q-input
              v-model="newUser.email"
              label="Email"
              outlined
              dense
              class="q-mb-sm"
              :rules="[
                val => !!val || 'Email is required',
                val => /.+@.+\..+/.test(val) || 'Email must be valid'
              ]"
            />
            <q-input
              v-model="newUser.password"
              label="Password"
              type="password"
              outlined
              dense
              class="q-mb-sm"
              :rules="[val => val.length >= 6 || 'Password must be at least 6 characters']"
            />
            <q-toggle
              v-model="newUser.enable"
              label="Active account"
              left-label
            />
          </q-form>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancel" color="negative" v-close-popup />
          <q-btn flat label="Add User" color="primary" @click="addUser" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import { Dark } from 'quasar'
import { fetchUsers, blockUserById, unbanUserById } from 'src/services/AdminService'

export default {
  name: 'UsersPage',
  data() {
    return {
      isDarkMode: Dark.isActive,
      users: [],
      loading: false,
      searchTerm: '',
      statusFilter: 'all',
      sortBy: 'name',
      pagination: {
        rowsPerPage: 10
      },
      showAddUserDialog: false,
      newUser: {
        username: '',
        email: '',
        password: '',
        enable: true
      },
      statusOptions: [
        { label: 'All', value: 'all' },
        { label: 'Active', value: 'active' },
        { label: 'Banned', value: 'banned' }
      ],
      sortOptions: [
        { label: 'Name (A-Z)', value: 'name' },
        { label: 'Name (Z-A)', value: 'name-desc' },
        { label: 'Recently added', value: 'recent' },
        { label: 'Oldest', value: 'oldest' }
      ],
      columns: [
        {
          name: 'avatar',
          label: '',
          field: 'avatarUrl',
          align: 'center'
        },
        {
          name: 'username',
          label: 'Username',
          field: 'username',
          align: 'left',
          sortable: true
        },
        {
          name: 'email',
          label: 'Email',
          field: 'email',
          align: 'left',
          sortable: true
        },
        {
          name: 'status',
          label: 'Status',
          field: 'enable',
          align: 'center',
          sortable: true
        },
        {
          name: 'createdAt',
          label: 'Created',
          field: 'createdAt',
          align: 'left',
          format: val => new Date(val).toLocaleDateString(),
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
    filteredUsers() {
      let users = [...this.users]
      
      // Filter by status
      if (this.statusFilter === 'active') {
        users = users.filter(user => user.enable)
      } else if (this.statusFilter === 'banned') {
        users = users.filter(user => !user.enable)
      }
      
      // Search filter
      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase()
        users = users.filter(user => 
          user.username.toLowerCase().includes(term) || 
          user.email.toLowerCase().includes(term))
      }
      
      // Sorting
      if (this.sortBy === 'name') {
        users.sort((a, b) => a.username.localeCompare(b.username))
      } else if (this.sortBy === 'name-desc') {
        users.sort((a, b) => b.username.localeCompare(a.username))
      } else if (this.sortBy === 'recent') {
        users.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      } else if (this.sortBy === 'oldest') {
        users.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
      }
      
      return users
    }
  },
  methods: {
    async fetchUsers() {
      this.loading = true
      try {
        const response = await fetchUsers()
        this.users = response.data
      } catch (error) {
        this.$q.notify({
          message: 'Failed to fetch users',
          color: 'negative',
          icon: 'error'
        })
      } finally {
        this.loading = false
      }
    },
    searchUsers() {
      // Handled in computed property
    },
    filterUsers() {
      // Handled in computed property
    },
    sortUsers() {
      // Handled in computed property
    },
    async toggleUserStatus(user) {
      try {
        if (user.enable) {
          await blockUserById(user.id)
          user.enable = false
          this.$q.notify({
            message: `User ${user.username} has been banned`,
            color: 'positive',
            icon: 'block'
          })
        } else {
          await unbanUserById(user.id)
          user.enable = true
          this.$q.notify({
            message: `User ${user.username} has been unbanned`,
            color: 'positive',
            icon: 'check_circle'
          })
        }
      } catch (error) {
        this.$q.notify({
          message: 'Failed to update user status',
          color: 'negative',
          icon: 'error'
        })
      }
    },
    editUser(user) {
      // Implement edit user functionality
      console.log('Edit user:', user)
      this.$q.notify({
        message: 'Edit user functionality coming soon',
        color: 'info'
      })
    },
    confirmDeleteUser(user) {
      this.$q.dialog({
        title: 'Confirm Delete',
        message: `Are you sure you want to delete ${user.username}?`,
        cancel: true,
        persistent: true
      }).onOk(() => {
        this.deleteUser(user)
      })
    },
    deleteUser(user) {
      // Implement delete user functionality
      console.log('Delete user:', user)
      this.$q.notify({
        message: 'Delete user functionality coming soon',
        color: 'info'
      })
    },
    addUser() {
      // Implement add user functionality
      console.log('Add user:', this.newUser)
      this.$q.notify({
        message: 'Add user functionality coming soon',
        color: 'info'
      })
      this.showAddUserDialog = false
      this.resetNewUser()
    },
    resetNewUser() {
      this.newUser = {
        username: '',
        email: '',
        password: '',
        enable: true
      }
    }
  },
  created() {
    this.fetchUsers()
  }
}
</script>

<style lang="scss" scoped>
.user-avatar {
  transition: transform 0.3s ease;
  
  &:hover {
    transform: scale(1.1);
  }
}
</style>