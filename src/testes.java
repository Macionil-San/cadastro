import java.io.*;
import java.util.Scanner;

public class testes {
    private static final String FILE_NAME = "arquivo.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean sair = false;

        while (!sair) {
            System.out.println("""
                    ________________________________________________
                    | ESCOLHA UMA OPÇÃO:                            |
                    | 1 - EXIBIR CADASTRO COMPLETO                  |
                    | 2 - CADASTRAR NOVO ALUNO                      |
                    | 3 - CONSULTAR ALUNO POR MATRÍCULA             |
                    | 4 - ATUALIZAR CADASTRO POR MATRÍCULA          |
                    | 5 - DELETAR CADASTRO POR MATRÍCULA            |
                    | 6 - SAIR                                      |
                    |_______________________________________________|
                    """);

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir o '\n'

            switch (opcao) {
                case 1 : exibirUsuarios();
                break;

                case 2 : cadastrarUsuario(scanner);
                break;

                case 3 : consultarUsuario(scanner);
                break;

                case 4 : atualizarUsuario(scanner);
                break;

                case 5 : deletarUsuario(scanner);
                break;

                case 6 : {
                    System.out.println("Até breve!");
                    sair = true;
                }
                default : System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }

    public static void exibirUsuarios() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            System.out.println("+---------------------+---------------------+---------------------+");
            System.out.println("| Matrícula           | Nome               | Telefone            |");
            System.out.println("+---------------------+---------------------+---------------------+");
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                System.out.printf("| %-19s | %-19s | %-19s |%n",
                        dados[0], formatString(dados[1]), formatString(dados[2]));
            }
            System.out.println("+---------------------------------------------------------------+");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cadastrarUsuario(Scanner scanner) {
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o número da matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Digite o telefone: ");
        String telefone = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(matricula + "\t" + nome + "\t" + telefone);
            writer.newLine();
            System.out.println("Cadastro efetuado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o aluno: " + e.getMessage());
        }
    }

    public static void consultarUsuario(Scanner scanner) {
        System.out.print("Digite o número da matrícula: ");
        String consulta = scanner.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            boolean encontrado = false;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (dados[0].equals(consulta)) {
                    System.out.println("+---------------------+---------------------+---------------------+");
                    System.out.println("| Matrícula           | Nome               | Telefone            |");
                    System.out.println("+---------------------+---------------------+---------------------+");
                    System.out.printf("| %-19s | %-19s | %-19s |%n",
                            dados[0], formatString(dados[1]), formatString(dados[2]));
                    System.out.println("+---------------------------------------------------------------+");
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("Matrícula não encontrada!");
            }
        } catch (IOException e) {
            System.out.println("Erro ao consultar o aluno: " + e.getMessage());
        }
    }

    public static void atualizarUsuario(Scanner scanner) {
        System.out.print("Digite o número da matrícula do aluno a ser atualizado: ");
        String matricula = scanner.nextLine();
        System.out.print("Digite o novo nome do aluno: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o novo telefone do aluno: ");
        String telefone = scanner.nextLine();

        atualizarRegistro(matricula, nome, telefone);
    }

    public static void deletarUsuario(Scanner scanner) {
        System.out.print("Digite o número da matrícula do aluno a ser deletado: ");
        String matricula = scanner.nextLine();

        deletarRegistro(matricula);
    }

    private static void atualizarRegistro(String matricula, String nome, String telefone) {
        File arquivo = new File(FILE_NAME);
        File arquivoTemp = new File("temp_" + FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {

            String linha;
            boolean atualizado = false;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (dados[0].equals(matricula)) {
                    writer.write(matricula + "\t" + nome + "\t" + telefone);
                    atualizado = true;
                } else {
                    writer.write(linha);
                }
                writer.newLine();
            }

            if (atualizado) {
                System.out.println("Cadastro atualizado com sucesso!");
            } else {
                System.out.println("Matrícula não encontrada!");
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar o cadastro: " + e.getMessage());
        }

        arquivo.delete();
        arquivoTemp.renameTo(arquivo);
    }

    private static void deletarRegistro(String matricula) {
        File arquivo = new File(FILE_NAME);
        File arquivoTemp = new File("temp_" + FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {

            String linha;
            boolean deletado = false;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split("\t");
                if (!dados[0].equals(matricula)) {
                    writer.write(linha);
                    writer.newLine();
                } else {
                    deletado = true;
                }
            }

            if (deletado) {
                System.out.println("Cadastro deletado com sucesso!");
            } else {
                System.out.println("Matrícula não encontrada!");
            }
        } catch (IOException e) {
            System.out.println("Erro ao deletar o cadastro: " + e.getMessage());
        }

        arquivo.delete();
        arquivoTemp.renameTo(arquivo);
    }

    public static String formatString(String str) {
        int maxLength = 19;
        if (str.length() > maxLength) {
            return str.substring(0, maxLength - 3) + "...";
        } else {
            return String.format("%-" + maxLength + "s", str);
        }
    }
}

