<?php
include_once("conexion.php");

class CRUDAutor {

    public static function listarAutores() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $sql = "SELECT * FROM autores";
        $resultado = $conectar->prepare($sql);
        $resultado->execute();

        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }

    public static function buscarPorNombre() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $nombre = isset($_GET['nombre']) ? trim($_GET['nombre']) : '';

        $sql = "SELECT * FROM autores WHERE AUT_NOM LIKE :nombre";
        $resultado = $conectar->prepare($sql);
        $resultado->bindValue(":nombre", "%" . $nombre . "%");
        $resultado->execute();

        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }

    public static function crearAutor() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $nombre = isset($_POST['AUT_NOM']) ? $_POST['AUT_NOM'] : '';
        $correo = isset($_POST['AUT_CORREO']) ? $_POST['AUT_CORREO'] : '';

        $sql = "INSERT INTO autores (AUT_NOM, AUT_CORREO)
                VALUES (:nombre, :correo)";

        $resultado = $conectar->prepare($sql);
        $resultado->bindParam(":nombre", $nombre);
        $resultado->bindParam(":correo", $correo);

        $ok = $resultado->execute();

        echo json_encode($ok ? "Autor creado correctamente" : "Error al crear autor");
    }

    public static function editarAutor() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $id     = isset($_GET['AUT_ID']) ? $_GET['AUT_ID'] : '';
        $nombre = isset($_GET['AUT_NOM']) ? $_GET['AUT_NOM'] : '';
        $correo = isset($_GET['AUT_CORREO']) ? $_GET['AUT_CORREO'] : '';

        $sql = "UPDATE autores
                SET AUT_NOM = :nombre,
                    AUT_CORREO = :correo
                WHERE AUT_ID = :id";

        $resultado = $conectar->prepare($sql);
        $resultado->bindParam(":nombre", $nombre);
        $resultado->bindParam(":correo", $correo);
        $resultado->bindParam(":id", $id, PDO::PARAM_INT);

        $ok = $resultado->execute();

        echo json_encode($ok ? "Autor actualizado correctamente" : "Error al actualizar autor");
    }

    public static function eliminarAutor() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $id = isset($_GET['AUT_ID']) ? $_GET['AUT_ID'] : '';

        $sql = "DELETE FROM autores WHERE AUT_ID = :id";
        $resultado = $conectar->prepare($sql);
        $resultado->bindParam(":id", $id, PDO::PARAM_INT);

        $ok = $resultado->execute();
        $filas = $resultado->rowCount();

        if ($ok && $filas > 0) {
            echo json_encode("Autor eliminado correctamente");
        } else {
            echo json_encode("No se encontró el autor");
        }
    }
}
?>
