<template>
  <q-page :class="isDarkMode ? 'bg-dark' : 'bg-grey-2'">
    <div class="q-pa-md">
      <div class="row q-col-gutter-md">
        <div class="col-12">
          <q-card :class="isDarkMode ? 'bg-grey-9' : 'bg-white'">
            <q-card-section>
              <div class="text-h6">System Overview</div>
            </q-card-section>
            <q-separator />
            <q-card-section>
              <div class="grafana-container">
                <iframe 
                  src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=8&__feature.dashboardSceneSolo" 
                  class="grafana-iframe"
                ></iframe>
                <iframe 
                  src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=9&__feature.dashboardSceneSolo" 
                  class="grafana-iframe"
                ></iframe>
                <iframe 
                  src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=10&__feature.dashboardSceneSolo" 
                  class="grafana-iframe"
                ></iframe>
                <iframe 
                  src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=11&__feature.dashboardSceneSolo" 
                  class="grafana-iframe"
                ></iframe>
                <iframe 
                  src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=12&__feature.dashboardSceneSolo" 
                  class="grafana-iframe"
                ></iframe>
                <iframe 
                  src="http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&timezone=browser&var-userId=1&var-projectId=1&var-branch=all&theme=light&panelId=13&__feature.dashboardSceneSolo" 
                  class="grafana-iframe"
                ></iframe>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import { Dark } from 'quasar'

export default {
  name: 'DashboardPage',
  data() {
    return {
      isDarkMode: Dark.isActive
    }
  }
}
</script>

<style lang="scss" scoped>
.grafana-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.grafana-iframe {
  width: 100%;
  height: 300px;
  border: none;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>