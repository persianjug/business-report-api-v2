-- `tasks`テーブルを削除（外部キー制約があるため、先に削除）
DROP TABLE IF EXISTS tasks;

-- `reports`テーブルを削除
DROP TABLE IF EXISTS reports;

-- `reports`テーブルを定義
CREATE TABLE
  IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE,
    end_date DATE,
    end_client VARCHAR(255),
    upper_client VARCHAR(255),
    industry VARCHAR(255),
    nearest_station VARCHAR(255),
    project_name VARCHAR(255),
    participation_date VARCHAR(255),
    number_of_participants INT,
    commute_hours INTEGER,
    commute_minutes INTEGER,
    work_style VARCHAR(50),
    position VARCHAR(50),
    main_technology VARCHAR(255),
    database VARCHAR(255),
    overall_progress TEXT,
    future_plans TEXT,
    customer_status TEXT,
    sales_info TEXT,
    health_status TEXT,
    vacation_plans TEXT,
    consultation TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'draft',
    -- created_at の自動設定
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- updated_at の自動設定（更新時も自動）
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  );

-- `tasks`テーブルを定義
CREATE TABLE
  IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_id BIGINT,
    task_name VARCHAR(255),
    task_progress TEXT,
    task_problem TEXT,
    FOREIGN KEY (report_id) REFERENCES reports (id)
  );