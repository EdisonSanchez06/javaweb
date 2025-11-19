package ec.edu.uta.grupo3.grupo3.utils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;

public class ApiLibroCliente {

    private static final String API_URL = "http://localhost/SOA/apiLibro.php";
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public static JSONArray getLibros() {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(API_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            String body = resp.body();
            return new JSONArray(body);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONArray getLibrosPorTitulo(String titulo) {
        try {
            String q = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String url = API_URL + "?accion=buscar_titulo&titulo=" + q;

            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            String body = resp.body();
            return new JSONArray(body);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONArray getLibrosPorAutor(String nombreAutor) {
        try {
            String q = URLEncoder.encode(nombreAutor, StandardCharsets.UTF_8);
            String url = API_URL + "?accion=buscar_autor&nombre=" + q;

            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            String body = resp.body();
            return new JSONArray(body);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static boolean crearLibro(String titulo, String anio, String autId) {
        try {
            String body = "LIB_TITULO=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8)
                        + "&LIB_ANIO=" + URLEncoder.encode(anio, StandardCharsets.UTF_8)
                        + "&AUT_ID=" + URLEncoder.encode(autId, StandardCharsets.UTF_8);

            HttpRequest req = HttpRequest.newBuilder(URI.create(API_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200 || resp.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarLibro(String id, String titulo, String anio, String autId) {
        try {
            String query = "LIB_ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8)
                         + "&LIB_TITULO=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8)
                         + "&LIB_ANIO=" + URLEncoder.encode(anio, StandardCharsets.UTF_8)
                         + "&AUT_ID=" + URLEncoder.encode(autId, StandardCharsets.UTF_8);

            String url = API_URL + "?" + query;

            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .method("PUT", HttpRequest.BodyPublishers.noBody())
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200 || resp.statusCode() == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarLibro(String id) {
        try {
            String url = API_URL + "?LIB_ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8);

            HttpRequest reqDel = HttpRequest.newBuilder(URI.create(url))
                    .header("Accept", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> respDel = HTTP.send(reqDel, HttpResponse.BodyHandlers.ofString());
            if (respDel.statusCode() == 200 || respDel.statusCode() == 204) {
                return true;
            }

            String body = "LIB_ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8) + "&_method=DELETE";
            HttpRequest reqPost = HttpRequest.newBuilder(URI.create(API_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> respPost = HTTP.send(reqPost, HttpResponse.BodyHandlers.ofString());
            return respPost.statusCode() == 200 || respPost.statusCode() == 204;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
