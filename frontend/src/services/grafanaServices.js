export async function fetchGrafanaData ({ projectId, branch }) {
  try {
    // await axios.get('http:///localhost:3000/api/dashboards/data', {
    //   headers: {
    //     Authorization: 'sa-1-admin1-21aa6013-3097-4cfe-8672-f4ae7eac65f8'
    //   }
    // })
    // Преобразуем и возвращаем данные в удобной для клиента форме
    return {
      graphUrl: generateGraphUrl(projectId, branch) // URL для iframe
    }
  } catch (error) {
    console.error('Error fetching Grafana data:', error)
    throw error
  }
}

function generateGraphUrl (projectId, branch) {
  return 'http://localhost:3000/d-solo/ee5t4ycbipwqoa/new-dashboard?orgId=1&from=1733212440189&to=1733234040189&timezone=browser&panelId=1&__feature.dashboardSceneSolo'
}
