/*
 * Servlet para gestionar AUTORES usando la API PHP
 */
package ec.edu.uta.grupo3.grupo3.servlets;

import ec.edu.uta.grupo3.grupo3.utils.ApiAutorCliente;
import ec.edu.uta.grupo3.grupo3.utils.ApiLibroCliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import java.io.IOException;

@WebServlet(name = "SvAutor", urlPatterns = {"/SvAutor"})
public class SvAutor extends HttpServlet {

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
            ok = ApiAutorCliente.crearAutor(
                    request.getParameter("AUT_NOM"),
                    request.getParameter("AUT_CORREO")
            );

            if (ok) {
                JSONArray autores = ApiAutorCliente.getAutores();
                JSONArray libros = ApiLibroCliente.getLibros();
                request.getSession(true).setAttribute("autores", autores);
                request.getSession(true).setAttribute("libros", libros);
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

        String id = request.getParameter("AUT_ID");
        String nom = request.getParameter("AUT_NOM");
        String correo = request.getParameter("AUT_CORREO");

        if (id == null || id.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta AUT_ID");
            return;
        }

        try {
            boolean ok = ApiAutorCliente.actualizarAutor(id, nom, correo);
            if (!ok) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se pudo actualizar el autor");
                return;
            }

            JSONArray autores = ApiAutorCliente.getAutores();
            JSONArray libros = ApiLibroCliente.getLibros();
            request.getSession().setAttribute("autores", autores);
            request.getSession().setAttribute("libros", libros);

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar autor");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String id = request.getParameter("AUT_ID");
        if (id == null || id.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta AUT_ID");
            return;
        }

        try {
            boolean ok = ApiAutorCliente.eliminarAutor(id);
            if (!ok) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No existe AUT_ID=" + id);
                return;
            }

            JSONArray autores = ApiAutorCliente.getAutores();
            JSONArray libros = ApiLibroCliente.getLibros();
            request.getSession().setAttribute("autores", autores);
            request.getSession().setAttribute("libros", libros);

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar autor");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String nombre = request.getParameter("nombre");
        JSONArray autores;

        if (nombre != null && !nombre.isBlank()) {
            autores = ApiAutorCliente.getAutoresPorNombre(nombre);
        } else {
            autores = ApiAutorCliente.getAutores();
        }

        JSONArray libros = ApiLibroCliente.getLibros();

        request.getSession().setAttribute("autores", autores);
        request.getSession().setAttribute("libros", libros);

        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestionar autores";
    }
}
