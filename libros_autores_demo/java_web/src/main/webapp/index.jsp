<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (request.getSession(false) == null
            || (request.getSession(false).getAttribute("autores") == null
            && request.getSession(false).getAttribute("libros") == null)) {
        response.sendRedirect(request.getContextPath() + "/SvAutor");
        return;
    }

    JSONArray autores = (JSONArray) session.getAttribute("autores");
    JSONArray libros = (JSONArray) session.getAttribute("libros");
    if (autores == null) autores = new JSONArray();
    if (libros == null) libros = new JSONArray();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Autores y Libros</title>
    </head>
    <body>
        <h1>Gestión de Autores y Libros</h1>

        <h2>Autores</h2>

        <h3>Buscar autor por nombre</h3>
        <form method="get" action="${pageContext.request.contextPath}/SvAutor">
            Nombre: <input type="text" name="nombre">
            <input type="submit" value="Buscar">
        </form>

        <h3>Crear autor</h3>
        <form method="post" action="${pageContext.request.contextPath}/SvAutor">
            Nombre: <input type="text" name="AUT_NOM" required><br>
            Correo: <input type="text" name="AUT_CORREO" required><br>
            <input type="submit" value="Guardar autor">
        </form>

        <h3>Lista de autores</h3>
        <table border="1" cellpadding="4" cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Correo</th>
                <th>Acciones</th>
            </tr>
            <%
                for (int i = 0; i < autores.length(); i++) {
                    JSONObject a = autores.getJSONObject(i);
            %>
            <tr>
                <td><%= a.optInt("AUT_ID") %></td>
                <td><%= a.optString("AUT_NOM") %></td>
                <td><%= a.optString("AUT_CORREO") %></td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/SvAutor" style="display:inline;">
                        <input type="hidden" name="_method" value="PUT">
                        <input type="hidden" name="AUT_ID" value="<%= a.optInt("AUT_ID") %>">
                        Nombre: <input type="text" name="AUT_NOM" value="<%= a.optString("AUT_NOM") %>">
                        Correo: <input type="text" name="AUT_CORREO" value="<%= a.optString("AUT_CORREO") %>">
                        <input type="submit" value="Editar">
                    </form>

                    <form method="post" action="${pageContext.request.contextPath}/SvAutor" style="display:inline;">
                        <input type="hidden" name="_method" value="DELETE">
                        <input type="hidden" name="AUT_ID" value="<%= a.optInt("AUT_ID") %>">
                        <input type="submit" value="Eliminar"
                               onclick="return confirm('¿Eliminar autor ID=<%= a.optInt("AUT_ID") %>?');">
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </table>

        <hr>

        <h2>Libros</h2>

        <h3>Buscar libro por título</h3>
        <form method="get" action="${pageContext.request.contextPath}/SvLibro">
            Título: <input type="text" name="titulo">
            <input type="submit" value="Buscar">
        </form>

        <h3>Buscar libro por autor</h3>
        <form method="get" action="${pageContext.request.contextPath}/SvLibro">
            Nombre Autor: <input type="text" name="autor">
            <input type="submit" value="Buscar">
        </form>

        <h3>Crear libro</h3>
        <form method="post" action="${pageContext.request.contextPath}/SvLibro">
            Título: <input type="text" name="LIB_TITULO" required><br>
            Año: <input type="text" name="LIB_ANIO" required><br>
            ID Autor: <input type="text" name="AUT_ID" required><br>
            <input type="submit" value="Guardar libro">
        </form>

        <h3>Lista de libros</h3>
        <table border="1" cellpadding="4" cellspacing="0">
            <tr>
                <th>ID Libro</th>
                <th>Título</th>
                <th>Año</th>
                <th>ID Autor</th>
                <th>Nombre Autor</th>
                <th>Correo Autor</th>
                <th>Acciones</th>
            </tr>
            <%
                for (int i = 0; i < libros.length(); i++) {
                    JSONObject l = libros.getJSONObject(i);
            %>
            <tr>
                <td><%= l.optInt("LIB_ID") %></td>
                <td><%= l.optString("LIB_TITULO") %></td>
                <td><%= l.optInt("LIB_ANIO") %></td>
                <td><%= l.optInt("AUT_ID") %></td>
                <td><%= l.optString("AUT_NOM") %></td>
                <td><%= l.optString("AUT_CORREO") %></td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/SvLibro" style="display:inline;">
                        <input type="hidden" name="_method" value="PUT">
                        <input type="hidden" name="LIB_ID" value="<%= l.optInt("LIB_ID") %>">
                        Título: <input type="text" name="LIB_TITULO" value="<%= l.optString("LIB_TITULO") %>">
                        Año: <input type="text" name="LIB_ANIO" value="<%= l.optInt("LIB_ANIO") %>">
                        ID Autor: <input type="text" name="AUT_ID" value="<%= l.optInt("AUT_ID") %>">
                        <input type="submit" value="Editar">
                    </form>

                    <form method="post" action="${pageContext.request.contextPath}/SvLibro" style="display:inline;">
                        <input type="hidden" name="_method" value="DELETE">
                        <input type="hidden" name="LIB_ID" value="<%= l.optInt("LIB_ID") %>">
                        <input type="submit" value="Eliminar"
                               onclick="return confirm('¿Eliminar libro ID=<%= l.optInt("LIB_ID") %>?');">
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </table>

    </body>
</html>
