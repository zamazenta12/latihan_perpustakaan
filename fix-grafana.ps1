# Script untuk reset Grafana password dan volume
# Pilih opsi yang diinginkan

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("reset-volume", "reset-password", "both")]
    [string]$Action = "both"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Grafana Login Fix" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

if ($Action -eq "reset-volume" -or $Action -eq "both") {
    Write-Host "[Option 1] Resetting Grafana volume..." -ForegroundColor Yellow
    Write-Host "  This will delete all Grafana data including dashboards!" -ForegroundColor Red
    Write-Host ""
    
    # Stop monitoring stack
    Write-Host "  Stopping monitoring stack..." -ForegroundColor Yellow
    docker compose -f docker-compose-monitoring.yml down
    
    # Remove Grafana volume
    Write-Host "  Removing Grafana volume..." -ForegroundColor Yellow
    docker volume rm latihan_perpustakaan_grafana_data -ErrorAction SilentlyContinue
    
    # Start monitoring stack
    Write-Host "  Starting monitoring stack..." -ForegroundColor Yellow
    docker compose -f docker-compose-monitoring.yml up -d
    
    Write-Host "  ✓ Grafana volume reset complete" -ForegroundColor Green
    Write-Host "  → Grafana will start with fresh data" -ForegroundColor Green
    Write-Host "  → Login: admin / admin" -ForegroundColor Green
    Write-Host ""
}

if ($Action -eq "reset-password" -or $Action -eq "both") {
    Write-Host "[Option 2] Resetting admin password via CLI..." -ForegroundColor Yellow
    
    # Wait for Grafana to be ready
    Write-Host "  Waiting for Grafana to be ready..." -ForegroundColor Yellow
    Start-Sleep -Seconds 10
    
    # Reset password
    Write-Host "  Resetting password to 'admin'..." -ForegroundColor Yellow
    docker exec -it grafana grafana-cli admin reset-admin-password admin
    
    Write-Host "  ✓ Password reset complete" -ForegroundColor Green
    Write-Host "  → Login: admin / admin" -ForegroundColor Green
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Fix Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Open Grafana: http://localhost:3000" -ForegroundColor White
Write-Host "Login credentials: admin / admin" -ForegroundColor White
Write-Host ""

# Usage examples
Write-Host "Usage examples:" -ForegroundColor Yellow
Write-Host "  .\fix-grafana.ps1 -Action reset-volume" -ForegroundColor Gray
Write-Host "  .\fix-grafana.ps1 -Action reset-password" -ForegroundColor Gray
Write-Host "  .\fix-grafana.ps1 -Action both" -ForegroundColor Gray
Write-Host ""
