-- ================================================================
-- Script để tạo roles mặc định cho hệ thống
-- Database: spring_fruitshop
-- Chạy script này TRƯỚC KHI sử dụng chức năng đăng ký
-- ================================================================

USE spring_fruitshop;

-- Xóa dữ liệu cũ trong bảng roles (nếu có)
-- DELETE FROM user_roles;
-- DELETE FROM roles;

-- Thêm 2 roles mặc định: USER và ADMIN
INSERT INTO roles (id, created_at, updated_at, name) 
VALUES 
    (1, NOW(), NOW(), 'USER'),
    (2, NOW(), NOW(), 'ADMIN')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Kiểm tra kết quả
SELECT * FROM roles;

-- Ghi chú:
-- - Role USER (id=1): Được gán tự động khi user đăng ký
-- - Role ADMIN (id=2): Cần được gán thủ công cho quản trị viên
