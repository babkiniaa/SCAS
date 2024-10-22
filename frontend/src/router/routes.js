const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/IndexPage.vue') },
      { path: '/login', component: () => import('pages/loginPage.vue') },
      { path: '/register', component: () => import('pages/RegisterPage.vue') },
      { path: '/verify', component: () => import('pages/VerificationPage.vue') },
      { path: '/home', component: () => import('pages/MainPage.vue') },
      { path: '/create-project', component: () => import('pages/CreateProjectPage.vue') },
      { path: '/analysis', name: 'analysis', component: () => import('pages/AnalysisPage.vue'), props: true },
      { path: '/edit', component: () => import('pages/EditPage.vue') },
      { path: '/profile/:id', name: 'profile', component: () => import('pages/ProfPage.vue'), props: true },
      { path: '/projects', component: () => import('pages/AllProjectPage.vue') }
    ]
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
