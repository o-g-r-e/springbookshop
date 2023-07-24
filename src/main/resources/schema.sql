
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;
DROP TABLE IF EXISTS book_file;

CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    photo VARCHAR(255),
    slug VARCHAR(255),
    description TEXT
);

CREATE TABLE books(
    id SERIAL PRIMARY KEY,
    author_id INT NOT NULL,
    pub_date DATE NOT NULL,
    is_bestseller SMALLINT NOT NULL,
    slug VARCHAR(255),
    title VARCHAR(250) NOT NULL,
    image VARCHAR(250),
    description TEXT,
    price INT NOT NULL,
    discount SMALLINT,
    FOREIGN KEY (author_id) REFERENCES authors(id)
);

CREATE TABLE book_file(
    id SERIAL PRIMARY KEY,
    hash VARCHAR(255) NOT NULL,
    type_id INT NOT NULL,
    path VARCHAR(255) NOT NULL
);