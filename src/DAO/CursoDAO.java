package DAO;
import model.Curso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private final Connection connection;

    public CursoDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Curso> obtenerTodosCursos() {
        List<Curso> cursos = new ArrayList<>();
        String query = "SELECT id, nombre, precio FROM cursos"; // Ajusta la tabla y columnas
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Curso curso = new Curso(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"));
                cursos.add(curso);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cursos: " + e.getMessage());
        }
        return cursos;
    }
    public List<Curso> obtenerCursosPorIds(List<Integer> idsCursos) {
        List<Curso> cursosSeleccionados = new ArrayList<>();
        List<Curso> todosLosCursos = obtenerTodosCursos(); // Método existente que devuelve todos los cursos.

        for (Curso curso : todosLosCursos) {
            if (idsCursos.contains(curso.getId())) {
                cursosSeleccionados.add(curso);
            }
        }

        return cursosSeleccionados;
    }

    // Otros métodos para obtener un curso por ID, añadir un nuevo curso (si fuera necesario), etc.
}