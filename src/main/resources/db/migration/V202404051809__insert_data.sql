INSERT INTO role (id, title)
VALUES (1, 'ROLE_ADMIN');

INSERT INTO "user" (email, password, photo_url, role_id)
VALUES ('admin@gmail.com', '$2a$12$qJDkRMXKZbi5YSJvCi.D.uzT8QEvfkll1luR15djhdP1kamre6kYe', null, 1);

INSERT INTO product (title, description, price, type, photo_url)
VALUES ('Rose Bouquet', 'Beautiful bouquet of red roses', 29.99, 'BOUQUET',
        '1.jpg'),
       ('Tulip Bouquet', 'Colorful tulip bouquet', 19.99, 'BOUQUET',
        '43.jpg'),
       ('Lily Bouquet', 'Elegant lily bouquet', 24.99, 'BOUQUET',
        '57.jpg'),
       ('Sunflower Bouquet', 'Bright sunflower bouquet', 34.99, 'BOUQUET',
        '60.jpg'),
       ('Carnation Bouquet', 'Festive carnation bouquet', 22.99, 'BOUQUET',
        '101.jpg'),
       ('Daisy Bouquet', 'Charming daisy bouquet', 18.99, 'BOUQUET',
        '127.jpg'),
       ('Mixed Flower Bouquet', 'Assorted flower bouquet', 27.99, 'BOUQUET',
        '759.jpg'),
       ('Chocolate Box', 'Delicious assortment of chocolates', 14.99, 'GIFT',
        '2732.jpg'),
       ('Perfume Set', 'Luxurious perfume gift set', 49.99, 'GIFT',
        '2733.jpg'),
       ('Wine Bottle', 'Fine wine for special occasions', 39.99, 'GIFT',
        '2795.jpg'),
       ('Teddy Bear', 'Cuddly teddy bear for your loved ones', 9.99, 'GIFT',
        '2801.jpg'),
       ('Candle Set', 'Scented candle set for relaxation', 19.99, 'GIFT',
        '2802.jpg'),
       ('Earrings', 'Stylish earrings for everyday wear', 29.99, 'GIFT',
        '2803.jpg'),
       ('Bracelet', 'Elegant bracelet to accessorize', 24.99, 'GIFT',
        '2822.jpg'),
       ('Watch', 'Classic watch for a timeless look', 59.99, 'GIFT',
        '2825.jpg'),
       ('Ring', 'Beautiful ring for special moments', 39.99, 'GIFT',
        '2882.jpg'),
       ('Rose Plant', 'Gorgeous rose plant for indoors', 19.99, 'FLOWER',
        'rose.jpeg'),
       ('Lily Plant', 'Exquisite lily plant for home decor', 29.99, 'FLOWER',
        'lily.jpeg'),
       ('Orchid Plant', 'Elegant orchid plant for elegance', 34.99, 'FLOWER',
        'orchid.jpeg'),
       ('Bonsai Tree', 'Miniature bonsai tree for tranquility', 49.99, 'FLOWER',
        'tree.jpeg');

INSERT INTO "order" (name, email, phone_number, address, comment, delivery_time, status, timestamp, product_id)
VALUES ('John Doe', 'john@example.com', '+1234567890', '123 Main St, City, Country', 'No comments',
        '2024-04-09 12:00:00', 'CREATED', '2024-04-09 08:00:00', 1),
       ('Jane Smith', 'jane@example.com', '+9876543210', '456 Elm St, City, Country', 'Additional instructions',
        '2024-04-10 10:00:00', 'IN_PROGRESS', '2024-04-09 10:00:00', 2),
       ('Alice Johnson', 'alice@example.com', '+1122334455', '789 Oak St, City, Country',
        'Delivery preference: morning', '2024-04-11 14:00:00', 'COMPLETED', '2024-04-09 14:00:00', 3),
       ('Bob Brown', 'bob@example.com', '+5544332211', '101 Pine St, City, Country', 'Urgent delivery required',
        '2024-04-12 16:00:00', 'IN_PROGRESS', '2024-04-09 16:00:00', 4),
       ('Eve Taylor', 'eve@example.com', '+9988776655', '202 Cedar St, City, Country', 'No comments',
        '2024-04-13 18:00:00', 'CREATED', '2024-04-09 18:00:00', 5);
