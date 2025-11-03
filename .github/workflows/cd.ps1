# =========================
# Script deploy Windows
# Pull image mới, down + up container, load .env, retry
# =========================

# ---- Cấu hình ----
$composePath = "C:\Users\DELL\SpringToolSuite-Project\SpringBoot\interview-system\docker-compose.yml"
$envFile = "C:\Users\DELL\SpringToolSuite-Project\SpringBoot\interview-system\.env"

# ---- Lấy Docker Hub credentials từ environment variables ----
$dockerUsername = $env:DOCKERHUB_USERNAME
$dockerToken    = $env:DOCKERHUB_TOKEN

if (-not $dockerUsername -or -not $dockerToken) {
    throw "Docker Hub username or token not set in environment variables."
}

# ---- Login Docker Hub ----
Write-Host "Logging in to Docker Hub..."
echo $dockerToken | docker login -u $dockerUsername --password-stdin
if ($LASTEXITCODE -ne 0) { throw "Docker login failed" }

# ---- Điều hướng đến folder chứa docker-compose.yml ----
Set-Location (Split-Path $composePath)

# ---- Tắt container cũ ----
Write-Host "Stopping existing containers..."
docker compose -f $composePath down
Start-Sleep -Seconds 5  # chờ chắc chắn

# ---- Pull image mới với retry ----
$maxRetries = 3
for ($i=1; $i -le $maxRetries; $i++) {
    Write-Host "Pulling images (attempt $i)..."
    docker compose --env-file $envFile -f $composePath pull
    if ($LASTEXITCODE -eq 0) { break }
    Write-Host "Pull failed, retrying in 10s..."
    Start-Sleep -Seconds 10
}

# ---- Khởi động lại container ----
Write-Host "Starting containers..."
docker compose --env-file $envFile -f $composePath up -d --remove-orphans

Write-Host "✅ Deployment completed!"
