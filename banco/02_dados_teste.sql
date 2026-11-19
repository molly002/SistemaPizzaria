USE pizzaria;

-- Login do sistema: usuário "admin" / senha "admin123" (senha guardada como SHA-256)
INSERT INTO administrador (nome_admin, senha_admin) VALUES
('admin',   SHA2('admin123', 256)),
('gerente', SHA2('gerente123', 256)),
('maria',   SHA2('maria123', 256)),
('joao',    SHA2('joao123', 256)),
('paula',   SHA2('paula123', 256));

INSERT INTO cliente (nome_cliente) VALUES
('Ana Souza'), ('Bruno Lima'), ('Carla Mendes'), ('Diego Ferreira'), ('Elisa Rocha');

INSERT INTO cozinha (nome_func) VALUES
('Marcos Pizzaiolo'), ('Luciana Forneira'), ('Roberto Auxiliar'), ('Sandra Chef'), ('Tiago Montador');

INSERT INTO sabor (nome_sabor, descricao_sabor) VALUES
('Margherita',          'Molho de tomate, mussarela, tomate e manjericão'),
('Calabresa',           'Mussarela, calabresa fatiada e cebola'),
('Quatro Queijos',      'Mussarela, provolone, parmesão e gorgonzola'),
('Frango com Catupiry', 'Frango desfiado e catupiry'),
('Portuguesa',          'Presunto, ovo, cebola, ervilha e azeitona');

INSERT INTO tamanho (qtd_pedacos) VALUES (4), (6), (8), (10), (12);

INSERT INTO preco_sabor (id_sabor, id_tamanho, valor_unitario) VALUES
(1,1,25.00),(1,2,35.00),(1,3,45.00),
(2,1,27.00),(2,2,38.00),(2,3,48.00),
(3,1,32.00),(3,2,45.00),(3,3,55.00),
(4,1,30.00),(4,2,42.00),(4,3,52.00),
(5,1,31.00),(5,2,43.00),(5,3,53.00);

INSERT INTO pedido (id_cliente, id_func, data_hora_pedido, status_pedido, valor_pedido) VALUES
(1, 1, '2026-11-01 19:10:00', 'Entregue',   35.00),
(2, 2, '2026-11-02 20:05:00', 'Entregue',   55.00),
(3, 3, '2026-11-03 19:40:00', 'Entregue',   73.00),
(4, 4, '2026-11-04 21:00:00', 'Entregue',   53.00),
(5, 5, '2026-11-05 18:30:00', 'Em preparo', 45.00);

INSERT INTO pizza (id_pedido, id_tamanho, id_sabor1, id_sabor2, valor_unitario) VALUES
(1, 2, 1, NULL, 35.00),
(2, 3, 2, 3,    55.00),
(3, 1, 4, NULL, 30.00),
(3, 2, 5, NULL, 43.00),
(4, 3, 1, 5,    53.00),
(5, 2, 3, NULL, 45.00);

INSERT INTO pagamento (id_pedido, status_pagamento, metodo_pagamento) VALUES
(1, 'Pago', 'PIX'),
(2, 'Pago', 'Cartão de crédito'),
(3, 'Pago', 'Dinheiro'),
(4, 'Pago', 'Cartão de débito'),
(5, 'Pago', 'PIX');

INSERT INTO avaliacao (id_cliente, id_pedido, nota_avaliacao, comentario_avaliacao, data_avaliacao) VALUES
(1, 1, 5, 'Pizza quentinha e muito saborosa!', '2026-11-02'),
(2, 2, 4, 'Ótima, mas demorou um pouco.',       '2026-11-03'),
(3, 3, 5, 'Atendimento excelente.',             '2026-11-04'),
(4, 4, 3, 'Boa, porém chegou morna.',           '2026-11-05'),
(5, 5, 5, 'Melhor quatro queijos da cidade.',   '2026-11-06');

INSERT INTO relatorio (id_admin, id_pedido, faturamento_relatorio) VALUES
(1, 1, 35.00), (1, 2, 55.00), (1, 3, 73.00), (1, 4, 53.00), (1, 5, 45.00);
