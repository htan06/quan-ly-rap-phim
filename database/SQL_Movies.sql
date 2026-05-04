--create database Movies;
--use Movies;

CREATE TABLE [users] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [first_name] nvarchar(20) not null,
  [last_name] nvarchar(50) not null,
  [email] nvarchar(255) unique not null,
  [phone_number] nvarchar(20) unique,
  [status] nvarchar(255) NOT NULL CHECK ([status] IN ('ACTIVE', 'LOCK')),
  [username] nvarchar(100) unique not null,
  [password] nvarchar(255) not null,
  [role_id] int,
  [created_at] datetime default getdate(),
  [updated_at] datetime
)
GO

CREATE TABLE [auth_history] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,

  [user_id] bigint,
  [username] nvarchar(100) NOT NULL,

  [action] nvarchar(20) NOT NULL 
    CHECK ([action] IN ('LOGIN', 'LOGOUT')),

  [time] datetime DEFAULT getdate(),

  CONSTRAINT FK_auth_user 
    FOREIGN KEY ([user_id]) REFERENCES [users]([id])
)
GO

CREATE TABLE [roles] (
  [id] int IDENTITY(1,1) PRIMARY KEY,
  [role_name] varchar(20) unique not null,
  [created_at] datetime default getdate(),
  [updated_at] datetime
)
GO

CREATE TABLE [memberships] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [name] nvarchar(100) not null,
  [phone_number] nvarchar(20) unique not null,
  [created_at] datetime default getdate(),
  [updated_at] datetime,
  [member_point] int default 0 check (member_point >=0)
)
GO

CREATE TABLE [movies] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [title] nvarchar(255) not null,
  [description] nvarchar(max),
  [director] nvarchar(200),
  [cast] nvarchar(200),
  [genre] nvarchar(200),
  [duration] INT NOT NULL,
  [release_date] date not null,
  [end_date] date,
  [language] nvarchar(50),
  [subtitle_language] nvarchar(50),
  [country] nvarchar(100),
  [age_rating] nvarchar(10),
  [status] nvarchar(255) NOT NULL CHECK ([status] IN ('COMING_SOON', 'NOW_SHOWING', 'ENDED', 'HIDDEN')),
  [created_at] datetime default getdate(),
  [updated_at] datetime,

  constraint check_MovieDates check ([end_date] >= [release_date])
)
GO

CREATE TABLE [rooms] (
  [id] int IDENTITY(1,1) PRIMARY KEY,
  [name] varchar(20) unique not null,
  [room_type] nvarchar(255) NOT NULL CHECK ([room_type] IN ('STANDARD', 'IMAX', '_3D', '_4DX', 'VIP')),
  [capacity] int not null check ([capacity] > 0),
  [status] nvarchar(50) default 'AVAILABLE' check ([status] in ('AVAILABLE', 'MAINTENANCE', 'CLOSED')) ,
  [created_at] datetime default getdate(),
  [updated_at] datetime
)
GO

CREATE TABLE [seats] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [room_id] int not null,
  [name] varchar(20) not null,
  [status] varchar(50) DEFAULT 'AVAILABLE' CHECK ([status] IN ('AVAILABLE', 'BROKEN')),
  [type] nvarchar(255) NOT NULL CHECK ([type] IN ('STANDARD', 'VIP')),
  [created_at] datetime default getdate(),
  [updated_at] datetime,

  CONSTRAINT UQ_Room_SeatName UNIQUE ([room_id], [name])
)
GO

CREATE TABLE [show_times] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [movie_id] bigint not null,
  [room_id] int not null,
  [price] decimal(10,2) CHECK (price > 0),
  [start_time] datetime not null,
  [end_time] datetime not null,
  [created_at] datetime default getdate(),
  [updated_at] datetime,

  constraint check_ShowTime check ([end_time] > [start_time])
)
GO

CREATE TABLE [bookings] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [show_time_id] bigint not null,
  [membership_id] bigint,
  [staff_id] bigint not null,
  [qr_code] varbinary(max),
  [status] nvarchar(255) NOT NULL CHECK ([status] IN ('CANCELED', 'PENDING', 'PAID', 'EXPIRED')),
  [total_tax] decimal(10,2) default 0,
  [total_price] decimal(10,2) default 0 ,
  [created_at] datetime default getdate(),
  [updated_at] datetime
)
GO

CREATE TABLE [booking_details] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [booking_id] bigint not null,
  [seat_id] bigint not null,
  [created_at] datetime default getdate(),
  [updated_at] datetime,

  constraint UQ_Booking_Seat unique ([booking_id], [seat_id])
)
GO

CREATE TABLE [payments] (
  [id] bigint IDENTITY(1,1) PRIMARY KEY,
  [booking_id] bigint not null,
  [amount] decimal(10,2) not null,
  [received_amount] decimal(10,2) not null,
  [change_amount] decimal(10,2) not null,
  [payment_status] nvarchar(255) NOT NULL CHECK ([payment_status] IN ('CANCELED', 'PENDING', 'PAID', 'EXPIRED')),
  [created_at] datetime default getdate(),
  [updated_at] datetime,

  constraint CHK_PaymentAmount CHECK ([received_amount] >= [amount])
)
GO

ALTER TABLE [users] ADD FOREIGN KEY ([role_id]) REFERENCES [roles] ([id])
GO

ALTER TABLE [seats] ADD FOREIGN KEY ([room_id]) REFERENCES [rooms] ([id])
GO

ALTER TABLE [show_times] ADD FOREIGN KEY ([movie_id]) REFERENCES [movies] ([id])
GO

ALTER TABLE [show_times] ADD FOREIGN KEY ([room_id]) REFERENCES [rooms] ([id])
GO

ALTER TABLE [bookings] ADD FOREIGN KEY ([membership_id]) REFERENCES [memberships] ([id])
GO

ALTER TABLE [bookings] ADD FOREIGN KEY ([staff_id]) REFERENCES [users] ([id])
GO

ALTER TABLE [bookings] ADD FOREIGN KEY ([show_time_id]) REFERENCES [show_times] ([id])
GO

ALTER TABLE [booking_details] ADD FOREIGN KEY ([booking_id]) REFERENCES [bookings] ([id])
GO

ALTER TABLE [booking_details] ADD FOREIGN KEY ([seat_id]) REFERENCES [seats] ([id])
GO

ALTER TABLE [payments] ADD FOREIGN KEY ([booking_id]) REFERENCES [bookings] ([id])
GO

