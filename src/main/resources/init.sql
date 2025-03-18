CREATE DATABASE restaurant_app;

\c restaurant_app;

DROP TABLE IF EXISTS users;

CREATE TYPE role_type AS ENUM ('admin', 'customer');
CREATE TABLE users(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role role_type NOT NULL,
    registration_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE meals(
    meal_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0)
);

CREATE TABLE ingredients(
    ingredient_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(255) NOT NULL,
    stock INTEGER CHECK (stock >= 0)
);

-- Junction table to match ingredients to meals
CREATE TABLE meal_ingredients(
    meal_id INT REFERENCES meals(meal_id),
    ingredient_id INT REFERENCES ingredients(ingredient_id),
    quantity_required INT NOT NULL CHECK (quantity_required > 0),
    PRIMARY KEY (meal_id, ingredient_id)
);

CREATE TYPE order_status AS ENUM ('pending', 'preparing', 'delivered', 'cancelled');
CREATE TABLE orders(
    order_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status order_status NOT NULL DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Junction table to match meals to order
CREATE TABLE order_meals(
    order_id INT REFERENCES orders(order_id),
    meal_id INT REFERENCES meals(meal_id),
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, meal_id)
);



