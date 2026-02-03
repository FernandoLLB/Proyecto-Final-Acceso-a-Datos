# Script para cargar variables de entorno desde archivo .env
# Uso: .\load-env.ps1

$envFile = ".env"

if (-Not (Test-Path $envFile)) {
    Write-Host "ERROR: Archivo .env no encontrado." -ForegroundColor Red
    Write-Host "Crea un archivo .env copiando desde .env.example:" -ForegroundColor Yellow
    Write-Host "  copy .env.example .env" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Luego edita .env con tus credenciales reales." -ForegroundColor Yellow
    exit 1
}

Write-Host "Cargando variables de entorno desde $envFile..." -ForegroundColor Green

Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()

    # Ignorar líneas vacías y comentarios
    if ($line -eq "" -or $line.StartsWith("#")) {
        return
    }

    # Parsear línea en formato KEY=VALUE
    if ($line -match '^([^=]+)=(.*)$') {
        $key = $matches[1].Trim()
        $value = $matches[2].Trim()

        # Remover comillas si existen
        $value = $value -replace '^["'']|["'']$', ''

        # Establecer variable de entorno en el proceso actual
        [Environment]::SetEnvironmentVariable($key, $value, 'Process')

        Write-Host "  ✓ $key" -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host "Variables de entorno cargadas exitosamente!" -ForegroundColor Green
Write-Host "Ahora puedes ejecutar la aplicación con:" -ForegroundColor Yellow
Write-Host "  mvn spring-boot:run" -ForegroundColor Cyan
Write-Host ""
