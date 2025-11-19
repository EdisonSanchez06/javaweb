package ec.edu.uta.grupo3.grupo3.utils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;

public class ApiAutorCliente {

    private static final String API_URL = "http://localhost/SOA/apiAutor.php";
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public static JSONArray getAutores() {
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

    public static JSONArray getAutoresPorNombre(String nombre) {
        try {
            String q = URLEncoder.encode(nombre, StandardCharsets.UTF_8);
            String url = API_URL + "?accion=buscar&nombre=" + q;

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

    public static boolean crearAutor(String nombre, String correo) {
        try {
            String body = "AUT_NOM=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8)
                        + "&AUT_CORREO=" + URLEncoder.encode(correo, StandardCharsets.UTF_8);

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

    public static boolean actualizarAutor(String id, String nombre, String correo) {
        try {
            String query = "AUT_ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8)
                         + "&AUT_NOM=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8)
                         + "&AUT_CORREO=" + URLEncoder.encode(correo, StandardCharsets.UTF_8);

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

    public static boolean eliminarAutor(String id) {
        try {
            String url = API_URL + "?AUT_ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8);

            HttpRequest reqDel = HttpRequest.newBuilder(URI.create(url))
                    .header("Accept", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> respDel = HTTP.send(reqDel, HttpResponse.BodyHandlers.ofString());
            if (respDel.statusCode() == 200 || respDel.statusCode() == 204) {
                return true;
            }

            String body = "AUT_ID=" + URLEncoder.encode(id, StandardCharsets.UTF_8) + "&_method=DELETE";
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
