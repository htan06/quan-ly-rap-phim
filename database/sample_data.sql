--use Movies

insert into roles (role_name) values ('STAFF'), ('MANAGER')

INSERT INTO users (
    first_name,
    last_name,
    email,
    phone_number,
    status,
    username,
    password,
    role_id
) VALUES (
    N'Nguyen',
    N'Van A',
    'staff1@example.com',
    '0900000001',
    'ACTIVE',
    'staff1',
    '$2a$10$ZWUu7.HpJUdAuXA.ptB/LOUJ5w8iHrK.nvE6TDZGM7bQZ1kq5a.iq',
    1
),
(
    N'Tran',
    N'Van B',
    'manager1@example.com',
    '0900000002',
    'ACTIVE',
    'manager1',
    '$2a$10$ZWUu7.HpJUdAuXA.ptB/LOUJ5w8iHrK.nvE6TDZGM7bQZ1kq5a.iq',
    2
);

INSERT INTO movies (
    title, description, director, cast, genre, duration,
    release_date, end_date, language, subtitle_language,
    country, age_rating, status
) VALUES
(N'Mai', N'Chuyện tình cảm động', N'Trấn Thành',
 N'Phương Anh Đào', N'Tình cảm', 131,
 '2024-02-10', '2030-01-01', N'Vietnamese', N'English',
 N'Việt Nam', 'P', 'NOW_SHOWING'),

(N'Nhà Bà Nữ', N'Gia đình và mâu thuẫn', N'Trấn Thành',
 N'Lê Giang', N'Hài, Tình cảm', 102,
 '2023-01-22', '2030-01-01', N'Vietnamese', N'English',
 N'Việt Nam', 'P', 'NOW_SHOWING'),

(N'Bố Già', N'Câu chuyện cha con', N'Trấn Thành',
 N'Tuấn Trần', N'Hài, Gia đình', 128,
 '2021-03-12', '2030-01-01', N'Vietnamese', N'English',
 N'Việt Nam', 'P', 'NOW_SHOWING');

INSERT INTO rooms (name, room_type, capacity, status)
VALUES
('A1', 'STANDARD', 30, 'AVAILABLE'),
('A2', 'STANDARD', 30, 'AVAILABLE'),
('B1', 'VIP', 20, 'AVAILABLE'),
('B2', 'IMAX', 40, 'AVAILABLE'),
('C1', '_3D', 35, 'AVAILABLE');

INSERT INTO seats (room_id, name, type) VALUES
(1,'A1','STANDARD'),(1,'A2','STANDARD'),(1,'A3','STANDARD'),(1,'A4','STANDARD'),(1,'A5','STANDARD'),(1,'A6','STANDARD'),
(1,'B1','STANDARD'),(1,'B2','STANDARD'),(1,'B3','STANDARD'),(1,'B4','STANDARD'),(1,'B5','STANDARD'),(1,'B6','STANDARD'),
(1,'C1','STANDARD'),(1,'C2','STANDARD'),(1,'C3','STANDARD'),(1,'C4','STANDARD'),(1,'C5','STANDARD'),(1,'C6','STANDARD'),
(1,'D1','VIP'),(1,'D2','VIP'),(1,'D3','VIP'),(1,'D4','VIP'),(1,'D5','VIP'),(1,'D6','VIP'),
(1,'E1','VIP'),(1,'E2','VIP'),(1,'E3','VIP'),(1,'E4','VIP'),(1,'E5','VIP'),(1,'E6','VIP');

INSERT INTO seats (room_id, name, type)
SELECT 2, name, type FROM seats WHERE room_id = 1;

INSERT INTO seats (room_id, name, type) VALUES
(3,'A1','VIP'),(3,'A2','VIP'),(3,'A3','VIP'),(3,'A4','VIP'),(3,'A5','VIP'),
(3,'B1','VIP'),(3,'B2','VIP'),(3,'B3','VIP'),(3,'B4','VIP'),(3,'B5','VIP'),
(3,'C1','VIP'),(3,'C2','VIP'),(3,'C3','VIP'),(3,'C4','VIP'),(3,'C5','VIP'),
(3,'D1','VIP'),(3,'D2','VIP'),(3,'D3','VIP'),(3,'D4','VIP'),(3,'D5','VIP');

INSERT INTO seats (room_id, name, type) VALUES
(4,'A1','STANDARD'),(4,'A2','STANDARD'),(4,'A3','STANDARD'),(4,'A4','STANDARD'),(4,'A5','STANDARD'),(4,'A6','STANDARD'),(4,'A7','STANDARD'),(4,'A8','STANDARD'),
(4,'B1','STANDARD'),(4,'B2','STANDARD'),(4,'B3','STANDARD'),(4,'B4','STANDARD'),(4,'B5','STANDARD'),(4,'B6','STANDARD'),(4,'B7','STANDARD'),(4,'B8','STANDARD'),
(4,'C1','STANDARD'),(4,'C2','STANDARD'),(4,'C3','STANDARD'),(4,'C4','STANDARD'),(4,'C5','STANDARD'),(4,'C6','STANDARD'),(4,'C7','STANDARD'),(4,'C8','STANDARD'),
(4,'D1','STANDARD'),(4,'D2','STANDARD'),(4,'D3','STANDARD'),(4,'D4','STANDARD'),(4,'D5','STANDARD'),(4,'D6','STANDARD'),(4,'D7','STANDARD'),(4,'D8','STANDARD'),
(4,'E1','STANDARD'),(4,'E2','STANDARD'),(4,'E3','STANDARD'),(4,'E4','STANDARD'),(4,'E5','STANDARD'),(4,'E6','STANDARD'),(4,'E7','STANDARD'),(4,'E8','STANDARD');

INSERT INTO seats (room_id, name, type) VALUES
(5,'A1','STANDARD'),(5,'A2','STANDARD'),(5,'A3','STANDARD'),(5,'A4','STANDARD'),(5,'A5','STANDARD'),(5,'A6','STANDARD'),(5,'A7','STANDARD'),
(5,'B1','STANDARD'),(5,'B2','STANDARD'),(5,'B3','STANDARD'),(5,'B4','STANDARD'),(5,'B5','STANDARD'),(5,'B6','STANDARD'),(5,'B7','STANDARD'),
(5,'C1','STANDARD'),(5,'C2','STANDARD'),(5,'C3','STANDARD'),(5,'C4','STANDARD'),(5,'C5','STANDARD'),(5,'C6','STANDARD'),(5,'C7','STANDARD'),
(5,'D1','VIP'),(5,'D2','VIP'),(5,'D3','VIP'),(5,'D4','VIP'),(5,'D5','VIP'),(5,'D6','VIP'),(5,'D7','VIP'),
(5,'E1','VIP'),(5,'E2','VIP'),(5,'E3','VIP'),(5,'E4','VIP'),(5,'E5','VIP'),(5,'E6','VIP'),(5,'E7','VIP');