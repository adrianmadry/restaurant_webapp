-- Inserts for menu items --
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(1, 'spring rolls', 'starter', 'vegetarian spring rolls', 8.99, 'img/meals/spring_rolls.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(2, 'coconut shrimps', 'starter', 'coconut shrimp with mango salsa', 10.99, 'img/meals/coconut_shrimps.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(3, 'satay skewers', 'starter', 'chicken satay with peanut sauce', 11.99, 'img/meals/satay.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(4, 'pho bo', 'soup', 'vietnamese beef noodle soup', 15.90, 'img/meals/pho_bo.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(5, 'pho ga', 'soup', 'vietnamese chicken noodle soup', 14.90, 'img/meals/pho_ga.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(6, 'tom kha gai', 'soup', 'coconut chicken soup', 13.90, 'img/meals/tom_kha.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(7, 'tom yam kung', 'soup', 'spicy and sour soup with shrimps', 13.90, 'img/meals/tom_yam.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(8, 'pad thai', 'main', 'pad thai with shrimps', 18.90, 'img/meals/pad_thai.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(9, 'banh mi thit', 'main', 'vietnamese sandwich with pork', 12.90, 'img/meals/banh_mi.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(10, 'green curry', 'main', 'thai green curry with chicken', 16.90, 'img/meals/curry_green.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(11, 'red curry', 'main', 'thai red curry with beef', 17.90, 'img/meals/curry_red.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(12, 'pineapple fried rice', 'main', 'fried rice in pineapple with seafood', 18.90, 'img/meals/fried_rice.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(13, 'still water', 'beverage', 'still mineral water', 2.99, 'img/meals/still_water.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(14, 'sparkling water', 'beverage', 'sparkling mineral water', 3.99, 'img/meals/still_water.png');
INSERT INTO public.meals
(meal_id, "name", "type", description, price, image_path)
VALUES(15, 'mango lassi', 'beverage', 'creamy mango yogurt drink', 5.99, 'img/meals/mango_lassi.png');

-- Inserts for users --
INSERT INTO users (username, password, email, role) 
VALUES 
('username1', 'password', 'user1', 'admin'), 
('username2', 'password', 'user2', 'admin'), 
('username3', 'password', 'user3', 'admin');