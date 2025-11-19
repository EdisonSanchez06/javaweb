<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

include_once("crud_libro.php");

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

$method = $_SERVER['REQUEST_METHOD'];
$accion = isset($_GET['accion']) ? $_GET['accion'] : '';

switch ($method) {
    case 'GET':
        if ($accion === 'buscar_titulo') {
            CRUDLibro::buscarPorTitulo();
        } elseif ($accion === 'buscar_autor') {
            CRUDLibro::buscarPorAutor();
        } else {
            CRUDLibro::listarLibros();
        }
        break;

    case 'POST':
        CRUDLibro::crearLibro();
        break;

    case 'PUT':
        CRUDLibro::editarLibro();
        break;

    case 'DELETE':
        CRUDLibro::eliminarLibro();
        break;

    default:
        http_response_code(405);
        echo json_encode(["error" => "Método no permitido"]);
}
?>
