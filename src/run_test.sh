#!/bin/bash
# ==========================================
# Script de Execução e Teste Automatizado
# Versão Corrigida: Mostra output no terminal e anexa ao resultado.txt
# ==========================================

# Diretórios
ROOT="/home/wvxbs/Documentos/repos/ed2-projeto3-similaridade"
SRC_MAIN="$ROOT/src/main/java"
OUT="$ROOT/out"

# Nomes dos arquivos de teste
DOC_1="documento_01.txt"
DOC_5="documento_05.txt"

# --- COMPILAÇÃO ---
echo "=========================================="
echo "        Execução Automática"
echo "=========================================="

# Limpar e criar pasta out
rm -rf "$OUT"
mkdir "$OUT"

echo
echo "Compilando o projeto inteiro..."
# Limpa o arquivo de resultado antes de começar
echo "" > "$OUT/resultado.txt"

javac -d "$OUT" $(find "$SRC_MAIN" -name "*.java")

if [ $? -ne 0 ]; then
    echo "ERRO: A compilação falhou!"
    exit 1
fi

# --- VERIFICAÇÃO DE ESTRUTURA E COPIA DE ARQUIVOS ---
if [ ! -d "$ROOT/src/files" ]; then
    echo "ERRO CRÍTICO: Pasta de documentos '$ROOT/src/files' não encontrada."
    exit 1
fi

cp -r "$ROOT/src/files" "$OUT/"


# --- EXECUÇÃO DOS TESTES ---
echo
echo "=========================================="
echo "        Iniciando Testes de Modos"
echo "=========================================="

cd "$OUT" # Entra no diretório de execução 'out'

# Função para executar e capturar a saída
run_test() {
    local DESC=$1
    local CMD=$2
    local START_TIME=$(date +%s%N)

    # 1. Executa o comando e captura a saída completa (STDOUT + STDERR)
    local OUTPUT=$(eval $CMD 2>&1)

    local END_TIME=$(date +%s%N)
    local DURATION=$(( ($END_TIME - $START_TIME) / 1000000 ))

    # 2. Imprime a descrição do teste e a duração no terminal
    echo "--- $DESC ---"
    echo "Tempo total do Teste: ${DURATION}ms"
    echo "------------------------------------------"

    # 3. Imprime a saída capturada (do programa Java) no terminal
    echo "$OUTPUT"

    # 4. Anexa a saída capturada (e os separadores) ao resultado.txt
    echo -e "\n\n=== INÍCIO: $DESC ===" >> resultado.txt
    echo "$OUTPUT" >> resultado.txt
    echo "=== FIM: $DESC (Tempo: ${DURATION}ms) ===" >> resultado.txt
}

# 1. MODO LISTA (Listar similaridade >= 0.75)
run_test "TESTE 1: MODO LISTA (Threshold 0.75)" 'java com.mackenzie.Main "files/" 0.75 lista'

# 2. MODO TOP K (Top 3 mais semelhantes)
K=3
run_test "TESTE 2: MODO TOP K (K=$K)" "java com.mackenzie.Main \"files/\" 0.0 topK $K"

# 3. MODO BUSCA (Comparar doc_01 e doc_05)
run_test "TESTE 3: MODO BUSCA ($DOC_1 vs $DOC_5)" "java com.mackenzie.Main \"files/\" 0.0 busca \"$DOC_1\" \"$DOC_5\""

cd "$ROOT"

echo
echo "Execução e Testes finalizados. O arquivo resultado.txt deve estar na pasta 'out' com a saída completa de todos os 3 testes."