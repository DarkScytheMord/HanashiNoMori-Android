-- ============================================
-- SCRIPT DE INICIALIZACIÓN - HANASHINOMORI
-- Ejecutar DESPUÉS de que Spring Boot cree las tablas
-- ============================================

USE hanashinomori;

-- ============================================
-- LIMPIAR DATOS EXISTENTES (OPCIONAL)
-- Descomentar solo si quieres empezar desde cero
-- ============================================

-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE favorites;
-- TRUNCATE TABLE books;
-- TRUNCATE TABLE users;
-- SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- INSERTAR 30 LIBROS (10 Manga, 10 Manhwa, 10 Donghua)
-- ============================================

-- ====================
-- MANGA (10 libros)
-- ====================

INSERT INTO books (title, author, category, description, cover_url, isbn) VALUES
(
  'Naruto',
  'Masashi Kishimoto',
  'Manga',
  'Naruto Uzumaki es un joven ninja que busca reconocimiento de sus compañeros y sueña con convertirse en el Hokage, el líder de su aldea. La historia sigue sus aventuras mientras entrena para convertirse en el ninja más fuerte y proteger a sus seres queridos.',
  'https://m.media-amazon.com/images/I/81KLvXuB+rL._AC_UF894,1000_QL80_.jpg',
  '9781421500720'
),
(
  'One Piece',
  'Eiichiro Oda',
  'Manga',
  'Monkey D. Luffy y su tripulación pirata navegan por el Grand Line en busca del legendario tesoro One Piece para que Luffy se convierta en el Rey de los Piratas. En su camino, enfrentan enemigos poderosos, hacen nuevos amigos y descubren los secretos del mundo.',
  'https://m.media-amazon.com/images/I/81QPHThrE8L._AC_UF894,1000_QL80_.jpg',
  '9781421536255'
),
(
  'Attack on Titan',
  'Hajime Isayama',
  'Manga',
  'La humanidad vive dentro de enormes murallas para protegerse de los titanes, gigantes humanoides que devoran personas sin razón aparente. Eren Yeager jura venganza después de que un titán destruye su ciudad natal y devora a su madre, uniéndose al ejército para luchar contra ellos.',
  'https://m.media-amazon.com/images/I/81OSiWKV88L._AC_UF894,1000_QL80_.jpg',
  '9781612620244'
),
(
  'Death Note',
  'Tsugumi Ohba',
  'Manga',
  'Light Yagami encuentra un cuaderno sobrenatural que le permite matar a cualquiera cuyo nombre escriba en él. Decide usar el Death Note para crear un mundo sin criminales, pero pronto es perseguido por el brillante detective L en un juego mental de proporciones épicas.',
  'https://m.media-amazon.com/images/I/71SjJH0a3iL._AC_UF894,1000_QL80_.jpg',
  '9781421501376'
),
(
  'My Hero Academia',
  'Kohei Horikoshi',
  'Manga',
  'En un mundo donde el 80% de la población tiene superpoderes llamados "quirks", Izuku Midoriya nace sin ninguno. Sin embargo, su determinación lo lleva a heredar el quirk del héroe número uno, All Might, y cumplir su sueño de convertirse en un gran héroe.',
  'https://m.media-amazon.com/images/I/81gPcoGsCSL._AC_UF894,1000_QL80_.jpg',
  '9781421582696'
),
(
  'Demon Slayer',
  'Koyoharu Gotouge',
  'Manga',
  'Tanjiro Kamado se convierte en un cazador de demonios después de que su familia es asesinada y su hermana Nezuko es convertida en demonio. Busca una cura mientras lucha contra demonios poderosos y protege a los inocentes de las criaturas de la noche.',
  'https://m.media-amazon.com/images/I/81tW1-sKGsL._AC_UF894,1000_QL80_.jpg',
  '9781974700523'
),
(
  'Tokyo Ghoul',
  'Sui Ishida',
  'Manga',
  'Ken Kaneki es un estudiante tímido que se convierte en un híbrido mitad humano, mitad ghoul después de un encuentro mortal. Debe aprender a vivir entre ambos mundos mientras lucha por mantener su humanidad y enfrentar amenazas de ambos lados.',
  'https://m.media-amazon.com/images/I/81gMPrZpXbL._AC_UF894,1000_QL80_.jpg',
  '9781421580364'
),
(
  'Fullmetal Alchemist',
  'Hiromu Arakawa',
  'Manga',
  'Los hermanos Edward y Alphonse Elric intentan usar la alquimia para resucitar a su madre, pero el experimento sale mal. Edward pierde su brazo y pierna, mientras que Alphonse pierde todo su cuerpo. Ahora buscan la Piedra Filosofal para recuperar lo que perdieron.',
  'https://m.media-amazon.com/images/I/91R+5zGt9QL._AC_UF894,1000_QL80_.jpg',
  '9781591169208'
),
(
  'Bleach',
  'Tite Kubo',
  'Manga',
  'Ichigo Kurosaki obtiene accidentalmente los poderes de un Shinigami (segador de almas) y debe asumir los deberes de defender a los humanos de los espíritus malignos y guiar las almas departidas al más allá, mientras descubre secretos sobre su propio pasado.',
  'https://m.media-amazon.com/images/I/81tTFwN6JSL._AC_UF894,1000_QL80_.jpg',
  '9781421506197'
),
(
  'Jujutsu Kaisen',
  'Gege Akutami',
  'Manga',
  'Yuji Itadori se une a una escuela de hechicería después de tragar un dedo maldito para salvar a sus amigos. Ahora alberga al Rey de las Maldiciones, Sukuna, en su cuerpo y debe aprender a controlarlo mientras lucha contra espíritus malignos y maldiciones peligrosas.',
  'https://m.media-amazon.com/images/I/81PGR+t7RoL._AC_UF894,1000_QL80_.jpg',
  '9781974710027'
);

-- ====================
-- MANHWA (10 libros)
-- ====================

INSERT INTO books (title, author, category, description, cover_url, isbn) VALUES
(
  'Solo Leveling',
  'Chugong',
  'Manhwa',
  'Sung Jin-Woo es el cazador de rango E más débil de la humanidad en un mundo donde aparecen mazmorras llenas de monstruos. Después de sobrevivir milagrosamente a una mazmorra de doble mazmorra, obtiene el poder de subir de nivel sin límites. Ahora se embarca en un viaje para convertirse en el cazador más fuerte del mundo.',
  'https://m.media-amazon.com/images/I/81FKLMboZQL._AC_UF894,1000_QL80_.jpg',
  '9791138244014'
),
(
  'Tower of God',
  'SIU',
  'Manhwa',
  'Twenty-Fifth Bam entra a la Torre, una estructura misteriosa donde aquellos que llegan a la cima pueden tener cualquier cosa que deseen. Su único objetivo es encontrar a Rachel, la única persona importante en su vida que decidió escalar la Torre sin él.',
  'https://m.media-amazon.com/images/I/71+PbH5YLIL._AC_UF894,1000_QL80_.jpg',
  '9791138341653'
),
(
  'The Beginning After The End',
  'TurtleMe',
  'Manhwa',
  'El rey Grey posee una fuerza, riqueza y prestigio sin igual en un mundo gobernado por la habilidad marcial. Sin embargo, la soledad acompaña secretamente a su poder. Tras reencarnar en un nuevo mundo lleno de magia y monstruos, le es dada una segunda oportunidad para corregir sus errores del pasado.',
  'https://m.media-amazon.com/images/I/81Xd7D+iFoL._AC_UF894,1000_QL80_.jpg',
  '9791138341660'
),
(
  'Omniscient Readers Viewpoint',
  'Sing Shong',
  'Manhwa',
  'Kim Dokja es el único lector que ha terminado la novela web "Tres Formas de Sobrevivir en un Mundo Ruinado" de 3,149 capítulos. Cuando el mundo se convierte en ese mismo libro, él es el único que conoce cómo terminará la historia. Ahora debe usar ese conocimiento para sobrevivir y cambiar el destino.',
  'https://m.media-amazon.com/images/I/81kO5RQJQTL._AC_UF894,1000_QL80_.jpg',
  '9791138341677'
),
(
  'Noblesse',
  'Son Jeho',
  'Manhwa',
  'Cadis Etrama Di Raizel, un noble vampiro de noble rango, despierta después de 820 años de sueño. Se encuentra en la era moderna y debe adaptarse a este nuevo mundo mientras protege a sus nuevos amigos humanos de organizaciones misteriosas que buscan poder.',
  'https://m.media-amazon.com/images/I/81Y8aJ0MJHL._AC_UF894,1000_QL80_.jpg',
  '9791138341684'
),
(
  'The God of High School',
  'Yongje Park',
  'Manhwa',
  'Un torneo épico llamado "The God of High School" invita a los mejores luchadores de secundaria de Corea. Los ganadores obtienen lo que deseen, pero hay un misterio oculto detrás del torneo que involucra dioses, poderes sobrenaturales y el destino del mundo.',
  'https://m.media-amazon.com/images/I/81uZ8ZYJ9KL._AC_UF894,1000_QL80_.jpg',
  '9791138341691'
),
(
  'Eleceed',
  'Son Jeho',
  'Manhwa',
  'Jiwoo es un joven amable con reflejos increíblemente rápidos y la habilidad de controlar la electricidad. Después de salvar a un gato herido que resulta ser un poderoso despertador en forma felina, su vida cambia para siempre cuando entra al mundo de los despertadores.',
  'https://m.media-amazon.com/images/I/81V0pHl2UJL._AC_UF894,1000_QL80_.jpg',
  '9791138341707'
),
(
  'The Breaker',
  'Jeon Geuk-jin',
  'Manhwa',
  'Shi-Woon Yi es un estudiante de secundaria tímido y constantemente intimidado. Su vida cambia cuando conoce a su nuevo maestro sustituto, Han Chun-Woo, quien resulta ser un maestro de artes marciales. Shi-Woon le suplica que lo entrene para poder defenderse.',
  'https://m.media-amazon.com/images/I/81ZqJYvH+8L._AC_UF894,1000_QL80_.jpg',
  '9791138341714'
),
(
  'Hardcore Leveling Warrior',
  'Sehoon Kim',
  'Manhwa',
  'En el juego de realidad virtual Lucid Adventure, Hardcore Leveling Warrior es el jugador número 1. Sin embargo, es traicionado y pierde todo, cayendo al nivel 1. Ahora debe subir de nivel nuevamente desde cero mientras busca venganza contra quien lo traicionó.',
  'https://m.media-amazon.com/images/I/81PXGjYdRZL._AC_UF894,1000_QL80_.jpg',
  '9791138341721'
),
(
  'Sweet Home',
  'Carnby Kim',
  'Manhwa',
  'Después de una tragedia familiar, el solitario estudiante de secundaria Hyun Cha se muda a un nuevo apartamento. Cuando los humanos comienzan a convertirse en monstruos que reflejan sus deseos más oscuros, Hyun y los otros residentes deben sobrevivir en este nuevo y aterrador mundo.',
  'https://m.media-amazon.com/images/I/81V4fF7VYXL._AC_UF894,1000_QL80_.jpg',
  '9791138341738'
);

-- ====================
-- DONGHUA (10 libros)
-- ====================

INSERT INTO books (title, author, category, description, cover_url, isbn) VALUES
(
  'Mo Dao Zu Shi',
  'Mo Xiang Tong Xiu',
  'Donghua',
  'Wei Wuxian, el fundador de la cultivación demoníaca y el Patriarca Yiling, resucita 13 años después de su muerte en el cuerpo de un lunático. Se reúne con Lan Wangji, su antiguo compañero, para resolver misterios sobrenaturales mientras descubren la verdad sobre su pasado y enfrentan enemigos del presente.',
  'https://m.media-amazon.com/images/I/71VuCvj3QoL._AC_UF894,1000_QL80_.jpg',
  '9787559456458'
),
(
  'Heaven Officials Blessing',
  'Mo Xiang Tong Xiu',
  'Donghua',
  'Xie Lian, un príncipe que ascendió a la divinidad, es desterrado del cielo por tercera vez después de 800 años. Mientras investiga una serie de misteriosas desapariciones causadas por un fantasma en el reino mortal, conoce a un joven misterioso llamado San Lang que guarda un secreto increíble.',
  'https://m.media-amazon.com/images/I/81PXGjYdRZL._AC_UF894,1000_QL80_.jpg',
  '9787559468734'
),
(
  'The Kings Avatar',
  'Butterfly Blue',
  'Donghua',
  'Ye Xiu, un campeón de esports de alto nivel y capitán de equipo, es forzado a retirarse del juego profesional Glory. Sin embargo, no se rinde y comienza desde cero en un nuevo servidor del juego con un nuevo personaje, Glory, con el objetivo de regresar a la cima de la gloria profesional.',
  'https://m.media-amazon.com/images/I/71nU2CgQ3mL._AC_UF894,1000_QL80_.jpg',
  '9787559459824'
),
(
  'Scissor Seven',
  'He Xiaofeng',
  'Donghua',
  'Seven es un peluquero amnésico que trabaja como asesino a sueldo en la isla Pequeño Pollo, pero es terrible en su trabajo. Usa unas tijeras como arma y puede cambiar de forma. Mientras busca recuperar sus memorias perdidas, se involucra en situaciones cómicas y peligrosas.',
  'https://m.media-amazon.com/images/I/81V4fF7VYXL._AC_UF894,1000_QL80_.jpg',
  '9787559462341'
),
(
  'Link Click',
  'Li Haoling',
  'Donghua',
  'Cheng Xiaoshi y Lu Guang dirigen un estudio fotográfico con un secreto: pueden viajar al pasado a través de fotografías. Usan este poder para ayudar a clientes con asuntos pendientes, pero pronto descubren que cambiar el pasado tiene consecuencias peligrosas que afectan el presente.',
  'https://m.media-amazon.com/images/I/81qVw5DqLvL._AC_UF894,1000_QL80_.jpg',
  '9787559463852'
),
(
  'Hitori no Shita: The Outcast',
  'Dong Man Tang',
  'Donghua',
  'Zhang Chulan es un joven aparentemente ordinario hasta que es atacado por zombis en el cementerio de su abuelo. Descubre que pertenece a una sociedad secreta de personas con habilidades especiales llamadas "Inhumanos" y busca la verdad sobre el misterioso poder de su abuelo.',
  'https://m.media-amazon.com/images/I/81Z1bJ7PZRL._AC_UF894,1000_QL80_.jpg',
  '9787559465791'
),
(
  'Soul Land',
  'Tang Jia San Shao',
  'Donghua',
  'Tang San es un maestro del arte de las armas ocultas de la secta Tang del mundo marcial. Después de su muerte por robar las habilidades prohibidas de la secta, renace en un mundo de cultivación marcial llamado Douluo Dalu donde debe adaptarse y volverse más fuerte para proteger a sus seres queridos.',
  'https://m.media-amazon.com/images/I/81iYs6qJVzL._AC_UF894,1000_QL80_.jpg',
  '9787559467234'
),
(
  'Battle Through the Heavens',
  'Tian Can Tu Dou',
  'Donghua',
  'Xiao Yan era un genio de la cultivación, pero misteriosamente perdió todos sus poderes. Descubre que un espíritu ancestral viviendo en su anillo robó su poder. Ahora debe entrenar desde cero con la ayuda de este espíritu para recuperar su fuerza y proteger a su familia.',
  'https://m.media-amazon.com/images/I/81Y8aJ0MJHL._AC_UF894,1000_QL80_.jpg',
  '9787559468123'
),
(
  'Fog Hill of Five Elements',
  'Lin Hun',
  'Donghua',
  'En un mundo donde los cinco elementos son la base de todo poder, un grupo de guerreros debe proteger su tierra de invasores monstruosos. La animación fluida y el diseño de combate innovador hacen de esta serie una experiencia visual espectacular con una historia profunda sobre lealtad y sacrificio.',
  'https://m.media-amazon.com/images/I/81V0pHl2UJL._AC_UF894,1000_QL80_.jpg',
  '9787559469567'
),
(
  'The Daily Life of the Immortal King',
  'Kuxuan',
  'Donghua',
  'Wang Ling es un estudiante de secundaria extraordinariamente poderoso que solo quiere vivir una vida tranquila. A pesar de sus intentos por parecer normal, constantemente se ve envuelto en situaciones peligrosas donde debe usar sus poderes de cultivador para salvar el día sin revelar su verdadera fuerza.',
  'https://m.media-amazon.com/images/I/81PXGjYdRZL._AC_UF894,1000_QL80_.jpg',
  '9787559470234'
);

-- ============================================
-- VERIFICACIÓN
-- ============================================

-- Contar total de libros
SELECT
    'Total de libros insertados' as descripcion,
    COUNT(*) as cantidad
FROM books;

-- Contar por categoría
SELECT
    category as categoria,
    COUNT(*) as cantidad
FROM books
GROUP BY category
ORDER BY category;

-- Ver primeros 5 libros de cada categoría
SELECT
    id,
    title as titulo,
    author as autor,
    category as categoria
FROM books
ORDER BY category, id
LIMIT 15;

-- ============================================
-- RESULTADO ESPERADO
-- ============================================
-- Total: 30 libros
-- Manga: 10
-- Manhwa: 10
-- Donghua: 10

