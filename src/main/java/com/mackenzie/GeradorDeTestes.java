import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeradorDeTestes {

    private static final String ROOT_DIR = "files";
    private static final String TEST_SUBDIR = "documentos_teste";

    public static void main(String[] args) {
        // Constrói o caminho: files/documentos_teste
        Path diretorio = Paths.get(ROOT_DIR, TEST_SUBDIR);

        try {
            if (!Files.exists(diretorio)) {
                // Cria a estrutura de pastas, se necessário
                Files.createDirectories(diretorio);
                System.out.println("Diretório criado: " + diretorio.toAbsolutePath());
            } else {
                System.out.println("Diretório já existe: " + diretorio.toAbsolutePath());
            }

            // Mapa de arquivos de teste (O conteúdo não mudou)
            Map<String, String> arquivos = new LinkedHashMap<>();

            // --- CENÁRIO 1: SANITY CHECK ---
            arquivos.put("t1_base.txt", "o rato roeu a roupa do rei de roma");
            arquivos.put("t1_igual.txt", "o rato roeu a roupa do rei de roma");
            arquivos.put("t1_reordenado.txt", "roma de rei do roupa a roeu rato o");
            arquivos.put("t1_diferente.txt", "a batata assou no forno do padeiro");

            // --- CENÁRIO 2: DETECÇÃO DE PLÁGIO ---
            arquivos.put("t2_origem.txt",
                    "A linguagem Java é orientada a objetos e foi projetada para ter o menor número possível de dependências de implementação. " +
                            "Sua intenção é permitir que os desenvolvedores de aplicações escrevam uma vez e executem em qualquer lugar.");
            arquivos.put("t2_plagio.txt",
                    "Java é uma linguagem orientada a objetos, criada para minimizar dependências de implementação. " +
                            "O objetivo é permitir que programadores escrevam o código uma única vez e o rodem em qualquer lugar.");
            arquivos.put("t2_topico_diferente.txt",
                    "O bolo de cenoura é uma receita tradicional e foi criado para ter o melhor sabor possível com cobertura de chocolate. " +
                            "Sua intenção é permitir que os cozinheiros preparem uma vez e sirvam em qualquer lugar.");

            // --- CENÁRIO 3: CASOS DE BORDA ---
            arquivos.put("t3_vazio.txt", "");
            arquivos.put("t3_so_stopwords.txt", "de a o para em com por sem sob e ou nem mas");
            arquivos.put("t3_repeticao.txt", "dados dados dados dados dados dados dados dados dados dados");

            // Gera os arquivos .txt
            for (Map.Entry<String, String> entrada : arquivos.entrySet()) {
                Path arquivoPath = diretorio.resolve(entrada.getKey());
                Files.write(arquivoPath, entrada.getValue().getBytes(StandardCharsets.UTF_8));
                System.out.println("Gerado: " + diretorio.getFileName() + "/" + entrada.getKey());
            }

            // --- GERAÇÃO DO GABARITO (README) ---
            String gabaritoContent =
                    "=== GABARITO E GUIA DE TESTES ===\n" +
                            "Nota: Os valores exatos podem variar conforme a métrica (Cosseno, Jaccard) e o pré-processamento.\n\n" +

                            "1. t1_base.txt <-> t1_igual.txt -> Similaridade 1.0 (100%)\n" +
                            "2. t1_base.txt <-> t1_reordenado.txt -> Similaridade 1.0 (Bag-of-words ignora ordem)\n" +
                            "3. t1_base.txt <-> t1_diferente.txt -> Similaridade 0.0\n" +
                            "4. t2_origem.txt <-> t2_plagio.txt -> Alta (geralmente > 0.6 ou 0.7)\n" +
                            "5. t2_origem.txt <-> t2_topico_diferente.txt -> Baixa (geralmente < 0.3)\n" +
                            "6. t3_vazio.txt <-> t1_base.txt -> Similaridade 0.0 (Divisão por zero tratada)\n" +
                            "7. t3_so_stopwords.txt <-> t1_base.txt -> Similaridade 0.0 (Vetores vazios após filtro)";

            Files.write(diretorio.resolve("LEIA_ME_GABARITO.txt"), gabaritoContent.getBytes(StandardCharsets.UTF_8));
            System.out.println("Gerado: " + diretorio.getFileName() + "/LEIA_ME_GABARITO.txt");

            System.out.println("\nSucesso! Todos os arquivos foram gerados na pasta: " + ROOT_DIR + "/" + TEST_SUBDIR);

        } catch (IOException e) {
            System.err.println("Erro ao gerar arquivos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}