const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/IndexPage.vue') },
      { path: '/login', component: () => import('pages/loginPage.vue') },
      { path: '/register', component: () => import('pages/RegisterPage.vue') },
      { path: '/verify', component: () => import('pages/VerificationPage.vue') },
      { path: '/home', component: () => import('pages/MainPage.vue'), props: true },
      { path: '/create-project', component: () => import('pages/CreateProjectPage.vue'), props: true },
      { path: '/analysis', name: 'analysis', component: () => import('pages/AnalysisPage.vue'), props: true },
      { path: '/edit', component: () => import('pages/EditPage.vue'), props: true },
      { path: '/profile/:id', name: 'profile', component: () => import('pages/ProfPage.vue'), props: true },
      { path: '/projects/:id', name: 'projects', component: () => import('pages/AllProjectPage.vue'), props: true }
    ]
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
