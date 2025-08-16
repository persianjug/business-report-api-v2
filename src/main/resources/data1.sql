-- TRUNCATE TABLE tasks;
-- TRUNCATE TABLE reports;

-- --------------------------------------------------------
-- 業務報告書の初期データ (5件)
-- --------------------------------------------------------
-- 1件目: Webアプリケーション開発プロジェクト
INSERT INTO
  reports (
    start_date,
    end_date,
    end_client,
    upper_client,
    industry,
    nearest_station,
    project_name,
    participation_date,
    number_of_participants,
    commute_hours,
    commute_minutes,
    work_style,
    position,
    main_technology,
    database,
    overall_progress,
    future_plans,
    customer_status,
    sales_info,
    health_status,
    vacation_plans,
    consultation
    -- created_at
  )
VALUES
  (
    '2024-05-01',
    '2024-05-31',
    '株式会社ABC',
    '株式会社DEF',
    'IT・通信',
    '渋谷',
    'ECサイト刷新プロジェクト',
    '2024-01-15',
    5,
    1,
    30,
    '併用勤務(在宅率6割以上)',
    'SE',
    'Java, Spring Boot, React',
    'PostgreSQL',
    '要件定義フェーズが完了し、基本設計を開始。チーム内の連携は良好。',
    '来月は詳細設計と開発環境構築を進める。',
    '顧客からのフィードバックはポジティブ。スケジュールの遅延なし。',
    '別案件の引き合いあり。',
    '体調に問題なし。',
    '',
    '特にありません。'
    -- NOW ()
  );

-- 2件目: 社内システム改修プロジェクト
INSERT INTO
  reports (
    start_date,
    end_date,
    end_client,
    upper_client,
    industry,
    nearest_station,
    project_name,
    participation_date,
    number_of_participants,
    commute_hours,
    commute_minutes,
    work_style,
    position,
    main_technology,
    database,
    overall_progress,
    future_plans,
    customer_status,
    sales_info,
    health_status,
    vacation_plans,
    consultation
    -- created_at
  )
VALUES
  (
    '2024-05-08',
    '2024-05-14',
    '株式会社GHI',
    '株式会社JKL',
    '製造業',
    '品川',
    '生産管理システム改修',
    '2024-04-01',
    3,
    0,
    45,
    '現場勤務',
    'PG',
    'C#, .NET Framework',
    'SQL Server',
    '製造ラインからのデータ連携機能の改修が完了。結合テストを開始。',
    '来週は結合テストの実施と不具合修正を行う。',
    'システムの安定稼働を高く評価いただいている。',
    '関連部署からの追加機能の要望あり。',
    '問題ありません。',
    '5/20に有給休暇を取得予定です。',
    '結合テスト中に発生したパフォーマンスの問題について相談したいです。'
    -- NOW ()
  );

-- 3件目: 新規サービス立ち上げプロジェクト
INSERT INTO
  reports (
    start_date,
    end_date,
    end_client,
    upper_client,
    industry,
    nearest_station,
    project_name,
    participation_date,
    number_of_participants,
    commute_hours,
    commute_minutes,
    work_style,
    position,
    main_technology,
    database,
    overall_progress,
    future_plans,
    customer_status,
    sales_info,
    health_status,
    vacation_plans,
    consultation
    -- created_at
  )
VALUES
  (
    '2024-05-15',
    '2024-05-21',
    '株式会社MNO',
    '株式会社PQR',
    'サービス業',
    '新宿',
    '新規SaaS開発',
    '2024-05-15',
    1,
    1,
    0,
    '在宅勤務',
    'SE(社員代替)',
    'Python, Django',
    'MySQL',
    '初期環境構築とAPIの設計に着手。',
    '今週は初期APIの実装とテストを行う。',
    '新サービスへの期待値が高い。',
    '市場調査の結果を共有。',
    '特に体調の変化はありません。',
    '',
    'ありません。'
    -- NOW ()
  );

-- 4件目: インフラ構築案件
INSERT INTO
  reports (
    start_date,
    end_date,
    end_client,
    upper_client,
    industry,
    nearest_station,
    project_name,
    participation_date,
    number_of_participants,
    commute_hours,
    commute_minutes,
    work_style,
    position,
    main_technology,
    database,
    overall_progress,
    future_plans,
    customer_status,
    sales_info,
    health_status,
    vacation_plans,
    consultation
    -- created_at
  )
VALUES
  (
    '2024-05-22',
    '2024-05-28',
    '株式会社STU',
    '',
    '運輸・物流',
    '東京',
    'クラウド環境移行支援',
    '2024-03-10',
    4,
    1,
    15,
    '併用勤務(在宅率6割未満)',
    'PL',
    'AWS, Docker, Terraform',
    '',
    '開発環境のAWSへの移行が完了。検証テストを実施中。',
    '本番環境の移行計画を策定し、顧客と調整する。',
    '移行作業がスムーズに進んでいることに満足している。',
    '別プロジェクトでのクラウド技術コンサルティングの相談あり。',
    '良好です。',
    '',
    '特にありません。'
    -- NOW ()
  );

-- 5件目: テスト業務
INSERT INTO
  reports (
    start_date,
    end_date,
    end_client,
    upper_client,
    industry,
    nearest_station,
    project_name,
    participation_date,
    number_of_participants,
    commute_hours,
    commute_minutes,
    work_style,
    position,
    main_technology,
    database,
    overall_progress,
    future_plans,
    customer_status,
    sales_info,
    health_status,
    vacation_plans,
    consultation
    -- created_at
  )
VALUES
  (
    '2024-05-29',
    '2024-06-04',
    '株式会社VWX',
    '',
    '金融',
    '日本橋',
    '金融システム統合テスト',
    '2024-05-01',
    8,
    0,
    35,
    '現場勤務',
    'テスター',
    'テスト自動化ツール, Selenium',
    '',
    '結合テストフェーズが終盤に差し掛かっている。現在までに発見されたバグは全て修正済み。',
    '来週はシステムテストを開始する。',
    'QAチームとの連携が円滑で、テスト進捗に問題はない。',
    '',
    '健康状態は良好です。',
    '来月の長期休暇を検討しています。',
    'テスト設計の段階で想定外のケースがいくつか見つかっています。対応方針についてご相談させてください。'
    -- NOW ()
  );

-- --------------------------------------------------------
-- reportsテーブルの初期データ (5件)
-- ※省略※
-- --------------------------------------------------------
-- --------------------------------------------------------
-- tasksテーブルの初期データ (reportsテーブルに紐づく)
-- --------------------------------------------------------
-- reports_id = 1 に紐づくタスク
-- ECサイト刷新プロジェクト
INSERT INTO
  tasks (report_id, task_name, status, problem)
VALUES
  (1, '要件定義', '完了', '特になし'),
  (1, '基本設計', '着手', 'メンバー間の認識合わせに時間を要した');

-- reports_id = 2 に紐づくタスク
-- 生産管理システム改修
INSERT INTO
  tasks (report_id, task_name, status, problem)
VALUES
  (2, 'データ連携機能改修', '完了', '特になし'),
  (2, '結合テスト', '着手', '一部の機能でパフォーマンス問題が発生中');

-- reports_id = 3 に紐づくタスク
-- 新規SaaS開発
INSERT INTO
  tasks (report_id, task_name, status, problem)
VALUES
  (3, '初期環境構築', '完了', '特になし'),
  (3, 'API設計', '進行中', '顧客要件の整理に時間を要している');

-- reports_id = 4 に紐づくタスク
-- クラウド環境移行支援
INSERT INTO
  tasks (report_id, task_name, status, problem)
VALUES
  (4, '開発環境移行', '完了', '特になし'),
  (4, '本番環境移行計画', '進行中', '顧客とのスケジュール調整に難航');

-- reports_id = 5 に紐づくタスク
-- 金融システム統合テスト
INSERT INTO
  tasks (report_id, task_name, status, problem)
VALUES
  (5, '結合テスト', '完了', 'バグの再現に時間を要した');
