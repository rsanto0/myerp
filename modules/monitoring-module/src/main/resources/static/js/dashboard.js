// Dashboard JavaScript
let servicesChart, memoryChart;
let refreshInterval;

// Initialize dashboard
document.addEventListener('DOMContentLoaded', function() {
    initializeCharts();
    updateCurrentTime();
    refreshData();
    
    // Auto refresh every 30 seconds
    refreshInterval = setInterval(refreshData, 30000);
    
    // Update time every second
    setInterval(updateCurrentTime, 1000);
});

// Update current time
function updateCurrentTime() {
    const now = new Date();
    const timeString = now.toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
    document.getElementById('current-time').textContent = timeString;
}

// Initialize charts
function initializeCharts() {
    // Services Status Chart
    const servicesCtx = document.getElementById('servicesChart').getContext('2d');
    servicesChart = new Chart(servicesCtx, {
        type: 'doughnut',
        data: {
            labels: ['Online', 'Offline'],
            datasets: [{
                data: [0, 0],
                backgroundColor: ['#27ae60', '#e74c3c'],
                borderWidth: 0,
                cutout: '70%'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        font: {
                            size: 14,
                            weight: 'bold'
                        }
                    }
                }
            }
        }
    });

    // Memory Usage Chart
    const memoryCtx = document.getElementById('memoryChart').getContext('2d');
    memoryChart = new Chart(memoryCtx, {
        type: 'bar',
        data: {
            labels: ['Heap Usado', 'Heap Máximo', 'Non-Heap'],
            datasets: [{
                label: 'MB',
                data: [0, 0, 0],
                backgroundColor: [
                    'rgba(52, 152, 219, 0.8)',
                    'rgba(46, 204, 113, 0.8)',
                    'rgba(241, 196, 15, 0.8)'
                ],
                borderColor: [
                    '#3498db',
                    '#2ecc71',
                    '#f1c40f'
                ],
                borderWidth: 2,
                borderRadius: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0,0,0,0.1)'
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
}

// Refresh all data
async function refreshData() {
    try {
        showLoading();
        
        const response = await fetch('/api/monitoring/dashboard');
        const result = await response.json();
        
        if (result.success) {
            updateSummaryCards(result.data);
            updateCharts(result.data);
            updateServicesList(result.data.services);
            updateMetrics(result.data.metrics);
            updateSystemStatus(result.data.summary);
        }
        
        hideLoading();
        
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
        showError('Erro ao carregar dados do dashboard');
        hideLoading();
    }
}

// Update summary cards
function updateSummaryCards(data) {
    const summary = data.summary;
    
    document.getElementById('services-up').textContent = summary.upServices;
    document.getElementById('services-down').textContent = summary.downServices;
    
    // Memory usage percentage
    const memoryUsed = data.metrics.jvm.heapUsed / (1024 * 1024);
    const memoryMax = data.metrics.jvm.heapMax / (1024 * 1024);
    const memoryPercent = Math.round((memoryUsed / memoryMax) * 100);
    document.getElementById('memory-usage').textContent = memoryPercent + '%';
    
    // Uptime
    const uptimeSeconds = Math.floor(data.metrics.jvm.uptime / 1000);
    document.getElementById('uptime').textContent = formatUptime(uptimeSeconds);
}

// Update charts
function updateCharts(data) {
    const summary = data.summary;
    
    // Update services chart
    servicesChart.data.datasets[0].data = [summary.upServices, summary.downServices];
    servicesChart.update('none');
    
    // Update memory chart
    const jvm = data.metrics.jvm;
    memoryChart.data.datasets[0].data = [
        Math.round(jvm.heapUsed / (1024 * 1024)),
        Math.round(jvm.heapMax / (1024 * 1024)),
        Math.round(jvm.nonHeapUsed / (1024 * 1024))
    ];
    memoryChart.update('none');
}

// Update services list
function updateServicesList(services) {
    const servicesList = document.getElementById('services-list');
    servicesList.innerHTML = '';
    
    const serviceNames = {
        'eureka': 'Eureka Server',
        'api-gateway': 'API Gateway',
        'auth-service': 'Auth Service',
        'rh-module': 'RH Module',
        'biometria-module': 'Biometria Module'
    };
    
    Object.entries(services).forEach(([key, service]) => {
        const serviceItem = document.createElement('div');
        serviceItem.className = `service-item ${service.status.toLowerCase()} fade-in`;
        
        serviceItem.innerHTML = `
            <div class="service-info">
                <div class="service-name">${serviceNames[key] || key}</div>
                <div class="service-url">${service.url}</div>
            </div>
            <div class="service-status ${service.status.toLowerCase()}">
                <i class="fas fa-${service.status === 'UP' ? 'check-circle' : 'times-circle'}"></i>
                ${service.status}
            </div>
        `;
        
        servicesList.appendChild(serviceItem);
    });
}

// Update metrics
function updateMetrics(metrics) {
    const metricsGrid = document.getElementById('metrics-grid');
    metricsGrid.innerHTML = '';
    
    const metricsData = [
        { label: 'Processadores', value: metrics.system.processors, unit: '' },
        { label: 'Memória Livre', value: Math.round(metrics.system.freeMemory / (1024 * 1024)), unit: 'MB' },
        { label: 'Memória Total', value: Math.round(metrics.system.totalMemory / (1024 * 1024)), unit: 'MB' },
        { label: 'Memória Máxima', value: Math.round(metrics.system.maxMemory / (1024 * 1024)), unit: 'MB' }
    ];
    
    metricsData.forEach(metric => {
        const metricItem = document.createElement('div');
        metricItem.className = 'metric-item fade-in';
        
        metricItem.innerHTML = `
            <div class="metric-value">${metric.value}${metric.unit}</div>
            <div class="metric-label">${metric.label}</div>
        `;
        
        metricsGrid.appendChild(metricItem);
    });
}

// Update system status indicator
function updateSystemStatus(summary) {
    const statusIndicator = document.getElementById('system-status');
    const healthPercentage = summary.healthPercentage;
    
    if (healthPercentage >= 80) {
        statusIndicator.className = 'status-indicator online';
        statusIndicator.innerHTML = '<i class="fas fa-circle"></i> Sistema Online';
    } else {
        statusIndicator.className = 'status-indicator offline';
        statusIndicator.innerHTML = '<i class="fas fa-circle"></i> Sistema com Problemas';
    }
}

// Utility functions
function formatUptime(seconds) {
    const days = Math.floor(seconds / 86400);
    const hours = Math.floor((seconds % 86400) / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    
    if (days > 0) return `${days}d ${hours}h`;
    if (hours > 0) return `${hours}h ${minutes}m`;
    return `${minutes}m`;
}

function showLoading() {
    const refreshBtn = document.querySelector('.refresh-btn');
    refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Carregando...';
    refreshBtn.disabled = true;
}

function hideLoading() {
    const refreshBtn = document.querySelector('.refresh-btn');
    refreshBtn.innerHTML = '<i class="fas fa-sync-alt"></i> Atualizar';
    refreshBtn.disabled = false;
}

function showError(message) {
    // Simple error notification
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #e74c3c;
        color: white;
        padding: 15px 20px;
        border-radius: 8px;
        z-index: 1000;
        font-weight: bold;
    `;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        document.body.removeChild(notification);
    }, 5000);
}