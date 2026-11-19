CREATE DATABASE IF NOT EXISTS pizzaria;
USE pizzaria;

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome_cliente VARCHAR(100) NOT NULL
);

CREATE TABLE administrador (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nome_admin VARCHAR(100) NOT NULL,
    senha_admin VARCHAR(80) NOT NULL
);

CREATE TABLE cozinha (
    id_func INT AUTO_INCREMENT PRIMARY KEY,
    nome_func VARCHAR(100) NOT NULL
);

CREATE TABLE sabor (
    id_sabor INT AUTO_INCREMENT PRIMARY KEY,
    nome_sabor VARCHAR(100) NOT NULL,
    descricao_sabor VARCHAR(300)
);

CREATE TABLE tamanho (
    id_tamanho INT AUTO_INCREMENT PRIMARY KEY,
    qtd_pedacos INT NOT NULL
);

CREATE TABLE preco_sabor (
    id_preco_sabor INT AUTO_INCREMENT PRIMARY KEY,
    id_sabor INT NOT NULL,
    id_tamanho INT NOT NULL,
    valor_unitario DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (id_sabor) REFERENCES sabor(id_sabor),
    FOREIGN KEY (id_tamanho) REFERENCES tamanho(id_tamanho),
    UNIQUE (id_sabor, id_tamanho)
);

CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_func INT,
    data_hora_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_pedido VARCHAR(30) NOT NULL,
    valor_pedido DECIMAL(6,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_func) REFERENCES cozinha(id_func)
);

CREATE TABLE pizza (
    id_pizza INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_tamanho INT NOT NULL,
    id_sabor1 INT NOT NULL,
    id_sabor2 INT,
    valor_unitario DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    FOREIGN KEY (id_tamanho) REFERENCES tamanho(id_tamanho),
    FOREIGN KEY (id_sabor1) REFERENCES sabor(id_sabor),
    FOREIGN KEY (id_sabor2) REFERENCES sabor(id_sabor)
);

CREATE TABLE pagamento (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL UNIQUE,
    status_pagamento VARCHAR(30) NOT NULL,
    metodo_pagamento VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);

CREATE TABLE avaliacao (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_pedido INT NOT NULL UNIQUE,
    nota_avaliacao INT NOT NULL,
    comentario_avaliacao VARCHAR(500),
    data_avaliacao DATE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);

CREATE TABLE relatorio (
    id_relatorio INT AUTO_INCREMENT PRIMARY KEY,
    id_admin INT NOT NULL,
    id_pedido INT NOT NULL,
    faturamento_relatorio DECIMAL(7,2) NOT NULL,
    FOREIGN KEY (id_admin) REFERENCES administrador(id_admin),
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
);
