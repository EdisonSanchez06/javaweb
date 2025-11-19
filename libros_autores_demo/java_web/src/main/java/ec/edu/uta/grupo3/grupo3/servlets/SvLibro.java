/*
 * Servlet para gestionar LIBROS usando la API PHP
 */
package ec.edu.uta.grupo3.grupo3.servlets;

import ec.edu.uta.grupo3.grupo3.utils.ApiLibroCliente;
import ec.edu.uta.grupo3.grupo3.utils.ApiAutorCliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import java.io.IOException;

@WebServlet(name = "SvLibro", urlPatterns = {"/SvLibro"})
public class SvLibro extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String override = request.getHeader("X-HTTP-Method-Override");
        if (override == null) {
            override = request.getParameter("_method");
        }

        if ("DELETE".equalsIgnoreCase(override)) {
            doDelete(request, response);
            return;
        }
        if ("PUT".equalsIgnoreCase(override)) {
            doPut(request, response);
            return;
        }

        boolean ok = false;
        try {
            ok = ApiLibroCliente.crearLibro(
                    request.getParameter("LIB_TITULO"),
                    request.getParameter("LIB_ANIO"),
                    request.getParameter("AUT_ID")
            );

            if (ok) {
                JSONArray libros = ApiLibroCliente.getLibros();
                JSONArray autores = ApiAutorCliente.getAutores();
                request.getSession(true).setAttribute("libros", libros);
                request.getSession(true).setAttribute("autores", autores);
            }
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String id = request.getParameter("LIB_ID");
        String titulo = request.getParameter("LIB_TITULO");
        String anio = request.getParameter("LIB_ANIO");
        String autId = request.getParameter("AUT_ID");

        if (id == null || id.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta LIB_ID");
            return;
        }

        try {
            boolean ok = ApiLibroCliente.actualizarLibro(id, titulo, anio, autId);
            if (!ok) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se pudo actualizar el libro");
                return;
            }

            JSONArray libros = ApiLibroCliente.getLibros();
            JSONArray autores = ApiAutorCliente.getAutores();
            request.getSession().setAttribute("libros", libros);
            request.getSession().setAttribute("autores", autores);

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar libro");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String id = request.getParameter("LIB_ID");
        if (id == null || id.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta LIB_ID");
            return;
        }

        try {
            boolean ok = ApiLibroCliente.eliminarLibro(id);
            if (!ok) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No existe LIB_ID=" + id);
                return;
            }

            JSONArray libros = ApiLibroCliente.getLibros();
            JSONArray autores = ApiAutorCliente.getAutores();
            request.getSession().setAttribute("libros", libros);
            request.getSession().setAttribute("autores", autores);

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar libro");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String titulo = request.getParameter("titulo");
        String autorNombre = request.getParameter("autor");

        JSONArray libros;
        if (titulo != null && !titulo.isBlank()) {
            libros = ApiLibroCliente.getLibrosPorTitulo(titulo);
        } else if (autorNombre != null && !autorNombre.isBlank()) {
            libros = ApiLibroCliente.getLibrosPorAutor(autorNombre);
        } else {
            libros = ApiLibroCliente.getLibros();
        }

        JSONArray autores = ApiAutorCliente.getAutores();

        request.getSession().setAttribute("libros", libros);
        request.getSession().setAttribute("autores", autores);

        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestionar libros";
    }
}
