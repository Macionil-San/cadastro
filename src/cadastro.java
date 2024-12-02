import java.io.*;
import java.util.Scanner;

public class cadastro{
    private static final String arquivoNome = "arquivo.txt";
    private static final String[] cabecalho = {"ID", "Nome", "Telefone", "Email"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String menu = """
                ________________________________________________
                |  ESCOLHA UMA OPÇÃO:                           |
                |       1 - EXIBIR CADASTRO COMPLETO            |
                |       2 - INSERIR NOVO USUÁRIO                |
                |       3 - ATUALIZAR CADASTRO POR ID           |
                |       4 - DELETAR CADASTRO POR ID             |
                |       5 - SAIR                                |
                |_______________________________________________|               
                """;

        int opcao;
        do {
            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir o '\n'

            switch (opcao) {
                case 1 -> exibirUsuarios();
                case 2 -> cadastrarUsuario(scanner);
                case 3 -> atualizarUsuario(scanner);
                case 4 -> deletarUsuario(scanner);
                case 5 -> System.out.println("_________FIM DO PROGRAMA__________");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 5);
    }

    public static void exibirUsuarios() {
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoNome))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println(" Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static void cadastrarUsuario(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoNome, true))) {
            int id = obterProximoId();
            writer.write(id + "\t" + nome + "\t" + telefone + "\t" + email);
            writer.newLine();
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o usuário: " + e.getMessage());
        }
    }

    public static void atualizarUsuario(Scanner scanner) {
        System.out.print("Digite o ID do usuário a ser atualizado: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir o '\n'

        System.out.print("Novo Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Novo Email: ");
        String email = scanner.nextLine();

        atualizarRegistro(id, nome, telefone, email);
    }

    public static void deletarUsuario(Scanner scanner) {
        System.out.print("Digite o ID do usuário a ser deletado: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir o '\n'

        deletarRegistro(id);
    }

    private static int obterProximoId() {
        int maiorId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoNome))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                int id = Integer.parseInt(dados[0]);
                maiorId = Math.max(maiorId, id);
            }
        } catch (IOException | NumberFormatException e) {
            // Ignorar, o arquivo pode estar vazio
        }
        return maiorId + 1;
    }

    private static void atualizarRegistro(int id, String nome, String telefone, String email) {
        File arquivo = new File(arquivoNome);
        File arquivoTemp = new File("temp_" + arquivoNome);

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {

            String linha;
            boolean atualizado = false;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (Integer.parseInt(dados[0]) == id) {
                    writer.write(id + "\t" + nome + "\t" + telefone + "\t" + email);
                    atualizado = true;
                } else {
                    writer.write(linha);
                }
                writer.newLine();
            }

            if (!atualizado) {
                System.out.println("Usuário não encontrado!");
            } else {
                System.out.println("Usuário atualizado com sucesso!");
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar o registro: " + e.getMessage());
        }

        arquivo.delete();
        arquivoTemp.renameTo(arquivo);
    }

    private static void deletarRegistro(int id) {
        File arquivo = new File(arquivoNome);
        File arquivoTemp = new File("temp_" + arquivoNome);

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {

            String linha;
            boolean deletado = false;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (Integer.parseInt(dados[0]) != id) {
                    writer.write(linha);
                    writer.newLine();
                } else {
                    deletado = true;
                }
            }

            if (!deletado) {
                System.out.println("Usuário não encontrado!");
            } else {
                System.out.println("Usuário deletado com sucesso!");
            }
        } catch (IOException e) {
            System.out.println("Erro ao deletar o registro: " + e.getMessage());
        }

        arquivo.delete();
        arquivoTemp.renameTo(arquivo);
    }
}
