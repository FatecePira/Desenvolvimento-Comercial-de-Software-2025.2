
package atd1dcomersoft;

/**
 *
 * @author yasmi
 */
public class LivrariaOnlineMain  {
/**
* Método main - ponto de entrada do sistema
     * @param args
*/
public static void main(String[] args) {
System.out.println("=== INICIANDO SISTEMA DE GESTÃO DE LIVRARIA ONLINE ===");
System.out.println("Sistema desenvolvido aplicando conceitos de OOP:");
System.out.println("✓ Encapsulamento");
System.out.println("✓ Herança (Usuario -> Cliente/Administrador)");
System.out.println("✓ Polimorfismo (método exibirInformacoes())");
System.out.println("✓ Abstração (classe abstrata Usuario)");
System.out.println();

    SistemaLivraria sistema = new SistemaLivraria();
    sistema.menuPrincipal();
}
}
