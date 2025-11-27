@echo off
REM ==========================================
REM Script de Execução e Teste Automatizado para Windows (.bat)
REM ==========================================

REM Define o caminho raiz do projeto como o diretório atual do script
set "ROOT=%~dp0"
set "SRC_MAIN=%ROOT%src\main\java"
set "OUT=%ROOT%out"

REM Nomes dos arquivos de teste (assumindo que a pasta é src\files)
set "DOC_DIR=files\"
set "DOC_1=documento_01.txt"
set "DOC_5=documento_05.txt"

REM --- COMPILAÇÃO ---
echo ==========================================
echo         Execucao Automatica
echo ==========================================

REM Limpar e criar pasta out
if exist "%OUT%" rd /s /q "%OUT%"
md "%OUT%"

echo.
echo Compilando o projeto inteiro...
REM Encontra todos os arquivos .java no subdiretorio src\main\java e compila para out
javac -d "%OUT%" "%SRC_MAIN%\com\mackenzie\*.java" "%SRC_MAIN%\com\mackenzie\controller\*.java" "%SRC_MAIN%\com\mackenzie\model\*.java" "%SRC_MAIN%\com\mackenzie\service\*.java" "%SRC_MAIN%\com\mackenzie\view\*.java"

if errorlevel 1 goto :compilation_error

REM --- PREPARACAO DOS DOCUMENTOS ---
echo.
echo Copiando documentos para o diretorio de execucao...
REM Copia a pasta de documentos para dentro de 'out'
xcopy "%ROOT%src\files" "%OUT%\files\" /s /e /i /y > nul

REM --- EXECUÇÃO DOS TESTES ---
echo.
echo ==========================================
echo         Iniciando Testes de Modos
echo ==========================================

REM Entra no diretorio de execucao 'out'
cd "%OUT%"

REM --- Teste 1: MODO LISTA (Threshold 0.75) ---
echo --- TESTE 1: MODO LISTA (Threshold 0.75) ---
call :RUN_TEST "java com.mackenzie.Main %DOC_DIR% 0.75 lista"
echo ------------------------------------------

REM --- Teste 2: MODO TOP K (K=3) ---
set K=3
echo --- TESTE 2: MODO TOP K (K=%K%) ---
call :RUN_TEST "java com.mackenzie.Main %DOC_DIR% 0.0 topK %K%"
echo ------------------------------------------

REM --- Teste 3: MODO BUSCA (Comparar doc_01 e doc_05) ---
echo --- TESTE 3: MODO BUSCA (%DOC_1% vs %DOC_5%) ---
call :RUN_TEST "java com.mackenzie.Main %DOC_DIR% 0.0 busca %DOC_1% %DOC_5%"
echo ------------------------------------------

REM --- Teste 4: DESEMPENHO COMPLETO (Listar tudo) ---
echo --- TESTE 4: DESEMPENHO COMPLETO (Listar tudo) ---
call :RUN_TEST "java com.mackenzie.Main %DOC_DIR% 0.0 lista"
echo ------------------------------------------

goto :end

REM --- FUNÇÃO PARA MEDIR TEMPO E EXECUTAR ---
:RUN_TEST
set TEST_COMMAND=%1
set START_TIME=%time%

REM Executa o comando e captura a saida (a saida ja e direcionada pelo Main)
%TEST_COMMAND%

REM Medicao de tempo é complexa no Batch. Exibimos a hora de inicio e fim
REM Para o relatorio, use o tempo que o Main.java imprime (tempo de construcao da AVL).

echo Comando executado: %TEST_COMMAND%
echo.

goto :eof

:compilation_error
echo.
echo ERRO: A compilacao falhou! Verifique as mensagens de erro acima.
goto :end

:end
cd "%ROOT%"
echo.
echo Execucao e Testes finalizados. Verifique o arquivo resultado.txt na pasta out.
pause