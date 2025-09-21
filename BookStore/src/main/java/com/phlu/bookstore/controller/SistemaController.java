package com.phlu.bookstore.controller;

import com.phlu.bookstore.dao.CarrinhoDAO;
import com.phlu.bookstore.dao.CompraDAO;
import com.phlu.bookstore.dao.LivroDAO;
import com.phlu.bookstore.model.*;
import com.phlu.bookstore.service.RelatorioService;
import com.phlu.bookstore.service.UsuarioService;
import com.phlu.bookstore.view.ConsoleView;

import java.util.*;

public class SistemaController {

    private ConsoleView view;
    private UsuarioService usuarioService;
    private LivroDAO livroDAO;
    private CompraDAO compraDAO;
    private CarrinhoDAO carrinhoDAO;
    private RelatorioService relatorioService;
    private Usuario usuarioLogado;

    public SistemaController(ConsoleView view) {
        this.view = view;
        this.usuarioService = new UsuarioService();
        this.livroDAO = new LivroDAO();
        this.compraDAO = new CompraDAO();
        this.carrinhoDAO = new CarrinhoDAO();
        this.relatorioService = new RelatorioService();
    }

    public void iniciar() {
        criarAdministradorInicial();

        while (true) {
            if (usuarioLogado == null) {
                exibirMenuLogin();
            } else if (usuarioLogado instanceof Administrador) {
                exibirMenuAdministrador();
            } else {
                exibirMenuCliente();
            }
        }
    }

    // --- Administrador inicial ---
    private void criarAdministradorInicial() {
        if (!usuarioService.existeAdministrador()) {
            view.exibirMensagem("Nenhum administrador encontrado. Crie o administrador inicial:");
            view.exibirMensagem("Nome: "); String nome = view.capturarEntrada();
            view.exibirMensagem("Email: "); String email = view.capturarEntrada();
            view.exibirMensagem("Senha: "); String senha = view.capturarEntrada();
            usuarioService.cadastrarAdministradorInicial(nome, email, senha);
        }
    }

    // --- Login / Logout ---
    private void exibirMenuLogin() {
        view.exibirMenuLogin();
        String opcao = view.capturarEntrada();
        switch (opcao) {
            case "1": login(); break;
            case "2": cadastrarCliente(); break;
            case "3": System.exit(0); break;
            default: view.mostrarErro("Opção inválida."); break;
        }
    }

    private void login() {
        view.exibirMensagem("Email: "); String email = view.capturarEntrada();
        view.exibirMensagem("Senha: "); String senha = view.capturarEntrada();
        usuarioLogado = usuarioService.login(email, senha);
        if (usuarioLogado != null)
            view.exibirMensagem("Login bem-sucedido! Bem-vindo, " + usuarioLogado.getNome());
        else
            view.mostrarErro("Email ou senha inválidos.");
    }

    private void logout() {
        usuarioLogado = null;
        view.exibirMensagem("Logout realizado.");
    }

    // --- Menus Dinâmicos ---
    private void exibirMenuCliente() {
        view.exibirMenuCliente(usuarioLogado.getNome());
        String opcao = view.capturarEntrada();
        switch (opcao) {
            case "1": exibirCatalogoCliente(); break;
            case "2": exibirCarrinho(); break;
            case "3": visualizarCompras(); break;
            case "4": logout(); break;
            default: view.mostrarErro("Opção inválida."); break;
        }
    }

    private void exibirMenuAdministrador() {
        view.exibirMenuAdministrador(usuarioLogado.getNome());
        String opcao = view.capturarEntrada();
        switch (opcao) {
            case "1": exibirCatalogoAdministrador(); break;
            case "2": cadastrarLivro(); break;
            case "3": cadastrarUsuarioLogado(); break;
            case "4": gerarRelatorios(); break;
            case "5": logout(); break;
            default: view.mostrarErro("Opção inválida."); break;
        }
    }

    // --- Cadastros ---
    private void cadastrarCliente() {
        view.exibirMensagem("Cadastro de Cliente");
        view.exibirMensagem("Nome: "); String nome = view.capturarEntrada();
        view.exibirMensagem("Email: "); String email = view.capturarEntrada();
        view.exibirMensagem("CPF: "); String cpf = view.capturarEntrada();
        view.exibirMensagem("Senha: "); String senha = view.capturarEntrada();
        usuarioService.cadastrarUsuario(nome, email, senha, "1", cpf);
        view.exibirMensagem("Cliente cadastrado com sucesso!");
    }

    private void cadastrarUsuarioLogado() {
        view.exibirMensagem("Cadastro de Usuario/Admin");
        view.exibirMensagem("Nome: "); String nome = view.capturarEntrada();
        view.exibirMensagem("Email: "); String email = view.capturarEntrada();
        view.exibirMensagem("Senha: "); String senha = view.capturarEntrada();
        view.exibirMensagem("CPF (se cliente): "); String cpf = view.capturarEntrada();
        view.exibirMensagem("Tipo (1=Cliente, 2=Administrador): "); String tipo = view.capturarEntrada();

        if ("2".equals(tipo) && !(usuarioLogado instanceof Administrador)) {
            view.mostrarErro("Apenas administradores podem criar outro administrador.");
            return;
        }

        usuarioService.cadastrarUsuario(nome, email, senha, tipo, cpf);
        view.exibirMensagem("Usuário cadastrado com sucesso!");
    }

    private void cadastrarLivro() {
        view.exibirMensagem("Título: "); String titulo = view.capturarEntrada();
        view.exibirMensagem("Autor: "); String autor = view.capturarEntrada();
        view.exibirMensagem("Preço: "); double preco = Double.parseDouble(view.capturarEntrada().replace(",", "."));
        view.exibirMensagem("Categoria: "); String categoria = view.capturarEntrada();
        view.exibirMensagem("Quantidade em estoque: "); int qtd = Integer.parseInt(view.capturarEntrada());
        livroDAO.adicionarLivro(new Livro(titulo, autor, preco, categoria, qtd));
        view.exibirMensagem("Livro cadastrado com sucesso!");
    }

    // --- Catálogo Cliente (adiciona no carrinho persistente) ---
    private void exibirCatalogoCliente() {
        Cliente cliente = (Cliente) usuarioLogado;

        while (true) {
            view.exibirMensagem("\n--- Catálogo ---");
            List<Livro> livros = livroDAO.listarLivros();
            view.exibirLivros(livros);

            view.exibirMensagem("Deseja filtrar? (S/N) ou 0 para voltar:");
            String resp = view.capturarEntrada();
            if (resp.equalsIgnoreCase("0")) break;

            if (resp.equalsIgnoreCase("S")) {
                view.exibirMensagem("Escolha filtro: 1=Título, 2=Autor, 3=Categoria");
                int tipoFiltro = Integer.parseInt(view.capturarEntrada());
                view.exibirMensagem("Digite termo para filtro:");
                String termo = view.capturarEntrada();

                String campo = switch (tipoFiltro) {
                    case 1 -> "titulo";
                    case 2 -> "autor";
                    case 3 -> "categoria";
                    default -> "titulo";
                };

                livros = livroDAO.buscarLivrosPorCampo(campo, termo);
                if (livros.isEmpty()) {
                    view.mostrarErro("Nenhum livro encontrado.");
                    continue;
                }
                view.exibirLivros(livros);
            }

            view.exibirMensagem("Digite o número do livro para adicionar ao carrinho ou Enter para continuar:");
            String escolhaStr = view.capturarEntrada();
            if (escolhaStr.isEmpty()) continue;

            int escolha;
            try { escolha = Integer.parseInt(escolhaStr); }
            catch (NumberFormatException e) { view.mostrarErro("Entrada inválida."); continue; }

            if (escolha < 1 || escolha > livros.size()) {
                view.mostrarErro("Escolha inválida.");
                continue;
            }

            Livro selecionado = livros.get(escolha - 1);
            if (selecionado.getQuantidadeEstoque() == 0) {
                view.mostrarErro("Livro indisponível.");
                continue;
            }

            // Necessita do ID do livro (se seu Livro não tiver getId(), crie um método no DAO para buscar por título/autor)
            int livroId = selecionado.getId();
            carrinhoDAO.adicionarItem(cliente.getId(), livroId, 1);
            view.exibirMensagem("Livro adicionado ao carrinho: " + selecionado.getTitulo());
        }
    }

    // --- Catálogo Administrador ---
    private void exibirCatalogoAdministrador() {
        List<Livro> livros = livroDAO.listarLivros();
        view.exibirLivros(livros);

        view.exibirMensagem("Deseja filtrar? (S/N) ou Enter para voltar:");
        String resp = view.capturarEntrada();
        if (!resp.equalsIgnoreCase("S")) return;

        view.exibirMensagem("Escolha filtro: 1=Título, 2=Autor, 3=Categoria");
        int tipoFiltro = Integer.parseInt(view.capturarEntrada());
        view.exibirMensagem("Digite termo para filtro:");
        String termo = view.capturarEntrada();

        String campo = switch (tipoFiltro) {
            case 1 -> "titulo";
            case 2 -> "autor";
            case 3 -> "categoria";
            default -> "titulo";
        };

        livros = livroDAO.buscarLivrosPorCampo(campo, termo);
        view.exibirLivros(livros);
    }

    // --- Carrinho Cliente (persistente) ---
    private void exibirCarrinho() {
        Cliente cliente = (Cliente) usuarioLogado;

        while (true) {
            Map<Livro, Integer> itens = carrinhoDAO.listarItens(cliente.getId());
            List<Integer> livroIds = carrinhoDAO.listarLivroIdsNoCarrinho(cliente.getId());

            System.out.println("\n--- Carrinho ---");
            if (itens.isEmpty()) {
                view.exibirMensagem("Carrinho vazio. 0 para voltar.");
                String s = view.capturarEntrada();
                return;
            }

            int i = 1;
            double total = 0.0;
            List<Livro> livrosOrdem = new ArrayList<>(itens.keySet()); // mesma ordem do SELECT
            for (Livro l : livrosOrdem) {
                int qtd = itens.get(l);
                System.out.printf("%d. %s | %s | R$ %.2f | Qtde: %d%n",
                        i++, l.getTitulo(), l.getAutor(), l.getPreco(), qtd);
                total += l.getPreco() * qtd;
            }
            System.out.printf("Total: R$ %.2f%n", total);

            view.exibirMensagem("Comandos: R <num>=remover | Q <num> <qtd>=alterar | F=finalizar | 0=voltar");
            String cmd = view.capturarEntrada().trim();

            if (cmd.equals("0")) return;

            if (cmd.equalsIgnoreCase("F")) {
                // Valida e debita estoque
                Map<Livro, Integer> itensFinal = carrinhoDAO.listarItens(cliente.getId());
                List<Integer> idsFinal        = carrinhoDAO.listarLivroIdsNoCarrinho(cliente.getId());

                List<Livro> livrosFinalOrdem = new ArrayList<>(itensFinal.keySet());
                for (int idx = 0; idx < livrosFinalOrdem.size(); idx++) {
                    Livro l = livrosFinalOrdem.get(idx);
                    int qtdComprada = itensFinal.get(l);
                    int novaQtd = l.getQuantidadeEstoque() - qtdComprada;
                    if (novaQtd < 0) {
                        view.mostrarErro("Estoque insuficiente para: " + l.getTitulo());
                        return;
                    }
                }
                for (int idx = 0; idx < livrosFinalOrdem.size(); idx++) {
                    Livro l = livrosFinalOrdem.get(idx);
                    int qtdComprada = itensFinal.get(l);
                    int novaQtd = l.getQuantidadeEstoque() - qtdComprada;
                    livroDAO.atualizarEstoque(l.getTitulo(), novaQtd);
                }

                // Salva compra com IDs corretos
                Compra compra = new Compra(itensFinal);
                int compraId = compraDAO.salvarCompraComIds(cliente.getId(), compra, idsFinal);

                if (compraId > 0) {
                    carrinhoDAO.limpar(cliente.getId());
                    view.exibirMensagem("Compra #" + compraId + " finalizada com sucesso!");
                } else {
                    view.mostrarErro("Falha ao registrar compra.");
                }
                return;
            }

            try {
                if (cmd.startsWith("R ")) {
                    int pos = Integer.parseInt(cmd.substring(2).trim());
                    if (pos >= 1 && pos <= livroIds.size()) {
                        int livroId = livroIds.get(pos - 1);
                        carrinhoDAO.removerItem(cliente.getId(), livroId);
                        view.exibirMensagem("Item removido.");
                    } else view.mostrarErro("Posição inválida.");
                } else if (cmd.startsWith("Q ")) {
                    String[] p = cmd.split("\\s+");
                    int pos = Integer.parseInt(p[1]);
                    int nova = Integer.parseInt(p[2]);
                    if (pos >= 1 && pos <= livroIds.size()) {
                        int livroId = livroIds.get(pos - 1);
                        carrinhoDAO.atualizarQuantidade(cliente.getId(), livroId, nova);
                        view.exibirMensagem("Quantidade atualizada.");
                    } else view.mostrarErro("Posição inválida.");
                }
            } catch (Exception e) {
                view.mostrarErro("Comando inválido.");
            }
        }
    }

    // --- Histórico de compras (banco) ---
    private void visualizarCompras() {
        if (!(usuarioLogado instanceof Cliente)) {
            view.mostrarErro("Apenas clientes podem visualizar compras.");
            return;
        }
        Cliente cliente = (Cliente) usuarioLogado;
        List<Compra> compras = compraDAO.listarComprasDoUsuario(cliente.getId());

        if (compras == null || compras.isEmpty()) {
            view.mostrarErro("Nenhuma compra realizada.");
            return;
        }

        System.out.println("\n--- Histórico de Compras ---");
        int idx = 1;
        for (Compra compra : compras) {
            System.out.println("\nCompra " + (idx++));
            double total = 0.0;
            for (Map.Entry<Livro, Integer> item : compra.getItens().entrySet()) {
                Livro l = item.getKey();
                int q = item.getValue();
                System.out.printf("%s | %s | R$ %.2f | Qtde: %d%n",
                        l.getTitulo(), l.getAutor(), l.getPreco(), q);
                total += l.getPreco() * q;
            }
            System.out.printf("Total: R$ %.2f | Entrega prevista: %s%n",
                    total, compra.getDataEntregaPrevista());
        }
    }

    // --- Relatórios ---
    private void gerarRelatorios() {
        if (!(usuarioLogado instanceof Administrador)) return;
        view.exibirMensagem("\n--- RELATÓRIOS ---");
        view.exibirMensagem("Livros mais vendidos:");
        relatorioService.gerarLivrosMaisVendidos().forEach(view::exibirMensagem);
        view.exibirMensagem("\nClientes que mais compraram:");
        relatorioService.gerarClientesMaisCompraram().forEach(view::exibirMensagem);
    }
}
