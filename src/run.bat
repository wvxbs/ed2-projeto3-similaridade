@echo off
setlocal enabledelayedexpansion
REM ==========================================
REM Script de Execução e Teste Automatizado
REM Versão Windows (.bat)
REM ==========================================

REM --- DIRETÓRIOS ---
set ROOT=C:\Users\Kaiki\Faculdade\EstruturaDeDados\ed2-projeto3-similaridade
set SRC_MAIN=%ROOT%\src\main\java
set OUT=%ROOT%\out

set DOC_1=documento_01.txt
set DOC_5=documento_05.txt

echo ==========================================
echo          Execucao Automatica
echo ==========================================

REM --- LIMPAR PASTA OUT ---
if exist "%OUT%" (
    rmdir /s /q "%OUT%"
)
mkdir "%OUT%"

echo.
echo Compilando o projeto inteiro...

REM Zera arquivo resultado.txt
type nul > "%OUT%\resultado.txt"

REM Compilar todos os .java encontrados recursivamente
for /r "%SRC_MAIN%" %%f in (*.java) do (
    set "FILES=!FILES! %%f"
)

REM Habilita delayed expansion
setlocal enabledelayedexpansion
javac -d "%OUT%" !FILES!
if %errorlevel% neq 0 (
    echo ERRO: A compilacao falhou!
    exit /b 1
)
endlocal

REM --- VERIFICAR ESTRUTURA E COPIAR FILES ---
if not exist "%ROOT%\src\files" (
    echo ERRO CRITICO: Pasta %ROOT%\src\files nao encontrada.
    exit /b 1
)

xcopy "%ROOT%\src\files" "%OUT%\files" /E /I /Y >nul

echo.
echo ==========================================
echo         Iniciando Testes
echo ==========================================

cd "%OUT%"

REM =====================================================
REM TESTE 1 - MODO LISTA
REM =====================================================
echo --- TESTE 1: MODO LISTA (Threshold 0.75) ---
set START=%time%

echo.>> resultado.txt
echo.>> resultado.txt
echo === INICIO: TESTE 1 - MODO LISTA (Threshold 0.75) ===>> resultado.txt

java com.mackenzie.Main "files/" 0.75 lista >> resultado.txt 2>&1

echo === FIM: TESTE 1 ===>> resultado.txt

set END=%time%
echo Tempo total do Teste 1: %START% → %END%
echo ------------------------------------------


REM =====================================================
REM TESTE 2 - MODO TOP K
REM =====================================================
set K=3
echo --- TESTE 2: MODO TOP K (K=%K%) ---
set START=%time%

echo.>> resultado.txt
echo.>> resultado.txt
echo === INICIO: TESTE 2 - MODO TOP K (K=%K%) ===>> resultado.txt

java com.mackenzie.Main "files/" 0.0 topK %K% >> resultado.txt 2>&1

echo === FIM: TESTE 2 ===>> resultado.txt

set END=%time%
echo Tempo total do Teste 2: %START% → %END%
echo ------------------------------------------


REM =====================================================
REM TESTE 3 - MODO BUSCA
REM =====================================================
echo --- TESTE 3: MODO BUSCA (%DOC_1% vs %DOC_5%) ---
set START=%time%

echo.>> resultado.txt
echo.>> resultado.txt
echo === INICIO: TESTE 3 - MODO BUSCA (%DOC_1% vs %DOC_5%) ===>> resultado.txt

java com.mackenzie.Main "files/" 0.0 busca "%DOC_1%" "%DOC_5%" >> resultado.txt 2>&1

echo === FIM: TESTE 3 ===>> resultado.txt

set END=%time%
echo Tempo total do Teste 3: %START% → %END%
echo ------------------------------------------

cd "%ROOT%"

echo.
echo Execucao finalizada! O arquivo resultado.txt esta na pasta OUT.
endlocal