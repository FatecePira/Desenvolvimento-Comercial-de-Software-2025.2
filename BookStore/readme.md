# 📑 Relatório Técnico - BookStore

**Integrantes:**  
- Lucas Antonio Bertin Silva  
- Phelipe Cauduro Pereira  

---

## 🏗 Arquitetura do Sistema

O **BookStore** é um sistema de console para livraria online, estruturado em camadas claras:

- **Interface**  
- **Controle**  
- **Domínio**  
- **Persistência**

O ponto de entrada apenas instancia a interface textual e o controlador, conectando os componentes e iniciando o ciclo de execução.  
Essa organização **reduz acoplamento** e **facilita a localização de regras** no código.

---

## 🎛 Interface Textual

- Classe: **`ConsoleView`**  
- Responsabilidades:  
  - Exibir menus, listas numeradas e mensagens  
  - Ler entradas do usuário  
  - Devolver escolhas ao controlador  

⚠️ Não contém regras de negócio, cálculos ou acesso a dados.  
👉 A interação com o usuário fica **isolada** e mudanças na apresentação **não afetam o núcleo** do sistema.

---

## 🧩 Controlador

- Classe: **`SistemaController`**  
- Mantém o estado da sessão:  
  - Usuário autenticado  
  - Carrinho de compras  

### Funções principais:
- Consultar catálogo  
- Adicionar e remover itens do carrinho  
- Finalizar compras  
- Gerar relatórios (quando permitido)  
- Validar operações: índices, quantidades, carrinho não vazio e permissões  

---

## 📦 Modelo de Domínio

- **`Usuario`** → base comum  
  - **`Cliente`**  
  - **`Administrador`**  

- **`Livro`** → título, autor, categoria, preço e estoque  
- **`CarrinhoDeCompras`** → estado transitório  
- **`Compra`** → registro definitivo da transação  

### Estrutura do Carrinho
- Mapeamento **Livro → Quantidade**  
- Simplifica soma de itens repetidos, remoção e cálculo de totais  
- Cálculos encapsulados na própria classe  

---

## 💾 Persistência (DAOs)

- **`UsuarioDAO`** → busca e instanciação de usuários  
- **`LivroDAO`** → listagem e filtros (título, autor, categoria)  
- **`CarrinhoDAO`** → suporte opcional para persistir carrinho  
- **`CompraDAO`** → gravação da compra com **transação única**  

### Estratégia de Transação
1. Inserção do cabeçalho da compra  
2. Captura do identificador  
3. Inserção dos itens  
4. Atualização dos estoques  
5. Commit ou rollback em caso de falha  

---

## 📊 Relatórios

- Classe: **`RelatorioService`**  
- Tipos de relatório:  
  - Livros mais vendidos (quantidades por obra)  
  - Clientes que mais compram (por volume ou valor)  

👉 O controlador apenas solicita os dados, e a interface exibe.  
👉 Mantém **separação entre cálculo, coordenação e exibição**.

---

## 🔑 Conceitos de Orientação a Objetos

- **Abstração** → `Usuario`  
- **Herança** → `Cliente`, `Administrador`  
- **Polimorfismo** → controlador opera sobre `Usuario`  
- **Composição** → `CarrinhoDeCompras` e `Compra`  
- **Encapsulamento** → cálculos e invariantes dentro das classes  

---

## 📈 Evolução do Código

- Novos filtros → adicionados no `LivroDAO`  
- Regras comerciais (ex: cupom, frete) → aplicadas antes da criação da `Compra`  
- Novos relatórios → extensão do `RelatorioService`  
- Troca de interface (console → GUI/web) → preserva domínio, serviços, DAOs e controlador  

---

## ✅ Qualidade Interna

- **Nomes descritivos** para classes e métodos  
- **Operações claras**, como:  
  - `listar`, `buscarPorCampo`, `adicionar`, `removerPorIndice`, `calcularTotal`, `finalizarCompra`, `salvarCompra`  
- **Responsabilidade única** por classe → melhora leitura, testes e manutenção  

---

## 👥 Responsabilidades dos Integrantes

- **Lucas Antonio Bertin Silva**  
  - Estruturou o fluxo no `SistemaController`  
  - Implementou operações do carrinho (adição, remoção, totalização)  
  - Conduziu finalização da compra com baixa de estoque  
  - Revisou entradas e mensagens na `ConsoleView`  

- **Phelipe Cauduro Pereira**  
  - Modelou entidades e relações do domínio  
  - Implementou `LivroDAO` e `UsuarioDAO`  
  - Desenvolveu `CompraDAO` com transação única  
  - Criou `RelatorioService`  
  - Suporte à autenticação (`UsuarioService`)  
