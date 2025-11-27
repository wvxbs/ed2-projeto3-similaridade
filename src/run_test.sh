#!/bin/bash

# ==========================================
# Script de Execução e Teste Automatizado
# ==========================================

# Diretórios
ROOT="/home/wvxbs/Documentos/repos/ed2-projeto3-similaridade"
SRC_MAIN="$ROOT/src/main/java"
OUT="$ROOT/out"
# CORREÇÃO: O caminho dos documentos é agora relativo ao diretório 'out' (ponto de execução)
# Assumindo que você moveu 'src/files' para DENTRO da pasta 'out' ou para um local acessível.
# Se a pasta 'src/files' estiver diretamente dentro de 'ROOT', o caminho para alcançá-la a partir de 'out' é '../src/files'
DOC_DIR="../src/files/"

# Nomes dos arquivos de teste
DOC_1="documento_01.txt"
DOC_5="documento_05.txt"
DOC_7="documento_07.txt"

# --- COMPILAÇÃO ---
echo "=========================================="
echo "        Execução Automática"
echo "=========================================="

# Limpar e criar pasta out
rm -rf "$OUT"
mkdir "$OUT"

echo
echo "Compilando o projeto inteiro..."
javac -d "$OUT" $(find "$SRC_MAIN" -name "*.java")

if [ $? -ne 0 ]; then
    echo "ERRO: A compilação falhou!"
    exit 1
fi

# --- VERIFICAÇÃO DE ESTRUTURA ---
# Certificar que a pasta 'files' está onde o programa espera.
# Esta etapa é crucial para a nova estratégia.
if [ ! -d "$ROOT/src/files" ]; then
    echo "ERRO CRÍTICO: Pasta de documentos '$ROOT/src/files' não encontrada."
    exit 1
fi


# --- EXECUÇÃO DOS TESTES ---
echo
echo "=========================================="
echo "        Iniciando Testes de Modos"
echo "=========================================="

# Mover a pasta 'files' para o ponto de execução para garantir acesso relativo.
# O ponto de execução é 'out'. Movemos 'src/files' para dentro de 'out'.
# Você pode fazer isso manualmente ou adaptar o script para copiar os arquivos.
# Vou adaptar o script para COPIAR os arquivos, o que é mais seguro.

cp -r "$ROOT/src/files" "$OUT/"


cd "$OUT" # Entra no diretório de execução 'out'

# 1. MODO LISTA (Listar similaridade >= 0.75)
echo "--- TESTE 1: MODO LISTA (Threshold 0.75) ---"
START_TIME=$(date +%s%N)
# O caminho AGORA é relativo ao 'out' e a pasta se chama 'files'
java com.mackenzie.Main "files/" 0.75 lista
END_TIME=$(date +%s%N)
DURATION=$(( ($END_TIME - $START_TIME) / 1000000 ))
echo "Tempo total do Teste 1: ${DURATION}ms"
echo "------------------------------------------"

# 2. MODO TOP K (Top 3 mais semelhantes)
K=3
echo "--- TESTE 2: MODO TOP K (K=$K) ---"
START_TIME=$(date +%s%N)
java com.mackenzie.Main "files/" 0.0 topK $K
END_TIME=$(date +%s%N)
DURATION=$(( ($END_TIME - $START_TIME) / 1000000 ))
echo "Tempo total do Teste 2: ${DURATION}ms"
echo "------------------------------------------"

# 3. MODO BUSCA (Comparar doc_01 e doc_05)
echo "--- TESTE 3: MODO BUSCA ($DOC_1 vs $DOC_5) ---"
START_TIME=$(date +%s%N)
java com.mackenzie.Main "files/" 0.0 busca "$DOC_1" "$DOC_5"
END_TIME=$(date +%s%N)
DURATION=$(( ($END_TIME - $START_TIME) / 1000000 ))
echo "Tempo total do Teste 3: ${DURATION}ms"
echo "------------------------------------------"

cd "$ROOT"

echo
echo "Execução e Testes finalizados. Verifique o arquivo resultado.txt na pasta 'out'."