ROOT="/home/wvxbs/Documentos/repos/ed2-projeto3-similaridade"
SRC_MAIN="$ROOT/src/main/java"
SRC_FILES="$ROOT/src/files"
OUT="$ROOT/out"
TEST_DIR="files/documentos_teste"
EXECUTION_SUMMARY=""

echo "=========================================="
echo "        Execução Automática de Testes de Gabarito"
echo "=========================================="

rm -rf "$OUT"
mkdir "$OUT"

echo
echo "Compilando o projeto inteiro..."
echo "" > "$OUT/resultado.txt"

# Compilação
javac -d "$OUT" $(find "$SRC_MAIN" -name "*.java")

if [ $? -ne 0 ]; then
    echo "ERRO: A compilação falhou! Verifique a estrutura de pacotes."
    exit 1
fi

echo "Copiando arquivos de teste (apenas $TEST_DIR) para $OUT/files/..."
if [ ! -d "$SRC_FILES" ]; then
    echo "ERRO CRÍTICO: Pasta de documentos '$SRC_FILES' não encontrada."
    exit 1
fi
cp -r "$SRC_FILES" "$OUT/"


echo
echo "=========================================="
echo "        Iniciando Testes de Gabarito (Casos de Borda)"
echo "=========================================="

cd "$OUT"

run_test() {
    local DESC=$1
    local CMD=$2
    local START_TIME=$(date +%s%N)
    local SHELL_STATUS
    local PASSED

    echo ""
    echo "========================================================"
    echo ">>>> TESTE INICIADO: $DESC"
    echo ">> COMANDO: ${CMD}"
    echo "========================================================"

    local OUTPUT=$(eval $CMD 2>&1)

    SHELL_STATUS=$?

    if [ $SHELL_STATUS -eq 0 ]; then
        PASSED="✅ OK"
    else
        PASSED="❌ ERRO (Cód. $SHELL_STATUS)"
    fi

    local END_TIME=$(date +%s%N)
    local DURATION=$(( ($END_TIME - $START_TIME) / 1000000 ))

    local DIR_BASE=$(echo $CMD | awk '{print $3}' | sed 's/\"//g')

    EXECUTION_SUMMARY+="${PASSED} | ${DESC} | ${DURATION}ms\n"

    echo "DIRETÓRIO BASE: $DIR_BASE"
    echo "STATUS GERAL: ${PASSED}"
    echo "DURAÇÃO: ${DURATION}ms"
    echo "STATUS CÓDIGO SHELL: ${SHELL_STATUS}"
    echo "--------------------------------------------------------"

    echo "$OUTPUT"

    echo "========================================================"
    echo ">>>> FIM DO TESTE: $DESC"
    echo "========================================================"
    echo ""

    echo -e "\n\n=== INÍCIO: $DESC ===" >> resultado.txt
    echo "COMANDO: $CMD" >> resultado.txt
    echo "DIRETÓRIO BASE: $DIR_BASE" >> resultado.txt
    echo "STATUS SHELL: $SHELL_STATUS" >> resultado.txt
    echo "$OUTPUT" >> resultado.txt
    echo "=== FIM: $DESC (Status: ${PASSED} | Tempo: ${DURATION}ms) ===" >> resultado.txt
}

finalize_report() {
    echo -e "\n\n========================================================" >> resultado.txt
    echo "        SUMÁRIO CONDENSADO DA EXECUÇÃO" >> resultado.txt
    echo "========================================================" >> resultado.txt
    echo -e "STATUS | TESTE | TEMPO" >> resultado.txt
    echo "--------------------------------------------------------" >> resultado.txt
    echo -e "$EXECUTION_SUMMARY" >> resultado.txt
    echo "========================================================" >> resultado.txt
}

# ========================================================
#          TESTES DO PROFESSOR (CENÁRIOS DE BORDA)
# ========================================================

echo
echo "=========================================="
echo "        Iniciando Testes de Borda (Gabarito)"
echo "=========================================="

run_test "TESTE 1: BORDA - Identidade (1.0)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 busca t1_base.txt t1_igual.txt"

run_test "TESTE 2: BORDA - Reordenamento (1.0)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 busca t1_base.txt t1_reordenado.txt"

run_test "TESTE 3: BORDA - Vocabulário Zero (0.0)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 busca t1_base.txt t1_diferente.txt"

run_test "TESTE 4: BORDA - Plágio (Alta)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 busca t2_origem.txt t2_plagio.txt"

run_test "TESTE 5: BORDA - Tópico Diferente (Baixa)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 busca t2_origem.txt t2_topico_diferente.txt"

run_test "TESTE 6: BORDA - Vazio vs. Normal (0.0)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 busca t3_vazio.txt t1_base.txt"

run_test "TESTE 7: BORDA - Só Stop Words (0.0)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 busca t3_so_stopwords.txt t1_base.txt"

run_test "TESTE 8: MODO LISTA (Diretório de Borda)" 'java -cp . com.mackenzie.Main "$TEST_DIR" 0.0 lista'

run_test "TESTE 9: MODO TOP K (Diretório de Borda - Top 3)" "java -cp . com.mackenzie.Main \"$TEST_DIR\" 0.0 topK 3"


finalize_report

cd "$ROOT"

echo
echo "Execução e Testes finalizados. O arquivo $OUT/resultado.txt contém o sumário condensado no final."