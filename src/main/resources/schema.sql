INSERT INTO room_type (id_room_type, places, title, area, description, price) VALUES
                                                                                  (1, 2, 'Стандарт', 20.0, 'Простая стандартная комната', 7000.0),
                                                                                  (2, 2, 'Комфорт', 30.0, 'Просторная комната с удобствами', 1260.0),
                                                                                  (3, 4, 'Люкс', 50.0, 'Роскошный люкс с видом на город', 20000.0)
    ON DUPLICATE KEY UPDATE places=VALUES(places), title=VALUES(title),
    area=VALUES(area), description=VALUES(description), price=VALUES(price);


INSERT INTO booking_type (id_booking, description, discount, type) VALUES
    (1, 'Базовое бронирование с минимальными удобствами для кратковременного пребывания.', 2, 'Стандарт'),
    (2, 'Специальное предложение для празднования дня рождения с дополнительными услугами и подарками.', 2, 'День рождения'),
    (3, 'Уютное семейное проживание с расширенными удобствами для комфортного пребывания всей семьей.', 4, 'Семейное проживание')
ON DUPLICATE KEY UPDATE
    type = VALUES(type),
    description = VALUES(description);


INSERT INTO service (id_room_type, enabled, price, title) VALUES
                                                              (1, 1, 0, 'Телевизор'),
                                                              (2, 1, 0, 'WI-FI'),
                                                              (3, 1, 0, 'Минибар'),
                                                              (4, 1, 0, 'Телефон'),
                                                              (5, 1, 0, 'Чайник'),
                                                              (6, 1, 0, 'Душевая кабина'),
                                                              (7, 1, 0, 'Ванная'),
                                                              (8, 1, 0, 'Кондиционер'),
                                                              (9, 1, 0, 'Гардеробная'),
                                                              (10, 1, 0, 'Сейф'),
                                                              (11, 1, 0, 'Вид на город'),
                                                              (12, 1, 0, 'Вид на двор'),
                                                              (13, 1, 0, 'Гостинная'),
                                                              (14, 1, 0, 'Дополнительный санузел')
    ON DUPLICATE KEY UPDATE enabled=VALUES(enabled), price=VALUES(price), title=VALUES(title);

INSERT INTO status (id_status, title)
VALUES
    (1, 'Свободен'),
    (2, 'Забронирован'),
    (3, 'Активна'),
    (4, 'Отменена'),
    (5, 'Завершена'),
    (6, 'На рассмотрении')
    ON DUPLICATE KEY UPDATE
                         title=VALUES(title);
