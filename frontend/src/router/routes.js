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
  { path: '/reset-password', component: () => import('pages/EmailPage.vue') },
  { path: '/reset-password-page', component: () => import('pages/ResetPasswordPage.vue') }
]

export default routes
