-- 🔍 SCRIPT DE VERIFICACIÓN RÁPIDA - MYSQL

-- ============================================
-- EJECUTAR EN MYSQL WORKBENCH O TERMINAL
-- ============================================

-- PASO 1: Ver todas las bases de datos
SHOW DATABASES;
-- ¿Aparece 'hanashinomori'? → SI continuar / NO crearla

-- PASO 2: Usar la base de datos
USE hanashinomori;

-- PASO 3: Ver todas las tablas
SHOW TABLES;
-- ¿Aparece 'books'? → SI continuar / NO crearla

-- PASO 4: Ver estructura de la tabla books
DESCRIBE books;
-- Debe tener: id, title, author, category, description, cover_url

-- PASO 5: Contar cuántos libros hay
SELECT COUNT(*) FROM books;
-- Si devuelve 0 → No hay datos, cargar abajo

-- PASO 6: Ver los libros que existen
SELECT id, title, category FROM books LIMIT 10;

-- ============================================
-- SI LA TABLA NO EXISTE, CREAR CON ESTO:
-- ============================================

/*
CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    cover_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
*/

-- ============================================
-- SI NO HAY DATOS, INSERTAR ESTOS 5 LIBROS:
-- ============================================

/*
INSERT INTO books (title, author, category, description, cover_url) VALUES
('Naruto', 'Masashi Kishimoto', 'Manga', 'Historia de un ninja que busca ser Hokage', 'https://m.media-amazon.com/images/I/81KLvXuB+rL._AC_UF894,1000_QL80_.jpg'),
('One Piece', 'Eiichiro Oda', 'Manga', 'Aventuras de piratas en busca del tesoro One Piece', 'https://m.media-amazon.com/images/I/81QPHThrE8L._AC_UF894,1000_QL80_.jpg'),
('Solo Leveling', 'Chugong', 'Manhwa', 'Un cazador débil se convierte en el más fuerte', 'https://m.media-amazon.com/images/I/81FKLMboZQL._AC_UF894,1000_QL80_.jpg'),
('Mo Dao Zu Shi', 'Mo Xiang Tong Xiu', 'Donghua', 'Cultivador reencarnado descubre misterios', 'https://m.media-amazon.com/images/I/71VuCvj3QoL._AC_UF894,1000_QL80_.jpg'),
('Attack on Titan', 'Hajime Isayama', 'Manga', 'Humanidad lucha contra titanes gigantes', 'https://m.media-amazon.com/images/I/81OSiWKV88L._AC_UF894,1000_QL80_.jpg');
*/

-- ============================================
-- VERIFICACIÓN FINAL
-- ============================================

SELECT
    'Total de libros' as verificacion,
    COUNT(*) as cantidad
FROM books;

SELECT
    category,
    COUNT(*) as cantidad
FROM books
GROUP BY category;

-- ============================================
-- SI EL BACKEND BUSCA 'media' EN VEZ DE 'books'
-- ============================================

-- Ver si existe tabla 'media'
SHOW TABLES LIKE 'media';

-- Si existe, renombrar:
-- ALTER TABLE media RENAME TO books;

-- O verificar nombre exacto:
SHOW TABLES;

