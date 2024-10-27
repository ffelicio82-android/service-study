#!/bin/bash

# CONFIGURAÇÕES
APP_NAME="ServiceStudy"
APK_PATH="./app/build/intermediates/apk/debug/app-debug.apk"
SYSTEM_PATH="/system/app/${APP_NAME}"

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para log
log() {
    echo -e "${GREEN}[$(date '+%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[ERROR] $1${NC}"
    exit 1
}

warning() {
    echo -e "${YELLOW}[WARNING] $1${NC}"
}

# Verifica se está rodando como root
check_root() {
    if [ "$(id -u)" != "0" ]; then
        error "Este script precisa ser executado como root"
    fi
}

# Verifica se o APK existe
check_apk() {
    if [ ! -f "$APK_PATH" ]; then
        error "APK não encontrado em: $APK_PATH"
    fi
}

# Função principal
main() {
    check_root
    check_apk

    log "Iniciando instalação do app como system app..."

    # Obtém acesso root via adb
    log "Obtendo acesso root..."
    adb root || error "Falha ao obter acesso root"
    sleep 2

    # Desativa verificação dm-verity
    log "Desativando dm-verity..."
    adb disable-verity || warning "Falha ao desativar dm-verity"
    adb reboot || error "Falha ao reiniciar dispositivo"

    log "Aguardando dispositivo reiniciar..."
    adb wait-for-device
    sleep 10

    # Obtém root novamente
    adb root || error "Falha ao obter acesso root após reboot"
    sleep 2

    # Remonta system como gravável
    log "Remontando partição system..."
    adb remount || error "Falha ao remontar partição system"

    # Cria diretório
    log "Criando diretório em $SYSTEM_PATH..."
    adb shell "mkdir -p $SYSTEM_PATH" || error "Falha ao criar diretório"

    # Copia APK
    log "Copiando APK para $SYSTEM_PATH..."
    adb push "$APK_PATH" "$SYSTEM_PATH/${APP_NAME}.apk" || error "Falha ao copiar APK"

    # Define permissões
    log "Configurando permissões..."
    adb shell "chmod 644 $SYSTEM_PATH/${APP_NAME}.apk"
    adb shell "chown root:root $SYSTEM_PATH/${APP_NAME}.apk"

    log "Reiniciando dispositivo..."
    adb reboot

    log "Instalação concluída com sucesso!"
}

# Executa script
main