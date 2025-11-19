<?php
include_once("conexion.php");

class CRUDLibro {

    public static function listarLibros() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $sql = "SELECT l.LIB_ID,
                       l.LIB_TITULO,
                       l.LIB_ANIO,
                       a.AUT_ID,
                       a.AUT_NOM,
                       a.AUT_CORREO
                  FROM libros l
                  INNER JOIN autores a ON l.AUT_ID = a.AUT_ID";
        $resultado = $conectar->prepare($sql);
        $resultado->execute();

        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }

    public static function buscarPorTitulo() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $titulo = isset($_GET['titulo']) ? trim($_GET['titulo']) : '';

        $sql = "SELECT l.LIB_ID,
                       l.LIB_TITULO,
                       l.LIB_ANIO,
                       a.AUT_ID,
                       a.AUT_NOM,
                       a.AUT_CORREO
                  FROM libros l
                  INNER JOIN autores a ON l.AUT_ID = a.AUT_ID
                 WHERE l.LIB_TITULO LIKE :titulo";
        $resultado = $conectar->prepare($sql);
        $resultado->bindValue(":titulo", "%" . $titulo . "%");
        $resultado->execute();

        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }

    public static function buscarPorAutor() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $nombre = isset($_GET['nombre']) ? trim($_GET['nombre']) : '';

        $sql = "SELECT l.LIB_ID,
                       l.LIB_TITULO,
                       l.LIB_ANIO,
                       a.AUT_ID,
                       a.AUT_NOM,
                       a.AUT_CORREO
                  FROM libros l
                  INNER JOIN autores a ON l.AUT_ID = a.AUT_ID
                 WHERE a.AUT_NOM LIKE :nombre";
        $resultado = $conectar->prepare($sql);
        $resultado->bindValue(":nombre", "%" . $nombre . "%");
        $resultado->execute();

        $data = $resultado->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($data);
    }

    public static function crearLibro() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $titulo = isset($_POST['LIB_TITULO']) ? $_POST['LIB_TITULO'] : '';
        $anio   = isset($_POST['LIB_ANIO']) ? $_POST['LIB_ANIO'] : '';
        $aut_id = isset($_POST['AUT_ID']) ? $_POST['AUT_ID'] : '';

        $sql = "INSERT INTO libros (LIB_TITULO, LIB_ANIO, AUT_ID)
                VALUES (:titulo, :anio, :aut_id)";

        $resultado = $conectar->prepare($sql);
        $resultado->bindParam(":titulo", $titulo);
        $resultado->bindParam(":anio", $anio, PDO::PARAM_INT);
        $resultado->bindParam(":aut_id", $aut_id, PDO::PARAM_INT);

        $ok = $resultado->execute();

        echo json_encode($ok ? "Libro creado correctamente" : "Error al crear libro");
    }

    public static function editarLibro() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $id     = isset($_GET['LIB_ID']) ? $_GET['LIB_ID'] : '';
        $titulo = isset($_GET['LIB_TITULO']) ? $_GET['LIB_TITULO'] : '';
        $anio   = isset($_GET['LIB_ANIO']) ? $_GET['LIB_ANIO'] : '';
        $aut_id = isset($_GET['AUT_ID']) ? $_GET['AUT_ID'] : '';

        $sql = "UPDATE libros
                SET LIB_TITULO = :titulo,
                    LIB_ANIO   = :anio,
                    AUT_ID     = :aut_id
                WHERE LIB_ID   = :id";

        $resultado = $conectar->prepare($sql);
        $resultado->bindParam(":titulo", $titulo);
        $resultado->bindParam(":anio", $anio, PDO::PARAM_INT);
        $resultado->bindParam(":aut_id", $aut_id, PDO::PARAM_INT);
        $resultado->bindParam(":id", $id, PDO::PARAM_INT);

        $ok = $resultado->execute();

        echo json_encode($ok ? "Libro actualizado correctamente" : "Error al actualizar libro");
    }

    public static function eliminarLibro() {
        $objetoConn = new Conexion();
        $conectar = $objetoConn->conectar();

        $id = isset($_GET['LIB_ID']) ? $_GET['LIB_ID'] : '';

        $sql = "DELETE FROM libros WHERE LIB_ID = :id";
        $resultado = $conectar->prepare($sql);
        $resultado->bindParam(":id", $id, PDO::PARAM_INT);

        $ok = $resultado->execute();
        $filas = $resultado->rowCount();

        if ($ok && $filas > 0) {
            echo json_encode("Libro eliminado correctamente");
        } else {
            echo json_encode("No se encontró el libro");
        }
    }
}
?>
