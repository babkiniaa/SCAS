import MainLayout from 'src/layouts/MainLayout.vue'
import DashboardPage from 'src/pages/DashboardPage.vue'
import UsersPage from 'src/pages/UsersPage.vue'
import TasksPage from 'src/pages/TasksPage.vue'

const routes = [
  { path: '/login', component: () => import('pages/loginPage.vue') },
  { path: '/register', component: () => import('pages/RegisterPage.vue') },
  { path: '/verify', component: () => import('pages/VerificationPage.vue') },
  { path: '/home', component: () => import('pages/MainPage.vue'), props: true },
  { path: '/create-project', component: () => import('pages/CreateProjectPage.vue'), props: true },
  { path: '/analysis', name: 'analysis', component: () => import('pages/AnalysisPage.vue'), props: true },
  { path: '/edit', component: () => import('pages/EditPage.vue'), props: true },
  { path: '/profile/:id', name: 'profile', component: () => import('pages/ProfPage.vue'), props: true },
  { path: '/projects/:id', name: 'projects', component: () => import('pages/AllProjectPage.vue'), props: true },
  { path: '/reset-password', component: () => import('pages/EmPage.vue') },
  { path: '/reset-password-page', component: () => import('pages/ResetPasswordPage.vue') },
  { path: '/report/:id', name: 'report', component: () => import('pages/ReportPage.vue'), props: true },
  { path: '/project/:id', name: 'project', component: () => import('pages/ProjectPage.vue'), props: true },
  {
    path: '/admin',
    component: MainLayout,
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', component: DashboardPage },
      { path: 'users', component: UsersPage },
      { path: 'tasks', component: TasksPage }
    ]
  }
]

export default routes
