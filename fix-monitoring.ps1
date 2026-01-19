# Script untuk rebuild services yang bermasalah dan restart monitoring stack
# Run this script untuk memperbaiki masalah Prometheus targets DOWN

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Fixing Prometheus Monitoring Issues" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Stop all application containers
Write-Host "[1/6] Stopping application containers..." -ForegroundColor Yellow
docker compose -f docker-compose-app.yml down
Write-Host "[OK] Application containers stopped" -ForegroundColor Green
Write-Host ""

# Step 2: Rebuild anggota-service
Write-Host "[2/6] Rebuilding anggota-service..." -ForegroundColor Yellow
Set-Location anggota
docker build -t anggota-service:latest .
if ($LASTEXITCODE -ne 0) {
    Write-Host "[FAILED] Failed to build anggota-service" -ForegroundColor Red
    Set-Location ..
    exit 1
}
Set-Location ..
Write-Host "[OK] anggota-service rebuilt successfully" -ForegroundColor Green
Write-Host ""

# Step 3: Rebuild api-gateway
Write-Host "[3/6] Rebuilding api-gateway..." -ForegroundColor Yellow
Set-Location api_gateway
docker build -t api-gateway:latest .
if ($LASTEXITCODE -ne 0) {
    Write-Host "[FAILED] Failed to build api-gateway" -ForegroundColor Red
    Set-Location ..
    exit 1
}
Set-Location ..
Write-Host "[OK] api-gateway rebuilt successfully" -ForegroundColor Green
Write-Host ""

# Step 4: Restart application stack
Write-Host "[4/6] Starting application containers..." -ForegroundColor Yellow
docker compose -f docker-compose-app.yml up -d
Write-Host "[OK] Application containers started" -ForegroundColor Green
Write-Host ""

# Step 5: Wait for services to start
Write-Host "[5/6] Waiting for services to initialize (30 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30
Write-Host "[OK] Services should be ready" -ForegroundColor Green
Write-Host ""

# Step 6: Verify endpoints
Write-Host "[6/6] Verifying Prometheus endpoints..." -ForegroundColor Yellow
Write-Host ""

$endpoints = @(
    @{Name="Anggota Service"; Url="http://localhost:8081/actuator/prometheus"},
    @{Name="API Gateway"; Url="http://localhost:9000/actuator/prometheus"}
)

foreach ($endpoint in $endpoints) {
    try {
        $response = Invoke-WebRequest -Uri $endpoint.Url -UseBasicParsing -TimeoutSec 5
        if ($response.StatusCode -eq 200) {
            Write-Host "  [OK] $($endpoint.Name): OK" -ForegroundColor Green
        }
    } catch {
        Write-Host "  [FAILED] $($endpoint.Name): FAILED" -ForegroundColor Red
        Write-Host "    Error: $_" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Verification Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Open Prometheus: http://localhost:9090/targets" -ForegroundColor White
Write-Host "2. Verify all services show UP (green)" -ForegroundColor White
Write-Host "3. Open Grafana: http://localhost:3000" -ForegroundColor White
Write-Host "   Login: admin / admin" -ForegroundColor White
Write-Host ""
