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
      { path: 'profille', component: () => import('pages/ProfillePage.vue') }  
    ]
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
