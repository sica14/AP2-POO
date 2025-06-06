import dao.AvaliacaoDAO;
import dao.FilmeDAO;
import dao.UsuarioDAO;
import modelo.Avaliacao;
import modelo.Filme;
import modelo.Usuario;
import dao.Conexao;

import java.sql.Connection;
import java.util.ArrayList;

public class Principal {

    public static void main(String[] args) {

        Usuario u1 = new Usuario(0, "Alice", "alice@email.com", "123");
        Usuario u2 = new Usuario(0, "João", "joao@email.com", "456");
        Usuario u3 = new Usuario(0, "Carla", "carla@email.com", "789");

        Filme f1 = new Filme(0, "Inception", 2010, "Ficção");
        Filme f2 = new Filme(0, "Titanic", 1997, "Romance");
        Filme f3 = new Filme(0, "O Poderoso Chefão", 1972, "Drama");

        Avaliacao a1 = new Avaliacao(0, 0, 10, "Excelente filme!");
        Avaliacao a2 = new Avaliacao(0, 0, 8, "Um pouco longo, mas bom.");
        Avaliacao a3 = new Avaliacao(0, 0, 9, "Clássico absoluto.");

        Connection conn = Conexao.conectar();

        UsuarioDAO udao = new UsuarioDAO(conn);
        FilmeDAO fdao = new FilmeDAO(conn);
        AvaliacaoDAO adao = new AvaliacaoDAO(conn);

        udao.salvar(u1);
        udao.salvar(u2);
        udao.salvar(u3);

        fdao.salvar(f1);
        fdao.salvar(f2);
        fdao.salvar(f3);

        // Associações
        a1.setUsuarioId(u2.getId());
        a1.setFilmeId(f1.getId());

        a2.setUsuarioId(u1.getId());
        a2.setFilmeId(f2.getId());

        a3.setUsuarioId(u3.getId());
        a3.setFilmeId(f3.getId());

        adao.salvar(a1);
        adao.salvar(a2);
        adao.salvar(a3);

        System.out.println("### Avaliações com JOIN (Eager Loading)");
        ArrayList<Object> avaliacoes = adao.listarTodosEagerLoading();
        for (Object obj : avaliacoes) {
            System.out.println(obj);
        }

        System.out.println("\n### Atualizando nome do usuário e nota da avaliação");
        u1.setNome("Alice Atualizada");
        udao.atualizar(u1);

        a2.setNota(9);
        a2.setComentario("Na verdade, achei ótimo!");
        adao.atualizar(a2);

        System.out.println("\n### Avaliações após atualização:");
        for (Object obj : adao.listarTodosEagerLoading()) {
            System.out.println(obj);
        }

        System.out.println("\n### Excluindo uma avaliação...");
        adao.excluir(a3.getId());

        System.out.println("\n### Avaliações após exclusão:");
        for (Object obj : adao.listarTodosEagerLoading()) {
            System.out.println(obj);
        }
    }
}