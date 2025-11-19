<?php
class Conexion {

    public function conectar() {
        $server   = "localhost";
        $user     = "root";
        $password = "";
        $dataBase = "soa";

        try {
            $conn = new PDO("mysql:host=$server;dbname=$dataBase;charset=utf8", $user, $password);
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (Exception $e) {
            die("Fallo de conexión: " . $e->getMessage());
        }
        return $conn;
    }
}
?>
