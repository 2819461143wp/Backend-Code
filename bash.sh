#!/bin/bash

# 颜色设置
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # 无颜色

# 打印带颜色的信息函数
print_info() {
    echo -e "${GREEN}[INFO] $1${NC}"
}

print_warn() {
    echo -e "${YELLOW}[WARN] $1${NC}"
}

print_error() {
    echo -e "${RED}[ERROR] $1${NC}"
}

# 检查MySQL是否安装
check_mysql() {
    if ! command -v mysql &> /dev/null; then
        print_error "MySQL未安装，请先安装MySQL"
        exit 1
    fi
    print_info "MySQL已安装"
}

# 检查Java环境
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java未安装，请先安装JDK"
        exit 1
    fi
    print_info "Java已安装: $(java -version 2>&1 | head -n 1)"
}

# 创建数据库和用户
setup_database() {
    print_info "开始设置数据库..."

    read -p "请输入MySQL root密码: " ROOT_PASSWORD

    # 创建数据库和授权
    mysql -u root -p"$ROOT_PASSWORD" <<EOF
CREATE DATABASE IF NOT EXISTS test DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY '123';
GRANT ALL PRIVILEGES ON test.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
EOF

    if [ $? -ne 0 ]; then
        print_error "数据库创建失败"
        exit 1
    fi

    print_info "数据库创建成功"

    # 创建表结构
    mysql -u root -p"$ROOT_PASSWORD" test <<EOF
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) DEFAULT 'user'
);

CREATE TABLE IF NOT EXISTS characters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(50) NOT NULL,
    biography TEXT,
    avatar_url VARCHAR(255),
    fans_count INT DEFAULT 0,
    follow_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    student_name VARCHAR(50) NOT NULL,
    academy VARCHAR(50) NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    deyu INT DEFAULT 0,
    zhiyu INT DEFAULT 0,
    meiyu INT DEFAULT 0,
    tiyu INT DEFAULT 0,
    xiaoyuan INT DEFAULT 0,
    xiangtu INT DEFAULT 0,
    chanxue INT DEFAULT 0,
    jiating INT DEFAULT 0,
    qingshi INT DEFAULT 0,
    volunteer_time FLOAT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sutuo(
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50),
    activity VARCHAR(50) NOT NULL,
    deyu INT DEFAULT 0,
    zhiyu INT DEFAULT 0,
    meiyu INT DEFAULT 0,
    tiyu INT DEFAULT 0,
    xiaoyuan INT DEFAULT 0,
    xiangtu INT DEFAULT 0,
    chanxue INT DEFAULT 0,
    jiating INT DEFAULT 0,
    qingshi INT DEFAULT 0,
    volunteer_time FLOAT DEFAULT 0,
    file_hash VARCHAR(64),
    FOREIGN KEY (student_id) REFERENCES students(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS file_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    hash VARCHAR(64) UNIQUE,
    file_path VARCHAR(255),
    upload_time DATETIME,
    operator_id VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    title VARCHAR(255),
    content TEXT,
    image_count INT DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT 0,
    status INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    user_id INT,
    content TEXT,
    created_at TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS chat_conversation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(255),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    conversation_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (conversation_id) REFERENCES chat_conversation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
EOF

    if [ $? -ne 0 ]; then
        print_error "表结构创建失败"
        exit 1
    fi

    print_info "表结构创建成功"
}

# 创建文件存储目录
setup_file_storage() {
    print_info "设置文件存储目录..."

    # 文件存储路径
    STORAGE_PATH="/home/dachuang/source"

    if [ ! -d "$STORAGE_PATH" ]; then
        mkdir -p "$STORAGE_PATH"
        if [ $? -ne 0 ]; then
            print_error "创建存储目录失败"
            exit 1
        fi
    fi

    # 确保目录权限正确
    chmod 755 "$STORAGE_PATH"

    print_info "文件存储目录设置成功: $STORAGE_PATH"
    print_warn "请注意修改application.properties中的文件存储路径为: $STORAGE_PATH"
}

# 主函数
main() {
    print_info "开始部署后端项目..."

    check_mysql
    check_java
    setup_database
    setup_file_storage

    print_info "部署准备工作完成！"
    print_info "请确保在application.properties中设置正确的文件路径:"
    echo "spring.servlet.multipart.location=$STORAGE_PATH"
    echo "spring.web.resources.static-locations=file:$STORAGE_PATH/,classpath:/static/"

    print_info "你现在可以运行Spring Boot应用了"
}

main